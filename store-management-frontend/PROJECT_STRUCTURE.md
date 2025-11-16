# 📁 Project Structure Visualization

## Complete Directory Tree

```
store-management-frontend/
│
├── 📄 Documentation Files (6 files)
│   ├── START_HERE.md                 ⭐ BEGIN HERE! Overview & quick guide
│   ├── README.md                     📖 Complete user guide
│   ├── HƯỚNG_DẪN_CHẠY.md            🚀 How to run (3 methods)
│   ├── KIẾN_TRÚC.md                 🏗️  Architecture & design
│   ├── PROJECT_SUMMARY.md            📊 Project overview
│   ├── COMPLETION_REPORT.md          ✅ Requirements verification
│   ├── INDEX.md                      📚 Documentation index
│   └── PROJECT_STRUCTURE.md          This file
│
├── 🔧 Configuration & Build
│   └── pom.xml                       Maven configuration with Gson
│
└── 📦 Source Code (src/main/java/com/store/)
    │
    ├── 🎯 Main Application
    │   └── StoreManagementApplication.java        ENTRY POINT
    │       ├── Main window (JFrame)
    │       ├── Menu bar (File, Quản lý, Thống kê, Trợ giúp)
    │       └── TabbedPane (3 modules)
    │
    ├── 📋 Models (9 Classes - Data Entities)
    │   ├── Item.java
    │   │   └── Attributes: id, code, name, description, quantity, price
    │   │
    │   ├── Supplier.java
    │   │   └── Attributes: id, code, name, address, phoneNumber
    │   │
    │   ├── Distributor.java
    │   │   └── Attributes: id, code, brandName, address, phoneNumber
    │   │
    │   ├── ImportReceipt.java
    │   │   ├── Attributes: id, receiptNumber, importDate, supplier, items[], totalAmount
    │   │   └── Methods: addItem(), removeItem(), calculateTotalAmount()
    │   │
    │   ├── ImportReceiptItem.java
    │   │   ├── Attributes: itemId, itemCode, itemName, quantity, unitPrice, totalAmount
    │   │   └── Methods: calculateTotalAmount() [auto-calc when qty/price changes]
    │   │
    │   ├── ExportReceipt.java
    │   │   ├── Attributes: id, receiptNumber, exportDate, distributor, items[], totalAmount
    │   │   └── Methods: addItem(), removeItem(), calculateTotalAmount()
    │   │
    │   ├── ExportReceiptItem.java
    │   │   ├── Attributes: itemId, itemCode, itemName, quantity, unitPrice, totalAmount
    │   │   └── Methods: calculateTotalAmount()
    │   │
    │   ├── StatisticsItemData.java
    │   │   └── Attributes: itemId, itemCode, itemName, quantitySold, totalRevenue
    │   │
    │   └── StatisticsDistributorData.java
    │       └── Attributes: distributorId, distributorCode, distributorName, totalRevenue
    │
    ├── 🔌 Services (API & Data)
    │   ├── APIClient.java
    │   │   ├── Connectivity Methods
    │   │   │   ├── sendGetRequest(endpoint) → HTTP GET
    │   │   │   └── sendPostRequest(endpoint, jsonBody) → HTTP POST
    │   │   │
    │   │   ├── Search Methods
    │   │   │   ├── searchSuppliers(name)
    │   │   │   ├── searchDistributors(name)
    │   │   │   └── searchItems(name)
    │   │   │
    │   │   ├── Create Methods
    │   │   │   ├── createSupplier(...)
    │   │   │   ├── createDistributor(...)
    │   │   │   └── createItem(...)
    │   │   │
    │   │   ├── Receipt Methods
    │   │   │   ├── createImportReceipt(receipt)
    │   │   │   └── createExportReceipt(receipt)
    │   │   │
    │   │   ├── Statistics Methods
    │   │   │   ├── getItemStatistics(startDate, endDate)
    │   │   │   ├── getDistributorStatistics(startDate, endDate)
    │   │   │   ├── getItemImportDetails(itemId, startDate, endDate)
    │   │   │   └── getDistributorExportDetails(distributorId, startDate, endDate)
    │   │   │
    │   │   └── Parsing Methods
    │   │       └── parseXxx() [delegated to JsonParser]
    │   │
    │   └── (JsonParser is in utils/)
    │
    ├── 🎨 UI Components (12 Classes - Swing GUI)
    │   │
    │   ├── ✏️ Module 1: Nhập Hàng
    │   │   ├── ImportPanel.java
    │   │   │   ├── SupplierSelectionPanel
    │   │   │   ├── ItemSearchPanel
    │   │   │   ├── Input spinners (Quantity, Price)
    │   │   │   ├── JTable (import items list)
    │   │   │   └── Submit button + total display
    │   │   │
    │   │   ├── SupplierSelectionPanel.java
    │   │   │   ├── Search textfield
    │   │   │   ├── JTable (suppliers)
    │   │   │   ├── Select button
    │   │   │   └── Add new button → CreateSupplierDialog
    │   │   │
    │   │   ├── CreateSupplierDialog.java
    │   │   │   ├── Code field
    │   │   │   ├── Name field
    │   │   │   ├── Address field
    │   │   │   ├── Phone field
    │   │   │   └── Save/Cancel buttons
    │   │   │
    │   │   └── (ItemSearchPanel & CreateItemDialog below)
    │   │
    │   ├── 📤 Module 2: Xuất Hàng
    │   │   ├── ExportPanel.java
    │   │   │   ├── DistributorSelectionPanel
    │   │   │   ├── ItemSearchPanel (with qty validation)
    │   │   │   ├── Input spinners
    │   │   │   ├── JTable (export items list)
    │   │   │   └── Submit button + total display
    │   │   │
    │   │   ├── DistributorSelectionPanel.java
    │   │   │   ├── Search textfield
    │   │   │   ├── JTable (distributors)
    │   │   │   ├── Select button
    │   │   │   └── Add new button → CreateDistributorDialog
    │   │   │
    │   │   └── CreateDistributorDialog.java
    │   │       ├── Code field
    │   │       ├── Brand name field
    │   │       ├── Address field
    │   │       ├── Phone field
    │   │       └── Save/Cancel buttons
    │   │
    │   ├── 📊 Module 3: Thống Kê
    │   │   ├── StatisticsPanel.java
    │   │   │   ├── Tab 1: StatisticsItemPanel
    │   │   │   └── Tab 2: StatisticsDistributorPanel
    │   │   │
    │   │   ├── StatisticsItemPanel.java
    │   │   │   ├── Date range selectors (Từ - Đến)
    │   │   │   ├── Search button
    │   │   │   ├── JTable (Items: Mã, Tên, Qty sold, Revenue)
    │   │   │   └── Detail dialog on click (Import receipts)
    │   │   │
    │   │   └── StatisticsDistributorPanel.java
    │   │       ├── Date range selectors
    │   │       ├── Search button
    │   │       ├── JTable (Distributors: Mã, Tên, Revenue)
    │   │       └── Detail dialog on click (Export receipts)
    │   │
    │   ├── 🔍 Shared Components
    │   │   ├── ItemSearchPanel.java
    │   │   │   ├── Search textfield
    │   │   │   ├── JTable (items)
    │   │   │   ├── Select button
    │   │   │   └── Add new button → CreateItemDialog
    │   │   │
    │   │   └── CreateItemDialog.java
    │   │       ├── Code field
    │   │       ├── Name field
    │   │       ├── Description field
    │   │       └── Save/Cancel buttons
    │   │
    │   └── 📌 Listener Interfaces (defined in panels)
    │       ├── SupplierSelectionListener
    │       │   ├── onSupplierSelected(Supplier)
    │       │   └── onAddNewSupplier()
    │       │
    │       ├── DistributorSelectionListener
    │       │   ├── onDistributorSelected(Distributor)
    │       │   └── onAddNewDistributor()
    │       │
    │       └── ItemSelectionListener
    │           ├── onItemSelected(Item)
    │           └── onAddNewItem()
    │
    └── 🛠️ Utils (2 Classes - Helper Functions)
        │
        ├── DateUtils.java
        │   ├── dateToString(Date) → "yyyy-MM-dd"
        │   ├── stringToDate(String) → Date
        │   ├── formatCurrency(double) → "XXX.XX VNĐ"
        │   └── parseCurrency(String) → double
        │
        └── JsonParser.java
            ├── Supplier parsers
            │   ├── parseSupplier(json) → Supplier
            │   └── parseSupplierList(json) → List<Supplier>
            │
            ├── Distributor parsers
            │   ├── parseDistributor(json) → Distributor
            │   └── parseDistributorList(json) → List<Distributor>
            │
            ├── Item parsers
            │   ├── parseItem(json) → Item
            │   └── parseItemList(json) → List<Item>
            │
            ├── Receipt parsers
            │   ├── parseImportReceipt(json) → ImportReceipt
            │   ├── parseExportReceipt(json) → ExportReceipt
            │   ├── parseImportReceiptList(json) → List<ImportReceipt>
            │   └── parseExportReceiptList(json) → List<ExportReceipt>
            │
            └── Statistics parsers
                ├── parseItemStatisticsList(json) → List<StatisticsItemData>
                └── parseDistributorStatisticsList(json) → List<StatisticsDistributorData>
```

