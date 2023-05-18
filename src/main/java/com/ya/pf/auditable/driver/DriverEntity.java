package com.ya.pf.auditable.driver;

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
@Table(name = "driver")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE driver SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class DriverEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
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
        DriverEntity driverEntity = (DriverEntity) o;
        return id != null && Objects.equals(id, driverEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "name = " + name + ", " +
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
