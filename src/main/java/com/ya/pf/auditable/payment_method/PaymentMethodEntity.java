package com.ya.pf.auditable.payment_method;

import com.ya.pf.auditable.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "payment_method")
@SQLDelete(sql = "UPDATE payment_method SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class PaymentMethodEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private float balance;

    @Column(name = "start_balance", nullable = false)
    private float startBalance;

}
