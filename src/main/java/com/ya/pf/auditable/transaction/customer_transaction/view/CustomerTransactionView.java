package com.ya.pf.auditable.transaction.customer_transaction.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "customer_transaction_view")
public class CustomerTransactionView {

    @Id
    @Column(name = "transaction_id")
    private Long transactionId;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "customer_balance")
    private Float customerBalance;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "payment_number")
    private String paymentNumber;

    @Column(name = "payment_amount")
    private Float paymentAmount;

    @Column(name = "transferred_payment")
    private Boolean transferredPayment;

    @Column(name = "payment_method_id")
    private Long paymentMethodId;

    @Column(name = "payment_method_name")
    private String paymentMethodName;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "bill_number")
    private String billNumber;

    @Column(name = "bill_quantity")
    private Float billQuantity;

    @Column(name = "bill_customer_amount")
    private Float billCustomerAmount;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;

}
