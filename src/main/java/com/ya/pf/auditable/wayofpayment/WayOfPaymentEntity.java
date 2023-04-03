package com.ya.pf.auditable.wayofpayment;

import com.ya.pf.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "way_of_payment")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class WayOfPaymentEntity extends Auditable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

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
		WayOfPaymentEntity wayOfPaymentEntity = (WayOfPaymentEntity) o;
		return id != null && Objects.equals(id, wayOfPaymentEntity.id);
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
