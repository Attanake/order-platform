package arch.attanake.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    NEW(true),
    PROCESSING(false),
    SHIPPED(false),
    COMPLETED(false),
    CANCELLED(false);

    private final boolean cancellable;

    OrderStatus(boolean cancellable) {
        this.cancellable = cancellable;
    }

    public boolean isCancellable() {
        return cancellable;
    }
}
