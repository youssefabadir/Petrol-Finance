package com.ya.pf.auditable.transaction.customer_transaction.entity;

import com.ya.pf.auditable.transaction.TransactionEntity;
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
@DiscriminatorValue(value = "CUSTOMER_TRANSACTION")
@SQLDelete(sql = "UPDATE [transaction] SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0 AND transaction_type='CUSTOMER_TRANSACTION'")
public class CustomerTransactionEntity extends TransactionEntity {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "customer_balance")
    private Float customerBalance;

}
