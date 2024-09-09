package Entidades.FieldEffects;

import Entidades.Battle;
import Entidades.Moves.Move;
import Entidades.Pokemon;
import Entidades.Trainer;

public class Barrier extends FieldEffect {
    /**
     * Variable that defines the amount of turns a barrier has been on the field.
     */
    protected int turnsOnField;
    /**
     * Variable that defines the max turns a barrier should be on the field.
     */
    protected int maxTurnsOnField;

    /**
     * Constructor that initializes an instace of a barrier.
     * @param name The name of the move.
     * @param moveUsed The move associated with this specific barrier.
     * @param maxTurnsOnField
     */
    public Barrier(String name, Move moveUsed, int maxTurnsOnField) {
        super(name, moveUsed);
        this.turnsOnField = 0;
        this.maxTurnsOnField = maxTurnsOnField;
    }

    /**
     * Method that checks and implements the effect of a barrier present in a Trainers Field Effects.
     * @param trainer The trainer having their barries checked.
     * @param battle The battle which is being checked for field effects.
     */
    public void checkBarriers(Trainer trainer, Battle battle) {
        switch (this.name) {
            case "aurora-veil":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, 2, 0, 2, 0, 0, 0);
                break;
            case "reflect":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, 2, 0, 0, 0, 0, 0);
                break;
            case "light-screen":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, 0, 0, 2, 0, 0, 0);
                break;
        }
    }

    /**
     * Method that updates a barriers turns on the field.
     * @return return true if the barrier has spent more than maxturns on field so it can be removed.
     */
    public boolean updateTurnsOnField() {
        this.turnsOnField++;

        if (this.turnsOnField > maxTurnsOnField) {
            return true;
        }

        return false;
    }

    /**
     * Method taht removes the stats boosted or nerfed by the barrier after it is removed.
     * @param trainer The trainer to have stats updated.
     * @param battle The battle where the stats are set.
     */
    public void removeStats(Trainer trainer, Battle battle) {
        switch (this.name) {
            case "aurora-veil":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, -2, 0, -2, 0, 0, 0);
                break;
            case "reflect":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, -2, 0, 0, 0, 0, 0);
                break;
            case "light-screen":
                battle.getTrainerCurrentPokemon(trainer).updateStatModifiers(0, 0, 0, -2, 0, 0, 0);
                break;
        }
    }
}
