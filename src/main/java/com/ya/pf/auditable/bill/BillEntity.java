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
@SQLDelete(sql = "UPDATE customer SET deleted = true WHERE id=?")
@Where(clause = "deleted=0")
public class BillEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "supplier_id")
	private SupplierEntity supplierEntity;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private CustomerEntity customerEntity;

	@ManyToOne
	@JoinColumn(name = "product_id")
	private ProductEntity productEntity;

	@NonNull
	@Column(name = "number")
	private String number;

	@NonNull
	@Column(name = "liter")
	private double liter;

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
				"liter = " + liter + ", " +
				"amount = " + amount + ", " +
				"date = " + date +
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
