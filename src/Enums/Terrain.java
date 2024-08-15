package Enums;

public enum Terrain {
    MISTY_TERRAIN("Misty Terrain", "Misty Terrain prevents status conditions and reduces damage from Dragon-type moves"),
    GRASSY_TERRAIN("Grassy Terrain", "Grassy Terrain restores HP of Pokémon on the ground each turn and boosts the power of Grass-type moves"),
    ELECTRIC_TERRAIN("Electric Terrain", "Electric Terrain prevents Sleep status and boosts the power of Electric-type moves"),
    PSYCHIC_TERRAIN("Psychic Terrain", "Psychic Terrain prevents priority moves and boosts the power of Psychic-type moves");

    private final String name;
    private final String description;

    Terrain(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + ": " + description;
    }
}
