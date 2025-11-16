package com.store.services;

import com.store.models.*;
import com.store.utils.JsonParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Lớp APIClient xử lý tất cả các request tới backend
 */
public class APIClient {
    private static final String BASE_URL = "http://localhost:5000/api";
    private static final String TIMEOUT = "5000";

    /**
     * Gửi GET request đến backend
     */
    public static String sendGetRequest(String endpoint) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);

        StringBuilder response = new StringBuilder();
        if (connection.getResponseCode() == 200) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new Exception("HTTP " + connection.getResponseCode() + ": " + connection.getResponseMessage());
        }
    }

    /**
     * Gửi POST request đến backend
     */
    public static String sendPostRequest(String endpoint, String jsonBody) throws Exception {
        URL url = new URL(BASE_URL + endpoint);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.setDoOutput(true);

        try (OutputStream os = connection.getOutputStream()) {
            byte[] input = jsonBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }

        StringBuilder response = new StringBuilder();
        if (connection.getResponseCode() >= 200 && connection.getResponseCode() < 300) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            return response.toString();
        } else {
            throw new Exception("HTTP " + connection.getResponseCode() + ": " + connection.getResponseMessage());
        }
    }

    /**
     * Tìm kiếm nhà cung cấp theo tên
     */
    public static List<Supplier> searchSuppliers(String name) throws Exception {
        String endpoint = "/suppliers/search?searchTerm=" + java.net.URLEncoder.encode(name, "UTF-8");
        String response = sendGetRequest(endpoint);
        return parseSuppliersList(response);
    }

    /**
     * Tìm kiếm đại lý phụ theo tên
     */
    public static List<Distributor> searchDistributors(String name) throws Exception {
        String endpoint = "/distributors/search?searchTerm=" + java.net.URLEncoder.encode(name, "UTF-8");
        String response = sendGetRequest(endpoint);
        return parseDistributorsList(response);
    }

    /**
     * Tìm kiếm mặt hàng theo tên
     */
    public static List<Item> searchItems(String name) throws Exception {
        String endpoint = "/items/search?searchTerm=" + java.net.URLEncoder.encode(name, "UTF-8");
        String response = sendGetRequest(endpoint);
        return parseItemsList(response);
    }

    /**
     * Tạo nhà cung cấp mới
     */
    public static Supplier createSupplier(String code, String name, String address, String phoneNumber) throws Exception {
        String json = String.format(
            "{\"code\": \"%s\", \"name\": \"%s\", \"address\": \"%s\", \"phone\": \"%s\"}",
            code, name, address, phoneNumber
        );
        String response = sendPostRequest("/suppliers", json);
        return parseSupplier(response);
    }

    /**
     * Tạo đại lý phụ mới
     */
    public static Distributor createDistributor(String code, String brandName, String address, String phoneNumber) throws Exception {
        String json = String.format(
            "{\"code\": \"%s\", \"brand_name\": \"%s\", \"address\": \"%s\", \"phone\": \"%s\"}",
            code, brandName, address, phoneNumber
        );
        String response = sendPostRequest("/distributors", json);
        return parseDistributor(response);
    }

    /**
     * Tạo mặt hàng mới
     */
    public static Item createItem(String code, String name, String description) throws Exception {
        String json = String.format(
            "{\"code\": \"%s\", \"name\": \"%s\", \"description\": \"%s\"}",
            code, name, description
        );
        String response = sendPostRequest("/items", json);
        return parseItem(response);
    }

    /**
     * Tạo phiếu nhập
     */
    public static ImportReceipt createImportReceipt(ImportReceipt receipt) throws Exception {
        String json = convertImportReceiptToJson(receipt);
        String response = sendPostRequest("/import-receipts", json);
        return parseImportReceipt(response);
    }

    /**
     * Tạo phiếu xuất
     */
    public static ExportReceipt createExportReceipt(ExportReceipt receipt) throws Exception {
        String json = convertExportReceiptToJson(receipt);
        String response = sendPostRequest("/export-receipts", json);
        return parseExportReceipt(response);
    }

    /**
     * Lấy thống kê mặt hàng
     */
    public static List<StatisticsItemData> getItemStatistics(String startDate, String endDate) throws Exception {
        String endpoint = String.format("/statistics/items?startDate=%s&endDate=%s",
            java.net.URLEncoder.encode(startDate, "UTF-8"),
            java.net.URLEncoder.encode(endDate, "UTF-8")
        );
        String response = sendGetRequest(endpoint);
        return parseItemStatisticsList(response);
    }

    /**
     * Lấy thống kê đại lý phụ
     */
    public static List<StatisticsDistributorData> getDistributorStatistics(String startDate, String endDate) throws Exception {
        String endpoint = String.format("/statistics/distributors?startDate=%s&endDate=%s",
            java.net.URLEncoder.encode(startDate, "UTF-8"),
            java.net.URLEncoder.encode(endDate, "UTF-8")
        );
        String response = sendGetRequest(endpoint);
        return parseDistributorStatisticsList(response);
    }

    /**
     * Lấy chi tiết phiếu xuất của một đại lý
     */
    public static List<ExportReceipt> getDistributorExportDetails(String distributorId, String startDate, String endDate) throws Exception {
        String endpoint = String.format("/statistics/distributors/%s/details?startDate=%s&endDate=%s",
            distributorId,
            java.net.URLEncoder.encode(startDate, "UTF-8"),
            java.net.URLEncoder.encode(endDate, "UTF-8")
        );
        String response = sendGetRequest(endpoint);
        return parseExportReceiptsList(response);
    }

    /**
     * Lấy chi tiết phiếu nhập của một mặt hàng
     */
    public static List<ImportReceipt> getItemImportDetails(String itemId, String startDate, String endDate) throws Exception {
        String endpoint = String.format("/statistics/items/%s/details?startDate=%s&endDate=%s",
            itemId,
            java.net.URLEncoder.encode(startDate, "UTF-8"),
            java.net.URLEncoder.encode(endDate, "UTF-8")
        );
        String response = sendGetRequest(endpoint);
        return parseImportReceiptsList(response);
    }

    // ==================== Parsing Methods ====================

    private static Supplier parseSupplier(String json) {
        return JsonParser.parseSupplier(json);
    }

    private static Distributor parseDistributor(String json) {
        return JsonParser.parseDistributor(json);
    }

    private static Item parseItem(String json) {
        return JsonParser.parseItem(json);
    }

    private static List<Supplier> parseSuppliersList(String json) {
        return JsonParser.parseSupplierList(json);
    }

    private static List<Distributor> parseDistributorsList(String json) {
        return JsonParser.parseDistributorList(json);
    }

    private static List<Item> parseItemsList(String json) {
        return JsonParser.parseItemList(json);
    }

    private static ImportReceipt parseImportReceipt(String json) {
        return JsonParser.parseImportReceipt(json);
    }

    private static ExportReceipt parseExportReceipt(String json) {
        return JsonParser.parseExportReceipt(json);
    }

    private static List<StatisticsItemData> parseItemStatisticsList(String json) {
        return JsonParser.parseItemStatisticsList(json);
    }

    private static List<StatisticsDistributorData> parseDistributorStatisticsList(String json) {
        return JsonParser.parseDistributorStatisticsList(json);
    }

    private static List<ImportReceipt> parseImportReceiptsList(String json) {
        List<ImportReceipt> receipts = new ArrayList<>();
        // Thực hiện parsing tương tự
        return receipts;
    }

    private static List<ExportReceipt> parseExportReceiptsList(String json) {
        List<ExportReceipt> receipts = new ArrayList<>();
        // Thực hiện parsing tương tự
        return receipts;
    }

    private static String convertImportReceiptToJson(ImportReceipt receipt) {
        StringBuilder json = new StringBuilder("{");
        json.append("\"supplier_id\": \"").append(receipt.getSupplier().getId()).append("\",");
        json.append("\"items\": [");
        
        List<ImportReceiptItem> items = receipt.getItems();
        for (int i = 0; i < items.size(); i++) {
            ImportReceiptItem item = items.get(i);
            json.append("{\"item_id\": \"").append(item.getItemId()).append("\", ");
            json.append("\"quantity\": ").append(item.getQuantity()).append(", ");
            json.append("\"unit_price\": ").append(item.getUnitPrice()).append("}");
            if (i < items.size() - 1) json.append(",");
        }
        
        json.append("]");
        json.append("}");
        return json.toString();
    }

    private static String convertExportReceiptToJson(ExportReceipt receipt) {
        StringBuilder json = new StringBuilder("{");
        json.append("\"distributor_id\": \"").append(receipt.getDistributor().getId()).append("\",");
        json.append("\"items\": [");
        
        List<ExportReceiptItem> items = receipt.getItems();
        for (int i = 0; i < items.size(); i++) {
            ExportReceiptItem item = items.get(i);
            json.append("{\"item_id\": \"").append(item.getItemId()).append("\", ");
            json.append("\"quantity\": ").append(item.getQuantity()).append(", ");
            json.append("\"unit_price\": ").append(item.getUnitPrice()).append("}");
            if (i < items.size() - 1) json.append(",");
        }
        
        json.append("]");
        json.append("}");
        return json.toString();
    }
}
