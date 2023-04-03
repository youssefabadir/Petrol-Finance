CREATE TABLE customer
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255),
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE supplier
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255),
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE product
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255),
    price              DECIMAL(18, 2),
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE bill
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    supplier_id        INT NOT NULL,
    customer_id        INT NOT NULL,
    product_id         INT NOT NULL,
    liter              DECIMAL(18, 2),
    number             VARCHAR(255),
    amount             DECIMAL(18, 2),
    date               DATETIME,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_TRANSACTION_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_TRANSACTION_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_TRANSACTION_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE way_of_payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) UNIQUE,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE TABLE payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    customer_id        INT NOT NULL,
    way_of_payment_id  INT NOT NULL,
    number             VARCHAR(255),
    amount             DECIMAL(18, 2),
    date               DATETIME,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT UNIQUE_RECEIPT_NUMBER UNIQUE (way_of_payment_id, number),
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE VIEW payment_and_bill_details AS
SELECT payment_id,
       way_of_payment_id,
       payment_number,
       payment_amount,
       payment_date,
       bill_id,
       supplier_id,
       product_id,
       liter,
       bill_number,
       bill_amount,
       bill_date,
       CASE
           WHEN payment_date is NULL THEN bill_date
           ELSE payment_date
           END AS sort_date,
       CASE
           WHEN payment_amount is NULL THEN liter
           ELSE payment_amount
           END AS balance
FROM (SELECT p.id     AS payment_id,
             p.way_of_payment_id,
             p.number AS payment_number,
             p.amount AS payment_amount,
             p.date   AS payment_date,
             NULL     AS bill_id,
             NULL     AS supplier_id,
             NULL     AS product_id,
             NULL     AS liter,
             NULL     AS bill_number,
             NULL     AS bill_amount,
             NULL     AS bill_date
      FROM payment p
      UNION
      SELECT NULL     AS payment_id,
             NULL     AS way_of_payment_id,
             NULL     AS payment_number,
             NULL     AS payment_amount,
             NULL     AS payment_date,
             b.id     AS bill_id,
             b.supplier_id,
             b.product_id,
             b.liter,
             b.number AS bill_number,
             b.amount as bill_amount,
             b.date   AS bill_date
      FROM bill b) AS payments_and_bills