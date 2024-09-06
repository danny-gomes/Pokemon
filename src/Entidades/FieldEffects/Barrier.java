package Entidades.FieldEffects;

import Entidades.Battle;
import Entidades.Moves.Move;
import Entidades.Pokemon;
import Entidades.Trainer;

public class Barrier extends FieldEffect{
    protected int turnsOnField;
    protected int maxTurnsOnField;

    public Barrier(String name, Move moveUsed, int turnsOnField, int maxTurnsOnField) {
        super(name, moveUsed);
        this.turnsOnField = turnsOnField;
        this.maxTurnsOnField = maxTurnsOnField;
    }

    public void checkBarriers(Trainer trainer, Battle battle){
        switch (this.name){
            case "aurora-veil":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,2,0,2,0,0,0);
                }
                break;
            case "reflect":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,2,0,0,0,0,0);
                }
                break;
            case "light-screen":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,0,0,2,0,0,0);
                }
                break;
        }
    }

    public boolean updateTurnsOnField(){
        this.turnsOnField++;

        if(this.turnsOnField > maxTurnsOnField){
            return true;
        }

        return false;
    }

    public void removeStats(Trainer trainer) {
        switch (this.name){
            case "aurora-veil":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,-2,0,-2,0,0,0);
                }
                break;
            case "reflect":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,-2,0,0,0,0,0);
                }
                break;
            case "light-screen":
                for(Pokemon p : trainer.nonFaintedPokemon()){
                    p.updateStatModifiers(0,0,0,-2,0,0,0);
                }
                break;
        }
    }
}
