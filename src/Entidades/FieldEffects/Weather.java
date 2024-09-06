package Entidades.FieldEffects;

import Entidades.Moves.Move;

public class Weather extends FieldEffect {
    protected int turnsOnField;
    protected int maxTurnsOnField;

    public Weather(String name, Move moveUsed, int turnsOnField, int maxTurnsOnField) {
        super(name, moveUsed);
        this.turnsOnField = turnsOnField;
        this.maxTurnsOnField = maxTurnsOnField;
    }


}
