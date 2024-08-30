package Entidades.Moves;

import Entidades.Pokemon;
import Enums.MoveCategory;
import Enums.PokemonType;
import Enums.Target;

import java.util.ArrayList;

public class Move {
    private String name;
    private MoveCategory category;
    private int power;
    private int powerpoints;
    private int accuracy;
    private int effectChance;
    private int priority;
    private String description;
    private MoveInfo moveInfo;
    // Attack, Defense, SpeAttack, SpeDefense, Speed, Evasiness, Accuracy
    private Target target;
    private PokemonType type;

    public Move(String name, MoveCategory category, int power, int powerpoints, int accuracy, int effectChance, int priority, String description, MoveInfo moveInfo, Target target, PokemonType type) {
        this.name = name;
        this.category = category;
        this.power = power;
        this.powerpoints = powerpoints;
        this.accuracy = accuracy;
        this.effectChance = effectChance;
        this.priority = priority;
        this.description = description;
        this.moveInfo = moveInfo;
        this.target = target;
        this.type = type;
    }

    public String getName() {
        return this.name;
    }

    public int getPower() {
        return power;
    }

    public int getPriority() {
        return this.priority;
    }

    @Override
    public String toString() {
        return String.format(
                "==================== Move ====================\n" +
                        "Name: %s\n" +
                        "Category: %s\n" +
                        "Power: %d\n" +
                        "Powerpoints: %d\n" +
                        "Accuracy: %d\n" +
                        "Effect Chance: %d\n" +
                        "Priority: %d\n" +
                        "Description: %s\n" +
                        "Target: %s\n" +
                        "Type: %s\n" +
                        "------------------------------------------------\n" +
                        "Move Info:\n" +
                        "%s",
                name,
                category,
                power,
                powerpoints,
                accuracy,
                effectChance,
                priority,
                description,
                target,
                type,
                moveInfo != null ? moveInfo.toString() : "No additional information."
        );
    }

    public MoveCategory getCategory() {
        return category;
    }

    public MoveInfo getMoveInfo() {
        return moveInfo;
    }
}


