package com.spurspro.option.strategy;


import com.spurspro.option.dto.option.ETFDailyPrice;
import com.spurspro.option.dto.option.OptionDailyData;
import com.spurspro.option.strategy.map.BackTestParams;
import com.spurspro.option.strategy.map.BacktestResult;
import com.spurspro.option.utils.DateUtil;
import com.spurspro.option.utils.DoubleUtil;
import com.spurspro.option.utils.csv.CSVUtils;
import com.spurspro.option.utils.excel.dom.ExcelUtil;
import com.spurspro.option.utils.excel.sax.SaxExcelUtil;
import com.spurspro.option.utils.fin.MaxDrawDownUtil;
import com.spurspro.option.utils.fin.UpbaaDate;
import com.spurspro.option.utils.fin.XirrData;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Jimmy.Liu on 2019-10-22 09:06
 *
 * @since 02.12.00
 */
@Service("StrangleStrategy")
public class StrangleStrategy implements Strategy {
    private static final Logger logger = LoggerFactory.getLogger(StrangleStrategy.class);

    private Integer earlyCloseDay = 11;

    private Boolean isBackTestParams = true;

    public final Double DEFAULT_PROFIT_PERCENT = 0.8;
    public final Double DEFAULT_LOSS_PROFIT_PERCENT = 0.1;

    public final Double EACH_OPTION_COST = 2d;

    @Override
    public BacktestResult backTest(BackTestParams params) throws Exception {
        List<ETFDailyPrice> etfDailyPrices = params.getEtfDailyPrices();
        List<OptionDailyData> optionDailyDatas = params.getOptionDailyDatas();
        long startTime = params.getStartTime();
        long endTime = params.getEndTime();
        earlyCloseDay = params.getEarlyCloseDay();
        BacktestResult backTestResult = new BacktestResult();
        Double xirr = 0d;
        BigDecimal maxDrawdown = new BigDecimal(1);
        Double winPercent = 0d;
        Double targetProfitPercent = 0d;
        Double targetLossProfitPercent = 0d;
        isBackTestParams = params.getBackTestDifferentParams();
        if (isBackTestParams) {
            for (double profitPercent = 0.1; profitPercent < 1; profitPercent = profitPercent + 0.1) {
                for (double lossProfitPercent = 0.1; lossProfitPercent < 1; lossProfitPercent = lossProfitPercent + 0.1) {
                    BacktestResult result = backTestWithParams(etfDailyPrices, optionDailyDatas, startTime, endTime, profitPercent, lossProfitPercent);
                    if (params.getType() == 2) {
                        if (result.getXirr() > xirr) {
                            xirr = result.getXirr();
                            maxDrawdown = result.getMaxDrawdown();
                            targetProfitPercent = profitPercent;
                            targetLossProfitPercent = lossProfitPercent;
                            winPercent = result.getWinCount() * 1.0d / (result.getLoseCount() + result.getWinCount());
                        }
                    } else if (params.getType() == 1) {
                        if (maxDrawdown.compareTo(result.getMaxDrawdown()) == 1 && result.getXirr() > 0.05) {
                            maxDrawdown = result.getMaxDrawdown();
                            xirr = result.getXirr();
                            targetProfitPercent = profitPercent;
                            targetLossProfitPercent = lossProfitPercent;
                            winPercent = result.getWinCount() * 1.0d / (result.getLoseCount() + result.getWinCount());
                        }
                    } else if (params.getType() == 3) {
                        double percent = result.getWinCount() * 1.0d / (result.getLoseCount() + result.getWinCount());
                        if (winPercent < percent && result.getXirr() > 0.05) {
                            maxDrawdown = result.getMaxDrawdown();
                            xirr = result.getXirr();
                            targetProfitPercent = profitPercent;
                            targetLossProfitPercent = lossProfitPercent;
                            winPercent = percent;
                        }
                    }
                }
            }
        } else {
            BacktestResult result = backTestWithParams(etfDailyPrices, optionDailyDatas, startTime, endTime, params.getProfitPercent(), params.getLossProfitPercent());
            maxDrawdown = result.getMaxDrawdown();
            xirr = result.getXirr();
            targetProfitPercent = result.getTargetProfitPercent();
            targetLossProfitPercent = result.getTargetLossProfitPercent();
            winPercent = result.getWinCount() * 1.0d / (result.getLoseCount() + result.getWinCount());
        }
        logger.info("Max xirr: " + xirr);
        logger.info("Max draw down: " + maxDrawdown);
        logger.info("High win percent: " + winPercent);
        logger.info("targetProfitPercent: " + targetProfitPercent);
        logger.info("targetLossProfitPercent: " + targetLossProfitPercent);
        return backTestResult;
    }

