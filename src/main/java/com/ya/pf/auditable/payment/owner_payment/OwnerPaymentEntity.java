package com.ya.pf.auditable.payment.owner_payment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.expense.ExpenseEntity;
import com.ya.pf.auditable.payment.PaymentEntity;
import com.ya.pf.auditable.supplier.SupplierEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Setter
@Entity
@DiscriminatorValue(value = "OWNER_PAYMENT")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0 AND payment_type='OWNER_PAYMENT'")
public class OwnerPaymentEntity extends PaymentEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", referencedColumnName = "id")
    private SupplierEntity supplier;

    @JsonIgnore
    @OneToOne(mappedBy = "ownerPayment", fetch = FetchType.LAZY)
    private ExpenseEntity expense;

}
