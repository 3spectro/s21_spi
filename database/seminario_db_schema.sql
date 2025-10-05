-- ===== Database =====
CREATE DATABASE IF NOT EXISTS seminario_db
  DEFAULT CHARACTER SET = utf8mb4
  DEFAULT COLLATE = utf8mb4_unicode_ci;

USE seminario_db;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- Drop in reverse dependency order (idempotent rebuild)
DROP TABLE IF EXISTS sale_detail;
DROP TABLE IF EXISTS sale;
DROP TABLE IF EXISTS item;
DROP TABLE IF EXISTS article;
DROP TABLE IF EXISTS sub_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS vendor;
DROP TABLE IF EXISTS color;
DROP TABLE IF EXISTS size;
DROP TABLE IF EXISTS payment_method;
DROP TABLE IF EXISTS app_user;

SET FOREIGN_KEY_CHECKS = 1;

-- =============== LOOKUP TABLES ===============

CREATE TABLE category (
  category_id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
  category_name         VARCHAR(100) NOT NULL,
  category_description  VARCHAR(500) NULL,
  CONSTRAINT pk_category PRIMARY KEY (category_id),
  CONSTRAINT uq_category_name UNIQUE (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sub_category (
  sub_category_id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sub_category_name         VARCHAR(100) NOT NULL,
  sub_category_description  VARCHAR(500) NULL,
  category_id               INT UNSIGNED NOT NULL,
  CONSTRAINT pk_sub_category PRIMARY KEY (sub_category_id),
  CONSTRAINT uq_sub_category_name_per_category UNIQUE (category_id, sub_category_name),
  CONSTRAINT fk_sub_category__category
    FOREIGN KEY (category_id) REFERENCES category(category_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX ix_sub_category__category (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE color (
  color_id     INT UNSIGNED NOT NULL AUTO_INCREMENT,
  color_name   VARCHAR(80) NOT NULL,
  CONSTRAINT pk_color PRIMARY KEY (color_id),
  CONSTRAINT uq_color_name UNIQUE (color_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE size (
  size_id     INT UNSIGNED NOT NULL AUTO_INCREMENT,
  size_value  VARCHAR(40) NOT NULL,
  CONSTRAINT pk_size PRIMARY KEY (size_id),
  CONSTRAINT uq_size_value UNIQUE (size_value)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE payment_method (
  payment_method_id   INT UNSIGNED NOT NULL AUTO_INCREMENT,
  payment_method_name VARCHAR(80) NOT NULL,
  CONSTRAINT pk_payment_method PRIMARY KEY (payment_method_id),
  CONSTRAINT uq_payment_method_name UNIQUE (payment_method_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE app_user (
  user_id        INT UNSIGNED NOT NULL AUTO_INCREMENT,
  user_name      VARCHAR(120) NOT NULL,
  user_username  VARCHAR(80)  NOT NULL,
  user_password  VARCHAR(255) NOT NULL,
  CONSTRAINT pk_app_user PRIMARY KEY (user_id),
  CONSTRAINT uq_app_user_username UNIQUE (user_username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE vendor (
  vendor_id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
  vendor_name        VARCHAR(150) NOT NULL,
  vendor_cel         VARCHAR(50)  NULL,
  vendor_instagram   VARCHAR(120) NULL,
  vendor_email       VARCHAR(254) NULL,
  vendor_address     VARCHAR(250) NULL,
  vendor_description VARCHAR(500) NULL,
  CONSTRAINT pk_vendor PRIMARY KEY (vendor_id),
  INDEX ix_vendor_name (vendor_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE article (
  article_id               INT UNSIGNED NOT NULL AUTO_INCREMENT,
  article_name             VARCHAR(150) NOT NULL,
  article_description      VARCHAR(500) NULL,
  article_profit_percentage DECIMAL(5,2) NOT NULL,
  sub_category_id          INT UNSIGNED NOT NULL,
  CONSTRAINT pk_article PRIMARY KEY (article_id),
  CONSTRAINT uq_article_name_per_subcategory UNIQUE (sub_category_id, article_name),
  CONSTRAINT fk_article__sub_category
    FOREIGN KEY (sub_category_id) REFERENCES sub_category(sub_category_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX ix_article__sub_category (sub_category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE item (
  item_id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
  article_id       INT UNSIGNED NOT NULL,
  vendor_id        INT UNSIGNED NOT NULL,
  item_buy_price   DECIMAL(10,2) NOT NULL,
  item_buy_date    DATE NOT NULL,
  item_list_price  DECIMAL(10,2) NOT NULL,
  item_code        VARCHAR(80) NOT NULL,
  color_id         INT UNSIGNED NOT NULL,
  size_id          INT UNSIGNED NOT NULL,
  item_stock_qty   INT NOT NULL DEFAULT 0,
  CONSTRAINT pk_item PRIMARY KEY (item_id),
  CONSTRAINT uq_item_code UNIQUE (item_code),
  CONSTRAINT uq_item_variant_per_vendor UNIQUE (article_id, color_id, size_id, vendor_id),
  CONSTRAINT fk_item__article FOREIGN KEY (article_id) REFERENCES article(article_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_item__vendor FOREIGN KEY (vendor_id) REFERENCES vendor(vendor_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_item__color FOREIGN KEY (color_id) REFERENCES color(color_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_item__size FOREIGN KEY (size_id) REFERENCES size(size_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX ix_item__article (article_id),
  INDEX ix_item__vendor (vendor_id),
  INDEX ix_item__color (color_id),
  INDEX ix_item__size (size_id),
  CHECK (item_stock_qty >= 0),
  CHECK (item_buy_price >= 0),
  CHECK (item_list_price >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sale (
  sale_id              INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sale_date            DATE NOT NULL,
  sale_code            VARCHAR(80) NOT NULL,
  sale_total           DECIMAL(12,2) NOT NULL,
  payment_method_id    INT UNSIGNED NOT NULL,
  created_by_user_id   INT UNSIGNED NOT NULL,
  CONSTRAINT pk_sale PRIMARY KEY (sale_id),
  CONSTRAINT uq_sale_code UNIQUE (sale_code),
  CONSTRAINT fk_sale__payment_method FOREIGN KEY (payment_method_id) REFERENCES payment_method(payment_method_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  CONSTRAINT fk_sale__created_by FOREIGN KEY (created_by_user_id) REFERENCES app_user(user_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX ix_sale__payment_method (payment_method_id),
  INDEX ix_sale__created_by (created_by_user_id),
  CHECK (sale_total >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE sale_detail (
  sale_detail_id  INT UNSIGNED NOT NULL AUTO_INCREMENT,
  sale_id         INT UNSIGNED NOT NULL,
  item_id         INT UNSIGNED NOT NULL,
  sale_quantity   INT NOT NULL,
  sale_unit_price DECIMAL(10,2) NOT NULL,
  sale_line_total DECIMAL(12,2) NOT NULL,
  CONSTRAINT pk_sale_detail PRIMARY KEY (sale_detail_id),
  CONSTRAINT fk_sale_detail__sale FOREIGN KEY (sale_id) REFERENCES sale(sale_id)
    ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT fk_sale_detail__item FOREIGN KEY (item_id) REFERENCES item(item_id)
    ON DELETE RESTRICT ON UPDATE CASCADE,
  INDEX ix_sale_detail__sale (sale_id),
  INDEX ix_sale_detail__item (item_id),
  CHECK (sale_quantity > 0),
  CHECK (sale_unit_price >= 0),
  CHECK (sale_line_total >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
