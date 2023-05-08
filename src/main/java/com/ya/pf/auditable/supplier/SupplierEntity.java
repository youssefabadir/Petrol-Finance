package com.ya.pf.auditable.supplier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.owner_payment.OwnerPaymentEntity;
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

@Setter
@Getter
@Entity
@Table(name = "supplier")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE supplier SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class SupplierEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "balance", nullable = false)
	private Double balance;

	@JsonIgnore
	@OneToMany(mappedBy = "supplierEntity")
	private Set<BillEntity> bills = new LinkedHashSet<>();

	@JsonIgnore
	@OneToMany(mappedBy = "supplierEntity")
	private Set<OwnerPaymentEntity> payments = new LinkedHashSet<>();

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
		SupplierEntity supplierEntity = (SupplierEntity) o;
		return id != null && Objects.equals(id, supplierEntity.id);
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
