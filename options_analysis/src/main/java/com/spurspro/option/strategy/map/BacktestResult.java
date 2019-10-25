package com.spurspro.option.strategy.map;


import io.swagger.models.auth.In;

import java.math.BigDecimal;

/**
 * Created by Jimmy.Liu on 2019-10-22 09:06
 *
 * @since 02.12.00
 */

public class BacktestResult {

    private Double xirr = 0d;
    private  BigDecimal maxDrawdown = new BigDecimal(1);
    private  Double targetProfitPercent = 0d;
    private  Double targetLossProfitPercent = 0d;
    private Integer winCount = 0;
    private Integer loseCount = 0;

    public Integer getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(Integer loseCount) {
        this.loseCount = loseCount;
    }

    public Integer getWinCount() {
        return winCount;
    }

    public void setWinCount(Integer winCount) {
        this.winCount = winCount;
    }

    public Double getXirr() {
        return xirr;
    }

    public void setXirr(Double xirr) {
        this.xirr = xirr;
    }

    public BigDecimal getMaxDrawdown() {
        return maxDrawdown;
    }

    public void setMaxDrawdown(BigDecimal maxDrawdown) {
        this.maxDrawdown = maxDrawdown;
    }

    public Double getTargetProfitPercent() {
        return targetProfitPercent;
    }

    public void setTargetProfitPercent(Double targetProfitPercent) {
        this.targetProfitPercent = targetProfitPercent;
    }

    @Override
    public String toString() {
        return "BacktestResult{" +
                "xirr=" + xirr +
                ", maxDrawdown=" + maxDrawdown +
                ", targetProfitPercent=" + targetProfitPercent +
                ", targetLossProfitPercent=" + targetLossProfitPercent +
                ", winCount=" + winCount +
                ", loseCount=" + loseCount +
                '}';
    }

    public Double getTargetLossProfitPercent() {
        return targetLossProfitPercent;
    }

    public void setTargetLossProfitPercent(Double targetLossProfitPercent) {
        this.targetLossProfitPercent = targetLossProfitPercent;
    }
}