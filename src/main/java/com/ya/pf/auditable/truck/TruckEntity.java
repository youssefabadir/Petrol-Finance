package com.ya.pf.auditable.truck;

import com.ya.pf.auditable.Auditable;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.SQLDelete;

@Setter
@Getter
@Entity
@Table(name = "truck")
@Accessors(chain = true)
@SQLDelete(sql = "UPDATE truck SET deleted = 1 WHERE id=?")
@FilterDef(name = "deletedTruckFilter")
@Filter(name = "deletedTruckFilter", condition = "deleted = 0")
public class TruckEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "number", nullable = false)
    private String number;

    @Column(name = "balance", nullable = false)
    private Float balance;

}
