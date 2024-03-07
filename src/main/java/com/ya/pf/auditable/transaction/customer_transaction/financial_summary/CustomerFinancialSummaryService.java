package com.ya.pf.auditable.transaction.customer_transaction.financial_summary;

import java.sql.SQLException;
import java.time.LocalDate;

public interface CustomerFinancialSummaryService {

    CustomerFinancialSummary getCustomerFinancialSummary(long customerId, Integer productId, Integer paymentMethodId, LocalDate start, LocalDate end) throws SQLException;

}
