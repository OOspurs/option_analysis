package com.spurspro.option.dto.option;


import com.spurspro.option.utils.DoubleUtil;
import org.apache.commons.lang.StringUtils;

/**
 * Created by Jimmy.Liu on 2019-10-22 07:52
 *
 * @since 02.12.00
 */

public class ETFDailyPrice {
    private String id;
    private String name;
    private String date;
    private Double openPrice;
    private Double highPrice;
    private Double lowPrice;
    private Double closePrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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

    public ETFDailyPrice from(String[] row) {
        if (StringUtils.isBlank(row[0])) {
            return this;
        }
        this.id = row[0];
        this.name = row[1];
        this.date = row[2];
        this.openPrice = DoubleUtil.getDoubleValue(row[3]);
        this.highPrice = DoubleUtil.getDoubleValue(row[4]);
        this.lowPrice = DoubleUtil.getDoubleValue(row[5]);
        this.closePrice = DoubleUtil.getDoubleValue(row[6]);
        return this;
    }
}