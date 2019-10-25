package com.spurspro.option.dto.option;


import com.spurspro.option.service.impl.OptionsAnalysisServiceImpl;
import com.spurspro.option.utils.DoubleUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Created by Jimmy.Liu on 2019-10-22 08:01
 *
 * @since 02.12.00
 */

public class OptionDailyData {
    private static final Logger logger = LoggerFactory.getLogger(OptionDailyData.class);

    private String date;
    private String optionId;
    private String optionK;
    private String optionName;
    private Double volumeValue;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;
    private Double settlementPrice;
    private Double volume;
    private Double openInterest;
    private Double upLimit;
    private Double downLimit;
    private Double delta;
    private Double gamma;
    private Double vega;
    private Double theta;
    private Double rho;

    private String type; //call, put
    private String optionCloseDay; //close day

    public OptionDailyData from(String[] row) {
        if (StringUtils.isBlank(row[0])) {
            return this;
        }
        this.date = row[0];
        this.optionId = row[1];
        this.optionK = row[2];
        this.optionName = row[3];
        this.volumeValue = DoubleUtil.getDoubleValue(row[4]);
        this.openPrice = DoubleUtil.getDoubleValue(row[5]);
        this.highPrice = DoubleUtil.getDoubleValue(row[6]);
        this.lowPrice = DoubleUtil.getDoubleValue(row[7]);
        this.closePrice = DoubleUtil.getDoubleValue(row[8]);
        this.settlementPrice = DoubleUtil.getDoubleValue(row[9]);
        this.volume = DoubleUtil.getDoubleValue(row[10]);
        this.openInterest = DoubleUtil.getDoubleValue(row[11]);
        this.upLimit = DoubleUtil.getDoubleValue(row[12]);
        this.downLimit = DoubleUtil.getDoubleValue(row[13]);
        this.delta = DoubleUtil.getDoubleValue(row[14]);
        this.gamma = DoubleUtil.getDoubleValue(row[15]);
        this.vega = DoubleUtil.getDoubleValue(row[16]);
        this.theta = DoubleUtil.getDoubleValue(row[17]);
        this.rho = DoubleUtil.getDoubleValue(row[18]);

        if (StringUtils.contains(optionName, '购')) {
            this.type = "call";
        } else {
            this.type = "put";
        }
        String year = StringUtils.substring(optionName, 6, 10);
        String month = StringUtils.substring(optionName, 11, 12);
        if (NumberUtils.isNumber(StringUtils.substring(optionName, 11, 13))) {
            month = StringUtils.substring(optionName, 11, 13);
        }
        this.optionCloseDay = year + '/' + month + '/' + 28;
        return this;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOptionId() {
        return optionId;
    }

    public void setOptionId(String optionId) {
        this.optionId = optionId;
    }

    public String getOptionK() {
        return optionK;
    }

    public void setOptionK(String optionK) {
        this.optionK = optionK;
    }

    public String getOptionName() {
        return optionName;
    }

    public void setOptionName(String optionName) {
        this.optionName = optionName;
    }

    public Double getVolumeValue() {
        return volumeValue;
    }

    public void setVolumeValue(Double volumeValue) {
        this.volumeValue = volumeValue;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(Double openPrice) {
        this.openPrice = openPrice;
    }

    public Double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(Double highPrice) {
        this.highPrice = highPrice;
    }

    public Double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(Double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public Double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(Double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    public Double getVolume() {
        return volume;
    }

    public void setVolume(Double volume) {
        this.volume = volume;
    }

    public Double getOpenInterest() {
        return openInterest;
    }

    public void setOpenInterest(Double openInterest) {
        this.openInterest = openInterest;
    }

    public Double getUpLimit() {
        return upLimit;
    }

    public void setUpLimit(Double upLimit) {
        this.upLimit = upLimit;
    }

    public Double getDownLimit() {
        return downLimit;
    }

    public void setDownLimit(Double downLimit) {
        this.downLimit = downLimit;
    }

    public Double getDelta() {
        return delta;
    }

    public void setDelta(Double delta) {
        this.delta = delta;
    }

    public Double getGamma() {
        return gamma;
    }

    public void setGamma(Double gamma) {
        this.gamma = gamma;
    }

    public Double getVega() {
        return vega;
    }

    public void setVega(Double vega) {
        this.vega = vega;
    }

    public Double getTheta() {
        return theta;
    }

    public void setTheta(Double theta) {
        this.theta = theta;
    }

    public Double getRho() {
        return rho;
    }

    public void setRho(Double rho) {
        this.rho = rho;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOptionCloseDay() {
        return optionCloseDay;
    }

    public void setOptionCloseDay(String optionCloseDay) {
        this.optionCloseDay = optionCloseDay;
    }

    @Override
    public String toString() {
        return "OptionDailyData{" +
                "date='" + date + '\'' +
                ", optionId='" + optionId + '\'' +
                ", optionK='" + optionK + '\'' +
                ", optionName='" + optionName + '\'' +
                ", volumeValue=" + volumeValue +
                ", openPrice=" + openPrice +
                ", highPrice=" + highPrice +
                ", lowPrice=" + lowPrice +
                ", closePrice=" + closePrice +
                ", settlementPrice=" + settlementPrice +
                ", volume=" + volume +
                ", openInterest=" + openInterest +
                ", upLimit=" + upLimit +
                ", downLimit=" + downLimit +
                ", delta=" + delta +
                ", gamma=" + gamma +
                ", vega=" + vega +
                ", theta=" + theta +
                ", rho=" + rho +
                ", type='" + type + '\'' +
                ", optionCloseDay='" + optionCloseDay + '\'' +
                '}';
    }
}