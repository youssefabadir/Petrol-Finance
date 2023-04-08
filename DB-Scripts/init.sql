CREATE TABLE customer
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) NOT NULL,
    balance            DECIMAL(18, 2) DEFAULT 0.00,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE supplier
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE product
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255)  NOT NULL,
    price              DECIMAL(18, 2) NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE bill
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    supplier_id        INT            NOT NULL,
    customer_id        INT            NOT NULL,
    product_id         INT            NOT NULL,
    quantity           DECIMAL(18, 2) NOT NULL,
    number             VARCHAR(255)   NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    date               DATETIME       NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT UNIQUE_BILL_NUMBER UNIQUE (supplier_id, number),
    CONSTRAINT FK_TRANSACTION_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_TRANSACTION_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_TRANSACTION_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE payment_method
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) UNIQUE NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE TABLE payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    number             VARCHAR(255)   NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    customer_id        INT            NOT NULL,
    supplier_id        INT            NOT NULL,
    payment_method_id  INT            NOT NULL,
    date               DATETIME       NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT UNIQUE_PAYMENT_NUMBER UNIQUE (payment_method_id, number),
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_PAYMENT_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_PAYMENT_PAYMENT_METHOD FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
);

CREATE TABLE [transaction]
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    customer_id        INT,
    supplier_id        INT,
    customer_balance   DECIMAL(18, 2),
    payment_id         INT,
    bill_id            INT,
    date               DATETIME,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE VIEW transaction_view AS
SELECT t.id               AS transaction_id,
       c.id               AS customer_id,
       c.name             AS customer_name,
       t.customer_balance AS customer_balance,
       s.id               AS supplier_id,
       s.name             AS supplier_name,
       p.id               AS payment_id,
       p.number           AS payment_number,
       p.amount           AS payment_amount,
       pm.id              AS payment_method_id,
       pm.name            AS payment_method_name,
       b.id               AS bill_id,
       b.number           AS bill_number,
       b.liter            AS bill_liter,
       b.amount           AS bill_amount,
       prod.id            AS product_id,
       prod.name          AS product_name,
       t.date
FROM [transaction] t
         LEFT OUTER JOIN customer c ON c.id = t.customer_id
         LEFT OUTER JOIN supplier s ON s.id = t.supplier_id
         LEFT OUTER JOIN payment p ON p.id = t.payment_id
         LEFT OUTER JOIN bill b ON b.id = t.bill_id
         LEFT OUTER JOIN payment_method pm ON pm.id = p.payment_method_id
         LEFT OUTER JOIN product prod ON prod.id = b.product_id
WHERE t.deleted = 0