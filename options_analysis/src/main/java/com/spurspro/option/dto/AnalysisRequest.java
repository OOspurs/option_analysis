package com.spurspro.option.dto;

import com.spurspro.option.dto.BaseRequest;

import java.io.Serializable;

public class AnalysisRequest extends BaseRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    private Boolean backTestDifferentParams = true;
    private Double profitPercent = 0.8;
    private Double lossProfitPercent = 0.1;
    private Double eachOptionCost = 2d;
    private Integer type = 1; //1: get lowest draw down, 2: get highest return

    private long startTime;
    private long endTime;

    private Integer earlyCloseDay = 21;

    public Integer getEarlyCloseDay() {
        return earlyCloseDay;
    }

    public void setEarlyCloseDay(Integer earlyCloseDay) {
        this.earlyCloseDay = earlyCloseDay;
    }

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

    public Double getLossProfitPercent() {
        return lossProfitPercent;
    }

    public Double getEachOptionCost() {
        return eachOptionCost;
    }

    public void setProfitPercent(Double profitPercent) {
        this.profitPercent = profitPercent;
    }

    public void setLossProfitPercent(Double lossProfitPercent) {
        this.lossProfitPercent = lossProfitPercent;
    }

    public void setEachOptionCost(Double eachOptionCost) {
        this.eachOptionCost = eachOptionCost;
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
}
