package com.spurspro.option.utils;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @Description: Double类型操作
 * Created by Jericho.Ou on 2018-06-22 06:10
 * @since 2.00.00
 */
public class DoubleUtil {
    private static final Logger logger = LoggerFactory.getLogger(DoubleUtil.class);

    public final static int DEF_DIV_SCALE = 2;

    /**
     * 进行加减，并变为2位小数
     *
     * @param value
     * @param addValue
     * @return
     * @Since 2.00.00
     */
    public static Double sumOrCostCount(Double value, Double addValue) {
        return com.spurspro.option.utils.DoubleUtil.toFixed(BigDecimal.valueOf(value).add(BigDecimal.valueOf(addValue)).doubleValue());
    }

    public static Double getDoubleValue(Double value) {
        if (value == null) {
            return 0d;
        } else {
            return value;
        }
    }

    public static Double toFixed(Double request) {
        return com.spurspro.option.utils.DoubleUtil.toFixed(request, 2);
    }

    public static Double toFixed(Double request, int length) {
        return Double.parseDouble(String.format("%." + String.valueOf(length) + "f", (request)));
    }

    /**
     * 两个Double数相除
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     * @since 2.00.00
     */
    public static Double div(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.divide(b2, DEF_DIV_SCALE, BigDecimal.ROUND_HALF_UP)
                .doubleValue());
    }

    /**
     * * 两个Double数相乘 *
     *
     * @param v1 *
     * @param v2 *
     * @return Double
     * @since 2.00.00
     */
    public static Double mul(Double v1, Double v2) {
        BigDecimal b1 = new BigDecimal(v1.toString());
        BigDecimal b2 = new BigDecimal(v2.toString());
        return new Double(b1.multiply(b2).doubleValue());
    }

    /**
     * 两个double 比较
     * @param val1
     * @param val2
     * @since 2.01.06
     */
    public static int compare(double val1, double val2) {
        BigDecimal data1 = new BigDecimal(val1);
        BigDecimal data2 = new BigDecimal(val2);
        return data1.compareTo(data2);
    }

    /**
     * 两个double是否相等
     * @param a
     * @param b
     * @return
     * @Since 2.10.0
     */
    public static boolean isEqual(double a, double b) {
        if (Double.isNaN(a) || Double.isNaN(b) || Double.isInfinite(a) || Double.isInfinite(b)) {
            return false;
        }
        return Math.abs(a - b) < 0.001d;
    }

    public static double getDoubleValue(String data) {
        double value = 0d;
        try {
            if (StringUtils.isNotBlank(data)) {
                data = data.replaceAll(",", "");
                value = Double.parseDouble(data);
            }
        } catch (Exception e) {
            logger.error("getDoubleValue error: ", e);
        }
        return value;
    }
}
