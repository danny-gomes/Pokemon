package Entidades.Items;

public abstract class Item {
    protected String name;
    protected String description;
    protected boolean isSingleUse;
    protected boolean isHeld;

    public Item(String name, String description, boolean isSingleUse, boolean isHeld) {
        this.name = name;
        this.description = description;
        this.isSingleUse = isSingleUse;
        this.isHeld = isHeld;
    }
}
