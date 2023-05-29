package com.ya.pf.auditable.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "payment")
@DiscriminatorColumn(name = "payment_type")
@SQLDelete(sql = "UPDATE payment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public abstract class PaymentEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_type", nullable = false)
    private String paymentType;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "amount", nullable = false)
    private double amount;

    @Column(name = "payment_method_id", nullable = false)
    private long paymentMethodId;

    @Column(name = "transferred")
    private boolean transferred;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;

}
