package com.store.utils;

import com.store.models.*;
import com.google.gson.*;
import java.util.ArrayList;
import java.util.List;

/**
 * JSON Parser utility
 */
public class JsonParser {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Parse Supplier từ JSON
     */
    public static Supplier parseSupplier(String json) {
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            Supplier supplier = new Supplier();
            
            if (obj.has("_id")) supplier.setId(obj.get("_id").getAsString());
            if (obj.has("code")) supplier.setCode(obj.get("code").getAsString());
            if (obj.has("name")) supplier.setName(obj.get("name").getAsString());
            if (obj.has("address")) supplier.setAddress(obj.get("address").getAsString());
            if (obj.has("phoneNumber")) supplier.setPhoneNumber(obj.get("phoneNumber").getAsString());
            
            return supplier;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse danh sách Supplier từ JSON
     */
    public static List<Supplier> parseSupplierList(String json) {
        List<Supplier> suppliers = new ArrayList<>();
        try {
            JsonArray array = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Supplier supplier = new Supplier();
                
                if (obj.has("_id")) supplier.setId(obj.get("_id").getAsString());
                if (obj.has("code")) supplier.setCode(obj.get("code").getAsString());
                if (obj.has("name")) supplier.setName(obj.get("name").getAsString());
                if (obj.has("address")) supplier.setAddress(obj.get("address").getAsString());
                if (obj.has("phoneNumber")) supplier.setPhoneNumber(obj.get("phoneNumber").getAsString());
                
                suppliers.add(supplier);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return suppliers;
    }

    /**
     * Parse Distributor từ JSON
     */
    public static Distributor parseDistributor(String json) {
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            Distributor distributor = new Distributor();
            
            if (obj.has("_id")) distributor.setId(obj.get("_id").getAsString());
            if (obj.has("code")) distributor.setCode(obj.get("code").getAsString());
            if (obj.has("brandName")) distributor.setBrandName(obj.get("brandName").getAsString());
            if (obj.has("address")) distributor.setAddress(obj.get("address").getAsString());
            if (obj.has("phoneNumber")) distributor.setPhoneNumber(obj.get("phoneNumber").getAsString());
            
            return distributor;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse danh sách Distributor từ JSON
     */
    public static List<Distributor> parseDistributorList(String json) {
        List<Distributor> distributors = new ArrayList<>();
        try {
            JsonArray array = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Distributor distributor = new Distributor();
                
                if (obj.has("_id")) distributor.setId(obj.get("_id").getAsString());
                if (obj.has("code")) distributor.setCode(obj.get("code").getAsString());
                if (obj.has("brandName")) distributor.setBrandName(obj.get("brandName").getAsString());
                if (obj.has("address")) distributor.setAddress(obj.get("address").getAsString());
                if (obj.has("phoneNumber")) distributor.setPhoneNumber(obj.get("phoneNumber").getAsString());
                
                distributors.add(distributor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return distributors;
    }

    /**
     * Parse Item từ JSON
     */
    public static Item parseItem(String json) {
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            Item item = new Item();
            
            if (obj.has("_id")) item.setId(obj.get("_id").getAsString());
            if (obj.has("code")) item.setCode(obj.get("code").getAsString());
            if (obj.has("name")) item.setName(obj.get("name").getAsString());
            if (obj.has("description")) item.setDescription(obj.get("description").getAsString());
            if (obj.has("quantity")) item.setQuantity(obj.get("quantity").getAsLong());
            if (obj.has("price")) item.setPrice(obj.get("price").getAsDouble());
            
            return item;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse danh sách Item từ JSON
     */
    public static List<Item> parseItemList(String json) {
        List<Item> items = new ArrayList<>();
        try {
            JsonArray array = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                Item item = new Item();
                
                if (obj.has("_id")) item.setId(obj.get("_id").getAsString());
                if (obj.has("code")) item.setCode(obj.get("code").getAsString());
                if (obj.has("name")) item.setName(obj.get("name").getAsString());
                if (obj.has("description")) item.setDescription(obj.get("description").getAsString());
                if (obj.has("quantity")) item.setQuantity(obj.get("quantity").getAsLong());
                if (obj.has("price")) item.setPrice(obj.get("price").getAsDouble());
                
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /**
     * Parse ImportReceipt từ JSON
     */
    public static ImportReceipt parseImportReceipt(String json) {
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            ImportReceipt receipt = new ImportReceipt();
            
            if (obj.has("_id")) receipt.setId(obj.get("_id").getAsString());
            if (obj.has("receiptNumber")) receipt.setReceiptNumber(obj.get("receiptNumber").getAsString());
            
            return receipt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse ExportReceipt từ JSON
     */
    public static ExportReceipt parseExportReceipt(String json) {
        try {
            JsonObject obj = com.google.gson.JsonParser.parseString(json).getAsJsonObject();
            ExportReceipt receipt = new ExportReceipt();
            
            if (obj.has("_id")) receipt.setId(obj.get("_id").getAsString());
            if (obj.has("receiptNumber")) receipt.setReceiptNumber(obj.get("receiptNumber").getAsString());
            
            return receipt;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Parse danh sách StatisticsItemData từ JSON
     */
    public static List<StatisticsItemData> parseItemStatisticsList(String json) {
        List<StatisticsItemData> dataList = new ArrayList<>();
        try {
            JsonArray array = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                StatisticsItemData data = new StatisticsItemData();
                
                if (obj.has("_id")) data.setItemId(obj.get("_id").getAsString());
                if (obj.has("code")) data.setItemCode(obj.get("code").getAsString());
                if (obj.has("name")) data.setItemName(obj.get("name").getAsString());
                if (obj.has("quantitySold")) data.setQuantitySold(obj.get("quantitySold").getAsLong());
                if (obj.has("totalRevenue")) data.setTotalRevenue(obj.get("totalRevenue").getAsDouble());
                
                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Parse danh sách StatisticsDistributorData từ JSON
     */
    public static List<StatisticsDistributorData> parseDistributorStatisticsList(String json) {
        List<StatisticsDistributorData> dataList = new ArrayList<>();
        try {
            JsonArray array = com.google.gson.JsonParser.parseString(json).getAsJsonArray();
            for (JsonElement element : array) {
                JsonObject obj = element.getAsJsonObject();
                StatisticsDistributorData data = new StatisticsDistributorData();
                
                if (obj.has("_id")) data.setDistributorId(obj.get("_id").getAsString());
                if (obj.has("code")) data.setDistributorCode(obj.get("code").getAsString());
                if (obj.has("brandName")) data.setDistributorName(obj.get("brandName").getAsString());
                if (obj.has("totalRevenue")) data.setTotalRevenue(obj.get("totalRevenue").getAsDouble());
                
                dataList.add(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }
}
