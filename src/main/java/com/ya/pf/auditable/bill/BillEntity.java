package com.ya.pf.auditable.bill;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.product.ProductEntity;
import com.ya.pf.auditable.supplier.SupplierEntity;
import lombok.Getter;
import lombok.NonNull;
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
@Table(name = "bill")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE bill SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class BillEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "supplier_id", referencedColumnName = "id")
	private SupplierEntity supplierEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", referencedColumnName = "id")
	private CustomerEntity customerEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", referencedColumnName = "id")
	private ProductEntity productEntity;

	@NonNull
	@Column(name = "number")
	private String number;

	@NonNull
	@Column(name = "quantity")
	private double quantity;

	@Column(name = "amount")
	private double amount;

	@NonNull
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
		BillEntity billEntity = (BillEntity) o;
		return id != null && Objects.equals(id, billEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"supplier id = " + supplierEntity.getId() + ", " +
				"customer id = " + customerEntity.getId() + ", " +
				"product id = " + productEntity.getId() + ", " +
				"number = " + number + ", " +
				"liter = " + quantity + ", " +
				"amount = " + amount + ", " +
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
