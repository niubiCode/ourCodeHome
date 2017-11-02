package com.cn.xuyhproject.number;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * @Author: XUYH
 * @Description:
 * @Date: 2017/11/2
 * @Version:
 */

public class NumberUtils {
    /**
     * 处理数字金额太大出现科学计数法
     * NumberFormat 形式小数位最大四位
     *
     * @param pFormatNum
     * @return
     */
    public static String numFormat(double pFormatNum){
        return numFormat(pFormatNum, 2);
    }

    public static String numFormat(String pFormatNum){
        return String.valueOf(numFormat(pFormatNum, 2)); // 经测试小数位最大为四位大于四取四位
    }

    public static String numFormat(String pFormatNum, int pLength){
        return numFormat(new Double(pFormatNum), pLength);
    }

    public static String numFormat(double pFormatNum, int pLength){
        NumberFormat pNumberFormat = NumberFormat.getInstance();
        pNumberFormat.setGroupingUsed(false);
        pNumberFormat.setMaximumFractionDigits(pLength);
        return pNumberFormat.format(pFormatNum);
    }

    /**
     * 处理科学计数法
     *
     * DecimalFormat 形式，小数位根据需求传入格式确定，有效位为四位，第四位小数根据第五位值四舍五入取值，后面补零
     * @param d
     * @return
     */
    public static String decimalFormat(double d) {
        return decimalFormat(d, "0.00");
    }
    public static String decimalFormat(String d) {
        return decimalFormat(d, "0.00");
    }
    public static String decimalFormat(String d, String format) {
        return decimalFormat(new Double(d), format);
    }
    public static String decimalFormat(double d, String format) {
        DecimalFormat decimalFormat = new DecimalFormat(format);//格式化设置
        return decimalFormat.format(d);
    }

    /**
     * 处理科学计数法
     * BigDecimal 小数精度四位，第四位的值根据第五位四舍五入后得到,第五位以后失真
     * @param num
     * @return
     */
    public static String scientificNotation2String(double num) {
        return scientificNotation2String(num, 2);
    }
    public static String scientificNotation2String(String str) {
        return scientificNotation2String(str, 2);
    }
    public static String scientificNotation2String(String str, int length) {
        return scientificNotation2String(new Double(str), length);
    }
    public static String scientificNotation2String(double number, int length) {
        BigDecimal bd = new BigDecimal(number);
        BigDecimal value = bd.setScale(length, BigDecimal.ROUND_HALF_UP);
        return value.toPlainString();
    }
}
