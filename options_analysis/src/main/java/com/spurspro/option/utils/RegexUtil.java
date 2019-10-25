package com.spurspro.option.sassou.utils;

import org.apache.commons.lang.StringUtils;

/**
 * 正则表达式帮助类
 * Created by Jimmy.Liu on 2018-09-21 06:39
 *
 * @since 2.01.06
 */
public class RegexUtil {

    /**
     * @title
     * @description 替换正则表达式中的所有特殊字符
     * @author HUAZAI
     * @param
     *      <ul>
     *          <li></li>
     *          <li></li>
     *      <ul>
     * @return
     *      <ul>
     *          <li></li>
     *          <li></li>
     *      <ul>
     * @since 2.01.06
     */
    public static String replaceQueryStringAllRegExp(String str) {
        String[] from =
                {"\\", "*", "+", "|", "{", "}", "(", ")", "^", "$", "[", "]", "?", ",", ".", "&"};
        String[] to =
                {"\\\\", "\\*", "\\+", "\\|", "\\{", "\\}", "\\(", "\\)", "\\^", "\\$", "\\[", "\\]", "\\?", "\\,", "\\.", "\\&"};
        return StringUtils.replaceEach(str, from, to);
    }

}
