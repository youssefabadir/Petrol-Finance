package com.ya.pf.auditable.product;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "product")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE product SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class ProductEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "supplier_price", nullable = false)
    private Float supplierPrice;

    @Column(name = "customer_price", nullable = false)
    private Float customerPrice;

    @JsonIgnore
    @OneToMany(mappedBy = "productEntity")
    private Set<BillEntity> bills = new LinkedHashSet<>();

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
                "supplier price = " + supplierPrice + ", " +
                "customer price = " + customerPrice + ", " +
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
