package Entidades.FieldEffects;

import Entidades.Battle;
import Entidades.Moves.Move;
import Entidades.Pokemon;
import Entidades.Trainer;
import Enums.Ailment;
import Enums.PokemonType;

import java.util.ArrayList;

public abstract class FieldEffect {

    protected String name;

    protected Move move;

    public FieldEffect(String name, Move moveUsed) {
        this.name = name;
        this.move = moveUsed;
    }

    public String getName() {
        return name;
    }
}
