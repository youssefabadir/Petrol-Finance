package com.ya.pf.auditable.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

	Page<PaymentEntity> findByNumberContaining(String receiptNumber, Pageable pageable);

	@Query("SELECT CASE WHEN COUNT(p) = 0 THEN true ELSE false END " +
			"FROM PaymentEntity p " +
			"WHERE p.id != :id AND " +
			"p.number = :number AND " +
			"p.paymentMethodEntity.id = :wayOfPaymentId")
	boolean checkUniquePayment(
			@Param("id") long id,
			@Param("number") String number,
			@Param("wayOfPaymentId") long wayOfPaymentId
	);

	boolean existsByNumberAndPaymentMethodEntity_Id(String receiptNumber, long id);

}
