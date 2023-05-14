package com.ya.pf.auditable.owner_payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.payment_method.PaymentMethodEntity;
import com.ya.pf.auditable.supplier.SupplierEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "owner_payment")
@SQLDelete(sql = "UPDATE owner_payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class OwnerPaymentEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id", nullable = false)
    private SupplierEntity supplierEntity;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
    private PaymentMethodEntity paymentMethodEntity;

    @Column(name = "transferred")
    private boolean transferred;

    @Column(name = "date", nullable = false)
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
        OwnerPaymentEntity ownerPaymentEntity = (OwnerPaymentEntity) o;
        return id != null && Objects.equals(id, ownerPaymentEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "number = " + number + ", " +
                "amount = " + amount + ", " +
                "supplier id = " + supplierEntity.getId() + ", " +
                "way of payment = " + paymentMethodEntity.getId() + ", " +
                "transferred = " + transferred + ", " +
                "payment date = " + date + ", " +
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
