package com.spurspro.option.utils.fin;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Jimmy.Liu on 2019-10-23 09:59
 *
 * @since 02.12.00
 */

public class MaxDrawDownUtil {
    /**
     * 获取最大回撤率
     *
     * @param list
     * @return
     */
    public static BigDecimal getMaxDrawDown(List<BigDecimal> list) {
        // 只有数据量大于等于2个的时候才有回撤率
        if (list == null || list.size() <= 1) {
            return BigDecimal.ZERO;
        }

        // 获得数组的最小值
        BigDecimal minInRange = getMinInRange(list);
        // 第一个回撤率
        BigDecimal maxDrawDown = (list.get(0).subtract(minInRange)).divide(list.get(0), 4, BigDecimal.ROUND_HALF_UP);

        // 并不是每一次都需要计算最小值，一开始计算一次，等待下次达到最小值再计算
        boolean needCalculateMin = false;
        for (int i = 1; i < list.size() - 1; i++) {
            List<BigDecimal> subList = list.subList(i + 1, list.size());
            if (needCalculateMin) {
                minInRange = getMinInRange(subList);
            }

            // 到达最小值，下次需要计算最小值
            if (minInRange.compareTo(list.get(i)) == 0) {
                needCalculateMin = true;
            }else{
                needCalculateMin = false;
            }

            BigDecimal rate = (list.get(i).subtract(minInRange).divide(list.get(i), 4, BigDecimal.ROUND_HALF_UP));
            // 获得最大回撤率
            if (rate.compareTo(maxDrawDown) > 0) {
                maxDrawDown = rate;
            }
        }

        return maxDrawDown;
    }

    /**
     * 获取区间内最小值
     *
     * @param list
     * @return
     */
    public static BigDecimal getMinInRange(List<BigDecimal> list) {
        BigDecimal min = list.get(0);
        for (int i = 1; i < list.size(); i++) {
            if (list.get(i).compareTo(min) < 0) {
                min = list.get(i);
            }
        }

        return min;
    }

    // 测试方法
//    public static void main(String[] args) {
//        List<BigDecimal> list =
//                Lists.newArrayList(new BigDecimal(100), new BigDecimal(200), new BigDecimal(50), new BigDecimal(300),
//                        new BigDecimal(150), new BigDecimal(100), new BigDecimal(200));
//
//        System.out.println("最大回撤率:" + getMaxDrawDown(list));
//    }


}