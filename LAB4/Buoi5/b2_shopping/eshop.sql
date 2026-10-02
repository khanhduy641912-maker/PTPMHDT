CREATE DATABASE EShopDB;
GO

USE EShopDB;
GO

-- 1. Bảng Khách hàng
CREATE TABLE Customers (
    CustomerID VARCHAR(50) PRIMARY KEY,
    FullName NVARCHAR(100) NOT NULL,
    Email VARCHAR(100) NOT NULL,
    Phone VARCHAR(20) NOT NULL,
    ShippingAddress NVARCHAR(255) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- 2. Bảng Sản phẩm (Đồng bộ/Cache từ PIM)
CREATE TABLE Products (
    ProductID VARCHAR(50) PRIMARY KEY,
    SKU VARCHAR(50) UNIQUE NOT NULL,
    ProductName NVARCHAR(200) NOT NULL,
    Price DECIMAL(18,2) NOT NULL,
    StockQty INT NOT NULL DEFAULT 0
);

-- 3. Bảng Đơn hàng
CREATE TABLE Orders (
    OrderID VARCHAR(50) PRIMARY KEY,
    CustomerID VARCHAR(50) FOREIGN KEY REFERENCES Customers(CustomerID),
    OrderDate DATETIME DEFAULT GETDATE(),
    TotalAmount DECIMAL(18,2) NOT NULL,
    OrderStatus NVARCHAR(50) NOT NULL,
    PaymentStatus NVARCHAR(50) NOT NULL,
    ReserveToken VARCHAR(100) NULL
);

-- 4. Bảng Chi tiết Đơn hàng
CREATE TABLE OrderItems (
    OrderItemID INT IDENTITY(1,1) PRIMARY KEY,
    OrderID VARCHAR(50) FOREIGN KEY REFERENCES Orders(OrderID),
    ProductID VARCHAR(50) FOREIGN KEY REFERENCES Products(ProductID),
    Quantity INT NOT NULL,
    UnitPrice DECIMAL(18,2) NOT NULL
);

-- 5. Bảng Nhật ký Giao dịch Thanh toán
CREATE TABLE PaymentTransactions (
    TransactionID VARCHAR(50) PRIMARY KEY,
    OrderID VARCHAR(50) FOREIGN KEY REFERENCES Orders(OrderID),
    Amount DECIMAL(18,2) NOT NULL,
    PaymentMethod VARCHAR(50) NOT NULL,
    TxnRef VARCHAR(100) NOT NULL,
    Status VARCHAR(50) NOT NULL,
    CreatedAt DATETIME DEFAULT GETDATE()
);

-- Chèn dữ liệu mẫu
INSERT INTO Products (ProductID, SKU, ProductName, Price, StockQty) VALUES
('P01', 'SKU-IP15', N'Điện thoại iPhone 15 Pro', 25000000, 10),
('P02', 'SKU-MACM3', N'Laptop MacBook Air M3', 28000000, 5);