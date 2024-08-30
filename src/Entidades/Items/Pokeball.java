package Entidades.Items;

public class Pokeball extends Item{
    protected double boostCatch;

    public Pokeball(String name, String description, boolean isSingleUse, boolean isHeld, double boostCatch) {
        super(name, description, isSingleUse, isHeld);
        this.boostCatch = boostCatch;
    }


}
