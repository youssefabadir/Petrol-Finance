package com.ya.pf.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.transaction.TransactionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "product")
@Accessors(chain = true)
public class ProductEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "price")
	private Double price;

	@JsonIgnore
	@OneToMany(mappedBy = "productEntity")
	private Set<TransactionEntity> transactions = new LinkedHashSet<>();

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
		ProductEntity productEntity = (ProductEntity) o;
		return id != null && Objects.equals(id, productEntity.id);
	}

	@Override
	public String toString() {

		return getClass().getSimpleName() + "(" +
				"id = " + id + ", " +
				"name = " + name + ", " +
				"price = " + price +
				")";
	}

	@Override
	public int hashCode() {

		return getClass().hashCode();
	}

}
