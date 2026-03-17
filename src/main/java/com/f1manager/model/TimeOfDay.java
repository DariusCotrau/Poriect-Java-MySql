package com.f1manager.model;

public enum TimeOfDay {
    DAY("Zi"),
    SUNSET("Apus"),
    NIGHT("Noapte");

    private final String label;

    TimeOfDay(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
