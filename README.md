Jewelry Management System
Hệ thống quản lý cửa hàng trang sức được xây dựng bằng Java và MySQL.
Tính Năng
Khách Hàng

Xem danh sách sản phẩm trang sức
Tìm kiếm và lọc sản phẩm
Thêm sản phẩm vào giỏ hàng
Đặt hàng và thanh toán (COD, ZaloPay)
Xem lịch sử đơn hàng
Chatbot hỗ trợ 24/7

Quản Trị Viên

Quản lý sản phẩm (Thêm/Sửa/Xóa)
Quản lý đơn hàng
Quản lý khách hàng
Thống kê doanh thu

Công Nghệ

Backend: Java
Database: MySQL
Thanh toán: ZaloPay API
AI Chatbot: Claude AI (Anthropic)

Cài Đặt
Yêu Cầu

JDK 8 trở lên
MySQL 5.7+
Maven hoặc IDE (Eclipse, IntelliJ IDEA)

Các Bước

Clone repository

bashgit clone https://github.com/vanben277/jewelry-management.git
cd jewelry-management

Tạo database

sqlCREATE DATABASE jewelry_management;

Import database (nếu có file SQL)

bashmysql -u root -p jewelry_management < database.sql

Cấu hình kết nối database
Chỉnh sửa file cấu hình (ví dụ: application.properties hoặc db.properties):

propertiesdb.url=jdbc:mysql://localhost:3306/jewelry_management
db.username=root
db.password=your_password

Chạy ứng dụng

bash# Nếu dùng Maven
mvn clean install
mvn spring-boot:run

# Hoặc chạy file JAR
java -jar target/jewelry-management.jar