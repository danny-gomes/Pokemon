package Entidades.Items;

import Enums.Ailment;

public class HealingItem extends Item{
    protected int hpHeal;
    protected int ppHeal;
    protected Ailment statusHeal;
    protected boolean isFullHpHeal;
    protected boolean isAllStatus;

    public HealingItem(String name, String description, boolean isSingleUse, boolean isHeld, int hpHeal, int ppHeal, Ailment statusHeal, boolean isFullHpHeal, boolean isAllStatus) {
        super(name, description, isSingleUse, isHeld);
        this.hpHeal = hpHeal;
        this.ppHeal = ppHeal;
        this.statusHeal = statusHeal;
        this.isFullHpHeal = isFullHpHeal;
        this.isAllStatus = isAllStatus;
    }


}
