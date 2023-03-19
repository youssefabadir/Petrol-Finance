package com.ya.pf.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.transaction.TransactionEntity;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "customerEntity")
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
        CustomerEntity customerEntity = (CustomerEntity) o;
        return id != null && Objects.equals(id, customerEntity.id);
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
