CREATE TABLE customer
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) NOT NULL,
    balance            FLOAT DEFAULT 0.00,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE supplier
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) NOT NULL,
    balance            FLOAT DEFAULT 0.00,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE product
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) NOT NULL,
    supplier_price     FLOAT         NOT NULL,
    customer_price     FLOAT         NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE payment_method
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) UNIQUE NOT NULL,
    balance            FLOAT                NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE TABLE treasury
(
    id                     INT IDENTITY (1,1) PRIMARY KEY,
    balance                FLOAT NOT NULL,
    payment_method_id      INT   NOT NULL,
    payment_method_balance FLOAT,
    payment_number         NVARCHAR(255),
    deleted                BIT,
    created_date           DATETIME,
    last_modified_date     DATETIME,
);

CREATE TABLE bill
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    supplier_id        INT          NOT NULL,
    customer_id        INT          NOT NULL,
    product_id         INT          NOT NULL,
    quantity           FLOAT        NOT NULL,
    number             VARCHAR(255) NOT NULL,
    amount             FLOAT        NOT NULL,
    date               DATETIME     NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_BILL_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_BILL_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_BILL_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    payment_type       NVARCHAR(255) NOT NULL,
    number             NVARCHAR(255),
    amount             FLOAT         NOT NULL,
    payment_method_id  INT           NOT NULL,
    customer_id        INT,
    supplier_id        INT,
    transferred        BIT,
    date               DATE,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE TABLE [transaction]
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    transaction_type   NVARCHAR(255) NOT NULL,
    payment_id         INT,
    bill_id            INT,
    customer_id        INT,
    customer_balance   FLOAT,
    supplier_id        INT,
    supplier_balance   FLOAT,
    date               DATE,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);

CREATE TABLE discount
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    discount           FLOAT NOT NULL,
    customer_id        INT   NOT NULL,
    product_id         INT   NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_DISCOUNT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_DISCOUNT_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id),
);

CREATE VIEW customer_transaction_view AS
SELECT t.id               AS transaction_id,
       c.id               AS customer_id,
       c.name             AS customer_name,
       t.customer_balance AS customer_balance,
       p.id               AS payment_id,
       p.number           AS payment_number,
       p.amount           AS payment_amount,
       p.transferred      AS transferred_payment,
       pm.id              AS payment_method_id,
       pm.name            AS payment_method_name,
       b.id               AS bill_id,
       b.number           AS bill_number,
       b.quantity         AS bill_quantity,
       b.amount           AS bill_amount,
       pr.id              AS product_id,
       pr.name            AS product_name,
       t.date
FROM [transaction] t
         LEFT OUTER JOIN customer c ON c.id = t.customer_id
         LEFT OUTER JOIN payment p ON p.id = t.payment_id
         LEFT OUTER JOIN payment_method pm ON pm.id = p.payment_method_id
         LEFT OUTER JOIN bill b ON b.id = t.bill_id
         LEFT OUTER JOIN product pr ON pr.id = b.product_id
WHERE t.deleted = 0
  AND t.transaction_type = 'CUSTOMER_TRANSACTION'

CREATE VIEW owner_transaction_view AS
SELECT t.id               AS transaction_id,
       s.id               AS supplier_id,
       s.name             AS supplier_name,
       t.supplier_balance AS owner_supplier_balance,
       p.id               AS payment_id,
       p.number           AS payment_number,
       p.amount           AS payment_amount,
       p.transferred      AS transferred_payment,
       pm.id              AS payment_method_id,
       pm.name            AS payment_method_name,
       b.id               AS bill_id,
       b.number           AS bill_number,
       b.quantity         AS bill_quantity,
       b.amount           AS bill_amount,
       pr.id              AS product_id,
       pr.name            AS product_name,
       t.date
FROM [transaction] t
         LEFT OUTER JOIN supplier s ON s.id = t.supplier_id
         LEFT OUTER JOIN payment p ON p.id = t.payment_id
         LEFT OUTER JOIN payment_method pm ON pm.id = p.payment_method_id
         LEFT OUTER JOIN bill b ON b.id = t.bill_id
         LEFT OUTER JOIN product pr ON pr.id = b.product_id
WHERE t.deleted = 0
  AND t.transaction_type = 'OWNER_TRANSACTION'

CREATE VIEW discount_view AS
SELECT d.id   AS id,
       d.discount,
       c.id   AS customer_id,
       c.name AS customer_name,
       p.id   AS product_id,
       p.name AS product_name
FROM discount d
         INNER JOIN customer c ON c.id = d.customer_id
         INNER JOIN product p ON p.id = d.product_id
WHERE d.deleted = 0
