package com.ya.pf.auditable.wayofpayment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface WayOfPaymentRepository extends JpaRepository<WayOfPaymentEntity, Long> {

	Page<WayOfPaymentEntity> findByNameContaining(String name, Pageable pageable);

	boolean existsByName(String name);

	@Query("SELECT CASE WHEN COUNT(w) = 0 THEN true ELSE false END " +
			"FROM WayOfPaymentEntity w " +
			"WHERE w.id != :id AND " +
			"w.name = :name")
	boolean checkUniquePayment(
			@Param("id") long id,
			@Param("name") String name
	);

}
