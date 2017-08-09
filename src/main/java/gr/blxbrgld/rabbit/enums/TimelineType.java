package gr.blxbrgld.rabbit.enums;

/**
 * Twitter Timeline Type Enumeration
 * @author blxbrgld
 */
public enum TimelineType {

    HOME("Home"),
    USER("User"),
    MENTIONS("Mentions"),
    FAVORITES("Favorites");

    private final String code;

    private TimelineType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public static TimelineType get(final String code) {
        for(final TimelineType type : TimelineType.values()) {
            if(type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        return null;
    }
}
