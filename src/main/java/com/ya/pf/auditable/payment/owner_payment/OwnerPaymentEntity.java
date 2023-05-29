package com.ya.pf.auditable.payment.owner_payment;

import com.ya.pf.auditable.payment.PaymentEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = "OWNER_PAYMENT")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0 AND payment_type='OWNER_PAYMENT'")
public class OwnerPaymentEntity extends PaymentEntity {

    @Column(name = "supplier_id")
    private long supplierId;

}
