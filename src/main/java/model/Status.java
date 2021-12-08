package model;

import java.util.Arrays;

public enum Status {
    AVAILABLE("AVAILABLE"),
    ABSENT("ABSENT"),
    EXPECTED("EXPECTED");

    private final String name;

    Status(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Status getEnumByName(String name) {
        return Arrays.stream(Status.values())
                .filter(e -> e.getName().equals(name.toUpperCase()))
                .findFirst().orElseThrow();
    }
}
