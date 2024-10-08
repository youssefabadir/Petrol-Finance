package com.ya.pf.auditable.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.payment.customer_payment.CustomerPaymentEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

import jakarta.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "customer")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE customer SET deleted = 1 WHERE id=?")
@FilterDef(name = "deletedCustomerFilter")
@Filter(name = "deletedCustomerFilter", condition = "deleted = 0")
public class CustomerEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "balance", nullable = false)
    private Float balance;

    @Column(name = "start_balance", nullable = false)
    private Float startBalance;

    @JsonIgnore
    @OneToMany(mappedBy = "customerEntity")
    private Set<BillEntity> bills = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "customer")
    private Set<CustomerPaymentEntity> payments = new LinkedHashSet<>();

}
