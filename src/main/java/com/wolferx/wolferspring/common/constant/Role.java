package com.wolferx.wolferspring.common.constant;

public enum Role {

    USER("ROLE_USER", 1),
    ADMIN("ROLE_ADMIN", 2);

    private final String name;
    private final Integer value;

    private Role(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public boolean equalsName(String otherName) {
        return otherName != null && name.equals(otherName);
    }

    public Integer getValue() {
        return this.value;
    }

    public String toString() {
        return this.name;
    }

}
