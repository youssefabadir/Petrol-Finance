package com.ya.pf.auditable.supplier;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.payment.owner_payment.OwnerPaymentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "supplier")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE supplier SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class SupplierEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @JsonIgnore
    @OneToMany(mappedBy = "supplierEntity")
    private Set<BillEntity> bills = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "supplier")
    private Set<OwnerPaymentEntity> payments = new LinkedHashSet<>();

}
