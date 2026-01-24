-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Oct 16, 2025 at 07:34 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jewelry_management`
--

-- --------------------------------------------------------

--
-- Table structure for table `account`
--

CREATE TABLE `account` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `firstname` varchar(100) NOT NULL,
  `lastname` varchar(100) NOT NULL,
  `date_of_birth` date DEFAULT NULL,
  `gender` enum('MALE','FEMALE','OTHER') DEFAULT NULL,
  `avatar` text DEFAULT NULL,
  `phone` varchar(20) DEFAULT NULL,
  `address` text DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','STAFF','USER') NOT NULL DEFAULT 'USER',
  `status` enum('ACTIVE','INACTIVE','BANNED','PENDING') NOT NULL DEFAULT 'ACTIVE',
  `otp_sku` varchar(10) DEFAULT NULL,
  `expiry_otp` datetime DEFAULT NULL,
  `last_login_at` datetime DEFAULT NULL,
  `created_at` datetime DEFAULT current_timestamp(),
  `updated_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `account`
--

INSERT INTO `account` (`id`, `username`, `firstname`, `lastname`, `date_of_birth`, `gender`, `avatar`, `phone`, `address`, `email`, `password`, `role`, `status`, `otp_sku`, `expiry_otp`, `last_login_at`, `created_at`, `updated_at`) VALUES
(5, 'user1', 'Nu', 'Ben', '2004-07-27', 'MALE', 'http://localhost:8080/uploads/users/d9729e09-0d10-46c3-94f2-6a88c42eea46_avatar5.jpg', '0123456712', 'Vân Dương - Bắc Ninh', 'toduc5233338@gmail.com', '$2a$10$ra8/CWebxUaKpypZXJ6DpeFOwf2TEUahsbkcGSaCU4GD5QtruT4Ka', 'USER', 'ACTIVE', NULL, NULL, '2025-08-30 15:16:56', '2025-07-25 15:30:22', '2025-09-10 01:55:11'),
(6, 'admin1', 'Toi la', 'ben nu', '2025-07-02', 'MALE', 'http://localhost:8080/uploads/users/5b9da4b0-b2ee-4684-b51b-906c6605045f_anhnenkhoamanhinh.jpg', '0123456721', 'Vân Dương - Bắc Ninh', 'abc@gmail.com', '$2a$10$ebpvUxLNMDN0XqY8odXhGu1wtbaCp1sl0vGFYTqcBHzpEPz6nkk7G', 'USER', 'ACTIVE', NULL, NULL, '2025-09-21 00:05:33', '2025-07-25 15:51:03', '2025-09-21 00:05:33'),
(7, 'admin', 'Tô Văn', 'Đức', '2004-07-27', 'MALE', 'http://localhost:8080/uploads/users/f4f18bbd-2b9a-46ec-8937-e6ffc8570af0_avatar4.jpg', '0364398622', 'Bắc Ninh', 'toduc52389@gmail.com', '$2a$10$tF6m36OCv8i4lD4DVqF3IevijfnGsl4eBlV4sBb5Qy0tuTukKdLkK', 'ADMIN', 'ACTIVE', NULL, NULL, '2025-09-10 08:58:10', '2025-08-10 11:02:38', '2025-09-10 08:58:10'),
(8, 'nguyenthihien', 'Nguyễn Thị', 'Hiền', '2004-05-23', 'FEMALE', 'http://localhost:8080/uploads/users/b225ee04-60a5-4c72-a3ab-a5602c2cacee_avatar1.jpg', '0352344417', 'Mê Linh - Hà Nội', 'hien51035@gmail.com', '$2a$10$CIslwQ2cH3yCiWiTRxf2te/4jOGgtf1YHCgB.r1N4aQz0pHnIFByW', 'USER', 'ACTIVE', NULL, NULL, NULL, '2025-08-15 22:23:14', '2025-08-16 14:13:18'),
(10, 'taikhoan1', 'Van Nu', 'Ben', '2025-09-09', 'MALE', 'http://localhost:8080/uploads/users/5b89b32c-b600-48e3-800f-b0243c5ba9ee_6dfe9114eb6719f3d2148daa69e5b5e7.jpg', '0123456789', 'abc', 'toduc5238@gmail.com', '$2a$10$.V1bzn/TdcsW95.bjSnxa.wxHyeGZp/TbjVmiGUcLv6nZWEL.d1jW', 'USER', 'ACTIVE', NULL, NULL, '2025-09-10 08:55:01', '2025-09-10 01:49:46', '2025-09-10 08:55:01');

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text DEFAULT NULL,
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `banner_url` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `category`
--

INSERT INTO `category` (`id`, `name`, `description`, `create_at`, `update_at`, `is_deleted`, `parent_id`, `banner_url`) VALUES
(1, 'Nhẫn', '', '2025-07-29', '2025-07-29', 0, 2, 'http://localhost:8080/uploads/categories/e786ab2e-53ab-4e71-b220-77f50b0ea3e2_bannerNhan.jpg'),
(2, 'Trang sức', NULL, '2025-07-30', '2025-07-30', 0, NULL, 'http://localhost:8080/uploads/categories/2ac773e4-290e-4843-9c98-048cbaf5f885_trang-suc.png'),
(4, 'Bông tai', NULL, '2025-08-03', '2025-08-03', 0, 2, 'http://localhost:8080/uploads/categories/cfc49d2c-8771-40f6-a654-9ca5456a4406_bannerBongTai.jpg'),
(7, 'Trang sức cưới', '', '2025-08-19', '2025-08-21', 0, NULL, 'http://localhost:8080/uploads/categories/e8d3a855-882a-467e-bd02-f7c8e4e12524_trang-suc-cuoi.png'),
(9, 'Kiềng cưới', '', '2025-09-08', '2025-09-08', 0, 7, 'http://localhost:8080/uploads/categories/0207e4f2-1504-4ed3-8eb8-5f90bb9db0f8_bannerKieng.jpg'),
(10, 'Thương hiệu hợp tác', '', '2025-09-08', '2025-09-08', 0, NULL, 'http://localhost:8080/uploads/categories/bdadeb2d-c1ad-419d-8d88-d55955dddeef_thuong-hieu-hop-tac.png'),
(11, 'PNJ ❤️ HELLO KITTY', '', '2025-09-08', '2025-09-08', 0, 10, 'http://localhost:8080/uploads/categories/101828b7-ed6d-423b-a34e-b8b551bf0f90_bannerHelloKitty.png'),
(12, 'Disney | PNJ', '', '2025-09-08', '2025-09-08', 0, 10, 'http://localhost:8080/uploads/categories/7bbebc58-6e07-42e0-8056-1b035c2d4303_bannerDisneyPnj.jpg');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `id` int(11) NOT NULL,
  `customer_name` varchar(255) NOT NULL,
  `customer_phone` varchar(50) NOT NULL,
  `customer_address` text DEFAULT NULL,
  `order_status` enum('PENDING','CONFIRMED','SHIPPED','DELIVERED','CANCELLED') NOT NULL DEFAULT 'PENDING',
  `total_price` decimal(15,2) NOT NULL,
  `create_at` datetime DEFAULT current_timestamp(),
  `update_at` datetime DEFAULT current_timestamp() ON UPDATE current_timestamp(),
  `account_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`id`, `customer_name`, `customer_phone`, `customer_address`, `order_status`, `total_price`, `create_at`, `update_at`, `account_id`) VALUES
(1, 'Tran Thi Trang', '0353447779', '234 Vân Dương, Bắc Ninh', 'DELIVERED', 107362000.00, '2025-08-06 10:24:13', '2025-08-30 15:31:59', 6),
(3, 'Nu Ben', '0123456712', 'Nam Sơn - Vân Dương - Bắc Ninh', 'DELIVERED', 14578000.00, '2025-08-09 17:42:09', '2025-08-30 15:31:59', 5),
(4, 'BenBen', '0123456712', 'Nam Sơn - Vân Dương - Bắc Ninh', 'DELIVERED', 29156000.00, '2025-08-09 17:52:34', '2025-09-02 12:53:04', 5),
(5, 'BowBow', '0123456712', 'Nam Sơn - Vân Dương - Bắc Ninh', 'PENDING', 29156000.00, '2025-08-09 17:54:18', '2025-08-09 17:54:18', 5),
(6, 'Toi là ben ben', '0123456721', 'Vân Dương - Bắc Ninh', 'CONFIRMED', 13615000.00, '2025-08-09 18:01:34', '2025-08-24 00:07:48', 6),
(7, 'Nu Ben', '0123456712', 'Nam Sơn - Vân Dương - Bắc Ninh', 'PENDING', 12094000.00, '2025-08-16 14:05:53', '2025-08-16 14:05:53', 5),
(8, 'Nu Ben', '0123456712', 'Nam Sơn - Vân Dương - Bắc Ninh', 'PENDING', 10929000.00, '2025-08-16 14:07:18', '2025-08-16 14:07:18', 5),
(9, 'Tô Văn Đức', '0364398622', 'Bắc Ninh', 'PENDING', 82822000.00, '2025-08-31 15:53:28', '2025-08-31 15:53:28', 7),
(10, 'Van Duc', '0123456721', 'Vân Dương - Bắc Ninh', 'PENDING', 24063000.00, '2025-09-01 23:49:45', '2025-09-01 23:49:45', 6),
(11, 'testThanhToan', '0123456721', 'Vân Dương - Bắc Ninh', 'PENDING', 14578000.00, '2025-09-01 23:54:09', '2025-09-01 23:54:09', 6),
(12, 'testThanhToan1', '0123456721', 'Vân Dương - Bắc Ninh', 'PENDING', 14578000.00, '2025-09-01 23:56:31', '2025-09-01 23:56:31', 6),
(13, 'Tô Văn Đức', '0364398622', 'Bắc Ninh', 'PENDING', 21544000.00, '2025-09-05 15:10:21', '2025-09-05 15:10:21', 7),
(14, 'Tô Văn Đức', '0364398622', 'Bắc Ninh', 'PENDING', 14578000.00, '2025-09-07 12:49:15', '2025-09-07 12:49:15', 7),
(15, 'Van Ben', '0123456789', 'abc', 'SHIPPED', 19496000.00, '2025-09-10 01:57:24', '2025-09-10 01:59:06', 10),
(16, 'Van Nu Ben', '0123456789', 'abc', 'PENDING', 21842000.00, '2025-09-10 08:56:03', '2025-09-10 08:56:03', 10);

-- --------------------------------------------------------

--
-- Table structure for table `order_item`
--

CREATE TABLE `order_item` (
  `id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `product_name` varchar(255) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `total` decimal(15,2) NOT NULL,
  `create_at` datetime DEFAULT current_timestamp(),
  `size` varchar(10) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `order_item`
--

INSERT INTO `order_item` (`id`, `order_id`, `product_id`, `product_name`, `price`, `quantity`, `total`, `create_at`, `size`) VALUES
(1, 1, 1, 'Nhẫn Kim cương Vàng trắng 14K Disney', 15754000.00, 2, 31508000.00, '2025-08-06 10:24:13', '11'),
(2, 1, 2, 'Cặp nhẫn cưới Kim cương Vàng 18K PNJ Tình Hồng', 22173000.00, 2, 44346000.00, '2025-08-06 10:24:13', NULL),
(3, 1, 1, 'Nhẫn Kim cương Vàng trắng 14K Disney', 15754000.00, 2, 31508000.00, '2025-08-06 10:24:13', '12'),
(7, 3, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 1, 14578000.00, '2025-08-09 17:42:09', NULL),
(8, 4, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 2, 29156000.00, '2025-08-09 17:52:34', NULL),
(9, 5, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 2, 29156000.00, '2025-08-09 17:54:18', NULL),
(10, 6, 80, 'Bông tai cưới Vàng 24K PNJ Tình Nồng', 13615000.00, 1, 13615000.00, '2025-08-09 18:01:34', NULL),
(11, 7, 78, 'Bông tai Vàng 24K PNJ Turning Gold', 12094000.00, 1, 12094000.00, '2025-08-16 14:05:53', NULL),
(12, 8, 77, 'Bông tai cưới Vàng 24K PNJ Sắc Son', 10929000.00, 1, 10929000.00, '2025-08-16 14:07:18', NULL),
(13, 9, 12, 'Nhẫn Kim cương Vàng trắng 14K Đính đá PNJ', 40889000.00, 1, 40889000.00, '2025-08-31 15:53:28', '11'),
(14, 9, 13, 'Nhẫn Kim cương Vàng trắng 14K PNJ Timeless Diamond', 41933000.00, 1, 41933000.00, '2025-08-31 15:53:28', '11'),
(15, 10, 8, 'Nhẫn Vàng trắng 14K đính đá Topaz PNJ PNJ', 24063000.00, 1, 24063000.00, '2025-09-01 23:49:45', '13'),
(16, 11, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 1, 14578000.00, '2025-09-01 23:54:09', NULL),
(17, 12, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 1, 14578000.00, '2025-09-01 23:56:31', NULL),
(18, 13, 82, 'Bông tai cưới Vàng 24k Đính đá Aventurine PNJ Lá Ngọc Cành Vàng', 21544000.00, 1, 21544000.00, '2025-09-05 15:10:21', NULL),
(19, 14, 81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 1, 14578000.00, '2025-09-07 12:49:15', NULL),
(20, 15, 110, 'Bông tai Kim cương Vàng Trắng 14K Disney|PNJ Cinderella', 9748000.00, 2, 19496000.00, '2025-09-10 01:57:24', NULL),
(21, 16, 79, 'Bông tai cưới Vàng 24K PNJ Hoa Duyên', 12094000.00, 1, 12094000.00, '2025-09-10 08:56:03', NULL),
(22, 16, 110, 'Bông tai Kim cương Vàng Trắng 14K Disney|PNJ Cinderella', 9748000.00, 1, 9748000.00, '2025-09-10 08:56:03', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(15,2) NOT NULL,
  `quantity` int(11) NOT NULL,
  `date_of_entry` date NOT NULL,
  `description` text DEFAULT NULL,
  `status` enum('IN_STOCK','SOLD_OUT') NOT NULL DEFAULT 'IN_STOCK',
  `create_at` date DEFAULT NULL,
  `update_at` date DEFAULT NULL,
  `category_id` int(11) DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  `sku` varchar(255) NOT NULL,
  `gold_type` enum('GOLD_10K','GOLD_14K','GOLD_18K','GOLD_24K') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`id`, `name`, `price`, `quantity`, `date_of_entry`, `description`, `status`, `create_at`, `update_at`, `category_id`, `is_deleted`, `sku`, `gold_type`) VALUES
