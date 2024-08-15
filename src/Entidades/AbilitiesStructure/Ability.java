package Entidades.AbilitiesStructure;

public abstract class Ability {
    protected String name;
    protected String description;

    public Ability(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
