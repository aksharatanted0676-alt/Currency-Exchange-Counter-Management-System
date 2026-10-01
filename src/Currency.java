public class Currency {

    private String code;
    private String name;

    public Currency(String code, String name) {
        if (code == null || code.length() != 3) {
            throw new IllegalArgumentException("Currency code must contain exactly 3 characters.");
        }

        this.code = code.toUpperCase();
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return code + " - " + name;
    }
}