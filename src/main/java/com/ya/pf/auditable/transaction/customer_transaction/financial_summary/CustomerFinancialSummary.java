package com.ya.pf.auditable.transaction.customer_transaction.financial_summary;

public record CustomerFinancialSummary(
        Float totalPayments,
        Float totalBills,
        Float totalLiters
) {

}
