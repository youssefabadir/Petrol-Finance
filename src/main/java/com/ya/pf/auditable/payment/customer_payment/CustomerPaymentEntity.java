package com.ya.pf.auditable.payment.customer_payment;

import com.ya.pf.auditable.customer.CustomerEntity;
import com.ya.pf.auditable.payment.PaymentEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = "CUSTOMER_PAYMENT")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0 AND payment_type='CUSTOMER_PAYMENT'")
public class CustomerPaymentEntity extends PaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id")
    private CustomerEntity customer;

}
