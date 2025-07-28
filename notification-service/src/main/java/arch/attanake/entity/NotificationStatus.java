package arch.attanake.entity;

import lombok.Getter;

@Getter
public enum NotificationStatus {
    PENDING("Pending"),
    PROCESSING("Processing"),
    SENT("Sent"),
    FAILED("Failed"),
    DELIVERED("Delivered"),
    READ("Read");

    private final String displayName;

    NotificationStatus(String displayName) {
        this.displayName = displayName;
    }


    public static NotificationStatus fromString(String text) {
        for (NotificationStatus status : NotificationStatus.values()) {
            if (status.name().equalsIgnoreCase(text)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No constant with text " + text + " found");
    }
}
