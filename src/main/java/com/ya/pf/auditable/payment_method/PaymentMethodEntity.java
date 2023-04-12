package com.ya.pf.auditable.payment_method;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.customer_payment.CustomerPaymentEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "payment_method")
@SQLDelete(sql = "UPDATE payment_method SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class PaymentMethodEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@JsonIgnore
	@OneToMany(mappedBy = "paymentMethodEntity")
	private Set<CustomerPaymentEntity> payments = new LinkedHashSet<>();

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
		PaymentMethodEntity paymentMethodEntity = (PaymentMethodEntity) o;
		return id != null && Objects.equals(id, paymentMethodEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"name = " + name + ", " +
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
