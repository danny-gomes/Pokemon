package Enums;

public enum WeatherEffect {
    NONE("None", "No weather effect"),
    SUNLIGHT("Sunlight", "Sunlight intensifies the power of Fire-type moves and weakens Water-type moves"),
    RAIN("Rain", "Rain increases the power of Water-type moves and weakens Fire-type moves"),
    SANDSTORM("Sandstorm", "Sandstorm damages all Pokémon that are not Rock-, Ground-, or Steel-type each turn"),
    HAIL("Hail", "Hail damages all Pokémon that are not Ice-type each turn");

    private final String name;
    private final String description;

    WeatherEffect(String name, String description) {
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