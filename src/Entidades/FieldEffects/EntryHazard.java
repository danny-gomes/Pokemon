package Entidades.FieldEffects;

import Entidades.Moves.Move;
import Entidades.Pokemon;
import Enums.Ailment;
import Enums.PokemonType;

public class EntryHazard extends FieldEffect {
    private int layers;

    public EntryHazard(String name, Move moveUsed) {
        super(name, moveUsed);
        this.layers = 0;
    }

    public void checkEntryHazards(Pokemon switchedInPokemon, Pokemon opponentPokemon) {
        String moveName = move.getName();
        int damageDealt = 0;

        switch (moveName) {
            case "spikes":
                int maxHp = switchedInPokemon.getCurrentMaxHp();
                if (switchedInPokemon.getEffectiveness(PokemonType.GROUND) != 0) {
                    if (this.layers == 1) {
                        damageDealt = maxHp / 8;
                    } else if (this.layers == 2) {
                        damageDealt = maxHp * 3 / 16;
                    } else if (this.layers >= 3) {
                        damageDealt = maxHp / 4;
                    }
                }
                if (damageDealt > 0) {
                    switchedInPokemon.displayDamageDealt(damageDealt);
                }

                switchedInPokemon.takeDamage(damageDealt);
                System.out.println("The pokemon is hurt by spikes.");
                break;
            case "sticky-web":
                if (switchedInPokemon.getEffectiveness(PokemonType.GROUND) != 0) {
                    switchedInPokemon.updateStatModifiers(0, 0, 0, 0, -1, 0, 0);
                    System.out.println("The pokemon was caught in a sticky web!");
                }
                break;
            case "stealth-rock":
                double rockWeakness = switchedInPokemon.getEffectiveness(PokemonType.ROCK);
                if (rockWeakness == 0.25) {
                    damageDealt = (int) (damageDealt + switchedInPokemon.getCurrentMaxHp() * 0.0315);
                } else if (rockWeakness == 0.5) {
                    damageDealt = (int) (damageDealt + switchedInPokemon.getCurrentMaxHp() * 0.0625);
                } else if (rockWeakness == 1) {
                    damageDealt = (int) (damageDealt + switchedInPokemon.getCurrentMaxHp() * 0.125);
                } else if (rockWeakness == 2) {
                    damageDealt = (int) (damageDealt + switchedInPokemon.getCurrentMaxHp() * 0.25);
                } else if (rockWeakness == 4) {
                    damageDealt = (int) (damageDealt + switchedInPokemon.getCurrentMaxHp() * 0.5);
                }

                if (damageDealt > 0) {
                    switchedInPokemon.displayDamageDealt(damageDealt);
                }

                switchedInPokemon.takeDamage(damageDealt);
                System.out.println("The pokemon was hurt by stealth rock.");
                break;
            case "toxic-spikes":
                boolean isToxic = false;
                if (this.layers >= 2) {
                    isToxic = true;
                }

                if (!(switchedInPokemon.getEffectiveness(PokemonType.POISON) == 0)) {
                    switchedInPokemon.addAilment(opponentPokemon, isToxic ? Ailment.TOXIC : Ailment.POISON);
                }
                break;
        }
    }

    public void addLayer() {
        this.layers++;
    }
}
