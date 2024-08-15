package Entidades.AbilitiesStructure;

import Entidades.Battle;

public abstract class AbilityOnEntry extends Ability{

    public AbilityOnEntry(String name, String description) {
        super(name, description);
    }

    public abstract void triggerEffect(Battle battle);
}
