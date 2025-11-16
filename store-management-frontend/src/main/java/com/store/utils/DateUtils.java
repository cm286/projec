package com.store.utils;

/**
 * Utility class chứa các hàm tiện ích
 */
public class DateUtils {
    /**
     * Convert java.util.Date to ISO 8601 format string
     */
    public static String dateToString(java.util.Date date) {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
    }

    /**
     * Convert ISO 8601 format string to java.util.Date
     */
    public static java.util.Date stringToDate(String dateStr) throws Exception {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(dateStr);
    }

    /**
     * Format currency
     */
    public static String formatCurrency(double amount) {
        return String.format("%.2f VNĐ", amount);
    }

    /**
     * Parse currency string
     */
    public static double parseCurrency(String amount) {
        amount = amount.replace(" VNĐ", "").trim();
        return Double.parseDouble(amount);
    }
}
