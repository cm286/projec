# Hệ thống Quản lý Cửa hàng - Frontend Java Swing

## Mô tả
Ứng dụng desktop Java Swing cung cấp giao diện quản lý cửa hàng với các chức năng:
- **Nhập hàng**: Quản lý phiếu nhập từ nhà cung cấp
- **Xuất hàng**: Quản lý phiếu xuất cho đại lý phụ
- **Thống kê**: Phân tích doanh thu theo mặt hàng và đại lý phụ

## Cấu trúc Project

```
store-management-frontend/
├── pom.xml                          # Maven configuration
├── src/main/java/com/store/
│   ├── StoreManagementApplication.java  # Main application class
│   ├── models/                      # Classes đại diện cho dữ liệu
│   │   ├── Item.java
│   │   ├── Supplier.java
│   │   ├── Distributor.java
│   │   ├── ImportReceipt.java
│   │   ├── ImportReceiptItem.java
│   │   ├── ExportReceipt.java
│   │   ├── ExportReceiptItem.java
│   │   ├── StatisticsItemData.java
│   │   └── StatisticsDistributorData.java
│   ├── services/                    # API Client
│   │   └── APIClient.java           # Gọi backend API
│   └── ui/                          # UI Components
│       ├── ImportPanel.java
│       ├── ExportPanel.java
│       ├── StatisticsPanel.java
│       ├── StatisticsItemPanel.java
│       ├── StatisticsDistributorPanel.java
│       ├── SupplierSelectionPanel.java
│       ├── DistributorSelectionPanel.java
│       ├── ItemSearchPanel.java
│       ├── CreateSupplierDialog.java
│       ├── CreateDistributorDialog.java
│       └── CreateItemDialog.java
└── README.md
```

## Yêu cầu Hệ thống
- **Java**: JDK 11 hoặc cao hơn
- **Maven**: 3.6 hoặc cao hơn (nếu dùng Maven)
- **Backend**: Server Node.js/Express đang chạy tại `http://localhost:3000`

## Cài đặt

### 1. Sử dụng Maven
```bash
# Compile project
mvn clean compile

# Chạy ứng dụng
mvn exec:java -Dexec.mainClass="com.store.StoreManagementApplication"

# Tạo JAR file
mvn clean package
java -jar target/store-management-frontend-1.0.0-shaded.jar
```

### 2. Dùng IDE (IntelliJ IDEA hoặc Eclipse)
1. Mở project trong IDE
2. Cài đặt dependencies (nếu cần)
3. Run `StoreManagementApplication.java`

## Hướng dẫn Sử dụng

### 1. Module Nhập hàng
1. Chọn tab "Nhập hàng"
2. Tìm kiếm nhà cung cấp bằng tên
   - Nếu chưa có, nhấn "Thêm mới" để tạo nhà cung cấp mới
3. Chọn nhà cung cấp
4. Tìm kiếm mặt hàng theo tên
   - Nếu chưa có, nhấn "Thêm mới" để tạo mặt hàng mới
5. Chọn mặt hàng, nhập số lượng và đơn giá
6. Nhấn "Thêm mặt hàng" để thêm vào phiếu
7. Lặp lại bước 4-6 cho các mặt hàng khác
8. Nhấn "Nộp phiếu nhập" để lưu
9. Xem trước phiếu sẽ được in ra

### 2. Module Xuất hàng
1. Chọn tab "Xuất hàng"
2. Tìm kiếm đại lý phụ bằng tên
   - Nếu chưa có, nhấn "Thêm mới" để tạo đại lý mới
3. Chọn đại lý phụ
4. Tìm kiếm mặt hàng theo tên
5. Chọn mặt hàng, nhập số lượng (không vượt quá trong kho) và đơn giá
6. Nhấn "Thêm mặt hàng"
7. Lặp lại bước 4-6 cho các mặt hàng khác
8. Nhấn "Nộp phiếu xuất" để lưu
9. Xem trước phiếu sẽ được in ra

### 3. Module Thống kê
#### Thống kê mặt hàng:
1. Chọn tab "Thống kê" → "Thống kê mặt hàng"
2. Chọn khoảng thời gian (từ ngày - đến ngày)
3. Nhấn "Tìm kiếm"
4. Xem danh sách mặt hàng theo doanh thu (cao nhất đến thấp nhất)
5. Click vào một dòng để xem chi tiết phiếu nhập

