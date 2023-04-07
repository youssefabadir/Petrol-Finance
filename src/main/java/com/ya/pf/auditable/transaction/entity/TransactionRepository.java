package com.ya.pf.auditable.transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<TransactionEntity, Long> {

	TransactionEntity findFirstByCustomerIdOrderByIdDesc(@Param("customer_id") long customerId);

	void deleteAllByPaymentId(long id);

	void deleteAllByBillId(long id);

	@Modifying
	@Query("UPDATE TransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.billId > :billId")
	void updateBillCustomerBalance(@Param("billId") long billId, @Param("amount") double amount);

	@Modifying
	@Query("UPDATE TransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.paymentId > :paymentId")
	void updatePaymentCustomerBalance(@Param("paymentId") long paymentId, @Param("amount") double amount);

}
