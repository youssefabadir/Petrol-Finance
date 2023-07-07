package com.ya.pf.auditable.payment_method;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethodEntity, Long> {

    List<PaymentMethodEntity> findByNameContaining(String name);

    Page<PaymentMethodEntity> findByNameContaining(String name, Pageable pageable);

    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(w) = 0 THEN true ELSE false END " +
            "FROM PaymentMethodEntity w " +
            "WHERE w.id != :id AND " +
            "w.name = :name")
    boolean checkUniquePayment(
            @Param("id") long id,
            @Param("name") String name
    );

    @Modifying
    @Query("UPDATE PaymentMethodEntity p SET p.balance = :balance WHERE p.id = :id")
    void updatePaymentMethodBalance(@Param("id") Long id, @Param("balance") float balance);

}