#### Thống kê đại lý phụ:
1. Chọn tab "Thống kê" → "Thống kê đại lý phụ"
2. Chọn khoảng thời gian
3. Nhấn "Tìm kiếm"
4. Xem danh sách đại lý theo doanh thu
5. Click vào một dòng để xem chi tiết phiếu xuất

## Cấu trúc Dữ liệu

### Item (Mặt hàng)
- `id`: ID từ MongoDB
- `code`: Mã mặt hàng
- `name`: Tên mặt hàng
- `description`: Mô tả
- `quantity`: Số lượng trong kho
- `price`: Giá hiện tại

### Supplier (Nhà cung cấp)
- `id`: ID từ MongoDB
- `code`: Mã nhà cung cấp
- `name`: Tên nhà cung cấp
- `address`: Địa chỉ
- `phoneNumber`: Số điện thoại

### Distributor (Đại lý phụ)
- `id`: ID từ MongoDB
- `code`: Mã đại lý
- `brandName`: Tên thương hiệu
- `address`: Địa chỉ
- `phoneNumber`: Số điện thoại

### ImportReceipt (Phiếu nhập)
- `id`: ID từ MongoDB
- `receiptNumber`: Số phiếu
- `importDate`: Ngày nhập
- `supplier`: Nhà cung cấp
- `items`: Danh sách mặt hàng nhập
- `totalAmount`: Tổng tiền

### ExportReceipt (Phiếu xuất)
- `id`: ID từ MongoDB
- `receiptNumber`: Số phiếu
- `exportDate`: Ngày xuất
- `distributor`: Đại lý phụ
- `items`: Danh sách mặt hàng xuất
- `totalAmount`: Tổng tiền

## API Endpoints

Ứng dụng gọi các endpoint sau từ backend:

### Suppliers
- `GET /api/suppliers/search?name={name}` - Tìm nhà cung cấp
- `POST /api/suppliers` - Tạo nhà cung cấp mới

### Distributors
- `GET /api/distributors/search?name={name}` - Tìm đại lý phụ
- `POST /api/distributors` - Tạo đại lý mới

### Items
- `GET /api/items/search?name={name}` - Tìm mặt hàng
- `POST /api/items` - Tạo mặt hàng mới

### Import Receipts
- `POST /api/import-receipts` - Tạo phiếu nhập mới

### Export Receipts
- `POST /api/export-receipts` - Tạo phiếu xuất mới

### Statistics
- `GET /api/statistics/items?startDate={date}&endDate={date}` - Thống kê mặt hàng
- `GET /api/statistics/distributors?startDate={date}&endDate={date}` - Thống kê đại lý
- `GET /api/statistics/items/{itemId}/imports?startDate={date}&endDate={date}` - Chi tiết nhập
- `GET /api/statistics/distributors/{distId}/exports?startDate={date}&endDate={date}` - Chi tiết xuất

## Ghi chú

1. **Parsing JSON**: Hiện tại ứng dụng dùng string parsing đơn giản. Để tối ưu, nên thêm thư viện Gson hoặc Jackson để parse JSON.

2. **Error Handling**: Các lỗi sẽ hiển thị trong dialog message. Kiểm tra console để xem chi tiết lỗi.

3. **Connection**: Đảm bảo backend server đang chạy tại `http://localhost:3000` trước khi khởi động ứng dụng.

4. **Print**: Các phiếu in sẽ hiển thị dạng text preview trong dialog. Bạn có thể copy hoặc in từ đó.

## Phát triển tiếp

### Cải tiến có thể:
- Thêm authentication/login
- Cải tiến UI bằng Look and Feel khác
- Thêm chức năng in PDF
- Thêm chức năng export Excel
- Cải tiến JSON parsing bằng Gson/Jackson
- Thêm tính năng tìm kiếm nâng cao
- Thêm notification system

## License
Copyright © 2025. All rights reserved.

## Support
Nếu có vấn đề, vui lòng kiểm tra:
1. Backend server có chạy không
2. Port 3000 có được sử dụng không
3. Kết nối mạng có ổn định không
4. Java version có phù hợp không
