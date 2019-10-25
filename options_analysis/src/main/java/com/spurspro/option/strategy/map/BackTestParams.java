package com.spurspro.option.strategy.map;


import com.spurspro.option.dto.option.ETFDailyPrice;
import com.spurspro.option.dto.option.OptionDailyData;

import java.util.List;

/**
 * Created by Jimmy.Liu on 2019-10-24 03:45
 *
 * @since 02.12.00
 */

public class BackTestParams {

    private Boolean backTestDifferentParams = true;
    private Integer type = 1; //1: get lowest draw down, 2: get highest return, 3: get highest win count
    private Double profitPercent = 0.8;
    private Double lossProfitPercent = 0.1;
    private Double eachOptionCost = 2d;
    private Integer earlyCloseDay = 21;

    private List<ETFDailyPrice> etfDailyPrices;
    private List<OptionDailyData> optionDailyDatas;
    private long startTime;
    private long endTime;

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getBackTestDifferentParams() {
        return backTestDifferentParams;
    }

    public void setBackTestDifferentParams(Boolean backTestDifferentParams) {
        this.backTestDifferentParams = backTestDifferentParams;
    }


    public Double getProfitPercent() {
        return profitPercent;
    }

    public void setProfitPercent(Double profitPercent) {
        this.profitPercent = profitPercent;
    }

    public Double getLossProfitPercent() {
        return lossProfitPercent;
    }

    public void setLossProfitPercent(Double lossProfitPercent) {
        this.lossProfitPercent = lossProfitPercent;
    }

    public Double getEachOptionCost() {
        return eachOptionCost;
    }

    public void setEachOptionCost(Double eachOptionCost) {
        this.eachOptionCost = eachOptionCost;
    }

    public List<ETFDailyPrice> getEtfDailyPrices() {
        return etfDailyPrices;
    }

    public void setEtfDailyPrices(List<ETFDailyPrice> etfDailyPrices) {
        this.etfDailyPrices = etfDailyPrices;
    }

    public List<OptionDailyData> getOptionDailyDatas() {
        return optionDailyDatas;
    }

    public void setOptionDailyDatas(List<OptionDailyData> optionDailyDatas) {
        this.optionDailyDatas = optionDailyDatas;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public Integer getEarlyCloseDay() {
        return earlyCloseDay;
    }

    public void setEarlyCloseDay(Integer earlyCloseDay) {
        this.earlyCloseDay = earlyCloseDay;
    }
}