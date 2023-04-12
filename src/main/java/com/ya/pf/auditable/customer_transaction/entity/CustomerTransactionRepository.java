package com.ya.pf.auditable.customer_transaction.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerTransactionRepository extends JpaRepository<CustomerTransactionEntity, Long> {

	CustomerTransactionEntity findFirstByCustomerIdOrderByIdDesc(@Param("customer_id") long customerId);

	void deleteByCustomerPaymentId(long id);

	void deleteByBillId(long id);

	@Modifying
	@Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.billId > :billId")
	void updateBillCustomerBalance(@Param("billId") long billId, @Param("amount") double amount);

	@Modifying
	@Query("UPDATE CustomerTransactionEntity t SET t.customerBalance = t.customerBalance + :amount WHERE t.customerPaymentId > :paymentId")
	void updatePaymentCustomerBalance(@Param("paymentId") long paymentId, @Param("amount") double amount);

}
