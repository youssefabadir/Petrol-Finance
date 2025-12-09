package com.ya.pf.auditable.transaction;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ya.pf.auditable.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.Date;

@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Table(name = "\"transaction\"")
@DiscriminatorColumn(name = "transaction_type")
@SQLDelete(sql = "UPDATE [transaction] SET deleted = 1 WHERE id=?")
@Where(clause = "deleted=0")
public abstract class TransactionEntity extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private Long paymentId;

    @Column(name = "bill_id")
    private Long billId;

    @Column(name = "date")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date date;

}
