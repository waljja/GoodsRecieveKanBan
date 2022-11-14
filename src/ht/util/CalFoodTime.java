package ht.util;

/**
 * 封装工具类，减去用餐时间
 * @author 丁国钊
 * @date 2022-11-8
 * @version 1.0.0
 */
public class CalFoodTime {
    /** 上班、下班时间 */
    private static final String HOUR_8 = "8:00";
    private static final String HOUR_12 = "12:00";
    private static final String HOUR_13 = "13:00";
    private static final String HOUR_17 = "17:00";
    private static final String HOUR_18 = "18:00";
    private static final String HOUR_21 = "21:00";

    /**
     * 减去 用餐时间
     * @param startDateTime
     * @param endDateTime
     * @return 时间
     */
    public int calFoodTime(String startDateTime, String endDateTime) {
        int min = 0;
        // 从第11位开始剪切（时分秒）
        int subIndex = 11;
        if(startDateTime.substring(subIndex).compareTo(HOUR_12) < 0) {
            if(endDateTime.substring(subIndex).compareTo(HOUR_18) >= 0){
                min = min - 120;
            }else if(endDateTime.substring(subIndex).compareTo(HOUR_13) >= 0){
                min = min - 60;
            }
        }else if(startDateTime.substring(subIndex).compareTo(HOUR_13) < 0) {
            int grnTimeTo12 = Integer.parseInt(startDateTime.substring(14, 16));
            if(endDateTime.substring(subIndex).compareTo(HOUR_18) >= 0){
                min = min - 120 + grnTimeTo12;
            }else if(endDateTime.substring(subIndex).compareTo(HOUR_13) >= 0){
                min = min - 60 + grnTimeTo12;
            }
        }else if(startDateTime.substring(subIndex).compareTo(HOUR_17) < 0) {
            if(endDateTime.substring(subIndex).compareTo(HOUR_18) >= 0){
                min = min - 60;
            }
        }else if(startDateTime.substring(subIndex).compareTo(HOUR_18) < 0) {
            int grnTimeTo17 = Integer.parseInt(startDateTime.substring(14, 16));
            if(endDateTime.substring(subIndex).compareTo(HOUR_18) >= 0){
                min = min - 60 + grnTimeTo17;
            }
        }
        return min;
    }
}