(1, 'Nhẫn Kim cương Vàng trắng 14K Disney', 15754000.00, 76, '2025-07-29', '', 'IN_STOCK', '2025-07-29', '2025-08-22', 1, 0, 'GNDDDDW010579', 'GOLD_14K'),
(2, 'Cặp nhẫn cưới Kim cương Vàng 18K PNJ Tình Hồng', 22173000.00, 51, '2025-07-29', NULL, 'IN_STOCK', '2025-07-29', '2025-08-06', 1, 0, 'H000004-H000007', 'GOLD_18K'),
(3, 'Nhẫn Kim cương Vàng trắng 14K PNJ', 22220000.00, 118, '2025-07-29', NULL, 'IN_STOCK', '2025-07-29', '2025-07-29', 1, 0, 'DDDDW060621', 'GOLD_14K'),
(4, 'Nhẫn Kim cương Vàng 14K PNJ Timeless Diamond', 22482000.00, 78, '2025-07-29', NULL, 'IN_STOCK', '2025-07-29', '2025-07-29', 1, 0, 'GNDDDDH000454', 'GOLD_14K'),
(5, 'Nhẫn Kim cương Vàng trắng 14K Disney|PNJ The Little Mermaid', 22232000.00, 78, '2025-07-29', NULL, 'IN_STOCK', '2025-07-29', '2025-07-29', 1, 0, 'GNDDDDW009789', 'GOLD_14K'),
(6, 'Nhẫn cưới Vàng 24K PNJ', 22225000.00, 76, '2025-07-29', NULL, 'IN_STOCK', '2025-07-29', '2025-07-29', 1, 0, 'GN0000Y002329', 'GOLD_24K'),
(7, 'Nhẫn Vàng trắng 14K đính đá Ruby PNJ Power of Nature', 22988000.00, 40, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNRBXMW000230', 'GOLD_14K'),
(8, 'Nhẫn Vàng trắng 14K đính đá Topaz PNJ PNJ', 24063000.00, 49, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-09-01', 1, 0, 'GNTPXMW000613', 'GOLD_14K'),
(9, 'Nhẫn Kim cương Vàng Trắng 14K PNJ My First Diamond', 40150000.00, 108, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNDD00W005897', 'GOLD_14K'),
(10, 'Nhẫn Kim cương Vàng trắng 14K EZC PNJ ', 40665000.00, 23, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'DD00W004213', 'GOLD_14K'),
(12, 'Nhẫn Kim cương Vàng trắng 14K Đính đá PNJ', 40889000.00, 77, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-31', 1, 0, 'GNDD00W000735', 'GOLD_14K'),
(13, 'Nhẫn Kim cương Vàng trắng 14K PNJ Timeless Diamond', 41933000.00, 100, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-31', 1, 0, 'GNDDDDW010589', 'GOLD_14K'),
(14, 'Nhẫn Vàng trắng 10K đính Ngọc trai Akoya PNJ', 42380000.00, 57, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNPAXMW000174', 'GOLD_10K'),
(15, 'Nhẫn Vàng trắng 10K đính đá ECZ PNJ', 41980000.00, 148, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXMXMW000111', 'GOLD_10K'),
(16, 'Nhẫn cưới Vàng trắng 10K đính đá ECZ PNJ', 4635000.00, 123, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXM00W001722', 'GOLD_10K'),
(17, 'Nhẫn Vàng 10K đính đá ECZ STYLE by PNJ', 4639000.00, 36, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXMXMX000060', 'GOLD_10K'),
(18, 'Nhẫn Vàng trắng 10K đính đá PNJ', 4569000.00, 35, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXMXMW002435', 'GOLD_10K'),
(20, 'Nhẫn Vàng 10K đính đá Synthetic STYLE By PNJ Feminine', 4669000.00, 44, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNZTZTX000004', 'GOLD_10K'),
(21, 'Nhẫn Vàng trắng 10K đính đá ECZ PNJ Euphoria', 4688000.00, 64, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXMXMW003499', 'GOLD_10K'),
(22, 'Nhẫn Vàng trắng 10K đính đá ECZ PNJ x emoji™', 4767000.00, 91, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXM00W001908', 'GOLD_10K'),
(23, 'Nhẫn cưới Kim cương Vàng 18K Disney|PNJ Sleeping Beauty', 13399000.00, 57, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNDDDDC001491', 'GOLD_18K'),
(24, 'Nhẫn Vàng 18K đính đá Citrine PNJ', 13999000.00, 32, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNCTXMY000457', 'GOLD_18K'),
(27, 'Nhẫn cưới Vàng 18K đính đá Ruby PNJ Trầu Cau', 17517000.00, 69, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNRBXMY001202', 'GOLD_18K'),
(28, 'Nhẫn Vàng 18K PNJ', 14140000.00, 66, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y060872', 'GOLD_18K'),
(30, 'Nhẫn Vàng 18K PNJ Kim Bảo Như Ý', 25067000.00, 72, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y003574', 'GOLD_18K'),
(31, 'Cặp nhẫn cưới Kim cương Vàng 18K PNJ Vàng Son', 15505000.00, 38, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN00633-00313', 'GOLD_18K'),
(32, 'Nhẫn nam Vàng 18K đính đá Ruby MANCODE by PNJ', 28954000.00, 93, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNRBXMY000597', 'GOLD_18K'),
(33, 'Nhẫn Vàng 18K MANCODE by PNJ hình bàn tính Abacus', 33680000.00, 55, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y060492', 'GOLD_18K'),
(34, 'Nhẫn cưới Vàng 24K đính đá PNJ', 14304000.00, 23, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y002158', 'GOLD_24K'),
(35, 'Nhẫn nam cưới Vàng 24K PNJ', 14571000.00, 63, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y003150', 'GOLD_24K'),
(36, 'Nhẫn cưới Vàng 24k Đính đá Aventurine PNJ Lá Ngọc Cành Vàng', 15189000.00, 87, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNQT00Y000015', 'GOLD_24K'),
(37, 'Nhẫn Vàng 24K ECZ PNJ', 17200000.00, 61, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y001995', 'GOLD_24K'),
(38, 'Nhẫn cưới Vàng 24K PNJ Trầu Cau', 19859000.00, 61, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y002594', 'GOLD_24K'),
(40, 'Nhẫn cưới Nam Vàng 24K PNJ', 55217000.00, 94, '2025-07-31', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GN0000Y002374', 'GOLD_24K'),
(41, 'Nhẫn Vàng 18K đính đá CZ PNJ', 8976000.00, 50, '2025-08-01', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNXMXMY014623', 'GOLD_18K'),
(43, 'Nhẫn Vàng 18K đính đá Tsavorite PNJ', 8976000.00, 77, '2025-08-01', NULL, 'IN_STOCK', '2025-08-01', '2025-08-01', 1, 0, 'GNSV00Y000005', 'GOLD_18K'),
(44, 'Nhẫn nam Vàng 24K MANCODE by PNJ Kim Long Trường Cửu', 35573000.00, 105, '2025-08-01', NULL, 'IN_STOCK', '2025-08-02', '2025-08-02', 1, 0, 'GN0000Y002799', 'GOLD_24K'),
(45, 'Bông tai trẻ em Vàng 10K Sweeties', 6975000.00, 25, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000C000457', 'GOLD_10K'),
(46, 'Bông tai trẻ em Vàng 10K Đính đá ECZ Sweeties', 3955000.00, 25, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00Y003698', 'GOLD_10K'),
(47, 'Bông tai trẻ em Bạc Disney|PNJ The Little Mermaid', 795000.00, 24, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000W060076', NULL),
(48, 'Bông tai Vàng 10K Đính đá Ruby PNJ Magnétique', 10493000.00, 33, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-22', 4, 0, 'GBRBXMY000392', 'GOLD_10K'),
(49, 'Bông tai Bạc đính đá STYLE By PNJ', 595000.00, 22, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-22', 4, 0, 'GBZTXMW060008', NULL),
(50, 'Bông tai Vàng Trắng 10K Đính đá ECZ STYLE By PNJ', 2889000.00, 27, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00W001541', 'GOLD_10K'),
(51, 'Bông tai Vàng 10K đính đá ECZ STYLE by PNJ', 2889000.00, 23, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00Y002920', 'GOLD_10K'),
(52, 'Bông tai Vàng trắng 10K đính đá ECZ PNJ ❤️ HELLO KITTY', 2905000.00, 17, '2025-08-01', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00W001457', 'GOLD_10K'),
(53, 'Bông tai Vàng trắng Ý 18K PNJ', 2922000.00, 14, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000W000130', 'GOLD_18K'),
(54, 'Bông tai Vàng Trắng 10K đính đá synthetic STYLE By PNJ', 2947000.00, 19, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZTZTW000007', 'GOLD_10K'),
(55, 'Bông tai Vàng trắng 10K đính đá ECZ STYLE By PNJ Feminine', 2957000.00, 25, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMW003108', 'GOLD_10K'),
(56, 'Bông tai Vàng Trắng 10K Đính đá ECZ STYLE By PNJ Love Potion', 2967000.00, 32, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00W001374', 'GOLD_10K'),
(57, 'Bông tai Vàng 10K đính đá ECZ PNJ x emoji™', 3220000.00, 24, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00C000360', 'GOLD_10K'),
(58, 'Bông tai Vàng 10K Đính đá ECZ STYLE By PNJ Lucky Me', 4090000.00, 32, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMMXC000001', 'GOLD_10K'),
(59, 'Bông tai Vàng 10K đính đá Disney|PNJ Alice In Wonderland', 3912000.00, 29, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZTZTB000001', 'GOLD_10K'),
(60, 'Bông tai Vàng 14K Đính đá ECZ Disney|PNJ Mickey', 5496000.00, 21, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMC000433', 'GOLD_14K'),
(61, 'Bông tai Vàng 14K Đính đá CZ PNJ', 5499000.00, 25, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00Y003637', 'GOLD_14K'),
(62, 'Bông tai Vàng trắng 14K đính đá Synthetic Disney|PNJ Cinderella', 5651000.00, 33, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZTXMW000104', 'GOLD_14K'),
(63, 'Bông tai Vàng trắng 14K đính đá ECZ Disney|PNJ Mickey & Minnie', 5664000.00, 11, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXM00W001168', 'GOLD_14K'),
(64, 'Bông tai Vàng 14K đính đá Synthetic PNJ ❤️ HELLO KITTY', 5698000.00, 12, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZTZTX000009', 'GOLD_14K'),
(65, 'Bông tai Vàng 14K Đính đá synthetic Disney|PNJ Snow White & the Seven Dwarfs', 5749000.00, 32, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZT00Y000080', 'GOLD_14K'),
(66, 'Bông tai Vàng 14K Đính đá ECZ PNJ', 7757000.00, 22, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMY009517', 'GOLD_14K'),
(67, 'Bông tai Vàng 14K đính đá Synthetic Disney|PNJ Beauty & The Beast', 7707000.00, 26, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBZT00Y000078', 'GOLD_14K'),
(68, 'Bông tai Vàng 14K PNJ Kim Bảo Như Ý', 7746000.00, 26, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000Y003224', 'GOLD_14K'),
(69, 'Bông tai Vàng 14K đính đá Ruby PNJ', 7984000.00, 24, '2025-08-02', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBRBXMX000004', 'GOLD_14K'),
(70, 'Bông tai Vàng trắng 14K đính đá ECZ PNJ', 8023000.00, 24, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMW002624', 'GOLD_14K'),
(71, 'Bông tai Vàng trắng 14K đính đá ECZ PNJ Audax Rosa', 15102000.00, 26, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMW003083', 'GOLD_14K'),
(72, 'Bông tai Vàng trắng Ý 18K đính đá PNJ', 7969000.00, 26, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000W002570', 'GOLD_18K'),
(73, 'Bông tai Vàng 18K đính đá Ruby PNJ', 7912000.00, 28, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBRBXMY000315', 'GOLD_18K'),
(74, 'Bông tai Vàng 18K PNJ hoa bốn cánh', 7342600.00, 20, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000Y060680', 'GOLD_18K'),
(75, 'Bông tai Vàng 18K đính đá CZ PNJ', 7986600.00, 22, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBXMXMY000071', 'GOLD_18K'),
(76, 'Bông tai Vàng 18K Đính đá Sapphire PNJ', 12888000.00, 29, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GBSP00X000005', 'GOLD_18K'),
(77, 'Bông tai cưới Vàng 24K PNJ Sắc Son', 10929000.00, 25, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-16', 4, 0, 'GB0000Y000078', 'GOLD_24K'),
(78, 'Bông tai Vàng 24K PNJ Turning Gold', 12094000.00, 26, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-16', 4, 0, 'GB0000Y003238', 'GOLD_24K'),
(79, 'Bông tai cưới Vàng 24K PNJ Hoa Duyên', 12094000.00, 21, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-09-10', 4, 0, 'GB0000Y000079', 'GOLD_24K'),
(80, 'Bông tai cưới Vàng 24K PNJ Tình Nồng', 13615000.00, 28, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-21', 4, 0, 'GB0000Y000084', 'GOLD_24K'),
(81, 'Bông tai cưới Vàng 24K PNJ', 14578000.00, 22, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-09-07', 4, 0, 'GB0000Y000070', 'GOLD_24K'),
(82, 'Bông tai cưới Vàng 24k Đính đá Aventurine PNJ Lá Ngọc Cành Vàng', 21544000.00, 29, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-09-05', 4, 0, 'GBQT00Y000019', 'GOLD_24K'),
(83, 'Bông tai cưới Vàng 24K PNJ Trầu Cau', 34088000.00, 18, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000Y002678', 'GOLD_24K'),
(84, 'Bông tai cưới Vàng 24K PNJ Lan Mùa Hạ', 16209000.00, 20, '2025-08-03', NULL, 'IN_STOCK', '2025-08-03', '2025-08-03', 4, 0, 'GB0000Y000071', 'GOLD_24K'),
(93, 'Kiềng cưới Vàng 24K PNJ', 16209000.00, 25, '2025-09-08', NULL, 'IN_STOCK', '2025-09-08', '2025-09-08', 9, 0, 'GH0000Y060105', 'GOLD_24K'),
(94, 'Kiềng cưới vàng 24K PNJ EZC', 26550000.00, 27, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 9, 0, '0000Y060007', 'GOLD_24K'),
(95, 'Kiềng cưới Vàng 18K PNJ Trầu Cau', 71286000.00, 20, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 9, 0, '0000Y000229', 'GOLD_18K'),
(96, 'Kiềng cưới Kim cương Vàng Trắng 14K PNJ Lá Ngọc Cành Vàng', 380636000.00, 12, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 9, 0, 'GHDDMXW000001', 'GOLD_14K'),
(97, 'Lắc tay Vàng trắng 10K Đính Ngọc trai Freshwater PNJ ❤️ HELLO KITTY', 6548000.00, 25, '2025-09-08', NULL, 'IN_STOCK', '2025-09-08', '2025-09-08', 9, 0, 'GLPFMXW000019', 'GOLD_10K'),
(98, 'Lắc tay Vàng 10K PNJ ❤️ HELLO KITTY', 5551000.00, 22, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GL0000C000331', 'GOLD_10K'),
(99, 'Lắc tay Vàng 14K Đính đá Synthetic PNJ ❤️ HELLO KITTY', 11761000.00, 11, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GLZT00H000003', 'GOLD_14K'),
(100, 'Lắc tay Bạc đính đá PNJ ❤️ HELLO KITTY', 2295000.00, 11, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'SLPFXMW000007', NULL),
(101, 'Lắc tay Bạc đính Ngọc trai PNJ ❤️ HELLO KITTY', 1095000.00, 15, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'SLPFXMH000003', NULL),
(102, 'Lắc tay trẻ em Bạc đính đá PNJ ❤️ HELLO KITTY', 895000.00, 11, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'SLXM00W000027', NULL),
(103, 'Lắc tay Vàng 14K đính đá EZC Synthetic PNJ ❤️ HELLO KITTY', 10525000.00, 11, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GLZTXMH000025', 'GOLD_14K'),
(104, 'Hạt Charm Vàng 14K đính đá Synthetic PNJ ❤️ HELLO KITTY', 11218000.00, 12, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GIZT00X000002', 'GOLD_14K'),
(105, 'Hạt Charm Bạc PNJ ❤️ HELLO KITTY', 475000.00, 13, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'SI0000H000003', NULL),
(106, 'Hạt Charm Vàng 14K đính đá Synthetic ECZ PNJ ❤️ HELLO KITTY', 5669000.00, 13, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GIZT00H000002', 'GOLD_14K'),
(107, 'Hạt Charm Vàng 10K đính đá ECZ PNJ ❤️ HELLO KITTY', 5095000.00, 14, '2025-09-07', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 11, 0, 'GIXM00Y000162', 'GOLD_10K'),
(108, 'Bông tai nam Bạc đính đá Marvel | PNJ Marvel Captain America', 695000.00, 25, '2025-09-08', NULL, 'IN_STOCK', '2025-09-08', '2025-09-08', 12, 0, 'SBXM00W000086', NULL),
(109, 'Nhẫn Kim cương Vàng Trắng 14K Disney|PNJ Cinderella', 24060000.00, 14, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 12, 0, 'GNDDMXW000009', 'GOLD_14K'),
(110, 'Bông tai Kim cương Vàng Trắng 14K Disney|PNJ Cinderella', 9748000.00, 13, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-10', 12, 0, 'GBDDDDW004494', 'GOLD_14K'),
(111, 'Dây cổ Kim cương Vàng Trắng 14K Disney|PNJ Cinderella', 13675000.00, 25, '2025-09-07', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 12, 0, 'GCDDDDW000832', 'GOLD_14K'),
(112, 'Lắc tay trẻ em Bạc Disney|PNJ', 1095000.00, 14, '2025-09-08', '', 'IN_STOCK', '2025-09-08', '2025-09-08', 12, 0, 'SL0000W060277', NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product_image`
--

CREATE TABLE `product_image` (
  `id` int(11) NOT NULL,
  `image_url` varchar(255) NOT NULL,
  `is_primary` tinyint(1) DEFAULT 0,
  `product_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_image`
--

INSERT INTO `product_image` (`id`, `image_url`, `is_primary`, `product_id`, `created_at`, `updated_at`) VALUES
(1, 'nhansp1_anh1', 1, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(2, 'nhansp1_anh2', 0, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(3, 'nhansp1_anh3', 0, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(4, 'nhansp1_anh4', 0, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(5, 'nhansp1_anh5', 0, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(6, 'nhansp1_anh6', 0, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(7, 'http://localhost:8080/uploads/products/c8d8bbed-a092-4619-8d7d-e7a49daf672a_tinh-hong-00004-00007.png', 1, 2, '2025-07-29 16:12:52', '2025-07-29 16:12:52'),
(8, 'http://localhost:8080/uploads/products/355615a6-d298-4acc-9e98-fad44294b841_tinh_hong_vang_18k.png', 0, 2, '2025-07-29 16:12:52', '2025-07-29 16:12:52'),
(9, 'http://localhost:8080/uploads/products/c7339b51-9db7-4053-b704-2d6cad5eef54_tinh-hong-vang-18k.png', 0, 2, '2025-07-29 16:12:52', '2025-07-29 16:12:52'),
(10, 'http://localhost:8080/uploads/products/a08a74a8-4d50-49b3-96da-b0bd4b0768d0_vang-trang-14k-pnj-1.png', 1, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(11, 'http://localhost:8080/uploads/products/fac1b072-9734-4523-bd7f-cc2c56c955b8_vang-trang-14k-pnj-2.png', 0, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(12, 'http://localhost:8080/uploads/products/44e33f2f-f31f-4d5e-ba97-07f6711c9f1b_vang-trang-14k-pnj-2.jpg', 0, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(13, 'http://localhost:8080/uploads/products/0a2807cb-0f7f-4c3d-9479-b5b85ed881d7_vang-trang-14k-pnj-1.jpg', 0, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(14, 'http://localhost:8080/uploads/products/8ec4cf56-cce6-4d9e-8536-c0dcc16431a6_vang-trang-14k-pnj-3.jpg', 0, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(15, 'http://localhost:8080/uploads/products/bb47e58c-8300-4616-b93d-5e9811735b31_vang-trang-14k-pnj-3.png', 0, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(16, 'http://localhost:8080/uploads/products/b58c1b03-8260-4e93-b9f8-bbafb1265de2_vang-14k-pnj-01.png', 1, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(17, 'http://localhost:8080/uploads/products/d1135946-75b1-492c-963a-5bab44af658f_vang-14k-pnj-02.png', 0, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(18, 'http://localhost:8080/uploads/products/8ace0267-ee15-4524-8127-efa3628e5c8f_vang-14k-pnj-03.png', 0, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(19, 'http://localhost:8080/uploads/products/8c7a9631-873d-4106-ae7c-f2712026a0a5_vang-14k-pnj-04.jpg', 0, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(20, 'http://localhost:8080/uploads/products/4b244353-37d0-4cbe-9630-809212fde1e2_vang-14k-pnj-05.jpg', 0, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(21, 'http://localhost:8080/uploads/products/a4f22745-ffa1-4eea-863b-db47dd742b88_vang-14k-pnj-06.jpg', 0, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(22, 'http://localhost:8080/uploads/products/b5c37fcc-de8c-4d6e-968f-612ae26ec351_the-little-mermaid-1.png', 1, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(23, 'http://localhost:8080/uploads/products/4a7394c2-8599-4c09-a530-df4a11c37a2e_the-little-mermaid-2.png', 0, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(24, 'http://localhost:8080/uploads/products/2cf1d032-1243-4961-96eb-0ebb85595293_the-little-mermaid-3.png', 0, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(25, 'http://localhost:8080/uploads/products/ad0f3721-0309-45f6-b28a-4e66bd0d62d3_the-little-mermaid-4.jpg', 0, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(26, 'http://localhost:8080/uploads/products/b880e53e-0843-438a-8860-dbdb968d6ea6_the-little-mermaid-5.jpg', 0, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(27, 'http://localhost:8080/uploads/products/4b3e2979-923a-40c8-b6da-0b1d6f40bd7b_the-little-mermaid-6.jpg', 0, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(28, 'http://localhost:8080/uploads/products/2fc21d20-fe6b-48c2-8ddf-1b4b9df54468_nhan-cuoi-vang-24k-pnj-1.png', 1, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(29, 'http://localhost:8080/uploads/products/4c52b5eb-c3bc-403a-a5c4-49d7e9fbd924_nhan-cuoi-vang-24k-pnj-2.png', 0, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(30, 'http://localhost:8080/uploads/products/98a108f6-91b8-448f-8ba5-a3589e8850b2_nhan-cuoi-vang-24k-pnj-3.png', 0, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(31, 'http://localhost:8080/uploads/products/eb4c6546-109d-4913-88fd-3a3c723f820f_nhan-cuoi-vang-24k-pnj-4.jpg', 0, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(32, 'http://localhost:8080/uploads/products/3251a55a-a443-4474-a313-c81f27aa0957_nhan-cuoi-vang-24k-pnj-5.jpg', 0, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(33, 'http://localhost:8080/uploads/products/90bc1834-f939-46e6-aaa0-f0301c9ef157_nhan-cuoi-vang-24k-pnj-6.jpg', 0, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(34, 'http://localhost:8080/uploads/products/a6c3a070-058b-4340-a400-b1d1c63aa6dc_dinh-da-ruby-pnj-1.png', 1, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(35, 'http://localhost:8080/uploads/products/4682bf39-9608-4c5c-a3c2-322adc93f92d_dinh-da-ruby-pnj-2.png', 0, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(36, 'http://localhost:8080/uploads/products/d946639f-37d3-4b4b-9110-2ac70f24742a_dinh-da-ruby-pnj-3.png', 0, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(37, 'http://localhost:8080/uploads/products/c9497cf3-8022-49f6-a578-041261dfc089_dinh-da-ruby-pnj-4.jpg', 0, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(38, 'http://localhost:8080/uploads/products/e0197b2d-ef12-4043-838d-9b793b8c26d2_dinh-da-ruby-pnj-5.jpg', 0, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(39, 'http://localhost:8080/uploads/products/00d6e336-fa07-4235-8d28-547e2f23c9f9_dinh-da-ruby-pnj-6.jpg', 0, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(40, 'http://localhost:8080/uploads/products/c14e49a4-de64-4079-a272-4d2bd8653957_dinh-da-topaz-pnj-pnj-1.png', 1, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(41, 'http://localhost:8080/uploads/products/5403f501-e321-4ca8-8045-0c362d344b95_dinh-da-topaz-pnj-pnj-2.png', 0, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(42, 'http://localhost:8080/uploads/products/74b3b630-784f-47c9-9774-edebdf8bf89e_dinh-da-topaz-pnj-pnj-3.png', 0, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(43, 'http://localhost:8080/uploads/products/bb3ffa8d-621f-4aa1-8899-b3289b9b1aa7_dinh-da-topaz-pnj-pnj-4.jpg', 0, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(44, 'http://localhost:8080/uploads/products/da94567f-d173-4ec7-9853-125492753d98_dinh-da-topaz-pnj-pnj-5.jpg', 0, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(45, 'http://localhost:8080/uploads/products/75844fe6-79fb-4ea7-9133-5a8ea0f4d0c2_dinh-da-topaz-pnj-pnj-6.jpg', 0, 8, '2025-08-01 15:30:28', '2025-08-01 15:30:28'),
(46, 'http://localhost:8080/uploads/products/9b94286b-ddbe-4301-b5ff-0ff8a1e3c6fe_kim-cuong-vang-trang-14k-pnj-1.png', 1, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(47, 'http://localhost:8080/uploads/products/af250da4-2e09-4723-a1c0-d4136c1aeb6c_kim-cuong-vang-trang-14k-pnj-2.png', 0, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(48, 'http://localhost:8080/uploads/products/90cef5cd-2bc7-4be9-a63c-35ec5b2d5fb2_kim-cuong-vang-trang-14k-pnj-2.jpg', 0, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(49, 'http://localhost:8080/uploads/products/90a49fd9-da60-4dbb-9880-64979275747c_kim-cuong-vang-trang-14k-pnj-1.jpg', 0, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(50, 'http://localhost:8080/uploads/products/45229290-59f5-4359-b597-b6b7607bff8d_kim-cuong-vang-trang-14k-pnj-3.jpg', 0, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(51, 'http://localhost:8080/uploads/products/7437a93b-ec3d-41a7-82be-99948fa9a3a5_kim-cuong-vang-trang-14k-pnj-3.png', 0, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(52, 'http://localhost:8080/uploads/products/72b7a658-c04f-4686-b17f-4a81be707664_vang-trang-14k-pnj-01.png', 1, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(53, 'http://localhost:8080/uploads/products/907af3f9-3d7f-4175-afc0-3d46248055ab_vang-trang-14k-pnj-02.png', 0, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(54, 'http://localhost:8080/uploads/products/98a73123-f08c-4257-be4d-5d4abe294d94_vang-trang-14k-pnj-03.png', 0, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(55, 'http://localhost:8080/uploads/products/a37a25b0-12c3-44c7-a0ea-49b812f25a0d_vang-trang-14k-pnj-04.jpg', 0, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(56, 'http://localhost:8080/uploads/products/20d2caa5-b40f-4334-b229-5416c73c8eef_vang-trang-14k-pnj-05.jpg', 0, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(57, 'http://localhost:8080/uploads/products/e5001eb9-c276-4773-9951-c9f5604a5757_vang-trang-14k-pnj-06.jpg', 0, 10, '2025-08-01 15:34:49', '2025-08-01 15:34:49'),
(58, 'http://localhost:8080/uploads/products/868c61ea-c511-4d22-bae9-2950b14fbd9d_vang-trang-14k-pnj.png', 1, 12, '2025-08-01 15:36:30', '2025-08-01 15:36:30'),
(59, 'http://localhost:8080/uploads/products/51d2aff8-1626-4c94-a4bd-e34a8e0b9d06_vang-trang-14k-pnj-01.png', 0, 12, '2025-08-01 15:36:30', '2025-08-01 15:36:30'),
(60, 'http://localhost:8080/uploads/products/b8e297b8-76c6-469d-87ea-de8d7c81335b_vang-trang-14k-pnj-02.png', 0, 12, '2025-08-01 15:36:30', '2025-08-01 15:36:30'),
(61, 'http://localhost:8080/uploads/products/9f77bc00-cd4b-4797-8553-9dd8ae7fd25b_vang-trang-14k-pnj-03.jpg', 0, 12, '2025-08-01 15:36:30', '2025-08-01 15:36:30'),
(62, 'http://localhost:8080/uploads/products/6a4c1c76-5237-4eda-a584-2895d3c96c9a_14k-kim-cuong-pnj-1.png', 1, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(63, 'http://localhost:8080/uploads/products/03e4ffee-724d-4b4f-bb24-9f103504cbcb_14k-kim-cuong-pnj-2.png', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(64, 'http://localhost:8080/uploads/products/e7508fd9-f8e2-4786-99e2-c0d233902b09_14k-kim-cuong-pnj-3.png', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(65, 'http://localhost:8080/uploads/products/31d29d5f-5786-43de-8aef-8eee1775e6dc_14k-kim-cuong-pnj-4.png', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(66, 'http://localhost:8080/uploads/products/e31ae67d-4125-4e7d-9ee9-be72ad4bcc3d_14k-kim-cuong-pnj-5.png', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(67, 'http://localhost:8080/uploads/products/756dfe9a-48f1-4000-9084-3781bb781f3c_14k-kim-cuong-pnj-6.png', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(68, 'http://localhost:8080/uploads/products/0d41de13-72a7-4166-92a5-9ab8c4b6c1a4_14k-kim-cuong-pnj-7.jpg', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(69, 'http://localhost:8080/uploads/products/a96e23bc-0d00-4af8-8c3c-4630ee08487f_14k-kim-cuong-pnj-8.jpg', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(70, 'http://localhost:8080/uploads/products/155afc64-0f33-4422-a65f-5ecd28e23743_14k-kim-cuong-pnj-9.jpg', 0, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(71, 'http://localhost:8080/uploads/products/53682c46-1285-4412-a171-2feb6280cef7_sp-gnpaxmw000174-nhan-vang-10k-dinh-ngoc-trai-akoya-pnj-1.png', 1, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(72, 'http://localhost:8080/uploads/products/44f4ddba-efb7-426a-876a-6503bf007b21_ngoc-trai-akoya-pnj-2.png', 0, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(73, 'http://localhost:8080/uploads/products/d5144b9c-4f19-4089-9b11-a59a223205e8_dinh-ngoc-trai-akoya-pnj-3.jpg', 0, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(74, 'http://localhost:8080/uploads/products/7f93c9e6-2ddc-4d83-98b1-92635f0697c2_dinh-ngoc-trai-akoya-pnj-3.png', 0, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(75, 'http://localhost:8080/uploads/products/c76d6585-6580-48be-87a9-1eba72690f9c_dinh-ngoc-trai-akoya-pnj-1.jpg', 0, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(76, 'http://localhost:8080/uploads/products/c76d6585-6580-48be-87a9-1eba72690f9c_dinh-ngoc-trai-akoya-pnj-1.jpg', 0, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(77, 'http://localhost:8080/uploads/products/1e72d5ed-a0e4-42d6-b93e-f5ca7b3ec524_10k-dinh-da-ecz-pnj-1.png', 1, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(78, 'http://localhost:8080/uploads/products/b9810f58-9b5a-4747-9588-bae731d30bda_10k-dinh-da-ecz-pnj-2.png', 0, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(79, 'http://localhost:8080/uploads/products/0492f455-ef01-40ea-adb6-de2f10cd29ac_10k-dinh-da-ecz-pnj-2.jpg', 0, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(80, 'http://localhost:8080/uploads/products/37d616fd-07b9-47b0-9b04-296818c0f731_10k-dinh-da-ecz-pnj-1.jpg', 0, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(81, 'http://localhost:8080/uploads/products/68ff6e0f-9631-438e-b550-a5e52b75f21c_10k-dinh-da-ecz-pnj-3.jpg', 0, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(82, 'http://localhost:8080/uploads/products/21312c3f-af9a-4176-8221-70584d5f69bf_10k-dinh-da-ecz-pnj-3.png', 0, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(83, 'http://localhost:8080/uploads/products/ed5e6ee2-ca57-4ee9-84e6-a2583dd94318_10k-dinh-da-ecz-pnj-1.png', 1, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(84, 'http://localhost:8080/uploads/products/6b288885-d931-4ccf-910b-6ecfe92c7e63_10k-dinh-da-ecz-pnj-2.png', 0, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(85, 'http://localhost:8080/uploads/products/316bb9b4-4307-4687-90bf-4a8775cffc64_10k-dinh-da-ecz-pnj-2.jpg', 0, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(86, 'http://localhost:8080/uploads/products/3cb45b6c-96e5-44ed-b813-ca44919efdb4_10k-dinh-da-ecz-pnj-1.jpg', 0, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(87, 'http://localhost:8080/uploads/products/6d20503e-e956-4de0-9b4b-bd6fe3b1d16c_10k-dinh-da-ecz-pnj-3.jpg', 0, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(88, 'http://localhost:8080/uploads/products/98ce3b9f-6daf-44ff-aa5b-62d8f220bd81_10k-dinh-da-ecz-pnj-3.png', 0, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(89, 'http://localhost:8080/uploads/products/c427f0ee-e839-42c7-95a4-2c121b0293d3_ecz-style-by-pnj-1.png', 1, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(90, 'http://localhost:8080/uploads/products/add38a4b-ee9f-4404-b697-7a4f58ed2c89_ecz-style-by-pnj-2.png', 0, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(91, 'http://localhost:8080/uploads/products/aec8bc7b-f038-4ef2-90e7-d306ff2008ea_ecz-style-by-pnj-3.png', 0, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(92, 'http://localhost:8080/uploads/products/1a16da1f-e20c-4ca9-bd67-e5bfa25690df_ecz-style-by-pnj-4.jpg', 0, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(93, 'http://localhost:8080/uploads/products/c6826c32-22be-450b-9dc2-5dd9b2880e9c_ecz-style-by-pnj-5.jpg', 0, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(94, 'http://localhost:8080/uploads/products/e3befa27-6ada-4a1f-a85b-198bb304e201_ecz-style-by-pnj-6.jpg', 0, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(95, 'http://localhost:8080/uploads/products/9b236e22-c43e-459e-a952-13d628819f3f_da-ecz-pnj-01.png', 1, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(96, 'http://localhost:8080/uploads/products/6e4a5a10-c1c5-43ef-9f5a-db2202b14468_da-ecz-pnj-02.png', 0, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(97, 'http://localhost:8080/uploads/products/80eaf152-c18b-4e9e-bb87-da9dd1bdf0d7_da-ecz-pnj-03.png', 0, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(98, 'http://localhost:8080/uploads/products/9b92d9e7-a412-4eb9-9429-0afa84b627a7_da-ecz-pnj-04.jpg', 0, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(99, 'http://localhost:8080/uploads/products/61a9a0f7-23c8-49b2-8735-f89e2a721821_da-ecz-pnj-05.jpg', 0, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(100, 'http://localhost:8080/uploads/products/62565bc1-4d35-4603-9709-9547682f734b_da-ecz-pnj-06.jpg', 0, 18, '2025-08-01 15:57:39', '2025-08-01 15:57:39'),
(107, 'http://localhost:8080/uploads/products/c28fe03b-4470-4d2a-bf00-5ba212792daf_by-pnj-feminine.png', 1, 20, '2025-08-01 16:00:04', '2025-08-01 16:00:04'),
(108, 'http://localhost:8080/uploads/products/d324c1d9-e79f-4185-b6ef-b2167de613e1_by-pnj-feminine-01.png', 0, 20, '2025-08-01 16:00:04', '2025-08-01 16:00:04'),
(109, 'http://localhost:8080/uploads/products/44b8c28b-87e2-4d19-a760-f7b905adbfa0_by-pnj-feminine-02.png', 0, 20, '2025-08-01 16:00:04', '2025-08-01 16:00:04'),
(110, 'http://localhost:8080/uploads/products/a6621610-fd92-4aca-8ba6-00331d8cc6e1_by-pnj-feminine-03.jpg', 0, 20, '2025-08-01 16:00:04', '2025-08-01 16:00:04'),
(111, 'http://localhost:8080/uploads/products/38cee0db-6651-497a-803a-bfd30733ecd3_by-pnj-feminine-04.jpg', 0, 20, '2025-08-01 16:00:05', '2025-08-01 16:00:05'),
(112, 'http://localhost:8080/uploads/products/3444ea37-9be7-4f42-9db5-bc71e6d0846b_by-pnj-feminine-05.jpg', 0, 20, '2025-08-01 16:00:05', '2025-08-01 16:00:05'),
(113, 'http://localhost:8080/uploads/products/0c72ebaf-63ed-4d93-926b-397aecd31a10_pnj-euphoria-1.png', 1, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(114, 'http://localhost:8080/uploads/products/23b2e045-5113-43e6-9343-739c7b4c859c_pnj-euphoria-2.png', 0, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(115, 'http://localhost:8080/uploads/products/244417de-2fa6-4b67-b7e5-0773f4239da5_pnj-euphoria-3.png', 0, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(116, 'http://localhost:8080/uploads/products/6d9acb99-84ce-4ae7-a14b-8a61745bfb31_pnj-euphoria-4.jpg', 0, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(117, 'http://localhost:8080/uploads/products/7993d404-6fc5-4d76-b049-a4695266c257_pnj-euphoria-5.jpg', 0, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(118, 'http://localhost:8080/uploads/products/e1f0495a-d885-4b40-be74-4454a61f0bdd_pnj-euphoria-6.jpg', 0, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(119, 'http://localhost:8080/uploads/products/95145e0e-3708-45ff-aa9f-6c83deda103e_ecz-pnj-1.png', 1, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(120, 'http://localhost:8080/uploads/products/7079438f-cf74-4464-b59d-6ae434e055b2_ecz-pnj-2.png', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(121, 'http://localhost:8080/uploads/products/44a136b3-7a21-43d8-a308-e12bc53bfa6e_ecz-pnj-2.jpg', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(122, 'http://localhost:8080/uploads/products/9258cb9e-94dc-48e5-998d-40f4d7e5f4f0_ecz-pnj-1.jpg', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(123, 'http://localhost:8080/uploads/products/e4006682-551f-4013-9ed7-02ca8cd5c620_ecz-pnj-3.jpg', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(124, 'http://localhost:8080/uploads/products/44eb343e-c16b-471a-a27d-09b8ee72e592_ecz-pnj-3.png', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(125, 'http://localhost:8080/uploads/products/6d9a12b9-0d93-474c-864a-3a16e16a9925_resize_GNXM00W001908.mp4', 0, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(126, 'http://localhost:8080/uploads/products/54efc244-b5d8-4562-b4d4-a8c28ef32e77_vang-18k-pnj-1.png', 1, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(127, 'http://localhost:8080/uploads/products/f9c2631e-d9ae-4466-8557-5e62ee8286bb_vang-18k-pnj-2.png', 0, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(128, 'http://localhost:8080/uploads/products/6512bc6a-5300-4f02-a9d8-183eac9f669a_vang-18k-pnj-3.png', 0, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(129, 'http://localhost:8080/uploads/products/91a06bf8-a8b7-4770-bf23-7ad6af43b6bb_vang-18k-pnj-4.jpg', 0, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(130, 'http://localhost:8080/uploads/products/9f3932dd-3443-4a0d-952d-bfe1a6ff3cf1_vang-18k-pnj-5.jpg', 0, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(131, 'http://localhost:8080/uploads/products/346fd07d-86ce-40b6-b415-74195ddd0ebf_vang-18k-pnj-6.jpg', 0, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(132, 'http://localhost:8080/uploads/products/4883457b-779b-4945-a03c-6db83b5efd75_citrine-pnj-1.png', 1, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(133, 'http://localhost:8080/uploads/products/862fa305-7d94-4808-8730-dc0d44b65af3_citrine-pnj-2.png', 0, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(134, 'http://localhost:8080/uploads/products/de46c806-7f2f-4390-bfc4-517084e1de00_citrine-pnj-3.png', 0, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(135, 'http://localhost:8080/uploads/products/2323b8a6-3684-4ed0-8940-9d35f3424255_citrine-pnj-4.jpg', 0, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(136, 'http://localhost:8080/uploads/products/0e05157d-2f6e-430f-bd41-0ec1362bbdbc_citrine-pnj-5.jpg', 0, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(137, 'http://localhost:8080/uploads/products/a7e977f7-5725-46c6-96ea-97e27aeaf820_citrine-pnj-6.jpg', 0, 24, '2025-08-01 16:24:41', '2025-08-01 16:24:41'),
(153, 'http://localhost:8080/uploads/products/b00880ca-9b65-4b4a-83d2-63611c299417_18k-dinh-da-ruby-pnj-01.png', 1, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(154, 'http://localhost:8080/uploads/products/d998e465-b9c6-4fdb-aa6c-01fef8854d97_18k-dinh-da-ruby-pnj-02.png', 0, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(155, 'http://localhost:8080/uploads/products/f15f7b39-b7a9-4c0d-b94b-790734d38565_18k-dinh-da-ruby-pnj-trau-cau-00022-00023-1.jpg', 0, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(156, 'http://localhost:8080/uploads/products/8143b2f7-e978-4529-b7a0-0e1b222a6893_dinh-da-ruby-pnj-03.png', 0, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(157, 'http://localhost:8080/uploads/products/3f197d75-700e-4df2-9ce4-1458fa54a632_nhan-vang-18k-pnj-1.png', 1, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(158, 'http://localhost:8080/uploads/products/d4f431dc-da29-464b-aac1-815208d1220f_nhan-vang-18k-pnj-2.png', 0, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(159, 'http://localhost:8080/uploads/products/85994269-ef36-4052-810b-c851f6ff53af_nhan-vang-18k-pnj-2.jpg', 0, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(160, 'http://localhost:8080/uploads/products/64143574-ea5a-464d-93ce-af8114fd70a2_nhan-vang-18k-pnj-1.jpg', 0, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(161, 'http://localhost:8080/uploads/products/a97db74b-e446-4a10-a039-195347d020c7_nhan-vang-18k-pnj-3.jpg', 0, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(162, 'http://localhost:8080/uploads/products/c4bb776b-a307-470f-a31d-81b08c1e599c_nhan-vang-18k-pnj-3.png', 0, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(169, 'http://localhost:8080/uploads/products/830721a7-53fa-43ab-814b-b8de6bb90e90_sp-gn0000y003574-nhan-vang-18k-pnj-1.png', 1, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(170, 'http://localhost:8080/uploads/products/eda1428b-8334-4558-946e-42d42a51712d_nhan-vang-18k-pnj-2.png', 0, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(171, 'http://localhost:8080/uploads/products/aee1a07f-16db-4871-a19c-ccf6774a1137_nhan-vang-18k-pnj-2.jpg', 0, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(172, 'http://localhost:8080/uploads/products/43451376-0cea-4b8b-8f82-6054f3fd9129_nhan-vang-18k-pnj-3.jpg', 0, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(173, 'http://localhost:8080/uploads/products/872c7cd0-dcce-40b0-b42b-6017262b5e7e_nhan-vang-18k-pnj-3.png', 0, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(174, 'http://localhost:8080/uploads/products/04e30074-bc1f-4ece-9a07-f5722135f94c_nhan-vang-18k-pnj-1.jpg', 0, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(175, 'http://localhost:8080/uploads/products/f690c64b-78a5-4e09-a06e-c1a3a8e33923_cuong-pnj-vang-18k.png', 1, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(176, 'http://localhost:8080/uploads/products/739f4c30-5f94-4034-974d-7d8c6bf29371_cuong-pnj-vang-18k-2.png', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(177, 'http://localhost:8080/uploads/products/b31ce94f-b53f-4fea-bc9f-9f05130c1021_cuong-pnj-vang-18k-3.png', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(178, 'http://localhost:8080/uploads/products/49081b17-c090-4a46-a4bd-998189fc5ca1_cuong-vang-18k-pnj-vang-son-00633-00313-1.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(179, 'http://localhost:8080/uploads/products/63b5a0c8-ed57-467a-8f7d-9f69421a909c_cuong-vang-18k-pnj-vang-son-00633-00313-2.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(180, 'http://localhost:8080/uploads/products/8500bfb5-bc98-44ea-ae27-b340eb1a9499_cuong-vang-18k-pnj-vang-son-00633-00313-3.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(181, 'http://localhost:8080/uploads/products/d76445ce-cf8c-43c6-ab5a-b902a57397e9_cuong-vang-18k-pnj-vang-son-00633-00313-4.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(182, 'http://localhost:8080/uploads/products/ca85ff8c-6127-429f-b6eb-d0e60c6475d7_cuong-vang-18k-pnj-vang-son-00633-00313-5.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(183, 'http://localhost:8080/uploads/products/5fe6989c-a2b5-4d9e-8ce5-3090b8600f10_cuong-vang-18k-pnj-vang-son-00633-00313-6.jpg', 0, 31, '2025-08-01 16:47:58', '2025-08-01 16:47:58'),
(184, 'http://localhost:8080/uploads/products/dd28fb11-9a90-4978-91e6-48c1665d282e_dinh-da-ruby-pnj.png', 1, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(185, 'http://localhost:8080/uploads/products/3e21a59a-95b1-4f4d-b02d-ff5c4837949a_dinh-da-ruby-pnj-01.png', 0, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(186, 'http://localhost:8080/uploads/products/1cb9c4be-c2af-4a67-8b3d-28cb303d7086_dinh-da-ruby-pnj-02.png', 0, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(187, 'http://localhost:8080/uploads/products/9cae62ba-6a61-4c4a-b54f-712a79346a26_dinh-da-ruby-pnj-03.jpg', 0, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(188, 'http://localhost:8080/uploads/products/50df3187-3d3a-4470-9c4b-b6a70896bd1c_nhan-vang-18k-pnj-1.png', 1, 33, '2025-08-01 16:57:50', '2025-08-01 16:57:50'),
(189, 'http://localhost:8080/uploads/products/9d4f646b-e853-4052-b617-b53dba4b251e_nhan-vang-18k-pnj-2.png', 0, 33, '2025-08-01 16:57:50', '2025-08-01 16:57:50'),
(190, 'http://localhost:8080/uploads/products/94639a21-2db2-4fdf-9956-c6502ed68ab9_nhan-vang-18k-pnj-3.png', 0, 33, '2025-08-01 16:57:50', '2025-08-01 16:57:50'),
(191, 'http://localhost:8080/uploads/products/e85119d4-32ca-48d6-86df-3855ddc78a09_nhan-cuoi-vang-24k-pnj-1.png', 1, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(192, 'http://localhost:8080/uploads/products/a177d73f-c4f3-4ae5-ab60-90f3c256b17b_nhan-cuoi-vang-24k-pnj-2.png', 0, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(193, 'http://localhost:8080/uploads/products/d159acee-1e32-49e4-a1ed-6781b20087bd_nhan-cuoi-vang-24k-pnj-2.jpg', 0, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(194, 'http://localhost:8080/uploads/products/78c83662-9777-4a5e-8f1a-22587ee34254_nhan-cuoi-vang-24k-pnj-1.jpg', 0, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(195, 'http://localhost:8080/uploads/products/814aef7a-20c2-4748-a279-860ae96b9683_nhan-cuoi-vang-24k-pnj-3.jpg', 0, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(196, 'http://localhost:8080/uploads/products/6eb6b79d-0003-467f-ad14-dd62c4d7231e_nhan-cuoi-vang-24k-pnj-3.png', 0, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(197, 'http://localhost:8080/uploads/products/c828b654-1601-4875-afbc-dac21a8c13b8_nhan-nam-vang-24k-pnj-1.png', 1, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(198, 'http://localhost:8080/uploads/products/d41969ee-18d8-4d23-b3d2-43e088e90ab6_nhan-nam-vang-24k-pnj-2.png', 0, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(199, 'http://localhost:8080/uploads/products/52b2d5a7-946a-4555-87ac-1bc6f8e687d0_nhan-nam-vang-24k-pnj-3.png', 0, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(200, 'http://localhost:8080/uploads/products/e1eaa803-a371-424c-81df-2ca50b476238_nhan-nam-cuoi-vang-24k-pnj-01.jpg', 0, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(201, 'http://localhost:8080/uploads/products/5169b7c3-5d58-4056-a124-ae43d99111d6_nhan-nam-cuoi-vang-24k-pnj-02.jpg', 0, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(202, 'http://localhost:8080/uploads/products/ad930405-c862-4817-b573-9660cbadc0a6_nhan-nam-cuoi-vang-24k-pnj-03.jpg', 0, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(203, 'http://localhost:8080/uploads/products/fb2a1b5d-e89d-4b92-808b-3f8439e48e20_vang-dinh-da-quartz-pnj-1.png', 1, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(204, 'http://localhost:8080/uploads/products/c20034be-b418-497e-a619-ad8bae411c2d_vang-dinh-da-quartz-pnj-2.png', 0, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(205, 'http://localhost:8080/uploads/products/6371d12a-cc8b-4bb6-8d80-c34a87146f46_vang-dinh-da-quartz-pnj-2.jpg', 0, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(206, 'http://localhost:8080/uploads/products/558e90bc-a361-4caa-befc-cf25b701b384_vang-dinh-da-quartz-pnj-1.jpg', 0, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(207, 'http://localhost:8080/uploads/products/6f07a753-08e3-4fb3-b6ec-54e94015b1a0_vang-dinh-da-quartz-pnj-3.jpg', 0, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(208, 'http://localhost:8080/uploads/products/748ed9db-87fe-4120-a864-72dcdcc5168f_vang-dinh-da-quartz-pnj-3.png', 0, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(209, 'http://localhost:8080/uploads/products/853913c4-3dad-4509-81f6-b4ea1892ac24_nhan-vang-24k-pnj-1.png', 1, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(210, 'http://localhost:8080/uploads/products/7a6d6982-080e-45e1-8429-b3bf2ef37610_nhan-vang-24k-pnj-2.png', 0, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(211, 'http://localhost:8080/uploads/products/3cf1afae-7915-4903-8aa2-f979d33feb8d_nhan-vang-24k-pnj-3.png', 0, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(212, 'http://localhost:8080/uploads/products/cbe61433-4c9b-4522-9709-c5adc18b9e20_nhan-vang-24k-pnj-4.jpg', 0, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(213, 'http://localhost:8080/uploads/products/7e4d903e-e05c-42eb-a80c-060178f6a0a4_nhan-vang-24k-pnj-5.jpg', 0, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(214, 'http://localhost:8080/uploads/products/f9089958-04b0-4d54-9e5c-dd5eae221159_nhan-vang-24k-pnj-6.jpg', 0, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(215, 'http://localhost:8080/uploads/products/dfa6b3e1-72b2-4aa8-9efd-3d2341d8a82f_vang-24k-pnj-trau-cau-01.png', 1, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(216, 'http://localhost:8080/uploads/products/cb409ccf-f9ae-47bd-8ced-1b11a844d867_vang-24k-pnj-trau-cau-02.png', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(217, 'http://localhost:8080/uploads/products/0be14e68-95ca-43c6-b328-d26597c17156_vang-24k-pnj-trau-cau-03.png', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(218, 'http://localhost:8080/uploads/products/eef41dc1-3ab2-40a6-9ea7-9972ce7947fc_cuoi-vang-24k-pnj-trau-cau-00360-02848-2.jpg', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(219, 'http://localhost:8080/uploads/products/1353e70e-8987-48fd-a796-73fd31f76980_nhan-cuoi-vang-24k-pnj-trau-cau-01.jpg', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(220, 'http://localhost:8080/uploads/products/2dddbb83-f9ee-44fb-b9b2-c35815f409d9_nhan-cuoi-vang-24k-pnj-trau-cau-02.jpg', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(221, 'http://localhost:8080/uploads/products/92078124-d278-4bad-867b-27c1a71f6b0c_nhan-cuoi-vang-24k-pnj-trau-cau-03.jpg', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(222, 'http://localhost:8080/uploads/products/83b41570-b08b-46ce-9507-a37bee63977a_cuoi-vang-24k-pnj-trau-cau-00360-02848-1.jpg', 0, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(227, 'http://localhost:8080/uploads/products/71e371ed-5943-408a-842d-1486ca8a04d3_kim-long-truong-cuu-1.png', 1, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(228, 'http://localhost:8080/uploads/products/320a233a-69d3-4361-b23c-6ffc26425dcf_kim-long-truong-cuu-2.png', 0, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(229, 'http://localhost:8080/uploads/products/ac98ec80-254c-4674-a22c-69199ee796dc_kim-long-truong-cuu-3.png', 0, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(230, 'http://localhost:8080/uploads/products/1e43b064-197f-408d-b940-65375daa7f8f_kim-long-truong-cuu-1.jpg', 0, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(231, 'http://localhost:8080/uploads/products/b494a217-3fa2-470e-a862-32b6695ba900_dinh-da-ecz-pnj-1.png', 1, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(232, 'http://localhost:8080/uploads/products/761b1b90-5532-4384-b9fc-faf7c2639b7d_dinh-da-ecz-pnj-2.png', 0, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(233, 'http://localhost:8080/uploads/products/0940b44c-f720-4ee5-9b67-92a64031c1c7_dinh-da-ecz-pnj-3.png', 0, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(234, 'http://localhost:8080/uploads/products/c0811a12-0213-462a-a0e9-cca5eef8b41c_dinh-da-ecz-pnj-1.jpg', 0, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(235, 'http://localhost:8080/uploads/products/611f389f-14c6-43c8-b283-9b688c97edb8_dinh-da-ecz-pnj-2.jpg', 0, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(236, 'http://localhost:8080/uploads/products/b15e6f14-6aa9-4bfd-bede-431cf078f253_dinh-da-ecz-pnj-3.jpg', 0, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(243, 'http://localhost:8080/uploads/products/77be388f-eff9-4f48-9886-dd5068a3bfad_tsavorite-pnj-1.png', 1, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(244, 'http://localhost:8080/uploads/products/23b9f96c-b20a-4844-be08-c6e6b938bece_tsavorite-pnj-2.png', 0, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(245, 'http://localhost:8080/uploads/products/540c7ad0-8007-417a-9708-aeb3b3ed593f_tsavorite-pnj-3.png', 0, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(246, 'http://localhost:8080/uploads/products/d22e63af-7d89-4447-ae83-dd800433393e_tsavorite-pnj-1.jpg', 0, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(247, 'http://localhost:8080/uploads/products/b72db842-242d-41eb-add0-9c286dc85c1b_tsavorite-pnj-2.jpg', 0, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(248, 'http://localhost:8080/uploads/products/b5a1ac81-f6be-41e2-aa79-c073e9dc6422_tsavorite-pnj-3.jpg', 0, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(249, 'http://localhost:8080/uploads/products/1e2bda6a-486d-4819-ace6-613108171bff_Nam-Vang-24K-PNJ-1.png', 1, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(250, 'http://localhost:8080/uploads/products/d82300d9-a7d4-4d7d-8d8c-3a30abf0535b_Nam-Vang-24K-PNJ-2.png', 0, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(251, 'http://localhost:8080/uploads/products/979a2925-4657-4604-8b26-7508c22fa7a4_Nam-Vang-24K-PNJ-3.png', 0, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(252, 'http://localhost:8080/uploads/products/31f38e69-f180-468c-9bb4-a76c936181fb_nam-vang-24k-pnj-4.jpg', 0, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(253, 'http://localhost:8080/uploads/products/9295c6d1-dba0-48b3-a3d6-198f08d3eaf1_nam-vang-24k-pnj-5.jpg', 0, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(254, 'http://localhost:8080/uploads/products/9b09f754-e1bf-48bf-8d51-40203550793d_nam-vang-24k-pnj-6.jpg', 0, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(255, 'http://localhost:8080/uploads/products/5b81c7cb-cb8d-429b-8166-2e0b4c5f6be4_bong-tai-tre-em-vang-10k-pnj-sweeties-1.png', 1, 45, '2025-08-03 13:58:35', '2025-08-03 13:58:35'),
(256, 'http://localhost:8080/uploads/products/a4875fab-d27d-4376-9ca0-5d722c5e8ff3_bong-tai-tre-em-vang-10k-pnj-sweeties-2.png', 0, 45, '2025-08-03 13:58:35', '2025-08-03 13:58:35'),
(257, 'http://localhost:8080/uploads/products/c1ab5220-7638-487a-b43d-61644867fcde_dinh-da-ecz-pnj-sweeties-1.png', 1, 46, '2025-08-03 13:59:24', '2025-08-03 13:59:24'),
(258, 'http://localhost:8080/uploads/products/82cb33c7-5956-4e5f-9661-f29be830aadd_dinh-da-ecz-pnj-sweeties-2.png', 0, 46, '2025-08-03 13:59:24', '2025-08-03 13:59:24'),
(259, 'http://localhost:8080/uploads/products/97e75fe6-29f2-4e84-ae7a-2d9de1e4a019_lac-tay-tre-em-bac-pnjsilver-1.png', 1, 47, '2025-08-03 14:00:11', '2025-08-03 14:00:11'),
(260, 'http://localhost:8080/uploads/products/c5ed8403-81d6-4d91-b683-3ef7b32bb01d_lac-tay-tre-em-bac-pnjsilver-2.png', 0, 47, '2025-08-03 14:00:11', '2025-08-03 14:00:11'),
(261, 'http://localhost:8080/uploads/products/80e08fd6-f073-482a-b837-a31766d4b6d9_ruby-pnj-magnetique-1.png', 1, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(262, 'http://localhost:8080/uploads/products/954482ba-ad15-4ef5-89e6-e0cbf7023a19_ruby-pnj-magnetique-2.png', 0, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(263, 'http://localhost:8080/uploads/products/61c7f39c-c933-4410-a88b-35ae59f0b410_ruby-pnj-magnetique-3.png', 0, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(264, 'http://localhost:8080/uploads/products/49f334cb-ca69-4297-be8a-dc3ef7a845b2_ruby-pnj-magnetique-4.jpg', 0, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(265, 'http://localhost:8080/uploads/products/29103bd3-c40d-4327-97a0-a5805a0e4452_ruby-pnj-magnetique-5.jpg', 0, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(266, 'http://localhost:8080/uploads/products/daf2cf8b-1566-4f1d-bf1f-699b9b2c6c48_ruby-pnj-magnetique-6.jpg', 0, 48, '2025-08-03 14:02:41', '2025-08-03 14:02:41'),
(267, 'http://localhost:8080/uploads/products/1a9ad64d-bec4-4459-8805-93fba30c5c23_dinh-da-pnjsilver-1.png', 1, 49, '2025-08-03 14:03:36', '2025-08-03 14:03:36'),
(268, 'http://localhost:8080/uploads/products/6802354d-9b28-43db-93ed-ad79b7ed5313_dinh-da-pnjsilver-2.png', 0, 49, '2025-08-03 14:03:36', '2025-08-03 14:03:36'),
(269, 'http://localhost:8080/uploads/products/c5eb6ed0-8c87-4db5-a780-1b7a66372cf9_dinh-da-pnjsilver-3.jpg', 0, 49, '2025-08-03 14:03:36', '2025-08-03 14:03:36'),
(270, 'http://localhost:8080/uploads/products/27124bb9-3aa8-4e16-af90-535104c463d6_dinh-da-pnjsilver-4.jpg', 0, 49, '2025-08-03 14:03:36', '2025-08-03 14:03:36'),
(271, 'http://localhost:8080/uploads/products/e06ebc14-dd05-4f5c-8d95-7ff447dd64f9_dinh-da-pnjsilver-5.jpg', 0, 49, '2025-08-03 14:03:36', '2025-08-03 14:03:36'),
(272, 'http://localhost:8080/uploads/products/fb68521d-9734-4a33-995f-df561dba0c44_ecz-style-by-pnj-1.png', 1, 50, '2025-08-03 14:04:21', '2025-08-03 14:04:21'),
(273, 'http://localhost:8080/uploads/products/47fe7b47-58c1-4ca6-9e02-8097d4372a2c_ecz-style-by-pnj-2.png', 0, 50, '2025-08-03 14:04:21', '2025-08-03 14:04:21'),
(274, 'http://localhost:8080/uploads/products/cb6c9c7d-cfbc-4ae0-8611-09d022185860_ecz-style-by-pnj-3.jpg', 0, 50, '2025-08-03 14:04:21', '2025-08-03 14:04:21'),
(275, 'http://localhost:8080/uploads/products/fd26c526-d898-473b-9c42-3bd05f5cdaeb_ecz-style-by-pnj-4.jpg', 0, 50, '2025-08-03 14:04:21', '2025-08-03 14:04:21'),
(276, 'http://localhost:8080/uploads/products/cb168a3b-2132-4da8-8457-17e81c1380e9_ecz-style-by-pnj-5.jpg', 0, 50, '2025-08-03 14:04:21', '2025-08-03 14:04:21'),
(277, 'http://localhost:8080/uploads/products/17bedea7-6c2c-4a2d-b897-18d8a4665fbf_ecz-style-by-pnj-1.png', 1, 51, '2025-08-03 14:05:15', '2025-08-03 14:05:15'),
(278, 'http://localhost:8080/uploads/products/ef8fff64-a36f-40a2-9590-4a86170b0cf8_ecz-style-by-pnj-2.png', 0, 51, '2025-08-03 14:05:15', '2025-08-03 14:05:15'),
(279, 'http://localhost:8080/uploads/products/cae4acb3-7138-4009-9d4d-200b51ff5f99_ecz-style-by-pnj-3.jpg', 0, 51, '2025-08-03 14:05:15', '2025-08-03 14:05:15'),
(280, 'http://localhost:8080/uploads/products/4c97f8dd-f239-4074-b82f-21b6f6d13533_ecz-style-by-pnj-4.jpg', 0, 51, '2025-08-03 14:05:15', '2025-08-03 14:05:15'),
(281, 'http://localhost:8080/uploads/products/772e4dba-ba96-4f63-aa3b-3b2cc73013f9_ecz-style-by-pnj-5.jpg', 0, 51, '2025-08-03 14:05:15', '2025-08-03 14:05:15'),
(282, 'http://localhost:8080/uploads/products/9b067603-f276-4732-940c-725db345281b_ecz-pnj-1.png', 1, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(283, 'http://localhost:8080/uploads/products/23c85204-f66a-45c4-82ac-b9eb903e4484_ecz-pnj-2.png', 0, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(284, 'http://localhost:8080/uploads/products/7c72096d-55d0-426f-8849-ed60cb2856a7_ecz-pnj-3.png', 0, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(285, 'http://localhost:8080/uploads/products/0432c765-8904-4a42-995c-b8840150b7c1_ecz-pnj-4.jpg', 0, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(286, 'http://localhost:8080/uploads/products/6910a3f4-1f23-4a1c-94b2-b965d8b40532_ecz-pnj-5.jpg', 0, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(287, 'http://localhost:8080/uploads/products/cd27ff48-5719-4540-b1ea-017ffdc43df9_ecz-pnj-6.jpg', 0, 52, '2025-08-03 14:05:58', '2025-08-03 14:05:58'),
(288, 'http://localhost:8080/uploads/products/69198e86-ee8b-479c-940e-92ea93285e05_18k-pnj-1.png', 1, 53, '2025-08-03 14:07:19', '2025-08-03 14:07:19'),
(289, 'http://localhost:8080/uploads/products/2b0ba0f3-1dcc-4286-861d-f29c61818409_18k-pnj-2.png', 0, 53, '2025-08-03 14:07:19', '2025-08-03 14:07:19'),
(290, 'http://localhost:8080/uploads/products/a5ddde24-5eca-4a71-85ac-41b3e4637b49_18k-pnj-3.jpg', 0, 53, '2025-08-03 14:07:19', '2025-08-03 14:07:19'),
(291, 'http://localhost:8080/uploads/products/24fcb9c2-0d73-4256-ac2a-b6939bf7dd75_18k-pnj-4.jpg', 0, 53, '2025-08-03 14:07:19', '2025-08-03 14:07:19'),
(292, 'http://localhost:8080/uploads/products/ca49b9fc-a3b3-4324-bfac-535993228bbd_18k-pnj-5.jpg', 0, 53, '2025-08-03 14:07:19', '2025-08-03 14:07:19'),
(293, 'http://localhost:8080/uploads/products/c4d2638b-204c-4bf6-891b-58bb21f7b690_by-pnj-1.png', 1, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(294, 'http://localhost:8080/uploads/products/de077b0b-1ed7-4c6e-b61d-2c09dfddd436_by-pnj-2.png', 0, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(295, 'http://localhost:8080/uploads/products/b1daf75d-33ce-4b75-b3ac-96bdca399293_by-pnj-3.jpg', 0, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(296, 'http://localhost:8080/uploads/products/19bf5e49-a99a-430d-9a97-936bcb35ca0f_by-pnj-4.jpg', 0, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(297, 'http://localhost:8080/uploads/products/3049de24-dc97-4658-9755-4ae8584cab86_by-pnj-5.jpg', 0, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(298, 'http://localhost:8080/uploads/products/3d0f4e2d-4e84-4c82-95cc-b1526540c273_by-pnj-6.jpg', 0, 54, '2025-08-03 14:08:09', '2025-08-03 14:08:09'),
(299, 'http://localhost:8080/uploads/products/498ab916-e831-4605-8911-b778e7b4a33c_dinh-da-ecz-pnj-1.png', 1, 55, '2025-08-03 14:09:01', '2025-08-03 14:09:01'),
(300, 'http://localhost:8080/uploads/products/71039618-c573-457a-a131-2da57a4de9d5_dinh-da-ecz-pnj-2.png', 0, 55, '2025-08-03 14:09:01', '2025-08-03 14:09:01'),
(301, 'http://localhost:8080/uploads/products/44812d7e-a039-4d9a-8028-efb737632e99_dinh-da-ecz-pnj-3.jpg', 0, 55, '2025-08-03 14:09:01', '2025-08-03 14:09:01'),
(302, 'http://localhost:8080/uploads/products/5f0d847d-dac3-4720-b5ad-6830735ed1fc_dinh-da-ecz-pnj-4.jpg', 0, 55, '2025-08-03 14:09:01', '2025-08-03 14:09:01'),
(303, 'http://localhost:8080/uploads/products/4b7bf4db-f1fb-4acc-b66f-582a8c7cafa5_by-pnj-love-potion-1.png', 1, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(304, 'http://localhost:8080/uploads/products/58b2b981-48c2-474a-8a1c-630b103bb423_by-pnj-love-potion-2.png', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(305, 'http://localhost:8080/uploads/products/3c2bf321-2ecb-4152-bddb-924dbaff707a_by-pnj-love-potion-3.jpg', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(306, 'http://localhost:8080/uploads/products/1f7ace97-2abd-41a4-bfda-8cbb9eaaa526_by-pnj-love-potion-4.jpg', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(307, 'http://localhost:8080/uploads/products/c6d9cfad-4b68-4fe2-9e7d-0d108dd754bd_by-pnj-love-potion-5.jpg', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(308, 'http://localhost:8080/uploads/products/fabd12fe-8966-4aa3-9fb8-d78d75d656fd_by-pnj-love-potion-6.jpg', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(309, 'http://localhost:8080/uploads/products/87f2542f-ad3f-4021-b1fa-7d78522c220f_by-pnj-love-potion-7.jpg', 0, 56, '2025-08-03 14:10:32', '2025-08-03 14:10:32'),
(310, 'http://localhost:8080/uploads/products/9c15baa0-0a50-49c3-840e-8f019c8ca650_da-ecz-pnj-1.png', 1, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(311, 'http://localhost:8080/uploads/products/feb30035-0b76-443f-bf6f-23e9262ae4b7_da-ecz-pnj-2.png', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(312, 'http://localhost:8080/uploads/products/f06bd033-f2cd-447f-ad54-95250c717680_da-ecz-pnj-3.png', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(313, 'http://localhost:8080/uploads/products/405b67f8-3561-47a0-bb0b-23f4e29896d7_dinh-da-ecz-pnj-4.jpg', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(314, 'http://localhost:8080/uploads/products/bfce27f8-0d61-4d74-bc94-ae50f70fb0aa_dinh-da-ecz-pnj-5.jpg', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(315, 'http://localhost:8080/uploads/products/8b4254ff-92a3-4da5-91ed-6408a52a4acf_dinh-da-ecz-pnj-6.jpg', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(316, 'http://localhost:8080/uploads/products/fe8830fc-5c37-4601-b2a4-6ec958d57f7f_dinh-da-ecz-pnj-7.jpg', 0, 57, '2025-08-03 14:11:31', '2025-08-03 14:11:31'),
(317, 'http://localhost:8080/uploads/products/5b20b3a1-d27d-476d-982e-5c4025ce9c1e_by-pnj-lucky-me-1.png', 1, 58, '2025-08-03 14:16:27', '2025-08-03 14:16:27'),
(318, 'http://localhost:8080/uploads/products/00e47c88-4ef0-47d8-80ef-b2fe041b154f_by-pnj-lucky-me-2.png', 0, 58, '2025-08-03 14:16:27', '2025-08-03 14:16:27'),
(319, 'http://localhost:8080/uploads/products/a78e0993-a171-4df9-b7d1-d07ea23ec64e_by-pnj-lucky-me-3.jpg', 0, 58, '2025-08-03 14:16:27', '2025-08-03 14:16:27'),
(320, 'http://localhost:8080/uploads/products/b32a039b-c4d8-4970-b6ba-fd7664c28edb_by-pnj-lucky-me-4jpg.jpg', 0, 58, '2025-08-03 14:16:27', '2025-08-03 14:16:27'),
(321, 'http://localhost:8080/uploads/products/727c7b6a-4656-4e59-847e-a3f36e232ba3_by-pnj-lucky-me-5.jpg', 0, 58, '2025-08-03 14:16:27', '2025-08-03 14:16:27'),
(322, 'http://localhost:8080/uploads/products/dc629a78-96f8-4bd6-a452-eb8e2d65257e_alice-in-wonderland-01.png', 1, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(323, 'http://localhost:8080/uploads/products/7bd2f8dd-c5fb-4ed9-91ca-e074de6acaf2_alice-in-wonderland-02.png', 0, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(324, 'http://localhost:8080/uploads/products/6c6dc8cd-6e66-415a-97c9-592ea5733622_alice-in-wonderland-03.png', 0, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(325, 'http://localhost:8080/uploads/products/9bd7f50a-9c10-4c68-96d1-f29f35e75449_alice-in-wonderland-04.jpg', 0, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(326, 'http://localhost:8080/uploads/products/95865fe6-2195-4e9e-a07a-36eaeeaac0cd_alice-in-wonderland-05.jpg', 0, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(327, 'http://localhost:8080/uploads/products/c96ec470-6ce8-4b6c-b0e5-01a14d989988_alice-in-wonderland-06.jpg', 0, 59, '2025-08-03 14:17:04', '2025-08-03 14:17:04'),
(328, 'http://localhost:8080/uploads/products/5129420d-1d86-4d20-883a-aa171aeff330_pnj-mickey-alone-1.png', 1, 60, '2025-08-03 14:17:57', '2025-08-03 14:17:57'),
(329, 'http://localhost:8080/uploads/products/42afef39-36d2-48ce-8e53-9875f0fdc8ef_pnj-mickey-alone-2.png', 0, 60, '2025-08-03 14:17:57', '2025-08-03 14:17:57'),
(330, 'http://localhost:8080/uploads/products/e892e905-f764-4d38-9113-90fbc9bd3d39_pnj-mickey-alone-3.jpg', 0, 60, '2025-08-03 14:17:57', '2025-08-03 14:17:57'),
(331, 'http://localhost:8080/uploads/products/0d53fc06-5ed8-4ea8-91af-fc1066e810da_pnj-mickey-alone-4.jpg', 0, 60, '2025-08-03 14:17:57', '2025-08-03 14:17:57'),
(332, 'http://localhost:8080/uploads/products/d532687f-7886-40d4-90e8-26c2e8956bfa_pnj-mickey-alone-5.jpg', 0, 60, '2025-08-03 14:17:57', '2025-08-03 14:17:57'),
(333, 'http://localhost:8080/uploads/products/15c96656-0108-4028-8ede-b8da4ba41402_ecz-pnj-1.png', 1, 61, '2025-08-03 14:18:37', '2025-08-03 14:18:37'),
(334, 'http://localhost:8080/uploads/products/d76e93cd-f1a3-4ec6-989f-e3ec7e7a23e3_ecz-pnj-2.png', 0, 61, '2025-08-03 14:18:37', '2025-08-03 14:18:37'),
(335, 'http://localhost:8080/uploads/products/eb807de8-90b3-430d-af2d-5b635aa79656_ecz-pnj-3.jpg', 0, 61, '2025-08-03 14:18:37', '2025-08-03 14:18:37'),
(336, 'http://localhost:8080/uploads/products/e34f73ea-87c4-4ce7-a5d8-09114496d83d_ecz-pnj-4.jpg', 0, 61, '2025-08-03 14:18:37', '2025-08-03 14:18:37'),
(337, 'http://localhost:8080/uploads/products/dd53baaf-892b-4dd4-9a3c-352643a49b50_ecz-pnj-5.jpg', 0, 61, '2025-08-03 14:18:37', '2025-08-03 14:18:37'),
(338, 'http://localhost:8080/uploads/products/3a5804fb-3035-4e72-9b08-0fa6c235dc3b_pnj-cinderella-1.png', 1, 62, '2025-08-03 14:19:13', '2025-08-03 14:19:13'),
(339, 'http://localhost:8080/uploads/products/e6b3a084-bb8d-4946-9e6b-dc447bd9161e_pnj-cinderella-2.png', 0, 62, '2025-08-03 14:19:13', '2025-08-03 14:19:13'),
(340, 'http://localhost:8080/uploads/products/90c2e291-5f7c-4d4f-8faf-83546e6873c7_pnj-cinderella-3.jpg', 0, 62, '2025-08-03 14:19:13', '2025-08-03 14:19:13'),
(341, 'http://localhost:8080/uploads/products/3e1b3a3f-1636-469a-8f9f-5d4b8df9c4d2_pnj-cinderella-4.jpg', 0, 62, '2025-08-03 14:19:13', '2025-08-03 14:19:13'),
(342, 'http://localhost:8080/uploads/products/ad73880d-01c2-4380-a2be-9ef574543e81_pnj-cinderella-5.jpg', 0, 62, '2025-08-03 14:19:13', '2025-08-03 14:19:13'),
(343, 'http://localhost:8080/uploads/products/7fad1a2c-72e9-432e-869a-6d1565d96514_disneypnj-01.png', 1, 63, '2025-08-03 14:20:02', '2025-08-03 14:20:02'),
(344, 'http://localhost:8080/uploads/products/186c233d-2f86-4697-8540-53455982e50f_disneypnj-02.png', 0, 63, '2025-08-03 14:20:02', '2025-08-03 14:20:02'),
(345, 'http://localhost:8080/uploads/products/29fa34a8-e746-489c-8916-5f6702fbc51a_disneypnj-04.jpg', 0, 63, '2025-08-03 14:20:02', '2025-08-03 14:20:02'),
(346, 'http://localhost:8080/uploads/products/aa6c5c43-4857-4536-a7b7-c25abe7878ad_disneypnj-05.jpg', 0, 63, '2025-08-03 14:20:02', '2025-08-03 14:20:02'),
(347, 'http://localhost:8080/uploads/products/a759bc1f-3e12-4ec0-8d48-4af2789715d7_disneypnj-06.jpg', 0, 63, '2025-08-03 14:20:02', '2025-08-03 14:20:02'),
(348, 'http://localhost:8080/uploads/products/d750e172-35ef-45f6-8732-50d0b009fd00_synthetic-pnj-1.png', 1, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41'),
(349, 'http://localhost:8080/uploads/products/f0dc788a-f0ee-48e8-9937-14502ea0a089_synthetic-pnj-2.png', 0, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41'),
(350, 'http://localhost:8080/uploads/products/2d74df86-6d80-4d0e-bdfa-b67a7c267af8_synthetic-pnj-3.png', 0, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41');
INSERT INTO `product_image` (`id`, `image_url`, `is_primary`, `product_id`, `created_at`, `updated_at`) VALUES
(351, 'http://localhost:8080/uploads/products/0880839c-4296-44c0-bfa6-3983c2745ccf_synthetic-pnj-4.jpg', 0, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41'),
(352, 'http://localhost:8080/uploads/products/1e4cf556-504f-4f37-90c2-f72cd2b32a47_synthetic-pnj-5.jpg', 0, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41'),
(353, 'http://localhost:8080/uploads/products/43b678f1-2f65-4859-8738-51749139bd1a_synthetic-pnj-6.jpg', 0, 64, '2025-08-03 14:20:41', '2025-08-03 14:20:41'),
(354, 'http://localhost:8080/uploads/products/39546f20-0c49-4699-9426-4bef9dc289a0_snow-white-the-seven-dwarfs-1.png', 1, 65, '2025-08-03 14:21:26', '2025-08-03 14:21:26'),
(355, 'http://localhost:8080/uploads/products/726bf0ca-2d1d-4d73-8bf7-c4acd45d6871_snow-white-the-seven-dwarfs-2.png', 0, 65, '2025-08-03 14:21:26', '2025-08-03 14:21:26'),
(356, 'http://localhost:8080/uploads/products/6160a198-52e9-4605-a59a-8f7a22eebfd2_snow-white-the-seven-dwarfs-3.jpg', 0, 65, '2025-08-03 14:21:26', '2025-08-03 14:21:26'),
(357, 'http://localhost:8080/uploads/products/681c6190-6015-4eb6-9765-a28a965accc4_snow-white-the-seven-dwarfs-4.jpg', 0, 65, '2025-08-03 14:21:26', '2025-08-03 14:21:26'),
(358, 'http://localhost:8080/uploads/products/17b602e5-0677-4b36-8216-c7a82a51d90b_snow-white-the-seven-dwarfs-5.jpg', 0, 65, '2025-08-03 14:21:26', '2025-08-03 14:21:26'),
(359, 'http://localhost:8080/uploads/products/4abe326a-67a7-4758-b257-70c14e51e9f4_ecz-pnj-1.png', 1, 66, '2025-08-03 14:24:05', '2025-08-03 14:24:05'),
(360, 'http://localhost:8080/uploads/products/182f483f-a169-45ff-8f6c-713da47427db_ecz-pnj-2.png', 0, 66, '2025-08-03 14:24:05', '2025-08-03 14:24:05'),
(361, 'http://localhost:8080/uploads/products/276c28f3-dc6e-450d-9b65-7ce70d6b2f28_ecz-pnj-3.jpg', 0, 66, '2025-08-03 14:24:05', '2025-08-03 14:24:05'),
(362, 'http://localhost:8080/uploads/products/09dd4814-7992-402c-bc51-a4f688af7c29_ecz-pnj-4.jpg', 0, 66, '2025-08-03 14:24:05', '2025-08-03 14:24:05'),
(363, 'http://localhost:8080/uploads/products/ffd8691a-3e50-4b0f-bcf3-84d51646cffd_ecz-pnj-5.jpg', 0, 66, '2025-08-03 14:24:05', '2025-08-03 14:24:05'),
(364, 'http://localhost:8080/uploads/products/eca816cf-283e-4237-82bc-d84d90a0c9fe_beauty-and-the-beast-1.png', 1, 67, '2025-08-03 14:24:55', '2025-08-03 14:24:55'),
(365, 'http://localhost:8080/uploads/products/bcdb9a8f-ae2d-4051-939e-b337502cf4da_beauty-and-the-beast-2.png', 0, 67, '2025-08-03 14:24:55', '2025-08-03 14:24:55'),
(366, 'http://localhost:8080/uploads/products/47723b4d-e3eb-4de1-812a-1f91c0222db8_beauty-and-the-beast-3.jpg', 0, 67, '2025-08-03 14:24:55', '2025-08-03 14:24:55'),
(367, 'http://localhost:8080/uploads/products/b3dd28f5-b1cd-41fe-82a5-883ace2e6d67_beauty-and-the-beast-4.jpg', 0, 67, '2025-08-03 14:24:55', '2025-08-03 14:24:55'),
(368, 'http://localhost:8080/uploads/products/88d14356-71e3-4d14-a2fa-e2c85238c07c_beauty-and-the-beast-5.jpg', 0, 67, '2025-08-03 14:24:55', '2025-08-03 14:24:55'),
(369, 'http://localhost:8080/uploads/products/8d786953-391d-4313-900c-e2fba322433d_vang-14k-pnj-1.png', 1, 68, '2025-08-03 14:25:28', '2025-08-03 14:25:28'),
(370, 'http://localhost:8080/uploads/products/0fe3dfae-f590-411f-a258-5d05bfbf4947_vang-14k-pnj-2.png', 0, 68, '2025-08-03 14:25:28', '2025-08-03 14:25:28'),
(371, 'http://localhost:8080/uploads/products/2a935629-d437-4387-a9a4-6cab053267c6_vang-14k-pnj-3.jpg', 0, 68, '2025-08-03 14:25:28', '2025-08-03 14:25:28'),
(372, 'http://localhost:8080/uploads/products/7042d327-6b0d-4925-85da-4f843d182f12_vang-14k-pnj-4.jpg', 0, 68, '2025-08-03 14:25:28', '2025-08-03 14:25:28'),
(373, 'http://localhost:8080/uploads/products/75e5a9d0-40e0-464a-b8f7-a81f131256fc_vang-14k-pnj-5.jpg', 0, 68, '2025-08-03 14:25:28', '2025-08-03 14:25:28'),
(374, 'http://localhost:8080/uploads/products/0d06c55b-95e4-41a8-aaed-b5cf00876add_dinh-da-ruby-pnj-1.png', 1, 69, '2025-08-03 14:26:01', '2025-08-03 14:26:01'),
(375, 'http://localhost:8080/uploads/products/d96ea056-56aa-4fd1-a7e5-6c4bfcb76b3a_dinh-da-ruby-pnj-2.png', 0, 69, '2025-08-03 14:26:01', '2025-08-03 14:26:01'),
(376, 'http://localhost:8080/uploads/products/f529d1bc-9043-42a9-a0cb-bb36121e5c93_dinh-da-ruby-pnj-3.jpg', 0, 69, '2025-08-03 14:26:01', '2025-08-03 14:26:01'),
(377, 'http://localhost:8080/uploads/products/7935face-32f2-4c88-9c45-b8923c93fca8_dinh-da-ruby-pnj-4.jpg', 0, 69, '2025-08-03 14:26:01', '2025-08-03 14:26:01'),
(378, 'http://localhost:8080/uploads/products/93c57fc4-55c5-4acc-9475-40ef5ea31d8b_dinh-da-ruby-pnj-5.jpg', 0, 69, '2025-08-03 14:26:01', '2025-08-03 14:26:01'),
(379, 'http://localhost:8080/uploads/products/60006c5c-aa2f-42c9-9302-eea79cf1e661_dinh-da-ecz-pnj-1.png', 1, 70, '2025-08-03 14:27:00', '2025-08-03 14:27:00'),
(380, 'http://localhost:8080/uploads/products/75cb1c61-120d-480f-9fa1-75c1f12eb4a0_dinh-da-ecz-pnj-2.png', 0, 70, '2025-08-03 14:27:00', '2025-08-03 14:27:00'),
(381, 'http://localhost:8080/uploads/products/c7685296-eb3e-46aa-9fed-ebef9a3967ee_dinh-da-ecz-pnj-3.jpg', 0, 70, '2025-08-03 14:27:00', '2025-08-03 14:27:00'),
(382, 'http://localhost:8080/uploads/products/524cc650-74a0-48b9-8831-6f81f13c96e3_dinh-da-ecz-pnj-4.jpg', 0, 70, '2025-08-03 14:27:00', '2025-08-03 14:27:00'),
(383, 'http://localhost:8080/uploads/products/04ce1237-c6dc-4890-a829-f43ee42b7e86_dinh-da-ecz-pnj-5.jpg', 0, 70, '2025-08-03 14:27:00', '2025-08-03 14:27:00'),
(384, 'http://localhost:8080/uploads/products/0d8e50c8-4477-4f19-9cd2-fee047d5d0b8_by-ecz-pnj-1.png', 1, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(385, 'http://localhost:8080/uploads/products/d27522f2-9adc-43dd-a02e-1f68ee45f1a9_by-ecz-pnj-2.png', 0, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(386, 'http://localhost:8080/uploads/products/b4c3e654-8a52-44c6-9545-fc10ddf8090f_dinh-da-ecz-pnj-3.jpg', 0, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(387, 'http://localhost:8080/uploads/products/aab69f71-637e-46d3-a79f-9c274614339b_dinh-da-ecz-pnj-4.jpg', 0, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(388, 'http://localhost:8080/uploads/products/83f43145-6650-4686-bbec-0d4b989466fb_dinh-da-ecz-pnj-5.jpg', 0, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(389, 'http://localhost:8080/uploads/products/e8722bb9-d688-461a-a3fe-f6e8d781be9a_dinh-da-ecz-pnj-6.jpg', 0, 71, '2025-08-03 14:28:16', '2025-08-03 14:28:16'),
(390, 'http://localhost:8080/uploads/products/83622d61-e9af-4547-b9da-e82741ab5425_y-18k-pnj-1.png', 1, 72, '2025-08-03 14:29:28', '2025-08-03 14:29:28'),
(391, 'http://localhost:8080/uploads/products/7144e937-5088-437b-9a70-e32c0aa5a049_y-18k-pnj-2.png', 0, 72, '2025-08-03 14:29:28', '2025-08-03 14:29:28'),
(392, 'http://localhost:8080/uploads/products/50f91afb-ae16-460f-8132-981418594e84_y-18k-pnj-3.jpg', 0, 72, '2025-08-03 14:29:28', '2025-08-03 14:29:28'),
(393, 'http://localhost:8080/uploads/products/ec226a7d-137d-4c45-8d03-eb881220064e_y-18k-pnj-4.jpg', 0, 72, '2025-08-03 14:29:28', '2025-08-03 14:29:28'),
(394, 'http://localhost:8080/uploads/products/f0590576-2711-410f-bb43-1e9dd8ebbc17_y-18k-pnj-5.jpg', 0, 72, '2025-08-03 14:29:28', '2025-08-03 14:29:28'),
(395, 'http://localhost:8080/uploads/products/4cc7b114-f103-461c-952d-0c540c92cbe7_dinh-da-ruby-pnj.png', 1, 73, '2025-08-03 14:30:06', '2025-08-03 14:30:06'),
(396, 'http://localhost:8080/uploads/products/18f0d251-8e97-4278-99fa-cdffdc9072b1_dinh-da-ruby-pnj-1.png', 0, 73, '2025-08-03 14:30:06', '2025-08-03 14:30:06'),
(397, 'http://localhost:8080/uploads/products/e244fe0f-d967-47aa-9992-97305a16e309_dinh-da-ruby-pnj-02.jpg', 0, 73, '2025-08-03 14:30:06', '2025-08-03 14:30:06'),
(398, 'http://localhost:8080/uploads/products/6b368e2d-f3e3-4b0e-867a-c6d78641e98a_dinh-da-ruby-pnj-3.jpg', 0, 73, '2025-08-03 14:30:06', '2025-08-03 14:30:06'),
(399, 'http://localhost:8080/uploads/products/33638afd-7686-4828-be8a-6dac748b4bd8_dinh-da-ruby-pnj-4.jpg', 0, 73, '2025-08-03 14:30:06', '2025-08-03 14:30:06'),
(400, 'http://localhost:8080/uploads/products/cf289c96-a4be-469a-8d32-9a5485dd9999_vang-18k-pnj-1.png', 1, 74, '2025-08-03 14:31:11', '2025-08-03 14:31:11'),
(401, 'http://localhost:8080/uploads/products/84523dc3-ca44-446e-abbd-afe93ff2d141_vang-18k-pnj-2.png', 0, 74, '2025-08-03 14:31:11', '2025-08-03 14:31:11'),
(402, 'http://localhost:8080/uploads/products/1e209397-c544-48e3-951f-a739d0a0f616_vang-18k-pnj-3.jpg', 0, 74, '2025-08-03 14:31:11', '2025-08-03 14:31:11'),
(403, 'http://localhost:8080/uploads/products/c6064a6a-7adc-4dcc-872b-aa652ae4c3f7_vang-18k-pnj-4.jpg', 0, 74, '2025-08-03 14:31:11', '2025-08-03 14:31:11'),
(404, 'http://localhost:8080/uploads/products/b7701ae4-4e02-4fc3-b103-1befc8de3bb9_vang-18k-pnj-5.jpg', 0, 74, '2025-08-03 14:31:11', '2025-08-03 14:31:11'),
(405, 'http://localhost:8080/uploads/products/e020f7b5-90bc-48a2-9512-413866878b64_dinh-da-cz-pnj-1.png', 1, 75, '2025-08-03 14:33:49', '2025-08-03 14:33:49'),
(406, 'http://localhost:8080/uploads/products/e431c716-4a6b-4283-8ba6-fcca5f4ef398_dinh-da-cz-pnj-2.png', 0, 75, '2025-08-03 14:33:49', '2025-08-03 14:33:49'),
(407, 'http://localhost:8080/uploads/products/2cbe477e-655c-4d1b-af4b-4311a10d64bf_dinh-da-cz-pnj-3.jpg', 0, 75, '2025-08-03 14:33:49', '2025-08-03 14:33:49'),
(408, 'http://localhost:8080/uploads/products/88170a6a-2b90-4f69-945a-08eda36cd7b3_dinh-da-cz-pnj-4.jpg', 0, 75, '2025-08-03 14:33:49', '2025-08-03 14:33:49'),
(409, 'http://localhost:8080/uploads/products/798d53fd-e8f8-49b7-8b80-2aaaa3b270b7_dinh-da-cz-pnj-5.jpg', 0, 75, '2025-08-03 14:33:49', '2025-08-03 14:33:49'),
(410, 'http://localhost:8080/uploads/products/64a5b4d0-f20d-4d9f-a38e-40684e662c33_peter-pan-1.png', 1, 76, '2025-08-03 14:37:06', '2025-08-03 14:37:06'),
(411, 'http://localhost:8080/uploads/products/6fc8b33b-4c37-4b2f-97da-8ed49dcb8a98_peter-pan-2.png', 0, 76, '2025-08-03 14:37:06', '2025-08-03 14:37:06'),
(412, 'http://localhost:8080/uploads/products/45676978-e17a-4f25-a9fa-da892cc9033e_peter-pan-4.jpg', 0, 76, '2025-08-03 14:37:06', '2025-08-03 14:37:06'),
(413, 'http://localhost:8080/uploads/products/20905ac7-e146-41b9-8fdd-f4e2f5312783_peter-pan-5.jpg', 0, 76, '2025-08-03 14:37:06', '2025-08-03 14:37:06'),
(414, 'http://localhost:8080/uploads/products/4cfd22e7-25dd-4d52-adad-25f92a4176f1_peter-pan-3.jpg', 0, 76, '2025-08-03 14:37:06', '2025-08-03 14:37:06'),
(415, 'http://localhost:8080/uploads/products/1c34754a-411c-4654-b8ca-381b82ba875e_24k-pnj-sac-son-1.png', 1, 77, '2025-08-03 14:40:03', '2025-08-03 14:40:03'),
(416, 'http://localhost:8080/uploads/products/1b5c1c87-cb85-44bf-87fb-afdb18f4258a_24k-pnj-sac-son-2.png', 0, 77, '2025-08-03 14:40:03', '2025-08-03 14:40:03'),
(417, 'http://localhost:8080/uploads/products/d20d5600-c6e4-414d-b0cc-2e5e71e2a444_24k-pnj-sac-son-3.jpg', 0, 77, '2025-08-03 14:40:03', '2025-08-03 14:40:03'),
(418, 'http://localhost:8080/uploads/products/07c1aed8-6fe7-441b-b29d-401a98b5b367_24k-pnj-sac-son-4.jpg', 0, 77, '2025-08-03 14:40:03', '2025-08-03 14:40:03'),
(419, 'http://localhost:8080/uploads/products/44c45850-c04d-4403-aeb4-401d5e6073dd_24k-pnj-sac-son-5.jpg', 0, 77, '2025-08-03 14:40:03', '2025-08-03 14:40:03'),
(420, 'http://localhost:8080/uploads/products/97e8eeaf-b551-4861-9fcc-59a8dede040f_24k-pnj-1.png', 1, 78, '2025-08-03 14:40:32', '2025-08-03 14:40:32'),
(421, 'http://localhost:8080/uploads/products/b8195fe3-f0c0-48ea-a035-34955b4c9daf_24k-pnj-2.png', 0, 78, '2025-08-03 14:40:32', '2025-08-03 14:40:32'),
(422, 'http://localhost:8080/uploads/products/4e923f9c-b6d5-4859-b54d-1775d7df489a_24k-pnj-3.jpg', 0, 78, '2025-08-03 14:40:32', '2025-08-03 14:40:32'),
(423, 'http://localhost:8080/uploads/products/be3f6a50-c9cd-4359-acd3-2f11ca38e293_24k-pnj-4.jpg', 0, 78, '2025-08-03 14:40:32', '2025-08-03 14:40:32'),
(424, 'http://localhost:8080/uploads/products/1e4826cf-6268-44fd-9eb9-27f73baacab4_24k-pnj-5.jpg', 0, 78, '2025-08-03 14:40:32', '2025-08-03 14:40:32'),
(425, 'http://localhost:8080/uploads/products/1840b81d-0d76-42b6-9b1c-6c13af6bd50a_hoa-duyen-1.png', 1, 79, '2025-08-03 14:41:45', '2025-08-03 14:41:45'),
(426, 'http://localhost:8080/uploads/products/108af181-2a80-423b-835d-e4e5b3a020fe_hoa-duyen-2.png', 0, 79, '2025-08-03 14:41:45', '2025-08-03 14:41:45'),
(427, 'http://localhost:8080/uploads/products/789ba4e0-17d0-42b6-a194-648a70095ca8_hoa-duyen-3.jpg', 0, 79, '2025-08-03 14:41:45', '2025-08-03 14:41:45'),
(428, 'http://localhost:8080/uploads/products/eeaac2ee-481f-433c-a3e4-96ad2587be7d_hoa-duyen-4.jpg', 0, 79, '2025-08-03 14:41:45', '2025-08-03 14:41:45'),
(429, 'http://localhost:8080/uploads/products/3127b5ee-7ffb-4f8f-a022-9f52a9f8c93c_hoa-duyen-5.jpg', 0, 79, '2025-08-03 14:41:45', '2025-08-03 14:41:45'),
(430, 'http://localhost:8080/uploads/products/3108f601-f4de-4910-b447-f45fd2f1fd8d_24k-pnj-tinh-nong-1.png', 1, 80, '2025-08-03 14:46:16', '2025-08-03 14:46:16'),
(431, 'http://localhost:8080/uploads/products/e2f0ecd1-0b82-4ace-83dd-447f4a34221f_24k-pnj-tinh-nong-2.png', 0, 80, '2025-08-03 14:46:16', '2025-08-03 14:46:16'),
(432, 'http://localhost:8080/uploads/products/e499f637-2606-43a2-a084-2389b950f3bf_24k-pnj-tinh-nong-3.jpg', 0, 80, '2025-08-03 14:46:16', '2025-08-03 14:46:16'),
(433, 'http://localhost:8080/uploads/products/0c0b5723-51b3-43ed-b3cc-a3a99f28d9de_24k-pnj-tinh-nong-4.jpg', 0, 80, '2025-08-03 14:46:16', '2025-08-03 14:46:16'),
(434, 'http://localhost:8080/uploads/products/80b402f6-23ff-4823-b687-f08c2ba9e35d_24k-pnj-tinh-nong-5.jpg', 0, 80, '2025-08-03 14:46:16', '2025-08-03 14:46:16'),
(435, 'http://localhost:8080/uploads/products/6d543550-4838-41f0-a1eb-a4b16d718d79_bong-tai-cuoi-vang-24k-pnj-1.png', 1, 81, '2025-08-03 14:48:31', '2025-08-03 14:48:31'),
(436, 'http://localhost:8080/uploads/products/9e472782-faa9-4a48-acb2-aee922ee81ad_bong-tai-cuoi-vang-24k-pnj-2.png', 0, 81, '2025-08-03 14:48:31', '2025-08-03 14:48:31'),
(437, 'http://localhost:8080/uploads/products/b11f64b2-cda7-4aef-a8cc-46c55484ee52_bong-tai-cuoi-vang-24k-pnj-3.jpg', 0, 81, '2025-08-03 14:48:31', '2025-08-03 14:48:31'),
(438, 'http://localhost:8080/uploads/products/5e31cb31-be41-41e8-9017-e324c7205df0_bong-tai-cuoi-vang-24k-pnj-4.jpg', 0, 81, '2025-08-03 14:48:31', '2025-08-03 14:48:31'),
(439, 'http://localhost:8080/uploads/products/0804bc3b-cb52-4ad4-837b-fd1fb5eeb864_bong-tai-cuoi-vang-24k-pnj-5.jpg', 0, 81, '2025-08-03 14:48:31', '2025-08-03 14:48:31'),
(440, 'http://localhost:8080/uploads/products/3b76793a-5e08-4087-aa8c-c3a358608f80_ngoc-canh-vang-10.png', 1, 82, '2025-08-03 14:49:30', '2025-08-03 14:49:30'),
(441, 'http://localhost:8080/uploads/products/f92b9048-a730-49a2-9478-6748434896c4_ngoc-canh-vang-11.png', 0, 82, '2025-08-03 14:49:30', '2025-08-03 14:49:30'),
(442, 'http://localhost:8080/uploads/products/c86e72cd-a18d-4137-8b54-c9d37b4b40fb_vang-dinh-da-quartz-pnj-12.jpg', 0, 82, '2025-08-03 14:49:30', '2025-08-03 14:49:30'),
(443, 'http://localhost:8080/uploads/products/71c28428-491e-4bbf-af40-0133e82cc556_vang-dinh-da-quartz-pnj-13.jpg', 0, 82, '2025-08-03 14:49:30', '2025-08-03 14:49:30'),
(444, 'http://localhost:8080/uploads/products/b6cfb1c8-fe36-4f3b-9720-e2045a36e8ec_vang-dinh-da-quartz-pnj-14.jpg', 0, 82, '2025-08-03 14:49:30', '2025-08-03 14:49:30'),
(445, 'http://localhost:8080/uploads/products/ebf8b4d1-7068-4838-8537-8eaf422701ed_pnj-trau-cau-1.png', 1, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(446, 'http://localhost:8080/uploads/products/ed7d93b8-b95b-4c12-b294-104f127d2eb2_pnj-trau-cau-2.png', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(447, 'http://localhost:8080/uploads/products/c55957c5-6109-4a16-8d7e-1d47bd8a3040_trau-cau-3.jpg', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(448, 'http://localhost:8080/uploads/products/5f2a8e64-db14-412d-8765-4d516569f667_trau-cau-4.jpg', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(449, 'http://localhost:8080/uploads/products/d7bf2b86-3917-4b1c-85fa-5f89b63cde52_trau-cau-5.jpg', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(450, 'http://localhost:8080/uploads/products/6284438f-a8f9-4217-846e-52680217d1f7_trau-cau-6.jpg', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(451, 'http://localhost:8080/uploads/products/07d68165-de28-49d9-a8a3-cb4588ddfe2d_trau-cau-7.jpg', 0, 83, '2025-08-03 14:50:39', '2025-08-03 14:50:39'),
(452, 'http://localhost:8080/uploads/products/8807124f-08ed-4550-a88d-74e9758a9ec8_vang-24k-pnj-lan-mua-ha-1.png', 1, 84, '2025-08-03 14:54:30', '2025-08-03 14:54:30'),
(453, 'http://localhost:8080/uploads/products/1826ac64-ab9a-4ced-9173-11d042f4a607_vang-24k-pnj-lan-mua-ha-2.png', 0, 84, '2025-08-03 14:54:30', '2025-08-03 14:54:30'),
(454, 'http://localhost:8080/uploads/products/6c2348e6-eeb9-4e92-8133-d00c4aee289b_vang-24k-pnj-lan-mua-ha-3.jpg', 0, 84, '2025-08-03 14:54:30', '2025-08-03 14:54:30'),
(455, 'http://localhost:8080/uploads/products/ca3becba-2512-4dc8-bae3-ec75d4d27f36_vang-24k-pnj-lan-mua-ha-4.jpg', 0, 84, '2025-08-03 14:54:30', '2025-08-03 14:54:30'),
(456, 'http://localhost:8080/uploads/products/6a2437a9-815e-46f1-98bb-83b0fdc4ebd1_vang-24k-pnj-lan-mua-ha-5.jpg', 0, 84, '2025-08-03 14:54:31', '2025-08-03 14:54:31'),
(474, 'http://localhost:8080/uploads/products/d218269f-6e3d-4503-a6a7-5eaf429a0f05_sap-gh0000y060105-kieng-cuoi-vang-24k-pnj-1.png', 1, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(475, 'http://localhost:8080/uploads/products/aebb664d-7d4a-4198-bb80-310abace9cfe_sap-gh0000y060105-kieng-cuoi-vang-24k-pnj-2.png', 0, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(476, 'http://localhost:8080/uploads/products/d2cd25b6-1a7b-40b6-9f49-082ad5e4e607_sap-gh0000y060105-kieng-cuoi-vang-24k-pnj-3.png', 0, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(477, 'http://localhost:8080/uploads/products/65942ca4-fb96-4e5d-bb0a-15f0c654b8cd_son-gh0000y060105-kieng-cuoi-vang-24k-pnj-4.jpg', 0, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(478, 'http://localhost:8080/uploads/products/0890b18d-f983-40c2-8c4c-df0a5d5a1cbe_son-gh0000y060105-kieng-cuoi-vang-24k-pnj-5.jpg', 0, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(479, 'http://localhost:8080/uploads/products/5401d700-a8dc-45c9-9afb-87cd9d84bc65_son-gh0000y060105-kieng-cuoi-vang-24k-pnj-6.jpg', 0, 93, '2025-09-08 10:27:29', '2025-09-08 10:27:29'),
(480, 'http://localhost:8080/uploads/products/64a1a9f3-0966-4dea-88ea-28971d639f4f_vang-24k-pnj-0000y0600071.png', 1, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(481, 'http://localhost:8080/uploads/products/f4cfa96c-1705-4eb1-9e8a-ea30e5c675b1_vang-24k-pnj-0000y0600072.png', 0, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(482, 'http://localhost:8080/uploads/products/c6e3798c-94f2-4bd7-a034-47e47dd9ed4d_vang-24k-pnj-0000y0600073.png', 0, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(483, 'http://localhost:8080/uploads/products/89461c69-b077-49c7-8148-f09612b95824_vang-24k-pnj-0000y0600075.jpg', 0, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(484, 'http://localhost:8080/uploads/products/25f4a6b0-6b76-4f96-86bb-2dbb2a9fe894_vang-24k-pnj-0000y0600074.jpg', 0, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(485, 'http://localhost:8080/uploads/products/3e435f61-9a22-4925-ba2a-ffdfd0e766c5_vang-24k-pnj-0000y0600076.jpg', 0, 94, '2025-09-08 10:31:13', '2025-09-08 10:31:13'),
(486, 'http://localhost:8080/uploads/products/7cde3439-3932-401b-b183-96e8f4911052_cuoi-vang-18k-pnj-trau-cau-1.png', 1, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(487, 'http://localhost:8080/uploads/products/e49e9169-99de-4ae4-9afd-09f236b9d38e_cuoi-vang-18k-pnj-trau-cau-2.png', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(488, 'http://localhost:8080/uploads/products/1a978c58-371b-48c6-9af3-899ef9e49610_cuoi-vang-18k-pnj-trau-cau-3.png', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(489, 'http://localhost:8080/uploads/products/aab330c9-dc50-4683-97d4-d660c76aa668_cuoi-vang-18k-pnj-trau-cau-5.jpg', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(490, 'http://localhost:8080/uploads/products/21d6c6ab-de7d-4475-838c-d492c34c2474_cuoi-vang-18k-pnj-trau-cau-6.jpg', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(491, 'http://localhost:8080/uploads/products/e14cfa98-21e7-4362-9cd7-e24f32db5aec_cuoi-vang-18k-pnj-trau-cau-00229-02683-4.jpg', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(492, 'http://localhost:8080/uploads/products/ae53608e-6242-4f31-8647-b1a6bc6215a3_cuoi-vang-18k-pnj-trau-cau-37.jpg', 0, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(493, 'http://localhost:8080/uploads/products/3bf2304a-76a6-43ff-8b03-a40c40756bfe_14k-pnj-la-ngoc-canh-vang-1.png', 1, 96, '2025-09-08 10:40:04', '2025-09-08 10:40:04'),
(494, 'http://localhost:8080/uploads/products/e0bc231f-188e-4d9a-bb09-135fc8c2d176_14k-pnj-la-ngoc-canh-vang-2.png', 0, 96, '2025-09-08 10:40:04', '2025-09-08 10:40:04'),
(495, 'http://localhost:8080/uploads/products/0159d7af-477b-4fbf-afb6-0ca863e34ea6_14k-pnj-la-ngoc-canh-vang-3.jpg', 0, 96, '2025-09-08 10:40:04', '2025-09-08 10:40:04'),
(496, 'http://localhost:8080/uploads/products/71e3454f-09e1-4cd8-8639-101422050d06_14k-pnj-la-ngoc-canh-vang-4.jpg', 0, 96, '2025-09-08 10:40:04', '2025-09-08 10:40:04'),
(497, 'http://localhost:8080/uploads/products/c9a696fc-1a38-4759-8c27-755f068dbeab_14k-pnj-la-ngoc-canh-vang-5.jpg', 0, 96, '2025-09-08 10:40:04', '2025-09-08 10:40:04'),
(498, 'http://localhost:8080/uploads/products/d9ce8bd4-3bca-4c41-90ee-1175c07ae6ff_ngoc-trai-freshwater-pnj-1.png', 1, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(499, 'http://localhost:8080/uploads/products/7463edaf-dc9d-4ef9-9f0f-fc905c630010_ngoc-trai-freshwater-pnj-2.png', 0, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(500, 'http://localhost:8080/uploads/products/4b8a8eb1-5dbf-4155-9334-62a45b3554ad_ngoc-trai-freshwater-pnj-3.jpg', 0, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(501, 'http://localhost:8080/uploads/products/2979ce16-897a-4980-abd5-52cc0f17e4e1_ngoc-trai-freshwater-pnj-4.jpg', 0, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(502, 'http://localhost:8080/uploads/products/6b8b923d-7b24-476f-851b-1bc73ad77f2a_ngoc-trai-freshwater-pnj-45jpg.jpg', 0, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(503, 'http://localhost:8080/uploads/products/c46359fa-31ca-44b7-ba9a-ba9772cc7055_10kapnj-hello-kitty-1.png', 1, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(504, 'http://localhost:8080/uploads/products/b7039aec-8652-427e-bde6-7c14edd3ec30_10kapnj-hello-kitty-2.png', 0, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(505, 'http://localhost:8080/uploads/products/35ef1172-e4cd-4dd0-80b1-f1fe10b5eb43_10kapnj-hello-kitty-3.png', 0, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(506, 'http://localhost:8080/uploads/products/99cf485f-0c90-47cd-acc4-4664cf67b903_10k-pnj-hello-kitty-4.jpg', 0, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(507, 'http://localhost:8080/uploads/products/96a34853-4121-4485-8532-153617c44a2e_10k-pnj-hello-kitty-6.jpg', 0, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(508, 'http://localhost:8080/uploads/products/95b4ad39-a133-4b82-a3ff-99cbc927246f_10k-pnj-hello-kitty-5.jpg', 0, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(509, 'http://localhost:8080/uploads/products/cc0128a2-2e59-4857-8652-c9de79d9d01a_14k-dinh-da-synthetic-pnj-1.png', 1, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(510, 'http://localhost:8080/uploads/products/3bf4727b-08e9-4ef9-9c4b-32b403cb33fe_14k-dinh-da-synthetic-pnj-2.png', 0, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(511, 'http://localhost:8080/uploads/products/b0ea5120-2b63-484c-8065-238f5bf69aa3_14k-dinh-da-synthetic-pnj-3.png', 0, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(512, 'http://localhost:8080/uploads/products/c670d70b-0eed-409e-be87-1957d38dd94c_14k-dinh-da-synthetic-pnj-5.jpg', 0, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(513, 'http://localhost:8080/uploads/products/3ec7d040-f335-4b45-b15b-c0ce0a803381_14k-dinh-da-synthetic-pnj-6.jpg', 0, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(514, 'http://localhost:8080/uploads/products/bfc0c389-ff6c-4e8b-bdf6-f4557ba8a57b_14k-dinh-da-synthetic-pnj-4.jpg', 0, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(515, 'http://localhost:8080/uploads/products/0d1aff04-8b27-4d31-8edc-c26d430fdd08_pnjsilver-1.png', 1, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(516, 'http://localhost:8080/uploads/products/76be433d-42d3-48a4-9eee-d9e45e51624e_pnjsilver-2.png', 0, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(517, 'http://localhost:8080/uploads/products/50048c62-afe1-4c3c-8e2a-ccf6183ccf86_pnjsilver-4.jpg', 0, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(518, 'http://localhost:8080/uploads/products/c0605d11-1364-44e1-818f-b94be0c24cff_pnjsilver-5.jpg', 0, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(519, 'http://localhost:8080/uploads/products/6a765fdc-d247-4361-9def-04c8793b2b5f_pnjsilver-6.jpg', 0, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(520, 'http://localhost:8080/uploads/products/5b11855e-d25f-47e7-af97-e7c83d575311_pnjsilver-1.png', 1, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(521, 'http://localhost:8080/uploads/products/c5b7cf66-f23e-4761-b7fb-532566bf8658_pnjsilver-2.png', 0, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(522, 'http://localhost:8080/uploads/products/85e9a266-a672-4442-8649-fa78d38e36d7_pnjsilver-3.png', 0, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(523, 'http://localhost:8080/uploads/products/875d23a1-9627-4905-a781-980cc71980a2_pnjsilver-4.jpg', 0, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(524, 'http://localhost:8080/uploads/products/2390b858-6525-452e-85fb-4dfa351ed384_pnjsilver-5.jpg', 0, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(525, 'http://localhost:8080/uploads/products/cad023a8-4746-46a1-8fa1-b27840b5f6fc_pnjsilver-6.jpg', 0, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(526, 'http://localhost:8080/uploads/products/8247a455-8d6b-4919-bfce-f9e3b373997b_pnjsilver-1.png', 1, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(527, 'http://localhost:8080/uploads/products/e62bf699-d79b-4e80-8b59-15a3cdf47396_pnjsilver-2.png', 0, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(528, 'http://localhost:8080/uploads/products/9a6f2b91-bd0d-4e71-b97b-1b39fcfc033e_pnjsilver-3.png', 0, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(529, 'http://localhost:8080/uploads/products/f0acaada-ffd2-4882-82c6-7eadaa2b88e8_skitty-4.jpg', 0, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(530, 'http://localhost:8080/uploads/products/6c57ea48-cc99-48cb-9c4b-da7b9512be37_skitty-5.jpg', 0, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(531, 'http://localhost:8080/uploads/products/10780230-f812-443d-9f97-e24fa8600f11_skitty-6.jpg', 0, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(532, 'http://localhost:8080/uploads/products/ff3d2df7-ebf9-4460-ae82-96f98d9233c1_pnj-hello-kitty-1.png', 1, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(533, 'http://localhost:8080/uploads/products/73f8b3e1-b5b7-46c4-8fd4-cebbdd09bcb1_pnj-hello-kitty-2.png', 0, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(534, 'http://localhost:8080/uploads/products/e7b58d84-7d75-41db-b1f3-4c9f5768e9c6_pnj-hello-kitty-3.png', 0, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(535, 'http://localhost:8080/uploads/products/a9951a1b-7743-418d-b0d1-50e624d82df6_pnj-hello-kitty-4.jpg', 0, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(536, 'http://localhost:8080/uploads/products/a6556982-fc89-4651-a65a-f873b0d53771_pnj-hello-kitty-5.jpg', 0, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(537, 'http://localhost:8080/uploads/products/bd03e40e-fa63-4d05-8fcf-97faa803d3c1_pnj-hello-kitty-6.jpg', 0, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(538, 'http://localhost:8080/uploads/products/313c596e-8ca1-48e8-a5e4-6e7cbd329876_pnj-hello-kitty-1.png', 1, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(539, 'http://localhost:8080/uploads/products/f370d0b7-f8b2-4590-8ba4-4272ef865f7b_pnj-hello-kitty-2.png', 0, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(540, 'http://localhost:8080/uploads/products/19fceb6e-006c-425a-ad8f-453ceaad1b5f_pnj-hello-kitty-3.png', 0, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(541, 'http://localhost:8080/uploads/products/d7d5c448-4881-451c-a66f-a25506e9e710_pnj-hello-kitty-4.jpg', 0, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(542, 'http://localhost:8080/uploads/products/3ad9165b-c5cc-43dc-9802-adb70bd6bfa9_pnj-hello-kitty-5.jpg', 0, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(543, 'http://localhost:8080/uploads/products/d38bbc55-d1d4-40f4-9df9-27b4c4ef2f79_pnj-hello-kitty-6.jpg', 0, 104, '2025-09-08 11:23:28', '2025-09-08 11:23:28'),
(544, 'http://localhost:8080/uploads/products/c314d3d9-4fbc-46fd-be70-3a8b502d7dbe_pnjsilver-1.png', 1, 105, '2025-09-08 15:26:24', '2025-09-08 15:26:24'),
(545, 'http://localhost:8080/uploads/products/82148664-9aac-404d-a12a-e1cb5d73c8a1_pnjsilver-2.png', 0, 105, '2025-09-08 15:26:24', '2025-09-08 15:26:24'),
(546, 'http://localhost:8080/uploads/products/b9ee98b0-e123-438f-bc14-a64dd35a6dfd_pnjsilver-3.png', 0, 105, '2025-09-08 15:26:24', '2025-09-08 15:26:24'),
(547, 'http://localhost:8080/uploads/products/520fa84e-3879-48d4-bcc5-38b0c5a2e34c_pnjsilver-4.jpg', 0, 105, '2025-09-08 15:26:24', '2025-09-08 15:26:24'),
(548, 'http://localhost:8080/uploads/products/615b8a3f-52b8-4468-b581-2baba58fc5db_pnjsilver-5.jpg', 0, 105, '2025-09-08 15:26:24', '2025-09-08 15:26:24'),
(549, 'http://localhost:8080/uploads/products/5523e866-6f53-4d74-a2af-49643acda4d0_sythetic-pnj-hello-kitty-1.png', 1, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(550, 'http://localhost:8080/uploads/products/3e8abb7f-2fc7-45d6-8ae4-59afd9afa1b6_sythetic-pnj-hello-kitty-2.png', 0, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(551, 'http://localhost:8080/uploads/products/6389a6cb-1784-4805-81b1-cb9175a3d0cd_sythetic-pnj-hello-kitty-3.png', 0, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(552, 'http://localhost:8080/uploads/products/1e622236-a552-4085-b624-cbf1cc9ada09_sythetic-pnj-hello-kitty-4.jpg', 0, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(553, 'http://localhost:8080/uploads/products/880adf98-5a87-42ca-85fb-f91b63860c9b_sythetic-pnj-hello-kitty-5.jpg', 0, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(554, 'http://localhost:8080/uploads/products/20e91505-cc55-4ad0-bbbe-1a3e6d43964d_sythetic-pnj-hello-kitty-6.jpg', 0, 106, '2025-09-08 15:29:09', '2025-09-08 15:29:09'),
(555, 'http://localhost:8080/uploads/products/f607eda6-7664-4f50-85a6-361b29a6922b_ecz-pnj-hello-kitty-1.png', 1, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(556, 'http://localhost:8080/uploads/products/9c5573a9-6f33-4fa7-bf40-f124aa0488cb_ecz-pnj-hello-kitty-2.png', 0, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(557, 'http://localhost:8080/uploads/products/d3d07b81-cadb-4254-8d95-7248d96d259b_ecz-pnj-hello-kitty-3.png', 0, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(558, 'http://localhost:8080/uploads/products/67a2565a-aadf-4396-b46d-4d2742571360_ecz-pnj-hello-kitty-4.jpg', 0, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(559, 'http://localhost:8080/uploads/products/10b04830-3c13-49ab-958f-742a623586ed_ecz-pnj-hello-kitty-5.jpg', 0, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(560, 'http://localhost:8080/uploads/products/fe44c0e0-ca5e-44f6-a87d-dd63792774fa_ecz-pnj-hello-kitty-6.jpg', 0, 107, '2025-09-08 15:32:21', '2025-09-08 15:32:21'),
(561, 'http://localhost:8080/uploads/products/b38cb889-a24b-49e8-b79b-71fe65ff7d9b_pnjsilver-1.png', 1, 108, '2025-09-08 15:49:26', '2025-09-08 15:49:26'),
(562, 'http://localhost:8080/uploads/products/98cd9c21-1c70-4e7f-b7c7-1ed88d34cd5d_pnjsilver-2.png', 0, 108, '2025-09-08 15:49:26', '2025-09-08 15:49:26'),
(563, 'http://localhost:8080/uploads/products/447347b5-21fd-4c73-93c0-2a3d3dece78f_pnjsilver-3.png', 0, 108, '2025-09-08 15:49:26', '2025-09-08 15:49:26'),
(564, 'http://localhost:8080/uploads/products/a8d6b9c2-2deb-435b-ad61-8d69a46996ca_pnjsilver-4.jpg', 0, 108, '2025-09-08 15:49:26', '2025-09-08 15:49:26'),
(565, 'http://localhost:8080/uploads/products/ae56f226-c744-4ccb-962a-bc09b3cc6174_pnjsilver-5.jpg', 0, 108, '2025-09-08 15:49:26', '2025-09-08 15:49:26'),
(566, 'http://localhost:8080/uploads/products/4c0a96f2-b846-4ac1-aa10-efe3a2ac2071_cinderella-1.png', 1, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(567, 'http://localhost:8080/uploads/products/389c776c-e8f1-4d23-8d9f-cba483ef5d74_cinderella-2.png', 0, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(568, 'http://localhost:8080/uploads/products/a83d09e3-0657-4037-ad4c-bf7ca4a6e3fe_cinderella-3.png', 0, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(569, 'http://localhost:8080/uploads/products/04902b9d-42ce-4ca7-b249-32a63e072c66_cinderella-4.jpg', 0, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(570, 'http://localhost:8080/uploads/products/40fcaeac-7b66-4366-b20b-e5e88a304743_cinderella-5.jpg', 0, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(571, 'http://localhost:8080/uploads/products/c0bc25cd-11bb-403e-a80a-61789995b202_cinderella-6.jpg', 0, 109, '2025-09-08 15:52:58', '2025-09-08 15:52:58'),
(572, 'http://localhost:8080/uploads/products/fc89d089-549a-48d6-be95-4f6a3dcdb0a3_cinderella-1.png', 1, 110, '2025-09-08 15:54:44', '2025-09-08 15:54:44'),
(573, 'http://localhost:8080/uploads/products/8458bfd7-83b3-47c8-826b-888cb23a8315_cinderella-2.png', 0, 110, '2025-09-08 15:54:44', '2025-09-08 15:54:44'),
(574, 'http://localhost:8080/uploads/products/94e22ece-7fc4-4c39-8697-e84c9d17d599_cinderella-3.jpg', 0, 110, '2025-09-08 15:54:44', '2025-09-08 15:54:44'),
(575, 'http://localhost:8080/uploads/products/f576fef6-60a7-450b-9fd3-8a7c9a7d84f1_cinderella-4.jpg', 0, 110, '2025-09-08 15:54:44', '2025-09-08 15:54:44'),
(576, 'http://localhost:8080/uploads/products/54a7e10e-50e0-487d-a658-b995873a5ba9_cinderella-5.jpg', 0, 110, '2025-09-08 15:54:44', '2025-09-08 15:54:44'),
(577, 'http://localhost:8080/uploads/products/93cec170-1153-40f0-8431-feb2099bc18e_pnj-cinderella-1.png', 1, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(578, 'http://localhost:8080/uploads/products/180954b7-0861-49a2-90b9-1b447c07fa77_pnj-cinderella-2.png', 0, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(579, 'http://localhost:8080/uploads/products/cb947637-4837-4938-a897-bec09ebcdebf_pnj-cinderella-3.png', 0, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(580, 'http://localhost:8080/uploads/products/9d63561d-812e-4255-956e-06de370c74f1_pnj-cinderella-4.jpg', 0, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(581, 'http://localhost:8080/uploads/products/42714dda-1460-40cf-97b3-4655678e285b_pnj-cinderella-5.jpg', 0, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(582, 'http://localhost:8080/uploads/products/7524d3f1-48fb-4e5f-b998-0f36a884eb63_pnj-cinderella-6.jpg', 0, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(583, 'http://localhost:8080/uploads/products/ca918ed8-efcc-4635-a312-285105add824_disney-pnj-1.png', 1, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26'),
(584, 'http://localhost:8080/uploads/products/e8dc5887-fad3-4674-a39e-8722593c9b33_disney-pnj-2.png', 0, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26'),
(585, 'http://localhost:8080/uploads/products/878379ca-6c0b-4310-ad8f-bf1b9a6f59e9_disney-pnj-3.png', 0, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26'),
(586, 'http://localhost:8080/uploads/products/7f39adbe-4b94-414c-a745-41cec8816d00_pnjsilver-6.jpg', 0, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26'),
(587, 'http://localhost:8080/uploads/products/ccec81d4-6da1-433d-9c63-9521f5b469c6_pnjsilver-4.jpg', 0, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26'),
(588, 'http://localhost:8080/uploads/products/1b76622d-8c50-4db0-ac50-5c52dfb169a8_pnjsilver-5.jpg', 0, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26');

-- --------------------------------------------------------

--
-- Table structure for table `product_size`
--

CREATE TABLE `product_size` (
  `id` int(11) NOT NULL,
  `size` varchar(50) NOT NULL,
  `quantity` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product_size`
--

INSERT INTO `product_size` (`id`, `size`, `quantity`, `product_id`, `created_at`, `updated_at`) VALUES
(1, '11', 21, 1, '2025-07-29 15:47:38', '2025-08-06 10:41:06'),
(2, '12', 26, 1, '2025-07-29 15:47:38', '2025-08-06 10:41:07'),
(3, '13', 29, 1, '2025-07-29 15:47:38', '2025-07-29 15:47:38'),
(4, '10', 25, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(5, '11', 30, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(6, '12', 29, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(7, '13', 34, 3, '2025-07-29 16:25:50', '2025-07-29 16:25:50'),
(8, '11', 20, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(9, '12', 29, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(10, '13', 29, 4, '2025-07-29 16:38:16', '2025-07-29 16:38:16'),
(11, '11', 20, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(12, '12', 29, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(13, '13', 29, 5, '2025-07-29 16:46:31', '2025-07-29 16:46:31'),
(14, '11', 40, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(15, '12', 12, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(16, '13', 24, 6, '2025-07-29 16:51:24', '2025-07-29 16:51:24'),
(17, '13', 40, 7, '2025-08-01 15:29:17', '2025-08-01 15:29:17'),
(18, '13', 49, 8, '2025-08-01 15:30:28', '2025-09-01 23:49:45'),
(19, '11', 35, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(20, '12', 43, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(21, '13', 30, 9, '2025-08-01 15:32:17', '2025-08-01 15:32:17'),
(22, '10', 35, 12, '2025-08-01 15:36:30', '2025-08-01 15:36:30'),
(23, '11', 42, 12, '2025-08-01 15:36:30', '2025-08-31 15:53:28'),
(24, '11', 31, 13, '2025-08-01 15:37:54', '2025-08-31 15:53:28'),
(25, '12', 34, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(26, '13', 35, 13, '2025-08-01 15:37:54', '2025-08-01 15:37:54'),
(27, '11', 30, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(28, '12', 27, 14, '2025-08-01 15:44:02', '2025-08-01 15:44:02'),
(29, '9', 25, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(30, '10', 28, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(31, '11', 29, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(32, '12', 34, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(33, '13', 32, 15, '2025-08-01 15:51:28', '2025-08-01 15:51:28'),
(34, '9', 20, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(35, '10', 16, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(36, '11', 28, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(37, '12', 27, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(38, '13', 32, 16, '2025-08-01 15:54:14', '2025-08-01 15:54:14'),
(39, '11', 20, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(40, '12', 16, 17, '2025-08-01 15:55:36', '2025-08-01 15:55:36'),
(41, '11', 20, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(42, '12', 16, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(43, '13', 28, 21, '2025-08-01 16:01:09', '2025-08-01 16:01:09'),
(44, '10', 20, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(45, '11', 16, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(46, '12', 28, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(47, '13', 27, 22, '2025-08-01 16:03:31', '2025-08-01 16:03:31'),
(48, '11', 30, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(49, '12', 27, 23, '2025-08-01 16:23:29', '2025-08-01 16:23:29'),
(54, '11', 36, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(55, '12', 33, 27, '2025-08-01 16:30:06', '2025-08-01 16:30:06'),
(56, '11', 36, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(57, '12', 30, 28, '2025-08-01 16:34:30', '2025-08-01 16:34:30'),
(61, '11', 22, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(62, '12', 24, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(63, '13', 26, 30, '2025-08-01 16:46:35', '2025-08-01 16:46:35'),
(64, '17', 23, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(65, '18', 44, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(66, '19', 26, 32, '2025-08-01 16:56:39', '2025-08-01 16:56:39'),
(67, '19', 23, 33, '2025-08-01 16:57:50', '2025-08-01 16:57:50'),
(68, '20', 32, 33, '2025-08-01 16:57:50', '2025-08-01 16:57:50'),
(69, '13', 23, 34, '2025-08-01 16:59:50', '2025-08-01 16:59:50'),
(70, '17', 26, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(71, '19', 37, 35, '2025-08-01 17:01:11', '2025-08-01 17:01:11'),
(72, '11', 28, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(73, '12', 37, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(74, '13', 22, 36, '2025-08-01 17:02:25', '2025-08-01 17:02:25'),
(75, '12', 26, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(76, '13', 35, 37, '2025-08-01 17:04:39', '2025-08-01 17:04:39'),
(77, '12', 26, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(78, '13', 35, 38, '2025-08-01 17:09:48', '2025-08-01 17:09:48'),
(83, '17', 22, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(84, '18', 28, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(85, '19', 23, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(86, '20', 21, 40, '2025-08-01 17:16:42', '2025-08-01 17:16:42'),
(87, '12', 22, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(88, '13', 28, 41, '2025-08-01 17:37:33', '2025-08-01 17:37:33'),
(92, '11', 28, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(93, '12', 23, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(94, '13', 26, 43, '2025-08-01 17:41:43', '2025-08-01 17:41:43'),
(95, '17', 25, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(96, '18', 32, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(97, '19', 24, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(98, '20', 24, 44, '2025-08-02 22:52:58', '2025-08-02 22:52:58'),
(102, '42', 20, 95, '2025-09-08 10:34:01', '2025-09-08 10:34:01'),
(103, '16', 25, 97, '2025-09-08 11:00:16', '2025-09-08 11:00:16'),
(104, '16', 22, 98, '2025-09-08 11:04:29', '2025-09-08 11:04:29'),
(105, '16', 11, 99, '2025-09-08 11:06:40', '2025-09-08 11:06:40'),
(106, '17', 11, 100, '2025-09-08 11:09:17', '2025-09-08 11:09:17'),
(107, '16', 15, 101, '2025-09-08 11:11:59', '2025-09-08 11:11:59'),
(108, '14', 11, 102, '2025-09-08 11:14:18', '2025-09-08 11:14:18'),
(109, '16', 11, 103, '2025-09-08 11:17:20', '2025-09-08 11:17:20'),
(110, '40', 12, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(111, '42', 13, 111, '2025-09-08 15:57:02', '2025-09-08 15:57:02'),
(112, '14', 14, 112, '2025-09-08 15:59:26', '2025-09-08 15:59:26');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `account`
--
ALTER TABLE `account`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`),
  ADD UNIQUE KEY `email` (`email`),
  ADD UNIQUE KEY `phone` (`phone`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `name` (`name`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_orders_account` (`account_id`);

--
-- Indexes for table `order_item`
--
ALTER TABLE `order_item`
  ADD PRIMARY KEY (`id`),
  ADD KEY `fk_item_order` (`order_id`),
  ADD KEY `fk_item_product` (`product_id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `sku` (`sku`),
  ADD KEY `pk_product_category` (`category_id`);

--
-- Indexes for table `product_image`
--
ALTER TABLE `product_image`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_image_ibfk_1` (`product_id`);

--
-- Indexes for table `product_size`
--
ALTER TABLE `product_size`
  ADD PRIMARY KEY (`id`),
  ADD KEY `product_id` (`product_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `account`
--
ALTER TABLE `account`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;

--
-- AUTO_INCREMENT for table `order_item`
--
ALTER TABLE `order_item`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- AUTO_INCREMENT for table `product_image`
--
ALTER TABLE `product_image`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=589;

--
-- AUTO_INCREMENT for table `product_size`
--
ALTER TABLE `product_size`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=113;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `fk_orders_account` FOREIGN KEY (`account_id`) REFERENCES `account` (`id`);

--
-- Constraints for table `order_item`
--
ALTER TABLE `order_item`
  ADD CONSTRAINT `fk_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  ADD CONSTRAINT `fk_item_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`);

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `pk_product_category` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`);

--
-- Constraints for table `product_image`
--
ALTER TABLE `product_image`
  ADD CONSTRAINT `product_image_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `product_size`
--
ALTER TABLE `product_size`
  ADD CONSTRAINT `product_size_ibfk_1` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`) ON DELETE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
