package Entidades.AbilitiesStructure.Abilities;

import Entidades.AbilitiesStructure.AbilityOnEntry;
import Entidades.Battle;
import Enums.Weather;

public class AirLock extends AbilityOnEntry {

    public AirLock(String name, String description) {
        super(name, description);
    }

    @Override
    public void triggerEffect(Battle battle) {
        battle.setWeather(Weather.NONE);
    }
}
