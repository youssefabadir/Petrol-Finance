package com.ya.pf.auditable.customer_payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.payment_method.PaymentMethodEntity;
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
@Table(name = "customer_payment")
@SQLDelete(sql = "UPDATE customer_payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class CustomerPaymentEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "amount", nullable = false)
    private double amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerEntity customerEntity;

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
        CustomerPaymentEntity customerPaymentEntity = (CustomerPaymentEntity) o;
        return id != null && Objects.equals(id, customerPaymentEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "number = " + number + ", " +
                "amount = " + amount + ", " +
                "customer id = " + customerEntity.getId() + ", " +
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
