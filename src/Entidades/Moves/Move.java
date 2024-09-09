package Entidades.Moves;

import Entidades.Pokemon;
import Enums.MoveCategory;
import Enums.MoveType;
import Enums.PokemonType;
import Enums.Target;

import java.util.ArrayList;
import java.util.Random;

public class Move {
    /**
     * Name of the Move.
     */
    private String name;
    /**
     * Category of the Move (STATUS, PHYSICAL, SPECIAL)
     */
    private MoveCategory category;
    /**
     * Moves base damage.
     */
    private int power;
    /**
     * The moves power points which defines the amount of times it can be used.
     */
    private int powerpoints;
    /**
     * The current moves power points which shows how many usages it has left.
     */
    private int currentPowerPoints;
    /**
     * Moves base accuracy.
     */
    private int accuracy;
    /**
     * The effect chance this move has of triggering its specific effect.
     */
    private int effectChance;
    /**
     * The moves base priority.
     */
    private int priority;
    /**
     * The moves descriptiong.
     */
    private String description;
    /**
     * The moves extra info and will be stored in this object.
     */
    private MoveInfo moveInfo;
    /**
     * Who the move will target.
     */
    private Target target;
    /**
     * The type of the move.
     */
    private PokemonType type;
    /**
     * If the move is a charge move.
     */
    private boolean isChargeMove;
    /**
     * If the move is a recharge move.
     */
    private boolean isRechargeMove;
    /**
     * If the move gives an evasion boost on the charge turn.
     */
    private boolean evasionOnCharge;

    /**
     * Constructor that creates a Pokemon move. Move Info object is necessary prior to its creation.
     */
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

    /**
     * Method that returns the name of the move.
     * @return the name of the move.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Method that returns the base damage of the move.
     * @return the base damage of the move.
     */
    public int getPower() {
        return power;
    }

    /**
     * Method that returns the priority of the move.
     * @return the priority of the move.
     */
    public int getPriority() {
        return this.priority;
    }

    /**
     * Method that retuns the moves info in String format.
     * @return the moves info in a string.
     */
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

    /**
     * Returns the category of a move.
     * @return The category of a move.
     */
    public MoveCategory getCategory() {
        return category;
    }

    /**
     * Method that returns the MoveInfo object of a move.
     * @return the MoveInfo object of a move.
     */
    public MoveInfo getMoveInfo() {
        return moveInfo;
    }

    /**
     * Returns the moves Type, move type generally defines what a move does.
     * @return The move type.
     */
    public MoveType getMoveType(){
        return moveInfo.getMoveType();
    }

    /**
     * Return the move description.
     * @return The moves description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns the effect chance for this moves effect.
     * @return the effect chance.
     */
    public int getEffectChance() {
        return effectChance;
    }

    /**
     * Method that checks if this instance of a move should crit taking into account MoveInfo crit rate.
     * @return 2 if crit, 1 if regular hit. Method returns 2 or 1 instead of boolean for damage calculation formula.
     */
    public int getCrit() {
        boolean isCrit = false;
        int critRate = this.moveInfo.getCritChance();

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

    /**
     * Return the type of the move (Fire, Grass, Water etc...)
     * @return the move type.
     */
    public PokemonType getType() {
        return type;
    }

    /**
     * Method that retuns the accuracy of the move.
     * @return the accuracy of the move.
     */
    public int getAccuracy() {
        return accuracy;
    }

    /**
     * Method that sets if the move is a charge move.
     * @param chargeMove value to set for isChargeMove.
     */
    public void setChargeMove(boolean chargeMove) {
        isChargeMove = chargeMove;
    }

    /**
     * Method that sets if the move is a recharge move.
     * @param rechargeMove the value to set to isRechargeMove.
     */
    public void setRechargeMove(boolean rechargeMove) {
        isRechargeMove = rechargeMove;
    }

    /**
     * Method that defines if evasion raises on charge turn.
     * @param evasionOnCharge value to set evasionOnCharge to.
     */
    public void setEvasionOnCharge(boolean evasionOnCharge) {
        this.evasionOnCharge = evasionOnCharge;
    }

    /**
     * Method that gets the drain attribute from moveInfo.
     * @return the moves drain.
     */
    public int getDrain(){
        return moveInfo.getDrain();
    }

    /**
     * Method that returns the moves target.
     * @return the moves target.
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Returns the moves PP.
     * @return the moves pp.
     */
    public int getPP() {
        return this.powerpoints;
    }

    /**
     * Method that returns the moves current powerpoints.
     * @return the moves current powerpoints.
     */
    public int getCurrentPowerPoints() {
        return currentPowerPoints;
    }
}


