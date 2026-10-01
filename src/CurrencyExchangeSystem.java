import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class CurrencyExchangeSystem {

    // =========================================================
    // COLLECTIONS
    // =========================================================

    // ArrayList -> stores available currencies
    private ArrayList<Currency> currencies = new ArrayList<>();

    // LinkedList -> maintains transaction history
    private LinkedList<Transaction> transactionHistory =
            new LinkedList<>();

    // HashMap -> Currency Code -> Exchange Rate
    private HashMap<String, ExchangeRate> exchangeRates =
            new HashMap<>();

    // TreeMap -> Converted Amount -> Transactions
    private TreeMap<BigDecimal, LinkedList<Transaction>>
            sortedTransactions = new TreeMap<>();

    // Used to generate unique transaction IDs
    private int nextTransactionId = 1;


    // =========================================================
    // CURRENCY CRUD
    // =========================================================

    // CREATE
    public void addCurrency(Currency currency) {

        if (currency == null) {
            throw new IllegalArgumentException(
                    "Currency cannot be null."
            );
        }

        currencies.add(currency);
    }


    // READ
    public ArrayList<Currency> getCurrencies() {

        return currencies;
    }


    // READ
    public void displayCurrencies() {

        System.out.println("\n===== AVAILABLE CURRENCIES =====");

        if (currencies.isEmpty()) {
            System.out.println("No currencies available.");
            return;
        }

        for (Currency currency : currencies) {
            System.out.println(currency);
        }
    }


    // UPDATE
    public void updateCurrency(
            String code,
            String newName) {

        for (Currency currency : currencies) {

            if (currency.getCode()
                    .equalsIgnoreCase(code)) {

                currency.setName(newName);

                System.out.println(
                        "Currency updated successfully."
                );

                return;
            }
        }

        System.out.println(
                "Currency not found."
        );
    }


    // DELETE
    public void deleteCurrency(String code) {

        boolean removed =
                currencies.removeIf(
                        currency ->
                                currency.getCode()
                                        .equalsIgnoreCase(code)
                );

        exchangeRates.remove(
                code.toUpperCase()
        );

        if (removed) {

            System.out.println(
                    "Currency deleted successfully."
            );

        } else {

            System.out.println(
                    "Currency not found."
            );
        }
    }


    // =========================================================
    // EXCHANGE RATE MANAGEMENT
    // =========================================================

    // CREATE
    public void addExchangeRate(
            ExchangeRate rate)
            throws InvalidRateException {

        if (rate == null) {
            throw new InvalidRateException(
                    "Exchange rate cannot be null."
            );
        }

        exchangeRates.put(
                rate.getCurrencyCode(),
                rate
        );
    }


    // READ
    public ExchangeRate getExchangeRate(
            String currencyCode) {

        if (currencyCode == null) {
            return null;
        }

        return exchangeRates.get(
                currencyCode.toUpperCase()
        );
    }


    // READ
    public void displayRates() {

        System.out.println("\n===== EXCHANGE RATES =====");

        if (exchangeRates.isEmpty()) {

            System.out.println(
                    "No exchange rates available."
            );

            return;
        }

        for (ExchangeRate rate :
                exchangeRates.values()) {

            System.out.println(rate);
        }
    }


    // UPDATE
    public void updateExchangeRate(
            String code,
            BigDecimal buyRate,
            BigDecimal sellRate)
            throws InvalidRateException {

        ExchangeRate rate =
                exchangeRates.get(
                        code.toUpperCase()
                );

        if (rate == null) {

            throw new InvalidRateException(
                    "Currency rate not found."
            );
        }

        if (buyRate == null ||
                sellRate == null) {

            throw new InvalidRateException(
                    "Rate cannot be null."
            );
        }

        rate.setBuyRate(buyRate);
        rate.setSellRate(sellRate);

        System.out.println(
                "Exchange rate updated successfully."
        );
    }


    // DELETE
    public void deleteExchangeRate(
            String currencyCode) {

        ExchangeRate removed =
                exchangeRates.remove(
                        currencyCode.toUpperCase()
                );

        if (removed != null) {

            System.out.println(
                    "Exchange rate deleted successfully."
            );

        } else {

            System.out.println(
                    "Exchange rate not found."
            );
        }
    }


    // =========================================================
    // TRANSACTION PROCESSING
    // =========================================================

    public Transaction processTransaction(
            Customer customer,
            String currencyCode,
            String transactionType,
            BigDecimal foreignAmount)
            throws InvalidTransactionException {

        // Validate customer
        if (customer == null) {

            throw new InvalidTransactionException(
                    "Customer cannot be null."
            );
        }


        // Validate currency code
        if (currencyCode == null ||
                currencyCode.length() != 3) {

            throw new InvalidTransactionException(
                    "Invalid currency code."
            );
        }


        // Validate amount
        if (foreignAmount == null ||
                foreignAmount.compareTo(
                        BigDecimal.ZERO
                ) <= 0) {

            throw new InvalidTransactionException(
                    "Amount must be greater than zero."
            );
        }


        // Validate transaction type
        if (transactionType == null ||
                (!transactionType.equalsIgnoreCase("BUY") &&
                        !transactionType.equalsIgnoreCase("SELL"))) {

            throw new InvalidTransactionException(
                    "Transaction type must be BUY or SELL."
            );
        }


        // Find exchange rate
        ExchangeRate rate =
                exchangeRates.get(
                        currencyCode.toUpperCase()
                );


        if (rate == null) {

            throw new InvalidTransactionException(
                    "Currency is not supported."
            );
        }


        // Select appropriate rate
        BigDecimal selectedRate;

        if (transactionType.equalsIgnoreCase("BUY")) {

            // Customer buys foreign currency
            selectedRate =
                    rate.getSellRate();

        } else {

            // Customer sells foreign currency
            selectedRate =
                    rate.getBuyRate();
        }


        // Create transaction
        Transaction transaction =
                new Transaction(
                        nextTransactionId++,
                        customer,
                        currencyCode,
                        transactionType,
                        foreignAmount,
                        selectedRate
                );


        // Add to transaction history
        transactionHistory.add(transaction);


        // Add to TreeMap
        addToSortedTransactions(transaction);


        return transaction;
    }


    // =========================================================
    // TREE MAP
    // =========================================================

    private void addToSortedTransactions(
            Transaction transaction) {

        BigDecimal amount =
                transaction.getConvertedAmount();

        sortedTransactions
                .computeIfAbsent(
                        amount,
                        key -> new LinkedList<>()
                )
                .add(transaction);
    }


    // Display transactions sorted by amount
    public void displayTransactionsSortedByAmount() {

        System.out.println(
                "\n===== TRANSACTIONS SORTED BY AMOUNT ====="
        );

        if (sortedTransactions.isEmpty()) {

            System.out.println(
                    "No transactions available."
            );

            return;
        }

        for (Map.Entry<BigDecimal,
                LinkedList<Transaction>> entry :
                sortedTransactions.entrySet()) {

            for (Transaction transaction :
                    entry.getValue()) {

                System.out.println(transaction);
            }
        }
    }


    // =========================================================
    // TRANSACTION HISTORY
    // =========================================================

    public LinkedList<Transaction>
    getTransactionHistory() {

        return transactionHistory;
    }


    public void displayTransactions() {

        System.out.println(
                "\n===== TRANSACTION HISTORY ====="
        );

        if (transactionHistory.isEmpty()) {

            System.out.println(
                    "No transactions found."
            );

            return;
        }

        for (Transaction transaction :
                transactionHistory) {

            System.out.println(transaction);
        }
    }


    // =========================================================
    // SEARCH BY CURRENCY
    // =========================================================

    public LinkedList<Transaction>
    searchByCurrency(String code) {

        LinkedList<Transaction> results =
                new LinkedList<>();

        if (code == null) {
            return results;
        }

        for (Transaction transaction :
                transactionHistory) {

            if (transaction.getCurrencyCode()
                    .equalsIgnoreCase(code)) {

                results.add(transaction);
            }
        }

        return results;
    }


    // Display search results
    public void displaySearchByCurrency(
            String code) {

        LinkedList<Transaction> results =
                searchByCurrency(code);

        System.out.println(
                "\n===== SEARCH BY CURRENCY ====="
        );

        if (results.isEmpty()) {

            System.out.println(
                    "No transaction found."
            );

            return;
        }

        for (Transaction transaction :
                results) {

            System.out.println(transaction);
        }
    }


    // =========================================================
    // SEARCH BY CUSTOMER
    // =========================================================

    public LinkedList<Transaction>
    searchByCustomer(String customerName) {

        LinkedList<Transaction> results =
                new LinkedList<>();

        if (customerName == null) {
            return results;
        }

        for (Transaction transaction :
                transactionHistory) {

            if (transaction.getCustomer()
                    .getName()
                    .equalsIgnoreCase(customerName)) {

                results.add(transaction);
            }
        }

        return results;
    }


    // Display search results
    public void displaySearchByCustomer(
            String customerName) {

        LinkedList<Transaction> results =
                searchByCustomer(customerName);

        System.out.println(
                "\n===== SEARCH BY CUSTOMER ====="
        );

        if (results.isEmpty()) {

            System.out.println(
                    "No transaction found."
            );

            return;
        }

        for (Transaction transaction :
                results) {

            System.out.println(transaction);
        }
    }


    // =========================================================
    // SORT BY AMOUNT
    // =========================================================

    public LinkedList<Transaction>
    sortByAmount() {

        LinkedList<Transaction> sorted =
                new LinkedList<>(
                        transactionHistory
                );

        sorted.sort(
                Comparator.comparing(
                        Transaction::getConvertedAmount
                )
        );

        return sorted;
    }


    public void displaySortedByAmount() {

        System.out.println(
                "\n===== SORTED BY AMOUNT ====="
        );

        LinkedList<Transaction> sorted =
                sortByAmount();

        if (sorted.isEmpty()) {

            System.out.println(
                    "No transactions available."
            );

            return;
        }

        for (Transaction transaction :
                sorted) {

            System.out.println(transaction);
        }
    }


    // =========================================================
    // SORT BY DATE
    // =========================================================

    public LinkedList<Transaction>
    sortByDate() {

        LinkedList<Transaction> sorted =
                new LinkedList<>(
                        transactionHistory
                );

        sorted.sort(
                Comparator.comparing(
                        Transaction::getDateTime
                )
        );

        return sorted;
    }


    public void displaySortedByDate() {

        System.out.println(
                "\n===== SORTED BY DATE ====="
        );

        LinkedList<Transaction> sorted =
                sortByDate();

        if (sorted.isEmpty()) {

            System.out.println(
                    "No transactions available."
            );

            return;
        }

        for (Transaction transaction :
                sorted) {

            System.out.println(transaction);
        }
    }


    // =========================================================
    // DAILY SUMMARY
    // =========================================================

    public String getDailySummary() {

        LocalDate today =
                LocalDate.now();

        int count = 0;

        BigDecimal total =
                BigDecimal.ZERO;


        for (Transaction transaction :
                transactionHistory) {

            if (transaction.getDateTime()
                    .toLocalDate()
                    .equals(today)) {

                count++;

                total =
                        total.add(
                                transaction
                                        .getConvertedAmount()
                        );
            }
        }


        StringBuilder summary =
                new StringBuilder();

        summary.append(
                "===== DAILY SUMMARY =====\n"
        );

        summary.append(
                "Date: "
        ).append(today).append("\n");

        summary.append(
                "Total Transactions: "
        ).append(count).append("\n");

        summary.append(
                "Total Converted Amount: ₹"
        ).append(total).append("\n");


        return summary.toString();
    }


    public void dailySummary() {

        System.out.println(
                "\n" + getDailySummary()
        );
    }


    // =========================================================
    // EXCHANGE REPORT
    // =========================================================

    public String getExchangeReport() {

        StringBuilder report =
                new StringBuilder();

        report.append(
                "===== EXCHANGE REPORT =====\n"
        );

        report.append(
                "Total Currencies: "
        ).append(currencies.size()).append("\n");

        report.append(
                "Total Exchange Rates: "
        ).append(exchangeRates.size()).append("\n");

        report.append(
                "Total Transactions: "
        ).append(transactionHistory.size()).append("\n");


        BigDecimal total =
                BigDecimal.ZERO;


        for (Transaction transaction :
                transactionHistory) {

            total =
                    total.add(
                            transaction
                                    .getConvertedAmount()
                    );
        }


        report.append(
                "Total Exchanged Amount: ₹"
        ).append(total).append("\n");


        return report.toString();
    }


    public void generateReport() {

        System.out.println(
                "\n" + getExchangeReport()
        );
    }


    // =========================================================
    // FIND TRANSACTION BY ID
    // =========================================================

    public Transaction getTransactionById(
            int transactionId) {

        for (Transaction transaction :
                transactionHistory) {

            if (transaction.getTransactionId()
                    == transactionId) {

                return transaction;
            }
        }

        return null;
    }


    // =========================================================
    // DELETE TRANSACTION
    // =========================================================

    public boolean deleteTransaction(
            int transactionId) {

        Transaction transaction =
                getTransactionById(
                        transactionId
                );

        if (transaction == null) {
            return false;
        }


        transactionHistory.remove(
                transaction
        );


        BigDecimal amount =
                transaction.getConvertedAmount();


        LinkedList<Transaction> transactions =
                sortedTransactions.get(amount);


        if (transactions != null) {

            transactions.remove(transaction);

            if (transactions.isEmpty()) {

                sortedTransactions.remove(amount);
            }
        }


        return true;
    }
}