package com.ya.pf.auditable.truck;

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
@Table(name = "truck")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE truck SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class TruckEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

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
        TruckEntity truckEntity = (TruckEntity) o;
        return id != null && Objects.equals(id, truckEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "number = " + number + ", " +
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
