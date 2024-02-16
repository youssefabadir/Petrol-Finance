package com.ya.pf.auditable.shipment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ya.pf.auditable.Auditable;
import com.ya.pf.auditable.bill.BillEntity;
import com.ya.pf.auditable.expense.ExpenseEntity;
import com.ya.pf.auditable.truck.TruckEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.LinkedHashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Table(name = "shipment")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE shipment SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public class ShipmentEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bill_id", referencedColumnName = "id")
    private BillEntity billEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id", referencedColumnName = "id")
    private TruckEntity truckEntity;

    @Column(name = "truck_balance", nullable = false)
    private float truckBalance;

    @Column(name = "revenue", nullable = false)
    private float revenue;

    @Column(name = "note")
    private String note;

    @JsonIgnore
    @OneToMany(mappedBy = "shipment")
    private Set<ExpenseEntity> expenseEntities = new LinkedHashSet<>();

}
