package com.ya.pf.transaction;

import com.ya.pf.customer.CustomerEntity;
import com.ya.pf.product.ProductEntity;
import com.ya.pf.supplier.SupplierEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Setter
@Getter
@Entity
@Table(name = "\"transaction\"")
public class TransactionEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private SupplierEntity supplierEntity;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private CustomerEntity customerEntity;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;

    @Column(name = "amount")
    private float amount;

    @Column(name = "due_money")
    private Double dueMoney;

    @Column(name = "paid_money")
    private Double paidMoney;

    @Column(name = "receipt_no")
    private int receiptNumber;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "transaction_date")
    private Date transactionDate;

    @Override
    public boolean equals(Object o) {

        if (getClass() != o.getClass()) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        TransactionEntity transactionEntity = (TransactionEntity) o;
        return id != null && Objects.equals(id, transactionEntity.id);
    }

    @Override
    public String toString() {

        return getClass().getSimpleName() + "(" +
                "id = " + id + ", " +
                "supplier id = " + supplierEntity.getId() + ", " +
                "customer id = " + customerEntity.getId() + ", " +
                "product id = " + productEntity.getId() + ", " +
                "amount = " + amount + ", " +
                "due money = " + dueMoney + ", " +
                "paid money = " + paidMoney + ", " +
                "receipt no = " + receiptNumber + ", " +
                "transaction date = " + transactionDate +
                ")";
    }

    @Override
    public int hashCode() {

        return getClass().hashCode();
    }

}
