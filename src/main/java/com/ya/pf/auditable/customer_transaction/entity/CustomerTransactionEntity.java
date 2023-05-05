package com.ya.pf.auditable.customer_transaction.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "customer_transaction")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE customer_transaction SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class CustomerTransactionEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "customer_id")
	private Long customerId;

	@Column(name = "customer_balance", nullable = false)
	private double customerBalance;

	@Column(name = "customer_payment_id")
	private Long customerPaymentId;

	@Column(name = "bill_id")
	private Long billId;

	@Column(name = "date")
	@JsonFormat(pattern = "dd/MM/yyyy")
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
		CustomerTransactionEntity customerTransactionEntity = (CustomerTransactionEntity) o;
		return id != null && Objects.equals(id, customerTransactionEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"customer id = " + customerId + ", " +
				"customer balance = " + customerBalance + ", " +
				"payment id = " + customerPaymentId + ", " +
				"bill id = " + billId + ", " +
				"date = " + date + ", " +
				"deleted = " + deleted + ", " +
				"createdDate = " + createdDate + ", " +
				"lastModifiedDate = " + lastModifiedDate +
				")";
	}

	@Override
	public int hashCode() {

		return getClass().hashCode();
	}

}
