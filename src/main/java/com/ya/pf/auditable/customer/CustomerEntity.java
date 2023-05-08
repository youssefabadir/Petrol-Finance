package com.ya.pf.auditable.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.customer_payment.CustomerPaymentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
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
@Table(name = "customer")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE customer SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class CustomerEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "balance", nullable = false)
	private Double balance;

	@JsonIgnore
	@OneToMany(mappedBy = "customerEntity")
	private Set<BillEntity> bills = new LinkedHashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "customerEntity")
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
		CustomerEntity customerEntity = (CustomerEntity) o;
		return id != null && Objects.equals(id, customerEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"name = " + name + ", " +
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
