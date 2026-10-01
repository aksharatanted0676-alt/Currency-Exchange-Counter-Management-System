import java.math.BigDecimal;

public class ExchangeRate {

    private String currencyCode;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

    public ExchangeRate(String currencyCode,
                        BigDecimal buyRate,
                        BigDecimal sellRate)
            throws InvalidRateException {

        if (currencyCode == null || currencyCode.length() != 3) {
            throw new InvalidRateException("Invalid currency code.");
        }

        if (buyRate.compareTo(BigDecimal.ZERO) <= 0 ||
                sellRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRateException("Exchange rate must be greater than zero.");
        }

        this.currencyCode = currencyCode.toUpperCase();
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setBuyRate(BigDecimal buyRate) throws InvalidRateException {
        if (buyRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRateException("Buy rate must be greater than zero.");
        }

        this.buyRate = buyRate;
    }

    public void setSellRate(BigDecimal sellRate) throws InvalidRateException {
        if (sellRate.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidRateException("Sell rate must be greater than zero.");
        }

        this.sellRate = sellRate;
    }

    @Override
    public String toString() {
        return currencyCode +
                " | Buy: ₹" + buyRate +
                " | Sell: ₹" + sellRate;
    }
}