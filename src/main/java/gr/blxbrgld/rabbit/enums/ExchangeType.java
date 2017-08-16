package gr.blxbrgld.rabbit.enums;

/**
 * Exchange Type Enumeration
 * @author blxbrgld
 */
public enum ExchangeType {

    DIRECT("direct"),
    TOPIC("topic"),
    FANOUT("fanout"),
    HEADERS("headers");

    private final String code;

    private ExchangeType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static ExchangeType get(final String code) {
        for(final ExchangeType type : ExchangeType.values()) {
            if(type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
