import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        try {

            CurrencyExchangeSystem system =
                    new CurrencyExchangeSystem();

            // Add currencies
            system.addCurrency(
                    new Currency("USD", "US Dollar")
            );

            system.addCurrency(
                    new Currency("EUR", "Euro")
            );

            system.addCurrency(
                    new Currency("GBP", "British Pound")
            );

            // Add exchange rates
            system.addExchangeRate(
                    new ExchangeRate(
                            "USD",
                            new BigDecimal("82.00"),
                            new BigDecimal("83.00")
                    )
            );

            system.addExchangeRate(
                    new ExchangeRate(
                            "EUR",
                            new BigDecimal("89.00"),
                            new BigDecimal("91.00")
                    )
            );

            system.addExchangeRate(
                    new ExchangeRate(
                            "GBP",
                            new BigDecimal("103.00"),
                            new BigDecimal("105.00")
                    )
            );

            // Display
            system.displayCurrencies();
            system.displayRates();

            // Customers
            Customer customer1 =
                    new Customer(
                            101,
                            "Akshara",
                            "9876543210"
                    );

            Customer customer2 =
                    new Customer(
                            102,
                            "Rahul",
                            "9876501234"
                    );

            // BUY USD
            Transaction t1 =
                    system.processTransaction(
                            customer1,
                            "USD",
                            "BUY",
                            new BigDecimal("100")
                    );

            // SELL EUR
            Transaction t2 =
                    system.processTransaction(
                            customer2,
                            "EUR",
                            "SELL",
                            new BigDecimal("200")
                    );

            System.out.println(
                    t1.generateReceipt()
            );

            System.out.println(
                    t2.generateReceipt()
            );

            // Transaction history
            system.displayTransactions();

            // Search
            system.searchByCurrency("USD");

            system.searchByCustomer("Rahul");

            // Sorting
            system.sortByAmount();

            system.sortByDate();

            // Summary
            system.dailySummary();

            // Report
            system.generateReport();

        } catch (Exception e) {

            System.out.println(
                    "Error: " + e.getMessage()
            );
        }
    }
}