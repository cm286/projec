# Hướng dẫn Chạy Ứng dụng

## Yêu cầu Tiên quyết
- JDK 11 trở lên đã cài đặt
- Backend server đang chạy tại `http://localhost:3000`
- Maven 3.6+ (tùy chọn)

## Cách 1: Chạy từ IDE (Đơn giản nhất)

### IntelliJ IDEA
1. Mở project folder trong IntelliJ
2. Chờ IDE index các file
3. Right-click vào `StoreManagementApplication.java`
4. Chọn "Run StoreManagementApplication"

### Eclipse
1. Mở project trong Eclipse
2. Right-click vào `StoreManagementApplication.java`
3. Chọn "Run As" → "Java Application"

### NetBeans
1. Mở project trong NetBeans
2. Right-click vào project → "Run"

## Cách 2: Chạy từ Maven (Command Line)

### Bước 1: Compile
```bash
cd c:\Users\Admin\Lập Trình Hướng Đối Tượng\store-management-frontend
mvn clean compile
```

### Bước 2: Chạy trực tiếp
```bash
mvn exec:java -Dexec.mainClass="com.store.StoreManagementApplication"
```

### Hoặc: Tạo JAR và chạy
```bash
mvn clean package
java -jar target/store-management-frontend-1.0.0-shaded.jar
```

## Cách 3: Compile & Run Manual (Java)

### Bước 1: Tạo thư mục output
```bash
mkdir bin
```

### Bước 2: Compile tất cả các file Java
```bash
javac -d bin src/main/java/com/store/**/*.java
```

### Bước 3: Chạy ứng dụng
```bash
java -cp bin com.store.StoreManagementApplication
```

## Troubleshooting

### Lỗi: "Cannot find symbol" hoặc "Exception in thread"
- Đảm bảo tất cả file Java đã được compile
- Kiểm tra classpath có chứa tất cả package không
- Đảm bảo Java version >= 11

### Lỗi: "Connection refused" hoặc "Unable to connect to localhost:3000"
- Kiểm tra backend server đã khởi động chưa
- Kiểm tra port 3000 có được sử dụng bởi application khác không
- Thử telnet localhost 3000 để test kết nối

### Giao diện không hiển thị hoặc lag
- Đảm bảo đủ RAM cho JVM
- Thử tắt các ứng dụng nền khác
- Kiểm tra tốc độ mạng

### Dữ liệu không lưu được
- Kiểm tra lại backend API endpoint có chính xác không
- Xem console log để tìm chi tiết lỗi
- Đảm bảo database (MongoDB) đang chạy

## Các Phím Tắt Hữu Ích (Khi mở)

| Phím | Chức năng |
|------|----------|
| Ctrl+Q | Thoát ứng dụng (tuỳ thuộc UI) |
| Tab | Di chuyển giữa các field |
| Enter | Xác nhận |
| Escape | Hủy/Đóng dialog |

## File Cấu hình (nếu cần)

Hiện tại ứng dụng không có file cấu hình. Nếu muốn sửa đổi:

### Để sửa base URL API
Mở file `APIClient.java`, tìm dòng:
```java
private static final String BASE_URL = "http://localhost:3000/api";
```

Sửa thành URL của bạn và compile lại.

## Next Steps

Sau khi chạy thành công:
1. Thử tìm kiếm nhà cung cấp
2. Tạo một phiếu nhập test
3. Tạo một phiếu xuất test
4. Xem thống kê

Good luck! 🚀
