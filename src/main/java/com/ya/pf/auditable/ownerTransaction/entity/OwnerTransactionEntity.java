package com.ya.pf.auditable.ownerTransaction.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "owner_transaction")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE owner_transaction SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class OwnerTransactionEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "owner_supplier_balance", nullable = false)
    private double ownerSupplierBalance;

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "owner_payment_id")
    private Long ownerPaymentId;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;

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
        OwnerTransactionEntity ownerTransactionEntity = (OwnerTransactionEntity) o;
        return id != null && Objects.equals(id, ownerTransactionEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "supplier id = " + supplierId + ", " +
                "owner supplier balance = " + ownerSupplierBalance + ", " +
                "payment id = " + ownerPaymentId + ", " +
                "bill id = " + billId + ", " +
                "date = " + date + ", " +
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
