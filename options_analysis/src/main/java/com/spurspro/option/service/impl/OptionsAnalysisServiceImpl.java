package com.spurspro.option.service.impl;

import com.spurspro.option.dto.AnalysisRequest;
import com.spurspro.option.dto.AnalysisResponse;
import com.spurspro.option.dto.option.ETFDailyPrice;
import com.spurspro.option.dto.option.OptionDailyData;
import com.spurspro.option.service.OptionsAnalysisService;
import com.spurspro.option.strategy.Strategy;
import com.spurspro.option.strategy.map.BackTestParams;
import com.spurspro.option.utils.csv.CSVUtils;
import com.spurspro.option.utils.excel.dom.ExcelUtil;
import com.spurspro.option.utils.excel.sax.SaxExcelUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * SmyyDataImportServiceImpl
 * Created by Jimmy.Liu on 2018-09-18 09:48
 *
 * @since 2.01.06
 */
@Service
public class OptionsAnalysisServiceImpl implements OptionsAnalysisService {
    private static final Logger logger = LoggerFactory.getLogger(OptionsAnalysisServiceImpl.class);

    @Resource(name="StrangleStrategy")
    private Strategy strategy;

    @Override
    public AnalysisResponse analysisOptions(AnalysisRequest request) throws Exception {
        String methodDesc = "Analysis Options";
        AnalysisResponse res = new AnalysisResponse();
        try {
            //read 50etf price data
            String filePath = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/50etf.csv";
            List<ETFDailyPrice> etfDatas = readEtfPriceDatas(filePath);

            //read option data years
            List<OptionDailyData> optionDatas = new ArrayList<>();
            String filePath1 = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/2015.csv";
            optionDatas.addAll(readOptionDatas(filePath1));
            String filePath2 = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/2016.csv";
            optionDatas.addAll(readOptionDatas(filePath2));
            String filePath3 = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/2017.csv";
            optionDatas.addAll(readOptionDatas(filePath3));
            String filePath4 = "/home/oliver/jimmy/sources/github/options_analysis/src/main/resources/data/2018.csv";
            optionDatas.addAll(readOptionDatas(filePath4));

            //backtest strategy
            BackTestParams params = new BackTestParams();
            BeanUtils.copyProperties(request, params);
            params.setEtfDailyPrices(etfDatas);
            params.setOptionDailyDatas(optionDatas);
            strategy.backTest(params);

            //output analysis result

            res.setSuccess(Boolean.TRUE);
            res.setMessage(methodDesc+"成功");
        } catch (Exception e) {
            logger.error(methodDesc + "失败", e);
            throw new Exception(methodDesc + "失败");
        }
        return res;
    }

    private List<ETFDailyPrice> readEtfPriceDatas(String filePath) throws Exception {
        List<String[]> rows = CSVUtils.readCSV(filePath);
        logger.info("rows count: " + rows.size());
        List<ETFDailyPrice> datas = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            ETFDailyPrice data = new ETFDailyPrice();
            data = data.from(row);
            if (StringUtils.isNotBlank(data.getId())) {
                datas.add(data);
            }
        }
        return datas;
    }

    private List<OptionDailyData> readOptionDatas(String filePath) throws Exception {
        List<String[]> rows = CSVUtils.readCSV(filePath);
        logger.info("rows count: " + rows.size());
        List<OptionDailyData> datas = new ArrayList<>();
        for (int i = 1; i < rows.size(); i++) {
            String[] row = rows.get(i);
            OptionDailyData data = new OptionDailyData();
            data = data.from(row);
            if (StringUtils.isNotBlank(data.getDate())) {
                datas.add(data.from(row));
            }
        }
        return datas;
    }
}