---

## 📊 Package Breakdown

### Package: `com.store` (Main)
```
└── StoreManagementApplication.java  [100 lines]
```

### Package: `com.store.models` [9 classes, ~780 lines]
```
├── Item.java                      [63 lines]
├── Supplier.java                  [73 lines]
├── Distributor.java               [73 lines]
├── ImportReceipt.java             [135 lines]
├── ImportReceiptItem.java         [85 lines]
├── ExportReceipt.java             [135 lines]
├── ExportReceiptItem.java         [85 lines]
├── StatisticsItemData.java        [62 lines]
└── StatisticsDistributorData.java [62 lines]
```

### Package: `com.store.services` [1 main class]
```
└── APIClient.java                 [270 lines]
    ├── 2 connection methods
    ├── 3 search methods
    ├── 3 create methods
    ├── 2 receipt methods
    ├── 4 statistics methods
    └── 12 parsing methods (delegated)
```

### Package: `com.store.ui` [12 classes, ~1,315 lines]
```
├── Main Application
│   └── ImportPanel.java             [280 lines]
│   └── ExportPanel.java             [280 lines]
│
├── Statistics
│   ├── StatisticsPanel.java         [40 lines]
│   ├── StatisticsItemPanel.java     [140 lines]
│   └── StatisticsDistributorPanel.java [140 lines]
│
├── Supplier Management
│   ├── SupplierSelectionPanel.java  [110 lines]
│   └── CreateSupplierDialog.java    [70 lines]
│
├── Distributor Management
│   ├── DistributorSelectionPanel.java [110 lines]
│   └── CreateDistributorDialog.java [70 lines]
│
├── Item Management
│   ├── ItemSearchPanel.java         [110 lines]
│   └── CreateItemDialog.java        [65 lines]
```

