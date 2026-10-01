import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transaction {

    private int transactionId;
    private Customer customer;
    private String currencyCode;
    private String transactionType;
    private BigDecimal foreignAmount;
    private BigDecimal exchangeRate;
    private BigDecimal convertedAmount;
    private LocalDateTime dateTime;

    public Transaction(int transactionId,
                       Customer customer,
                       String currencyCode,
                       String transactionType,
                       BigDecimal foreignAmount,
                       BigDecimal exchangeRate)
            throws InvalidTransactionException {

        if (customer == null) {
            throw new InvalidTransactionException("Customer cannot be null.");
        }

        if (foreignAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException(
                    "Amount must be greater than zero."
            );
        }

        if (!transactionType.equalsIgnoreCase("BUY") &&
                !transactionType.equalsIgnoreCase("SELL")) {

            throw new InvalidTransactionException(
                    "Transaction type must be BUY or SELL."
            );
        }

        this.transactionId = transactionId;
        this.customer = customer;
        this.currencyCode = currencyCode.toUpperCase();
        this.transactionType = transactionType.toUpperCase();
        this.foreignAmount = foreignAmount;
        this.exchangeRate = exchangeRate;

        this.convertedAmount =
                foreignAmount.multiply(exchangeRate);

        this.dateTime = LocalDateTime.now();
    }

    public int getTransactionId() {
        return transactionId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getForeignAmount() {
        return foreignAmount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public BigDecimal getConvertedAmount() {
        return convertedAmount;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String generateReceipt() {

        StringBuilder receipt = new StringBuilder();

        receipt.append("\n");
        receipt.append("====================================\n");
        receipt.append("      CURRENCY EXCHANGE RECEIPT     \n");
        receipt.append("====================================\n");

        receipt.append("Transaction ID : ")
                .append(transactionId)
                .append("\n");

        receipt.append("Customer       : ")
                .append(customer.getName())
                .append("\n");

        receipt.append("Currency       : ")
                .append(currencyCode)
                .append("\n");

        receipt.append("Transaction    : ")
                .append(transactionType)
                .append("\n");

        receipt.append("Foreign Amount : ")
                .append(foreignAmount)
                .append("\n");

        receipt.append("Exchange Rate  : ₹")
                .append(exchangeRate)
                .append("\n");

        receipt.append("Converted      : ₹")
                .append(convertedAmount)
                .append("\n");

        receipt.append("Date & Time    : ")
                .append(dateTime)
                .append("\n");

        receipt.append("====================================\n");

        return receipt.toString();
    }

    @Override
    public String toString() {

        return "ID: " + transactionId +
                " | Customer: " + customer.getName() +
                " | Currency: " + currencyCode +
                " | Type: " + transactionType +
                " | Foreign Amount: " + foreignAmount +
                " | Converted: ₹" + convertedAmount +
                " | Date: " + dateTime;
    }
}