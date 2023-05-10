package com.ya.pf.auditable.discount.view;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "discount_view")
public class DiscountView {

    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "discount")
    private double discount;

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_name")
    private String customerName;

    @Column(name = "product_id")
    private Long productId;

    @Column(name = "product_name")
    private String productName;

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
        DiscountView discountView = (DiscountView) o;
        return id != null && Objects.equals(id, discountView.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "discount = " + discount + ", " +
                "customer id = " + customerId + ", " +
                "customer name = " + customerName + ", " +
                "product id = " + productId + ", " +
                "product name = " + productName + ", " +
                ')';
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
