package Entidades.FieldEffects;

import Entidades.Battle;
import Entidades.Moves.Move;
import Entidades.Pokemon;
import Entidades.Trainer;
import Enums.Ailment;
import Enums.PokemonType;

import java.util.ArrayList;

public abstract class FieldEffect {
    /**
     * Name of a generic Field Effect
     */
    protected String name;
    /**
     * The move associated with a specific Field Effect.
     */
    protected Move move;

    /**
     * Constructor to create a Field Effect.
     * @param name The name of the Field Effect.
     * @param moveUsed The move associated with the Field Effect.
     */
    public FieldEffect(String name, Move moveUsed) {
        this.name = name;
        this.move = moveUsed;
    }

    /**
     * Method that gets the name of a specific Field Effect.
     * @return The name of the Field Effect.
     */
    public String getName() {
        return name;
    }
}
