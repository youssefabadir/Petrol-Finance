package com.ya.pf.auditable.discount;

import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.product.ProductEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "customer_discount")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE customer_discount SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class CustomerDiscountEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "discount", nullable = false)
	private double discount;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
	private CustomerEntity customerEntity;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", referencedColumnName = "id", nullable = false)
	private ProductEntity productEntity;

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
		CustomerDiscountEntity customerDiscountEntity = (CustomerDiscountEntity) o;
		return id != null && Objects.equals(id, customerDiscountEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"discount = " + discount + ", " +
				"customer id = " + customerEntity.getId() + ", " +
				"product id = " + productEntity.getId() + ", " +
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
