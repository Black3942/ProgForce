CREATE SCHEMA IF NOT EXISTS `pf_test_db` DEFAULT CHARACTER SET utf8;
USE `pf_test_db`;
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for products
-- ----------------------------
DROP TABLE IF EXISTS `products`;
CREATE TABLE `products`  (
                            `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
                            `title` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `price` decimal(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `status` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                            `is_deleted` bit(1) NOT NULL DEFAULT b'0',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for categories
-- ----------------------------
DROP TABLE IF EXISTS `categories`;
CREATE TABLE `categories`  (
                                  `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
                                  `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                                  `is_deleted` bit(1) NOT NULL DEFAULT b'0',
                                  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shops
-- ----------------------------
DROP TABLE IF EXISTS `shops`;
CREATE TABLE `shops`  (
                         `id` bigint(0) UNSIGNED NOT NULL AUTO_INCREMENT,
                         `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
                         `is_deleted` bit(1) NOT NULL DEFAULT b'0',
                         PRIMARY KEY (`id`) USING BTREE,
                         INDEX `FK_category_id`(`category_id`) USING BTREE,
                         CONSTRAINT `FK_category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for shops_categories
-- ----------------------------
DROP TABLE IF EXISTS `shops_categories`;
CREATE TABLE `shops_categories`  (
                                 `shop_id` bigint(0) UNSIGNED NOT NULL,
                                 `category_id` bigint(0) UNSIGNED NOT NULL,
                                 PRIMARY KEY (`shop_id`, `category_id`) USING BTREE,
                                 INDEX `category_id`(`category_id`) USING BTREE,
                                 INDEX `shop_id`(`shop_id`) USING BTREE,
                                 CONSTRAINT `shop_id` FOREIGN KEY (`shop_id`) REFERENCES `shops` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                 CONSTRAINT `category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for categories_products
-- ----------------------------
DROP TABLE IF EXISTS `categories_products`;
CREATE TABLE `categories_products`  (
                                     `category_id` bigint(0) UNSIGNED NOT NULL,
                                     `product_id` bigint(0) UNSIGNED NOT NULL,
                                     PRIMARY KEY (`category_id`, `product_id`) USING BTREE,
                                     INDEX `product_id`(`product_id`) USING BTREE,
                                     INDEX `category_id`(`category_id`) USING BTREE,
                                     CONSTRAINT `category_id` FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
                                     CONSTRAINT `product_id` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
