package com.ya.pf.auditable.transaction.owner_transaction.entity;

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
@DiscriminatorValue(value = "OWNER_TRANSACTION")
@SQLDelete(sql = "UPDATE [transaction] SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0 AND transaction_type='OWNER_TRANSACTION'")
public class OwnerTransactionEntity extends TransactionEntity {

    @Column(name = "supplier_id")
    private Long supplierId;

    @Column(name = "supplier_balance")
    private Double supplierBalance;

}