### Package: `com.store.utils` [2 classes, ~230 lines]
```
├── DateUtils.java                  [30 lines]
└── JsonParser.java                 [200 lines]
```

---

## 🔄 Data Flow Diagram

### Import Module Flow
```
User Action
    ↓
ImportPanel
    ├─ SupplierSelectionPanel
    │  └─ APIClient.searchSuppliers()
    │     └─ JsonParser.parseSupplierList()
    │
    ├─ ItemSearchPanel (multiple)
    │  └─ APIClient.searchItems()
    │     └─ JsonParser.parseItemList()
    │
    └─ Submit
       └─ APIClient.createImportReceipt()
          └─ Backend: POST /api/import-receipts
```

### Export Module Flow
```
User Action
    ↓
ExportPanel
    ├─ DistributorSelectionPanel
    │  └─ APIClient.searchDistributors()
    │     └─ JsonParser.parseDistributorList()
    │
    ├─ ItemSearchPanel (multiple)
    │  └─ APIClient.searchItems()
    │     └─ JsonParser.parseItemList()
    │
    └─ Submit
       └─ APIClient.createExportReceipt()
          └─ Backend: POST /api/export-receipts
```

### Statistics Module Flow
```
User Action
    ↓
StatisticsPanel
    ├─ StatisticsItemPanel
    │  └─ APIClient.getItemStatistics()
    │     ├─ JsonParser.parseItemStatisticsList()
    │     └─ Click item → APIClient.getItemImportDetails()
    │
    └─ StatisticsDistributorPanel
       └─ APIClient.getDistributorStatistics()
          ├─ JsonParser.parseDistributorStatisticsList()
          └─ Click distributor → APIClient.getDistributorExportDetails()
```

