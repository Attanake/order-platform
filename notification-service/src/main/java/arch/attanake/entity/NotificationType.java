package arch.attanake.entity;

import lombok.Getter;

@Getter
public enum NotificationType {
    EMAIL("Email"),
    SMS("SMS"),
    PUSH("Push"),
    WEBHOOK("Webhook");

    private final String displayName;

    NotificationType(String displayName) {
        this.displayName = displayName;
    }

    public static NotificationType fromString(String text) {
        for (NotificationType type : NotificationType.values()) {
            if (type.name().equalsIgnoreCase(text)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
