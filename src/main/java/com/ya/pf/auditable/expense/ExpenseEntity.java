package com.ya.pf.auditable.expense;

import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import com.ya.pf.auditable.shipment.ShipmentEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Setter
@Getter
@Entity
@Table(name = "expenses")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE expenses SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class ExpenseEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipment_id", referencedColumnName = "id")
    private ShipmentEntity shipment;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "note")
    private String note;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", referencedColumnName = "id")
    private OwnerPaymentEntity ownerPayment;

}
