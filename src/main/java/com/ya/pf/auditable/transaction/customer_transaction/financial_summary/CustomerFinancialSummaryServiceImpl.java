package com.ya.pf.auditable.transaction.customer_transaction.financial_summary;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.sql.init.dependency.DependsOnDatabaseInitialization;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDate;

@DependsOnDatabaseInitialization
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CustomerFinancialSummaryServiceImpl implements CustomerFinancialSummaryService {

    private final DataSource dataSource;

    @Override
    public CustomerFinancialSummary getCustomerFinancialSummary(long customerId, Integer productId, Integer paymentMethodId, LocalDate start, LocalDate end) throws SQLException {

        String statement = """
                SELECT SUM(payment_amount) AS totalPayments, SUM(bill_customer_amount) AS totalBills, SUM(bill_quantity) AS totalLiters
                FROM customer_transaction_view
                WHERE customer_id = ? AND date >= ? AND date <= ?
                """;
        if (productId != null) {
            statement += " AND product_id = ?";
        }
        if (paymentMethodId != null) {
            statement += " AND payment_method_id = ?";
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(statement)) {
            preparedStatement.setLong(1, customerId);
            preparedStatement.setDate(2, Date.valueOf(start));
            preparedStatement.setDate(3, Date.valueOf(end));
            if (productId != null) {
                preparedStatement.setInt(4, productId);
            }
            if (paymentMethodId != null) {
                preparedStatement.setInt(5, paymentMethodId);
            }
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Float totalPayments = resultSet.getFloat("totalPayments");
                Float totalBills = resultSet.getFloat("totalBills");
                Float totalLiters = resultSet.getFloat("totalLiters");
                return new CustomerFinancialSummary(totalPayments, totalBills, totalLiters);
            }
        }

        return null;
    }

}