    private BacktestResult backTestWithParams(List<ETFDailyPrice> etfDailyPrices, List<OptionDailyData> optionDailyDatas, long startTime, long endTime, double profitPercent, double lossProfitPercent) throws Exception {
        BacktestResult result = new BacktestResult();
        ArrayList<UpbaaDate> upbaaDates = new ArrayList<>();
        List<Row> rowList = new ArrayList<>();
        List<String[]> outCSVData = new ArrayList<>();
        List<BigDecimal> amountList = new ArrayList<>();
        Double startAmount = 30000d;
        Double targetProfit = 0d;
        OptionDailyData currentCallOption = null;
        OptionDailyData currentPutOption = null;
        Map<String, List<OptionDailyData>> optionDailyDatasMap = optionDailyDatas.stream().collect(Collectors.groupingBy(OptionDailyData::getDate));
        Map<String, ETFDailyPrice> etfDailyPriceMap = etfDailyPrices.stream().collect(Collectors.toMap(key -> key.getDate(), value -> value));
        Date startDate = new Date(startTime);
        upbaaDates.add(new UpbaaDate(DateUtil.formatDate(startDate, DateUtil.DAY_PATTERN), (-30000d)));
        amountList.add(BigDecimal.valueOf(30000d));
        Integer callDiffDays = 0;
        Integer putDiffDays = 0;
        while (startDate.getTime() <= endTime) {
            String startDay = DateUtil.formatDate(startDate, DateUtil.DAY_OTHER_PATTERN);
            Double dailyOptionTotalValue = 0d;
            if (etfDailyPriceMap.containsKey(startDay)) {
                //find etf price
                ETFDailyPrice dayPrice = etfDailyPriceMap.get(startDay);
                List<OptionDailyData> dayOptions = optionDailyDatasMap.get(startDay);
                // find validate call option
                if (currentCallOption == null) {
                    OptionDailyData callOption = null;
                    if (true || callDiffDays <= 0) {
                        callOption = findMeetConditionCallOption(startDate, dayOptions);
                    }
                    if (callOption != null) {
                        currentCallOption = callOption;
                        targetProfit = DoubleUtil.sumOrCostCount(targetProfit, callOption.getClosePrice());
                        Date closeDate = DateUtil.parseDate(currentCallOption.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                        callDiffDays = DateUtil.differentDays(startDate, closeDate);
                        logger.info("Sell a call option: " + currentCallOption);
                    }
                } else {
                    //find current option price
                    OptionDailyData dayCallOption = findOption(currentCallOption.getOptionId(), dayOptions);
                    if (dayCallOption != null) {
                        dailyOptionTotalValue += dayCallOption.getClosePrice();
                    }
                }
                // find validate put option
                if (currentPutOption == null) {
                    OptionDailyData putOption = null;
                    if (true || putDiffDays <= 0) {
                        putOption = findMeetConditionPutOption(startDate, dayOptions);
                    }
                    if (putOption != null) {
                        currentPutOption = putOption;
                        targetProfit = DoubleUtil.sumOrCostCount(targetProfit, putOption.getClosePrice());
                        Date closeDate = DateUtil.parseDate(currentPutOption.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                        putDiffDays = DateUtil.differentDays(startDate, closeDate);
                        logger.info("Sell a put option: " + currentPutOption);
                    }
                } else {
                    // find current option price
                    OptionDailyData dayPutOption = findOption(currentPutOption.getOptionId(), dayOptions);
                    if (dayPutOption != null) {
                        dailyOptionTotalValue += dayPutOption.getClosePrice();
                    }
                }
            }
            if ("2016/02/01".equals(startDay)) {
                logger.info("2016/02/01");
            }

            if (dailyOptionTotalValue != 0d) {
                Double dailyProfit = DoubleUtil.sumOrCostCount(targetProfit, 0 - dailyOptionTotalValue);
                if (dailyProfit != 0d) {
                    logger.info("Day:" + startDay + " target profit: " + targetProfit + " daily profit: " + dailyProfit);
                }
                double dailyTotalProfit = dailyProfit * 10000 - EACH_OPTION_COST;
                if (targetProfit != 0d && (dailyProfit >= (targetProfit * profitPercent) || dailyProfit < (targetProfit * -lossProfitPercent))) {
                    if (dailyProfit < (targetProfit * -lossProfitPercent)) {
                        dailyProfit = (targetProfit * -lossProfitPercent);
                    }
                    // close option
                    startAmount = DoubleUtil.sumOrCostCount(startAmount, dailyTotalProfit);
                    currentCallOption = null;
                    currentPutOption = null;
                    targetProfit = 0d;
                    logger.info("Close Option Day:" + startDay + " daily profit: " + dailyTotalProfit);
                    logger.info("Total Amount:" + startAmount);
                    if (dailyTotalProfit > 0d) {
                        result.setWinCount(result.getWinCount() + 1);
                    } else {
                        result.setLoseCount(result.getLoseCount() + 1);
                    }
                    upbaaDates.add(new UpbaaDate(DateUtil.formatDate(startDate, DateUtil.DAY_PATTERN), dailyTotalProfit));
                    amountList.add(BigDecimal.valueOf(startAmount));
                    String[] csvData = new String[1];
                    //csvData[0] = startDay;
                    csvData[0] = String.valueOf(dailyTotalProfit);
                    outCSVData.add(csvData);
                } else {
                    if (currentCallOption != null) {
                        Date closeDate = DateUtil.parseDate(currentCallOption.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                        if (DateUtil.differentDays(startDate, closeDate) <= earlyCloseDay) {
                            // close option
                            startAmount = DoubleUtil.sumOrCostCount(startAmount, dailyTotalProfit);
                            currentCallOption = null;
                            currentPutOption = null;
                            targetProfit = 0d;
                            logger.info("Close Option Day:" + startDay + " daily profit: " + dailyTotalProfit);
                            logger.info("Total Amount:" + startAmount);
                            upbaaDates.add(new UpbaaDate(DateUtil.formatDate(startDate, DateUtil.DAY_PATTERN), dailyTotalProfit));
                            amountList.add(BigDecimal.valueOf(startAmount));
                            if (dailyTotalProfit > 0d) {
                                result.setWinCount(result.getWinCount() + 1);
                            } else {
                                result.setLoseCount(result.getLoseCount() + 1);
                            }
                            String[] csvData = new String[1];
                            //csvData[0] = startDay;
                            csvData[0] = String.valueOf(dailyTotalProfit);
                            outCSVData.add(csvData);
                        }
                    } else if (currentPutOption != null) {
                        Date closeDate = DateUtil.parseDate(currentPutOption.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                        if (DateUtil.differentDays(startDate, closeDate) <= earlyCloseDay) {
                            // close option
                            startAmount = DoubleUtil.sumOrCostCount(startAmount, dailyTotalProfit);
                            currentCallOption = null;
                            currentPutOption = null;
                            targetProfit = 0d;
                            logger.info("Close Option Day:" + startDay + " daily profit: " + dailyTotalProfit);
                            logger.info("Total Amount:" + startAmount);
                            upbaaDates.add(new UpbaaDate(DateUtil.formatDate(startDate, DateUtil.DAY_PATTERN), dailyTotalProfit));
                            amountList.add(BigDecimal.valueOf(startAmount));
                            if (dailyTotalProfit > 0d) {
                                result.setWinCount(result.getWinCount() + 1);
                            } else {
                                result.setLoseCount(result.getLoseCount() + 1);
                            }
                            String[] csvData = new String[1];
                            //csvData[0] = startDay;
                            csvData[0] = String.valueOf(dailyTotalProfit);
                            outCSVData.add(csvData);
                        }
                    }
                }
            }
            startDate = new Date(DateUtil.getNewDate(new Date(startDate.getTime()), 1));
            if (callDiffDays > 0) {
                callDiffDays--;
            }
            if (putDiffDays > 0) {
                putDiffDays--;
            }
        }
        upbaaDates.add(new UpbaaDate(DateUtil.formatDate(startDate, DateUtil.DAY_PATTERN), (startAmount)));
        double newXirr = new XirrData(upbaaDates).getXirr();
        BigDecimal drawdown = MaxDrawDownUtil.getMaxDrawDown(amountList);
        result.setMaxDrawdown(drawdown);
        result.setXirr(newXirr);
        result.setTargetProfitPercent(profitPercent);
        result.setTargetLossProfitPercent(lossProfitPercent);
        logger.info("ProfitPercent: " + profitPercent);
        logger.info("LossProfitPercent: " + lossProfitPercent);
        logger.info("xirr: " + newXirr);
        logger.info("Draw down: " + drawdown.toString());
        if (upbaaDates != null && upbaaDates.size() > 0) {
            for (UpbaaDate data : upbaaDates) {
                logger.info(" profit: " + data);
            }
        }
//        //获得一个工作薄
//        Workbook wb = new HSSFWorkbook();
//        //第一个sheet
//        Sheet sheet = wb.createSheet("sheet");
//        //创建第一行
//        Row row = sheet.createRow(0);

        CSVUtils.writeCSV("/home/oliver/jimmy/temp/data_export/analysis.xlsx", outCSVData);
        return result;
    }

    private OptionDailyData findOption(String optionId, List<OptionDailyData> dayOptions) {
        OptionDailyData targetOption = null;
        if (dayOptions != null && dayOptions.size() > 0) {
            for (OptionDailyData option : dayOptions) {
                if (optionId.equals(option.getOptionId())) {
                    targetOption = option;
                    break;
                }
            }
        }
        return targetOption;
    }

    private OptionDailyData findMeetConditionCallOption(Date dayDate, List<OptionDailyData> dayOptions) {
        OptionDailyData targetOption = null;
        if (dayOptions != null && dayOptions.size() > 0) {
            Double delta = 0d;
            for (OptionDailyData option : dayOptions) {
                if ("call".equals(option.getType())) {
                    Date closeDate = DateUtil.parseDate(option.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                    if (DateUtil.differentDays(dayDate, closeDate) >= 45 && DateUtil.differentDays(dayDate, closeDate) <= 60) {
                        // chose those option from 60
                        if (option.getDelta() < 0.35 && option.getDelta() > delta) {
                            delta = option.getDelta();
                            targetOption = option;
                        }
                    }
                }
            }
        }
        return targetOption;
    }

    private OptionDailyData findMeetConditionPutOption(Date dayDate, List<OptionDailyData> dayOptions) {
        OptionDailyData targetOption = null;
        if (dayOptions != null && dayOptions.size() > 0) {
            Double delta = 0d;
            for (OptionDailyData option : dayOptions) {
                if ("put".equals(option.getType())) {
                    Date closeDate = DateUtil.parseDate(option.getOptionCloseDay(), DateUtil.DAY_OTHER_PATTERN);
                    if (DateUtil.differentDays(dayDate, closeDate) >= 45 && DateUtil.differentDays(dayDate, closeDate) <= 60) {
                        // chose those option from 60
                        if (option.getDelta() > -0.35 && option.getDelta() < delta) {
                            delta = option.getDelta();
                            targetOption = option;
                        }
                    }
                }
            }
        }
        return targetOption;
    }
}