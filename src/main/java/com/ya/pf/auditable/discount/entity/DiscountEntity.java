package com.ya.pf.auditable.discount.entity;

import com.ya.pf.auditable.Auditable;
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
@Table(name = "discount")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE discount SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class DiscountEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "discount", nullable = false)
    private float discount;

    @Column(name = "customer_id", nullable = false)
    private long customerId;

    @JoinColumn(name = "product_id", nullable = false)
    private long productId;

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
        DiscountEntity discountEntity = (DiscountEntity) o;
        return id != null && Objects.equals(id, discountEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "discount = " + discount + ", " +
                "customer id = " + customerId + ", " +
                "product id = " + productId + ", " +
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
