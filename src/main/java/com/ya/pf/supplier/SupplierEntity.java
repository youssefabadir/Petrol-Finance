package com.ya.pf.supplier;

import com.ya.pf.transaction.TransactionEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "supplier")
public class SupplierEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @OneToMany(mappedBy = "supplierEntity")
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
        SupplierEntity supplierEntity = (SupplierEntity) o;
        return id != null && Objects.equals(id, supplierEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
                ")";
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
