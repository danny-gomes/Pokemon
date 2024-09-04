package Entidades.Moves;

import Entidades.Pokemon;
import Enums.MoveCategory;
import Enums.MoveType;
import Enums.PokemonType;
import Enums.Target;

import java.util.ArrayList;
import java.util.Random;

public class Move {
    private String name;
    private MoveCategory category;
    private int power;
    private int powerpoints;
    private int currentPowerPoints;
    private int accuracy;
    private int effectChance;
    private int priority;
    private String description;
    private MoveInfo moveInfo;
    // Attack, Defense, SpeAttack, SpeDefense, Speed, Evasiness, Accuracy
    private Target target;
    private PokemonType type;
    private boolean isChargeMove;
    private boolean isRechargeMove;
    private boolean evasionOnCharge;

    public Move(String name, MoveCategory category, int power, int powerpoints, int accuracy, int effectChance, int priority, String description, MoveInfo moveInfo, Target target, PokemonType type) {
        this.name = name;
        this.category = category;
        this.power = power;
        this.powerpoints = powerpoints;
        this.currentPowerPoints = powerpoints;
        this.accuracy = accuracy;
        this.effectChance = effectChance;
        this.priority = priority;
        this.description = description;
        this.moveInfo = moveInfo;
        this.target = target;
        this.type = type;
        this.isChargeMove = false;
        this.isRechargeMove = false;

        if(this.description.contains("recharge")){
            this.isRechargeMove = true;
            this.isChargeMove = false;
        } else if(this.description.contains("charge")){
            this.isChargeMove = true;
            this.isRechargeMove = false;
        } else {
            this.isRechargeMove = false;
            this.isChargeMove = false;
        }
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

    public MoveType getMoveType(){
        return moveInfo.getMoveType();
    }

    public String getDescription() {
        return description;
    }

    public int getEffectChance() {
        return effectChance;
    }

    public int getCrit() {
        boolean isCrit = false;
        int critRate = this.moveInfo.getCritChance(); // Assuming getCritChance() returns the crit stage

        Random random = new Random();

        int rand = random.nextInt(1000);

        switch(critRate) {
            case 0:
                if(rand < 42) {
                    isCrit = true;
                }
                break;
            case 1:
                if(rand < 125){
                    isCrit = true;
                }
                break;
            case 2:
                if(rand < 500){
                    isCrit = true;
                }
                break;
            case 3:
                isCrit = true;
                break;
            default:
                isCrit = true;
                break;
        }

        return isCrit ? 2 : 1;
    }

    public PokemonType getType() {
        return type;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public void setChargeMove(boolean chargeMove) {
        isChargeMove = chargeMove;
    }

    public void setRechargeMove(boolean rechargeMove) {
        isRechargeMove = rechargeMove;
    }

    public void setEvasionOnCharge(boolean evasionOnCharge) {
        this.evasionOnCharge = evasionOnCharge;
    }

    public int getDrain(){
        return moveInfo.getDrain();
    }

    public Target getTarget() {
        return target;
    }

    public int getPP() {
        return this.powerpoints;
    }

    public int getCurrentPowerPoints() {
        return currentPowerPoints;
    }
}


