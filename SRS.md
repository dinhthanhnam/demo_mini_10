TÀI LIỆU ĐẶC TẢ YÊU CẦU PHẦN MỀM (SRS)

Tên dự án: Hệ thống Quản lý Mượn/Trả Thiết bị IT & Phòng Lab
Đối tượng thực hiện: Sinh viên Khóa học Java Web 
Mô hình phát triển: MVC (Model - View - Controller)

I. Tổng quan dự án
Mục đích dự án
Hiện tại, việc sinh viên đăng ký mượn các thiết bị (Màn hình rời, Cáp kết nối, Board mạch...) hoặc đăng ký sử dụng phòng Lab ngoài giờ lên lớp đang được quản lý thủ công qua giấy tờ, dẫn đến việc khó kiểm soát số lượng tồn kho và dễ trùng lịch. Dự án này nhằm xây dựng một Web Portal nội bộ cho phép sinh viên xem danh sách thiết bị/phòng Lab hiện có và điền form đăng ký mượn/sử dụng trực tuyến

Ràng buộc công nghệ (Nền tảng cốt lõi)
Dự án bắt buộc phải xây dựng dựa trên nền tảng kỹ thuật sau:
Framework: Pure Spring WebMVC (phiên bản 7.x).
Cấu hình: Bắt buộc cấu hình thủ công (Java Config hoặc XML) cho DispatcherServlet, ViewResolver, Validator
(Tuyệt đối không sử dụng Spring Boot)
View Template: Thymeleaf (tích hợp Spring 6/7).
Data Binding & Validation: Hibernate Validator (JSR-380)

II. Phân quyền người dùng
Hệ thống phục vụ 2 nhóm người dùng chính:
Sinh viên (Student): Người xem danh mục thiết bị/phòng Lab và thực hiện các thao tác điền form đăng ký mượn
Quản lý Lab (Lab Admin): Người xem danh sách các yêu cầu mượn từ sinh viên để chuẩn bị thiết bị

III. Yêu cầu chức năng (Functional Requirements)
Module Sinh viên (Người dùng cuối)
REQ-S01: Xem danh mục. Hiển thị danh sách các thiết bị hoặc phòng Lab đang có sẵn trên hệ thống (Tên, Hình ảnh minh họa, Số lượng còn lại trong kho).
REQ-S02: Form Đăng ký mượn (Chức năng cốt lõi). Sinh viên click vào một thiết bị/phòng để mở form đăng ký. Form yêu cầu các thông tin:
Họ và tên sinh viên
Mã sinh viên
Email liên hệ
Số lượng thiết bị muốn mượn (Nếu mượn phòng Lab thì mặc định là 1)
Ngày dự kiến nhận thiết bị / Ngày sử dụng phòng
Ngày dự kiến trả
Lý do mượn (Textarea)
Có thể tuỳ biến thêm ( sẽ được điểm cộng )

Module Quản lý Lab (Admin)
REQ-A01: Quản lý danh sách thiết bị
REQ-A02: Xem danh sách yêu cầu

IV. Ràng buộc Nghiệp vụ & Xác thực dữ liệu
Phần này yêu cầu sinh viên phải sử dụng Data Binding và các Annotation Validation trong Spring MVC
VAL-01 (Bắt buộc nhập): Tất cả các trường trong form đăng ký mượn đều không được để trống
VAL-02 (Logic Thời gian): "Ngày dự kiến nhận" phải là ngày ở tương lai (sau ngày hiện tại)
"Ngày dự kiến trả" phải diễn ra sau "Ngày dự kiến nhận"
VAL-03 (Logic Số lượng): "Số lượng muốn mượn" phải là số nguyên dương và không được vượt quá số lượng tồn kho hiện tại của thiết bị đó
VAL-04 (Định dạng Dữ liệu): Trường Email phải đúng định dạng, Mã sinh viên phải tuân theo format cố định (Ví dụ: Bắt đầu bằng 2 chữ cái, theo sau là các chữ số)
VAL-05 (Xử lý lỗi View): Khi sinh viên nhập sai, hệ thống không được crash trắng trang mà phải trả về lại đúng form đó, giữ nguyên các thông tin người dùng đã nhập, đồng thời hiển thị câu thông báo lỗi (bằng Tiếng Việt) ngay bên dưới trường dữ liệu bị sai

V. Yêu cầu Giao diện (UI & View Layer)
UI-01 (Layout): Yêu cầu sử dụng tính năng Fragment của Thymeleaf để tách biệt giao diện chung xuất hiện nhiều lần
UI-02: Giao diện không cần quá phức tạp nhưng phải sử dụng HTML5/CSS3 hoặc một thư viện CSS cơ bản (Bootstrap/Tailwind) để form nhập liệu nhìn gọn gàng, rõ ràng

VI. Tiêu chí Đánh giá Mở rộng (Bonus/Sáng tạo)
Dành cho các nhóm sinh viên muốn lấy điểm xuất sắc:
Bonus 1: Xây dựng Custom Validator (Tự viết logic @CheckDateCollision riêng) để kiểm tra xem phòng Lab đó trong khoảng thời gian đăng ký đã có nhóm khác mượn hay chưa.
Bonus 2: Áp dụng RedirectAttributes (Flash Attributes) để sau khi sinh viên submit form thành công, hệ thống redirect về trang chủ và hiện một Popup/Alert thông báo "Đăng ký thành công" mà không bị submit lại form nếu người dùng ấn F5
