# Tổng Quan Kiến Trúc Ứng Dụng

## Kiến Trúc Tổng Thể

```
┌─────────────────────────────────────────────────────────────┐
│               StoreManagementApplication (Main)             │
│                    (JFrame - Cửa sổ chính)                  │
└──────────────────┬──────────────────────────────────────────┘
                   │
        ┌──────────┼──────────┐
        ▼          ▼          ▼
   ┌────────┐ ┌────────┐ ┌────────────┐
   │Import  │ │Export  │ │Statistics  │
   │Panel   │ │Panel   │ │Panel       │
   └─┬──────┘ └─┬──────┘ └────┬───────┘
     │          │             │
     ▼          ▼             ▼
┌──────────────────────────────────────┐
│            APIClient                 │
│    (Gọi backend API qua HTTP)        │
└──────────┬───────────────────────────┘
           │
           ▼
    Backend Server
    (Node.js/Express)
        │
        ▼
    MongoDB
```

## Cấu Trúc Thư Mục

```
src/main/java/com/store/
├── StoreManagementApplication.java
│   └── Main entry point, khởi tạo giao diện chính
│
├── models/
│   ├── Item.java                      # Mặt hàng
│   ├── Supplier.java                  # Nhà cung cấp
│   ├── Distributor.java               # Đại lý phụ
│   ├── ImportReceipt.java             # Phiếu nhập
│   ├── ImportReceiptItem.java         # Chi tiết mặt hàng trong phiếu nhập
│   ├── ExportReceipt.java             # Phiếu xuất
│   ├── ExportReceiptItem.java         # Chi tiết mặt hàng trong phiếu xuất
│   ├── StatisticsItemData.java        # Dữ liệu thống kê mặt hàng
│   └── StatisticsDistributorData.java # Dữ liệu thống kê đại lý
│
├── services/
│   └── APIClient.java
│       ├── Gửi HTTP requests
│       ├── Parse JSON responses
│       └── Xử lý các API endpoints
│
├── ui/
│   ├── ImportPanel.java               # Module nhập hàng
│   │   ├── SupplierSelectionPanel
│   │   ├── ItemSearchPanel
│   │   └── Danh sách hàng nhập
│   │
│   ├── ExportPanel.java               # Module xuất hàng
│   │   ├── DistributorSelectionPanel
│   │   ├── ItemSearchPanel
│   │   └── Danh sách hàng xuất
│   │
│   ├── StatisticsPanel.java           # Module thống kê
│   │   ├── StatisticsItemPanel
│   │   └── StatisticsDistributorPanel
│   │
│   ├── SupplierSelectionPanel.java    # Tìm/chọn nhà cung cấp
│   ├── CreateSupplierDialog.java      # Tạo nhà cung cấp mới
│   │
│   ├── DistributorSelectionPanel.java # Tìm/chọn đại lý phụ
│   ├── CreateDistributorDialog.java   # Tạo đại lý mới
│   │
│   ├── ItemSearchPanel.java           # Tìm/chọn mặt hàng
│   ├── CreateItemDialog.java          # Tạo mặt hàng mới
│   │
│   ├── StatisticsItemPanel.java       # Thống kê mặt hàng
│   └── StatisticsDistributorPanel.java # Thống kê đại lý phụ
│
└── utils/
    ├── DateUtils.java                 # Tiện ích xử lý ngày tháng
    └── JsonParser.java                # Tiện ích parse JSON
```

## Luồng Dữ Liệu

### 1. Module Nhập Hàng

```
User chọn "Nhập hàng"
    ▼
ImportPanel hiển thị
    ▼
User tìm/chọn Supplier
    ▼ APIClient.searchSuppliers()
    ▼ Backend: GET /api/suppliers/search
    ▼ JsonParser.parseSupplierList()
    ▼
Hiển thị danh sách Supplier
    ▼
User chọn 1 Supplier
    ▼
ItemSearchPanel kích hoạt
    ▼
User tìm/chọn Item
    ▼ APIClient.searchItems()
    ▼ Backend: GET /api/items/search
    ▼
Hiển thị danh sách Item
    ▼
User chọn Item + nhập SL + giá
    ▼
Thêm ImportReceiptItem vào ImportReceipt
    ▼
(Lặp lại cho item khác)
    ▼
User nhấn "Nộp phiếu nhập"
    ▼ APIClient.createImportReceipt()
    ▼ Backend: POST /api/import-receipts
    ▼
Lưu thành công + in phiếu
```

