package Enums;

public enum PokemonType {
    NORMAL("Normal"),
    FIRE("Fire"),
    WATER("Water"),
    ELECTRIC("Electric"),
    GRASS("Grass"),
    ICE("Ice"),
    FIGHTING("Fighting"),
    POISON("Poison"),
    GROUND("Ground"),
    FLYING("Flying"),
    PSYCHIC("Psychic"),
    BUG("Bug"),
    ROCK("Rock"),
    GHOST("Ghost"),
    DRAGON("Dragon"),
    DARK("Dark"),
    STEEL("Steel"),
    FAIRY("Fairy");

    private final String displayName;

    // Constructor
    PokemonType(String displayName) {
        this.displayName = displayName;
    }

    // Override toString method
    @Override
    public String toString() {
        return displayName;
    }

    // Static method to get PokemonType from a string
    public static PokemonType typeString(String typeName) {
        for (PokemonType type : PokemonType.values()) {
            if (type.displayName.equalsIgnoreCase(typeName)) {
                return type;
            }
        }
        return null;
    }
}

