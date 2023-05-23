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
    balance            DECIMAL(18, 2) DEFAULT 0.00,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE product
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255)  NOT NULL,
    supplier_price     DECIMAL(18, 2) NOT NULL,
    customer_price     DECIMAL(18, 2) NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME
);

CREATE TABLE payment_method
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    name               NVARCHAR(255) UNIQUE NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
);
INSERT INTO payment_method
VALUES ('cash', 0, GETDATE(), GETDATE());

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
    CONSTRAINT FK_BILL_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_BILL_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_BILL_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id)
);

CREATE TABLE customer_payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    number             VARCHAR(255)   NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    customer_id        INT            NOT NULL,
    payment_method_id  INT            NOT NULL,
    transferred        BIT,
    date               DATETIME       NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_CUSTOMER_PAYMENT_PAYMENT_METHOD FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
);

CREATE TABLE owner_payment
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    number             VARCHAR(255)   NOT NULL,
    amount             DECIMAL(18, 2) NOT NULL,
    supplier_id        INT            NOT NULL,
    payment_method_id  INT            NOT NULL,
    transferred        BIT,
    date               DATETIME       NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_PAYMENT_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id),
    CONSTRAINT FK_OWNER_PAYMENT_PAYMENT_METHOD FOREIGN KEY (payment_method_id) REFERENCES payment_method (id)
);

CREATE TABLE customer_transaction
(
    id                  INT IDENTITY (1,1) PRIMARY KEY,
    customer_id         INT,
    customer_balance    DECIMAL(18, 2),
    customer_payment_id INT,
    bill_id             INT,
    date                DATETIME,
    deleted             BIT,
    created_date        DATETIME,
    last_modified_date  DATETIME,
    CONSTRAINT FK_TRANSACTION_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
);

CREATE TABLE owner_transaction
(
    id                     INT IDENTITY (1,1) PRIMARY KEY,
    owner_supplier_balance DECIMAL(18, 2),
    supplier_id            INT,
    owner_payment_id       INT,
    bill_id                INT,
    date                   DATETIME,
    deleted                BIT,
    created_date           DATETIME,
    last_modified_date     DATETIME,
    CONSTRAINT FK_TRANSACTION_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);

CREATE TABLE discount
(
    id                 INT IDENTITY (1,1) PRIMARY KEY,
    discount           DECIMAL(6, 3) NOT NULL,
    customer_id        INT           NOT NULL,
    product_id         INT           NOT NULL,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT FK_DISCOUNT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_DISCOUNT_PRODUCT FOREIGN KEY (product_id) REFERENCES product (id),
);

CREATE VIEW customer_transaction_view AS
SELECT ct.id               AS transaction_id,
       c.id                AS customer_id,
       c.name              AS customer_name,
       ct.customer_balance AS customer_balance,
       cp.id               AS payment_id,
       cp.number           AS payment_number,
       cp.amount           AS payment_amount,
       cp.transferred      AS transferred_payment,
       pm.id               AS payment_method_id,
       pm.name             AS payment_method_name,
       b.id                AS bill_id,
       b.number            AS bill_number,
       b.quantity          AS bill_quantity,
       b.amount            AS bill_amount,
       pr.id               AS product_id,
       pr.name             AS product_name,
       ct.date
FROM customer_transaction ct
         LEFT OUTER JOIN customer c ON c.id = ct.customer_id
         LEFT OUTER JOIN customer_payment cp ON cp.id = ct.customer_payment_id
         LEFT OUTER JOIN payment_method pm ON pm.id = cp.payment_method_id
         LEFT OUTER JOIN bill b ON b.id = ct.bill_id
         LEFT OUTER JOIN product pr ON pr.id = b.product_id
WHERE ct.deleted = 0

CREATE VIEW owner_transaction_view AS
SELECT ot.id                     AS transaction_id,
       s.id                      AS supplier_id,
       s.name                    AS supplier_name,
       ot.owner_supplier_balance AS owner_supplier_balance,
       op.id                     AS payment_id,
       op.number                 AS payment_number,
       op.amount                 AS payment_amount,
       op.transferred            AS transferred_payment,
       pm.id                     AS payment_method_id,
       pm.name                   AS payment_method_name,
       b.id                      AS bill_id,
       b.number                  AS bill_number,
       b.quantity                AS bill_quantity,
       b.amount                  AS bill_amount,
       pr.id                     AS product_id,
       pr.name                   AS product_name,
       ot.date
FROM owner_transaction ot
         LEFT OUTER JOIN supplier s ON s.id = ot.supplier_id
         LEFT OUTER JOIN owner_payment op ON op.id = ot.owner_payment_id
         LEFT OUTER JOIN payment_method pm ON pm.id = op.payment_method_id
         LEFT OUTER JOIN bill b ON b.id = ot.bill_id
         LEFT OUTER JOIN product pr ON pr.id = b.product_id
WHERE ot.deleted = 0

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