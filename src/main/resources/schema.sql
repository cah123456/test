-- 宠物商店数据库初始化脚本
-- 使用MySQL 8.0

CREATE DATABASE IF NOT EXISTS petstore DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE petstore;

-- 用户表
CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户唯一标识',
    username VARCHAR(50) NOT NULL COMMENT '用户名，用于登录',
    password VARCHAR(255) NOT NULL COMMENT '加密后的密码（MD5加盐）',
    nickname VARCHAR(50) DEFAULT NULL COMMENT '用户昵称',
    email VARCHAR(100) DEFAULT NULL COMMENT '用户邮箱',
    role VARCHAR(20) NOT NULL DEFAULT 'user' COMMENT '用户角色：user 或 admin',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '注册时间',
    UNIQUE INDEX idx_username (username),
    INDEX idx_role (role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 商品类别表
CREATE TABLE IF NOT EXISTS category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '类别唯一标识',
    name VARCHAR(50) NOT NULL COMMENT '类别名称',
    description VARCHAR(255) DEFAULT NULL COMMENT '类别描述',
    UNIQUE INDEX idx_category_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品类别表';

-- 商品表
CREATE TABLE IF NOT EXISTS product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '商品唯一标识',
    name VARCHAR(100) NOT NULL COMMENT '商品名称',
    description TEXT COMMENT '商品描述',
    price DECIMAL(10,2) NOT NULL COMMENT '商品价格',
    stock INT NOT NULL DEFAULT 0 COMMENT '库存数量',
    image_url VARCHAR(255) DEFAULT NULL COMMENT '商品图片URL',
    breed VARCHAR(50) DEFAULT NULL COMMENT '品种',
    age INT DEFAULT NULL COMMENT '年龄（月）',
    status VARCHAR(20) NOT NULL DEFAULT '有货' COMMENT '库存状态：有货 或 缺货',
    category_id BIGINT NOT NULL COMMENT '所属类别ID',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_product_name (name),
    INDEX idx_category_id (category_id),
    INDEX idx_status (status),
    INDEX idx_price (price),
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES category(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='商品表';

-- 购物车项表
CREATE TABLE IF NOT EXISTS cart_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '购物车项唯一标识',
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    quantity INT NOT NULL DEFAULT 1 COMMENT '商品数量',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '加入购物车时间',
    INDEX idx_user_id (user_id),
    INDEX idx_product_id (product_id),
    UNIQUE INDEX idx_user_product (user_id, product_id),
    CONSTRAINT fk_cart_user FOREIGN KEY (user_id) REFERENCES user(id),
    CONSTRAINT fk_cart_product FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='购物车项表';

-- 订单表
CREATE TABLE IF NOT EXISTS order_info (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单唯一标识',
    order_number VARCHAR(50) NOT NULL COMMENT '订单号',
    user_id BIGINT NOT NULL COMMENT '下单用户ID',
    total_amount DECIMAL(12,2) NOT NULL COMMENT '订单总金额',
    status VARCHAR(20) NOT NULL DEFAULT '待支付' COMMENT '订单状态：待支付、已支付、已发货、已完成',
    shipping_address VARCHAR(255) NOT NULL COMMENT '收货地址',
    contact_phone VARCHAR(20) NOT NULL COMMENT '联系电话',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '下单时间',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE INDEX idx_order_number (order_number),
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_created_at (created_at),
    CONSTRAINT fk_order_user FOREIGN KEY (user_id) REFERENCES user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- 订单项表
CREATE TABLE IF NOT EXISTS order_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单项唯一标识',
    order_id BIGINT NOT NULL COMMENT '所属订单ID',
    product_id BIGINT NOT NULL COMMENT '商品ID',
    product_name VARCHAR(100) NOT NULL COMMENT '商品名称（快照）',
    product_price DECIMAL(10,2) NOT NULL COMMENT '商品单价（快照）',
    quantity INT NOT NULL COMMENT '购买数量',
    subtotal DECIMAL(12,2) NOT NULL COMMENT '小计金额',
    INDEX idx_order_id (order_id),
    INDEX idx_product_id (product_id),
    CONSTRAINT fk_orderitem_order FOREIGN KEY (order_id) REFERENCES order_info(id),
    CONSTRAINT fk_orderitem_product FOREIGN KEY (product_id) REFERENCES product(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- 插入默认管理员账号（密码：admin123）
INSERT INTO user (username, password, nickname, email, role) VALUES
('admin', '0192023a7bbd73250516f069df18b500', '管理员', 'admin@petstore.com', 'admin');

-- 插入默认分类
INSERT INTO category (name, description) VALUES
('猫', '猫咪相关宠物及用品'),
('狗', '狗狗相关宠物及用品'),
('小宠', '仓鼠、兔子等小型宠物'),
('水族', '鱼类及水族用品'),
('爬宠', '爬行动物及用品');

-- 插入示例商品
INSERT INTO product (name, description, price, stock, image_url, breed, age, status, category_id) VALUES
('英短蓝猫', '英国短毛猫，蓝色，性格温顺', 1500.00, 10, '/images/cat1.jpg', '英短', 3, '有货', 1),
('布偶猫', '布偶猫，双色，性格粘人', 3000.00, 5, '/images/cat2.jpg', '布偶', 4, '有货', 1),
('金毛幼犬', '金毛寻回犬，金色，活泼可爱', 2000.00, 8, '/images/dog1.jpg', '金毛', 2, '有货', 2),
('泰迪犬', '玩具贵宾犬，棕色，聪明机灵', 1800.00, 6, '/images/dog2.jpg', '泰迪', 3, '有货', 2),
('仓鼠笼套装', '包含笼子、跑轮、食盆等全套用品', 200.00, 20, '/images/hamster1.jpg', NULL, NULL, '有货', 3),
('兔粮5kg', '优质兔粮，营养均衡', 80.00, 30, '/images/rabbit1.jpg', NULL, NULL, '有货', 3);