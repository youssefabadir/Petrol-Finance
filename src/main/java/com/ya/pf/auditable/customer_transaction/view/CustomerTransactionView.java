package com.ya.pf.auditable.customer_transaction.view;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.Objects;

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
	private Double customerBalance;

	@Column(name = "payment_id")
	private Long paymentId;

	@Column(name = "payment_number")
	private String paymentNumber;

	@Column(name = "payment_amount")
	private Double paymentAmount;

	@Column(name = "transferred_payment")
	private boolean transferredPayment;

	@Column(name = "payment_method_id")
	private Long paymentMethodId;

	@Column(name = "payment_method_name")
	private String paymentMethodName;

	@Column(name = "bill_id")
	private Long billId;

	@Column(name = "bill_number")
	private String billNumber;

	@Column(name = "bill_quantity")
	private Double billQuantity;

	@Column(name = "bill_amount")
	private Double billAmount;

	@Column(name = "product_id")
	private Long productId;

	@Column(name = "product_name")
	private String productName;

	@Column(name = "date")
	@JsonFormat(pattern = "dd-MM-yyyy")
	private Date date;

	@Override
	public boolean equals(Object o) {

		if (getClass() != o.getClass()) {
			return false;
		}
		if (this == o) {
			return true;
		}
		if (Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		CustomerTransactionView customerTransactionView = (CustomerTransactionView) o;
		return transactionId != null && Objects.equals(transactionId, customerTransactionView.transactionId);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"transactionId = " + transactionId + ", " +
				"customerId = " + customerId + ", " +
				"customerName = " + customerName + ", " +
				"customerBalance = " + customerBalance + ", " +
				"paymentId = " + paymentId + ", " +
				"paymentNumber = " + paymentNumber + ", " +
				"paymentAmount = " + paymentAmount + ", " +
				"transferredPayment = " + transferredPayment + ", " +
				"paymentMethodId = " + paymentMethodId + ", " +
				"paymentMethodName = " + paymentMethodName + ", " +
				"billId = " + billId + ", " +
				"billNumber = " + billNumber + ", " +
				"billLiter = " + billQuantity + ", " +
				"billAmount = " + billAmount + ", " +
				"productId = " + productId + ", " +
				"productName = " + productName + ", " +
				"date = " + date +
				')';
	}

	@Override
	public int hashCode() {

		return getClass().hashCode();
	}

}
