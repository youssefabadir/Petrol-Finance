package com.ya.pf.auditable.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.supplier.SupplierEntity;
import com.ya.pf.auditable.wayofpayment.WayOfPaymentEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "payment")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class PaymentEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "number", nullable = false)
	private String number;

	@Column(name = "amount", nullable = false)
	double amount;

	@ManyToOne
	@JoinColumn(name = "supplier_id", nullable = false)
	private SupplierEntity supplierEntity;

	@ManyToOne
	@JoinColumn(name = "customer_id", nullable = false)
	private CustomerEntity customerEntity;

	@ManyToOne
	@JoinColumn(name = "way_of_payment_id", nullable = false)
	private WayOfPaymentEntity wayOfPaymentEntity;


	@Column(name = "date", nullable = false)
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
		PaymentEntity paymentEntity = (PaymentEntity) o;
		return id != null && Objects.equals(id, paymentEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"customer id = " + customerEntity.getId() + ", " +
				"way of payment = " + wayOfPaymentEntity.getId() + ", " +
				"receipt number = " + number + ", " +
				"payment date = " + date + ", " +
				"deleted = " + deleted +
				"createdDate = " + createdDate +
				"lastModifiedDate = " + lastModifiedDate +
				")";
	}

	@Override
	public int hashCode() {

		return getClass().hashCode();
	}

}