### 2. Module Xuất Hàng

```
User chọn "Xuất hàng"
    ▼
ExportPanel hiển thị
    ▼
User tìm/chọn Distributor
    ▼ APIClient.searchDistributors()
    ▼ Backend: GET /api/distributors/search
    ▼
Hiển thị danh sách Distributor
    ▼
User chọn 1 Distributor
    ▼
ItemSearchPanel kích hoạt
    ▼
User tìm/chọn Item (kiểm tra SL trong kho)
    ▼ APIClient.searchItems()
    ▼ Backend: GET /api/items/search
    ▼
Hiển thị danh sách Item
    ▼
User chọn Item + nhập SL (≤ SL kho) + giá
    ▼
Thêm ExportReceiptItem vào ExportReceipt
    ▼
(Lặp lại cho item khác)
    ▼
User nhấn "Nộp phiếu xuất"
    ▼ APIClient.createExportReceipt()
    ▼ Backend: POST /api/export-receipts
    ▼
Lưu thành công + in phiếu
```

### 3. Module Thống Kê

```
User chọn "Thống kê"
    ▼
StatisticsPanel hiển thị 2 tab
    ▼
━━ Tab 1: Thống kê mặt hàng ━━
User chọn từ ngày - đến ngày
    ▼
User nhấn "Tìm kiếm"
    ▼ APIClient.getItemStatistics()
    ▼ Backend: GET /api/statistics/items
    ▼ JsonParser.parseItemStatisticsList()
    ▼
Hiển thị danh sách Item (sắp xếp theo doanh thu giảm dần)
    ▼
User click vào 1 Item
    ▼ APIClient.getItemImportDetails()
    ▼ Backend: GET /api/statistics/items/{itemId}/imports
    ▼
Hiển thị chi tiết phiếu nhập của Item đó
    
━━ Tab 2: Thống kê đại lý phụ ━━
User chọn từ ngày - đến ngày
    ▼
User nhấn "Tìm kiếm"
    ▼ APIClient.getDistributorStatistics()
    ▼ Backend: GET /api/statistics/distributors
    ▼ JsonParser.parseDistributorStatisticsList()
    ▼
Hiển thị danh sách Distributor (sắp xếp theo doanh thu giảm dần)
    ▼
User click vào 1 Distributor
    ▼ APIClient.getDistributorExportDetails()
    ▼ Backend: GET /api/statistics/distributors/{distId}/exports
    ▼
Hiển thị chi tiết phiếu xuất của Distributor đó
```

## Chi Tiết Các Lớp Model

### Item
Đại diện một mặt hàng trong kho
- Thuộc tính: id, code, name, description, quantity, price
- Phương thức: getter/setter, toString()

### Supplier
Đại diện nhà cung cấp
- Thuộc tính: id, code, name, address, phoneNumber
- Phương thức: getter/setter, toString()

### Distributor
Đại diện đại lý phụ
- Thuộc tính: id, code, brandName, address, phoneNumber
- Phương thức: getter/setter, toString()

### ImportReceipt & ExportReceipt
Đại diện phiếu nhập/xuất
- Thuộc tính: id, receiptNumber, date, supplier/distributor, items list, totalAmount
- Phương thức:
  - addItem(): thêm item vào phiếu
  - removeItem(): xóa item khỏi phiếu
  - calculateTotalAmount(): tính tổng tiền tự động
  - getItemCount(): lấy số lượng mặt hàng

### ImportReceiptItem & ExportReceiptItem
Chi tiết từng mặt hàng trong phiếu
- Thuộc tính: itemId, itemCode, itemName, quantity, unitPrice, totalAmount
- Phương thức: calculateTotalAmount() tự động khi quantity/price thay đổi

