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
    amount             DECIMAL(18, 2),
    receipt_no         VARCHAR(255),
    due_money          DECIMAL(18, 2),
    paid_money         DECIMAL(18, 2),
    bill_date          DATETIME,
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
    receipt_number     VARCHAR(255),
    amount             DECIMAL(18, 2),
    payment_date       DATETIME,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT UNIQUE_RECEIPT_NUMBER UNIQUE (way_of_payment_id, receipt_number),
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id)
);