package org.sid.accountserviceaxon.common.events;

import lombok.Getter;

public abstract class BaseEvent<T> {
    @Getter
    private final T id;

    protected BaseEvent(T id) {
        this.id = id;
    }
}
