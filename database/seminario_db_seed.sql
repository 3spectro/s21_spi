USE seminario_db;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- =================== LOOKUPS ===================
INSERT INTO category (category_id, category_name, category_description) VALUES
(1, 'Tops', 'Upper body garments'),
(2, 'Bottoms', 'Lower body garments'),
(3, 'Footwear', 'Shoes and sneakers'),
(4, 'Accessories', 'Belts, hats, etc.')
ON DUPLICATE KEY UPDATE category_name=VALUES(category_name);

INSERT INTO sub_category (sub_category_id, sub_category_name, sub_category_description, category_id) VALUES
(1, 'T-Shirts', 'Basic cotton t-shirts', 1),
(2, 'Shirts', 'Casual and formal shirts', 1),
(3, 'Jeans', 'Denim pants', 2),
(4, 'Shorts', 'Casual shorts', 2),
(5, 'Sneakers', 'Sport shoes', 3),
(6, 'Sandals', 'Summer sandals', 3),
(7, 'Belts', 'Waist belts', 4),
(8, 'Hats', 'Caps and hats', 4)
ON DUPLICATE KEY UPDATE sub_category_name=VALUES(sub_category_name);

INSERT INTO color (color_id, color_name) VALUES
(1, 'Black'),
(2, 'White'),
(3, 'Red'),
(4, 'Blue'),
(5, 'Green')
ON DUPLICATE KEY UPDATE color_name=VALUES(color_name);

INSERT INTO size (size_id, size_value) VALUES
(1, 'XS'),
(2, 'S'),
(3, 'M'),
(4, 'L'),
(5, 'XL')
ON DUPLICATE KEY UPDATE size_value=VALUES(size_value);

INSERT INTO payment_method (payment_method_id, payment_method_name) VALUES
(1, 'Cash'),
(2, 'Debit Card'),
(3, 'Credit Card'),
(4, 'Bank Transfer'),
(5, 'Wallet/QR')
ON DUPLICATE KEY UPDATE payment_method_name=VALUES(payment_method_name);

-- =================== usuarios y proveedores ===================
INSERT INTO app_user (user_id, user_name, user_username, user_password) VALUES
(1, 'Admin User', 'admin', 'Admin@123')
ON DUPLICATE KEY UPDATE user_username=VALUES(user_username);

INSERT INTO vendor (vendor_id, vendor_name, vendor_cel, vendor_instagram, vendor_email, vendor_address, vendor_description) VALUES
(1, 'Moda BA', '+54 11 5555 0001', '@moda_ba', 'ventas@modaba.com', 'Av. Santa Fe 1234, CABA', 'Main clothing supplier'),
(2, 'Acme Textiles', '+54 11 5555 0002', '@acmetextiles', 'sales@acmetextiles.com', 'Calle Corrientes 2000, CABA', 'Textiles and belts'),
(3, 'ColorFit', '+54 11 5555 0003', '@colorfit', 'info@colorfit.com', 'Av. Rivadavia 2350, CABA', 'Sport footwear')
ON DUPLICATE KEY UPDATE vendor_name=VALUES(vendor_name);

-- =================== ARTICLES ===================
INSERT INTO article (article_id, article_name, article_description, article_profit_percentage, sub_category_id) VALUES
(1, 'Basic T-Shirt', '100% cotton basic tee', 50.00, 1),
(2, 'Slim Jeans', 'Slim fit denim jeans', 60.00, 3),
(3, 'Running Sneakers', 'Lightweight running shoes', 55.00, 5),
(4, 'Classic Belt', 'Leather belt', 40.00, 7),
(5, 'Summer Hat', 'Light hat for summer', 45.00, 8)
ON DUPLICATE KEY UPDATE article_name=VALUES(article_name);

-- =================== ITEMS (Variants) ===================
INSERT INTO item (item_id, article_id, vendor_id, item_buy_price, item_buy_date, item_list_price, item_code, color_id, size_id, item_stock_qty) VALUES
(1, 1, 1, 4000.00, '2025-09-01', 8000.00, 'TSH-BLK-M-001', 1, 3, 10),
(2, 1, 1, 4000.00, '2025-09-01', 8000.00, 'TSH-WHT-L-001', 2, 4, 8),
(3, 2, 2, 12000.00, '2025-09-05', 24000.00, 'JEAN-BLU-L-001', 4, 4, 5),
(4, 3, 3, 20000.00, '2025-09-10', 38000.00, 'SNK-WHT-L-001', 2, 4, 3),
(5, 4, 2, 5000.00,  '2025-09-12', 9000.00,  'BEL-BLK-M-001', 1, 3, 7),
(6, 5, 1, 3000.00,  '2025-09-15', 6500.00,  'HAT-WHT-L-001', 2, 4, 6)
ON DUPLICATE KEY UPDATE item_code=VALUES(item_code);

-- =================== SALES (Header) ===================
INSERT INTO sale (sale_id, sale_date, sale_code, sale_total, payment_method_id, created_by_user_id) VALUES
(1, '2025-10-01', 'S-0001', 40000.00, 1, 1),
(2, '2025-10-02', 'S-0002', 14500.00, 3, 1)
ON DUPLICATE KEY UPDATE sale_code=VALUES(sale_code);

-- =================== SALES DETAILS (Lines) ===================
INSERT INTO sale_detail (sale_detail_id, sale_id, item_id, sale_quantity, sale_unit_price, sale_line_total) VALUES
(1, 1, 1, 2, 8000.00, 16000.00),
(2, 1, 3, 1, 24000.00, 24000.00),
(3, 2, 2, 1, 8000.00, 8000.00),
(4, 2, 6, 1, 6500.00, 6500.00)
ON DUPLICATE KEY UPDATE sale_line_total=VALUES(sale_line_total);

SET FOREIGN_KEY_CHECKS = 1;
