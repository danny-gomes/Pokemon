package Enums;

public enum Nature {
    ADAMANT (1.1, 1.0, 0.9, 1.0, 1.0),
    BASHFUL (1.0, 1.0, 1.0, 1.0, 1.0),
    BOLD    (0.9, 1.1, 1.0, 1.0, 1.0),
    BRAVE   (1.1, 1.0, 1.0, 1.0, 0.9),
    CALM    (1.0, 1.0, 1.0, 1.1, 0.9),
    CAREFUL (1.0, 1.0, 0.9, 1.1, 0.9),
    DOCILE  (1.0, 1.0, 1.0, 1.0, 1.0),
    GENTLE  (1.0, 0.9, 1.0, 1.1, 0.9),
    HARDY   (1.0, 1.0, 1.0, 1.0, 1.0),
    HASTY   (1.0, 0.9, 1.0, 1.0, 1.1),
    IMPISH  (0.9, 1.1, 0.9, 1.0, 1.0),
    JOLLY   (1.0, 1.0, 0.9, 1.0, 1.1),
    LAX     (1.0, 1.1, 1.0, 0.9, 1.1),
    LONELY  (1.1, 0.9, 1.0, 1.0, 1.0),
    MODEST  (0.9, 1.0, 1.1, 1.0, 1.0),
    MILD    (1.0, 0.9, 1.1, 1.0, 1.0),
    NAIVE   (1.0, 1.0, 0.9, 0.9, 1.1),
    NAUGHTY (1.1, 1.0, 1.0, 0.9, 1.0),
    QUIET   (1.0, 1.0, 1.1, 1.0, 0.9),
    QUIRKY  (1.0, 1.0, 1.0, 1.0, 1.0),
    RASH    (1.0, 1.0, 1.1, 1.1, 1.0),
    RELAXED (1.0, 1.1, 1.0, 1.0, 0.9),
    SASSY   (1.0, 1.0, 1.0, 1.1, 0.9),
    SERIOUS (1.0, 1.0, 1.0, 1.0, 1.0),
    TIMID   (0.9, 1.0, 1.0, 1.0, 1.1);

    private final double attackMultiplier;
    private final double defenseMultiplier;
    private final double specialAttackMultiplier;
    private final double specialDefenseMultiplier;
    private final double speedMultiplier;

    Nature(double attackMultiplier, double defenseMultiplier, double specialAttackMultiplier, double specialDefenseMultiplier, double speedMultiplier) {
        this.attackMultiplier = attackMultiplier;
        this.defenseMultiplier = defenseMultiplier;
        this.specialAttackMultiplier = specialAttackMultiplier;
        this.specialDefenseMultiplier = specialDefenseMultiplier;
        this.speedMultiplier = speedMultiplier;
    }

    public double getAttackMultiplier() {
        return attackMultiplier;
    }

    public double getDefenseMultiplier() {
        return defenseMultiplier;
    }

    public double getSpecialAttackMultiplier() {
        return specialAttackMultiplier;
    }

    public double getSpecialDefenseMultiplier() {
        return specialDefenseMultiplier;
    }

    public double getSpeedMultiplier() {
        return speedMultiplier;
    }
}