## Chi Tiết APIClient

### Phương thức Public
- `searchSuppliers(name)`: Tìm nhà cung cấp
- `searchDistributors(name)`: Tìm đại lý
- `searchItems(name)`: Tìm mặt hàng
- `createSupplier()`: Tạo nhà cung cấp mới
- `createDistributor()`: Tạo đại lý mới
- `createItem()`: Tạo mặt hàng mới
- `createImportReceipt()`: Nộp phiếu nhập
- `createExportReceipt()`: Nộp phiếu xuất
- `getItemStatistics()`: Lấy thống kê mặt hàng
- `getDistributorStatistics()`: Lấy thống kê đại lý
- `getDistributorExportDetails()`: Lấy chi tiết xuất
- `getItemImportDetails()`: Lấy chi tiết nhập

### Phương thức Private
- `sendGetRequest()`: Gửi GET request
- `sendPostRequest()`: Gửi POST request
- `parseXxx()`: Parse JSON responses

## Pattern & Design

### 1. Observer Pattern (Implicit)
- UI components lắng nghe events từ user
- Callback listeners (onSupplierSelected, onItemSelected, etc.)

### 2. Model-View Separation
- Models: Lưu dữ liệu (Item, Supplier, etc.)
- UI: Hiển thị dữ liệu (ImportPanel, ExportPanel, etc.)
- Services: Xử lý logic (APIClient, JsonParser)

### 3. Singleton Pattern (Implicit)
- APIClient có static methods (như singleton functionality)
- JsonParser có static methods

### 4. Dialog Pattern
- CreateSupplierDialog, CreateDistributorDialog, CreateItemDialog
- Các dialog ở chế độ modal (blocking)

## Sự Phụ Thuộc Giữa Các Lớp

```
StoreManagementApplication
    │
    ├── ImportPanel
    │   ├── SupplierSelectionPanel
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── ItemSearchPanel
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── CreateSupplierDialog
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── CreateItemDialog
    │   │   └── APIClient
    │   │       └── JsonParser
    │   └── ImportReceipt, ImportReceiptItem (Models)
    │
    ├── ExportPanel
    │   ├── DistributorSelectionPanel
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── ItemSearchPanel
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── CreateDistributorDialog
    │   │   └── APIClient
    │   │       └── JsonParser
    │   ├── CreateItemDialog
    │   │   └── APIClient
    │   │       └── JsonParser
    │   └── ExportReceipt, ExportReceiptItem (Models)
    │
    └── StatisticsPanel
        ├── StatisticsItemPanel
        │   └── APIClient
        │       └── JsonParser
        └── StatisticsDistributorPanel
            └── APIClient
                └── JsonParser
```

## Configuration & Constants

### APIClient
```java
private static final String BASE_URL = "http://localhost:3000/api";
```
Để thay đổi, sửa giá trị này và recompile.

### Timeouts
```java
connection.setConnectTimeout(5000);  // 5 giây
connection.setReadTimeout(5000);     // 5 giây
```

## Thread Safety

Hiện tại, ứng dụng chạy trên Swing's Event Dispatch Thread (EDT).
- UI updates an toàn
- Để tránh blocking, nên dùng SwingWorker cho long-running operations

## Error Handling

```
User Action
    ▼
Try-Catch Block
    ├─ Success: JOptionPane.showMessageDialog (success)
    └─ Exception: JOptionPane.showMessageDialog (error)
         └─ Stack trace in console
```

## Mở Rộng & Bảo Trì

### Để thêm tính năng mới:
1. Tạo Model class nếu cần
2. Thêm method vào APIClient
3. Thêm JsonParser.parseXxx() nếu cần
4. Tạo UI Panel/Dialog
5. Integrate vào main application

### Để fix bug:
1. Check console log cho stack trace
2. Verify backend API responses
3. Test từng component riêng
4. Update test cases

---

Hy vọng tài liệu này giúp bạn hiểu rõ kiến trúc ứng dụng! 🚀
