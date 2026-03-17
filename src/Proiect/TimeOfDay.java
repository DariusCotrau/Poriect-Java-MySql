package Proiect;

public enum TimeOfDay {
    DAY("Zi"),
    SUNSET("Apus"),
    NIGHT("Noapte");

    private final String label;

    TimeOfDay(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