---

## 📦 Dependency Map

```
StoreManagementApplication
    │
    ├─ ImportPanel
    │  ├─ SupplierSelectionPanel ──┬─ APIClient
    │  ├─ ItemSearchPanel ─────────┤
    │  ├─ CreateSupplierDialog ────┤
    │  ├─ CreateItemDialog ────────┤
    │  └─ ImportReceipt ─────────┐ └─ JsonParser
    │     └─ ImportReceiptItem    │
    │
    ├─ ExportPanel
    │  ├─ DistributorSelectionPanel ┬─ APIClient
    │  ├─ ItemSearchPanel ──────────┤
    │  ├─ CreateDistributorDialog ──┤
    │  ├─ CreateItemDialog ─────────┤
    │  └─ ExportReceipt ──────────┐ └─ JsonParser
    │     └─ ExportReceiptItem     │
    │
    └─ StatisticsPanel
       ├─ StatisticsItemPanel ─────┬─ APIClient
       └─ StatisticsDistributorPanel ┤
                                      └─ JsonParser
```

---

## 🎯 Class Responsibilities

| Class | Responsibility | Lines |
|-------|------------------|-------|
| StoreManagementApplication | Main window, menu, tabs | 100 |
| ImportPanel | Manage import workflow | 280 |
| ExportPanel | Manage export workflow | 280 |
| APIClient | HTTP communication | 270 |
| Item | Item entity | 63 |
| Supplier | Supplier entity | 73 |
| Distributor | Distributor entity | 73 |
| ImportReceipt | Receipt container + logic | 135 |
| ExportReceipt | Receipt container + logic | 135 |
| JsonParser | JSON parsing utility | 200 |
| DateUtils | Date utility | 30 |
| 8 other UI classes | Dialogs & panels | ~800 |

---

## 💾 File Count Summary

```
Total Java Files:        24
Total Documentation:      7
Total Config Files:       1
────────────────────────────
Total Files:             32

Total Lines:
  - Java Code:         ~2,500
  - Documentation:     ~1,800
  - Config:              ~50
  ──────────────
  Total:              ~4,350
```

---

## 🔗 Important Relationships

```
Entities (Models)
    │
    ├─ Item (used by ImportReceiptItem, ExportReceiptItem)
    ├─ Supplier (used by ImportReceipt)
    ├─ Distributor (used by ExportReceipt)
    │
    ├─ ImportReceipt (contains: Supplier + List<ImportReceiptItem>)
    │   └─ ImportReceiptItem (contains: Item info + qty/price)
    │
    ├─ ExportReceipt (contains: Distributor + List<ExportReceiptItem>)
    │   └─ ExportReceiptItem (contains: Item info + qty/price)
    │
    └─ Statistics Data (StatisticsItemData, StatisticsDistributorData)

UI (Views)
    │
    ├─ ImportPanel (uses: Supplier, Item, ImportReceipt)
    ├─ ExportPanel (uses: Distributor, Item, ExportReceipt)
    └─ StatisticsPanel (uses: StatisticsItemData, StatisticsDistributorData)

Services
    │
    └─ APIClient (communicates with Backend)
       └─ JsonParser (parses responses into Models)
```

---

**Created:** November 16, 2025  
**Version:** 1.0.0  
**Status:** ✅ Complete
