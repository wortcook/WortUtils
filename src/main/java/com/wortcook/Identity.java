package com.wortcook;

import java.util.UUID;

public record Identity(UUID id, UUID parentId) {

    public static final UUID ROOT_UUID = new UUID(0, 0);
    public static final Identity ROOT = new Identity(ROOT_UUID, ROOT_UUID);

    public Identity(final UUID id) {
        this(id, ROOT_UUID);
    }

    public Identity() {
        this(UUID.randomUUID(), ROOT_UUID);
    }

    public Identity(final UUID id, final Identity parent) {
        this(id, parent.id());
    }

    public Identity(final UUID id, final UUID parentId) {
        assert id != null;
        assert parentId != null;
        this.id = id;
        this.parentId = parentId;
    }


    public boolean isRoot(){
        return ROOT.equals(this);
    }
}
