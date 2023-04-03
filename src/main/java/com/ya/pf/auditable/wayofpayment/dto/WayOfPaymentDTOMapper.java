package com.ya.pf.auditable.wayofpayment.dto;

import com.ya.pf.auditable.wayofpayment.WayOfPaymentEntity;
import org.springframework.stereotype.Service;

import java.util.function.Function;

@Service
public class WayOfPaymentDTOMapper implements Function<WayOfPaymentEntity, WayOfPaymentDTO> {

	@Override
	public WayOfPaymentDTO apply(WayOfPaymentEntity wayOfPaymentEntity) {

		return new WayOfPaymentDTO(wayOfPaymentEntity.getId(), wayOfPaymentEntity.getName());
	}

}
