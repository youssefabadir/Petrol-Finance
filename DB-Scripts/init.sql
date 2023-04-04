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
    CONSTRAINT UNIQUE_BILL_NUMBER UNIQUE (supplier_id, number),
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
    number             VARCHAR(255),
    amount             DECIMAL(18, 2),
    customer_id        INT NOT NULL,
    supplier_id        INT NOT NULL,
    way_of_payment_id  INT NOT NULL,
    date               DATETIME,
    deleted            BIT,
    created_date       DATETIME,
    last_modified_date DATETIME,
    CONSTRAINT UNIQUE_PAYMENT_NUMBER UNIQUE (way_of_payment_id, number),
    CONSTRAINT FK_PAYMENT_CUSTOMER FOREIGN KEY (customer_id) REFERENCES customer (id),
    CONSTRAINT FK_PAYMENT_SUPPLIER FOREIGN KEY (supplier_id) REFERENCES supplier (id)
);

CREATE PROCEDURE transaction_report @customer_id int,
                                    @start_balance DECIMAL(18, 2),
                                    @start_date varchar(255),
                                    @end_date varchar(255)
AS
BEGIN
    SELECT payment_id,
           payment_customer_id,
           payment_supplier_id,
           payment_way_of_payment_id,
           payment_number,
           payment_amount,
           payment_date,
           bill_id,
           bill_supplier_id,
           bill_customer_id,
           bill_product_id,
           bill_liter,
           bill_number,
           bill_amount,
           bill_date,
           IIF(payment_date is NULL, bill_date, payment_date) AS sort_date,
           SUM(COALESCE(payment_amount, bill_amount))
               OVER (ORDER BY COALESCE(payment_date, bill_date) ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) +
           @start_balance                                     AS balance
    FROM (SELECT p.id                AS payment_id,
                 p.customer_id       AS payment_customer_id,
                 p.supplier_id       AS payment_supplier_id,
                 p.way_of_payment_id AS payment_way_of_payment_id,
                 p.number            AS payment_number,
                 p.amount            AS payment_amount,
                 p.date              AS payment_date,
                 NULL                AS bill_id,
                 NULL                AS bill_supplier_id,
                 NULL                AS bill_customer_id,
                 NULL                AS bill_product_id,
                 NULL                AS bill_liter,
                 NULL                AS bill_number,
                 NULL                AS bill_amount,
                 NULL                AS bill_date
          FROM payment p
          UNION
          SELECT NULL          AS payment_id,
                 NULL          AS payment_customer_id,
                 NULL          AS payment_supplier_id,
                 NULL          AS payment_way_of_payment_id,
                 NULL          AS payment_number,
                 NULL          AS payment_amount,
                 NULL          AS payment_date,
                 b.id          AS bill_id,
                 b.supplier_id AS bill_supplier_id,
                 b.customer_id AS bill_customer_id,
                 b.product_id  AS bill_product_id,
                 b.liter       AS bill_liter,
                 b.number      AS bill_number,
                 b.amount      as bill_amount,
                 b.date        AS bill_date
          FROM bill b) AS payments_and_bills
    where (payment_customer_id = @customer_id or bill_customer_id = @customer_id)
      and (payment_date > @start_date or bill_date > @start_date)
      and (payment_date < @end_date or bill_date < @end_date)
    order by sort_date
END;


    CREATE PROCEDURE check_previous_balance @customer_id int,
                                            @before_date varchar(255)
    AS
    BEGIN
        SELECT top 1 IIF(payment_date is NULL, bill_date, payment_date)                                                     AS sort_date,
                     SUM(COALESCE(payment_amount, bill_amount))
                         OVER (ORDER BY COALESCE(payment_date, bill_date) ROWS BETWEEN UNBOUNDED PRECEDING AND CURRENT ROW) AS balance
        FROM (SELECT p.customer_id AS payment_customer_id,
                     p.amount      AS payment_amount,
                     p.date        AS payment_date,
                     NULL          AS bill_customer_id,
                     NULL          AS bill_amount,
                     NULL          AS bill_date
              FROM payment p
              UNION
              SELECT NULL          AS payment_customer_id,
                     NULL          AS payment_amount,
                     NULL          AS payment_date,
                     b.customer_id AS bill_customer_id,
                     b.amount      as bill_amount,
                     b.date        AS bill_date
              FROM bill b) AS payments_and_bills
        where (payment_customer_id = @customer_id or bill_customer_id = @customer_id)
          and (bill_date < @before_date or payment_date < @before_date)
        order by sort_date desc
    END;