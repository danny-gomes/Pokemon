package Entidades.Moves;

import Enums.Ailment;
import Enums.MoveType;

import java.util.ArrayList;
import java.util.Random;

public class MoveInfo {
    /**
     * The ailment this move applies, if any.
     */
    private Ailment ailment;
    /**\
     * The moves Move Type, move type defines a moves general behaviour.
     */
    private MoveType moveType;
    /**
     * The minimum amount of hits a move hits in one turn.
     */
    private int minHits;
    /**
     * The maximum amount of hits a move hits in one turn.
     */
    private int maxHits;
    /**
     * The minimum amount of turns a move lasts.
     */
    private int minTurns;
    /**
     * The max amount of turns a move lasts.
     */
    private int maxTurns;
    /**
     * The drain of a move when damage is dealt, it can be positive if the move heals the user, or negative if the move has recoil.
     */
    private int drain;
    /**
     * The moves healing
     */
    private int healing;
    /**
     * Moves crit rate.
     */
    private int critRate;
    /**
     * The chance to apply the ailment above.
     */
    private int ailmentChance;
    /**
     * The moves chance to make opponent flinch.
     */
    private int flinchChance;
    /**
     * The chance this move has to apply its stat changes.
     */
    private int statChance;
    /**
     * The stat changes this move can boost or nerf.
     */
    private ArrayList<Integer> statChanges;

    /**
     * Constructor to initializae a MoveInfo object.
     */
    public MoveInfo(Ailment ailment, MoveType moveCategory, int minHits, int maxHits, int min_turns, int max_turns, int drain, int healing, int critRate, int ailmentChance, int flinchChance, int statChance, ArrayList<Integer> statChanges) {
        this.ailment = ailment;
        this.moveType = moveCategory;
        this.minHits = minHits;
        this.maxHits = maxHits;
        this.minTurns = min_turns;
        this.maxTurns = max_turns;
        this.drain = drain;
        this.healing = healing;
        this.critRate = critRate;
        this.ailmentChance = ailmentChance;
        this.flinchChance = flinchChance;
        this.statChance = statChance;
        this.statChanges = statChanges;
    }

    /**
     * Returns the moves potential stat changes.
     * @return list with the moves stat changes 0 - attack 1 - defense etc...
     */
    public ArrayList<Integer> getStatChanges() {
        return statChanges;
    }

    /**
     * Converts moveInfo object info displayed into a string.
     * @return the string with MoveInfo information.
     */
    @Override
    public String toString() {
        String statChangesStr = statChanges != null ?
                String.format(
                        "  Attack Change: %+d\n" +
                                "  Defense Change: %+d\n" +
                                "  Sp. Attack Change: %+d\n" +
                                "  Sp. Defense Change: %+d\n" +
                                "  Speed Change: %+d\n" +
                                "  Accuracy Change: %+d\n" +
                                "  Evasiness Change: %+d",
                        getStatChange(0),
                        getStatChange(1),
                        getStatChange(2),
                        getStatChange(3),
                        getStatChange(4),
                        getStatChange(5),
                        getStatChange(6)
                ) :
                "No stat changes.";

        return String.format(
                "  Ailment: %-20s\n" +
                        "  Move Category: %-20s\n" +
                        "  Hits: %d-%d\n" +
                        "  Turns: %d-%d\n" +
                        "  Drain: %d\n" +
                        "  Healing: %d\n" +
                        "  Crit Rate: %d\n" +
                        "  Ailment Chance: %d\n" +
                        "  Flinch Chance: %d\n" +
                        "  Stat Chance: %d\n" +
                        "%s",
                ailment,
                moveType,
                minHits,
                maxHits,
                minTurns,
                maxTurns,
                drain,
                healing,
                critRate,
                ailmentChance,
                flinchChance,
                statChance,
                statChangesStr
        );
    }

    /**
     * Method that returns the stat change for a given stat, can be 0 for no change, positive for stat raise, negative for stat lower.
     * @param index the index of the stat to check if it has a change.
     * @return the stat change for that given stat 0 - attack 1- defense etc...
     */
    public int getStatChange(int index) {
        return statChanges != null && index < statChanges.size() ? statChanges.get(index) : 0;
    }

    /**
     * The moves moveType
     * @return the moveType
     */
    public MoveType getMoveType() {
        return moveType;
    }

    /**
     * Method that returns the crit chance
     * @return the move crit chance
     */
    public int getCritChance() {
        return this.critRate;
    }

    /**
     * Returns the moves min hits.
     * @return The moves min hits.
     */
    public int getMinHits() {
        return minHits;
    }

    /**
     * Returns the moves max hits.
     * @return
     */
    public int getMaxHits() {
        return maxHits;
    }

    /**
     * Returns the moves drain
     * @return the moves drain
     */
    public int getDrain() {
        return drain;
    }

    /**
     * Returns the moves chance to change stats.
     * @return the moves chance to change stats.
     */
    public int getStatChance() {
        return statChance;
    }

    /**
     * Method that checks if move flinches opponent.
     * @return true if the move makes the opponent flinch, false if not.
     */
    public boolean isFlinch() {
        Random rd = new Random();
        int randomNumber = rd.nextInt(101);

        if(randomNumber < this.flinchChance){
            return true;
        }

        return false;
    }

    /**
     * Gets the ailment that this move can apply.
     * @return the ailment to apply.
     */
    public Ailment getAilment() {
        return ailment;
    }

    /**
     * Returns the chance of applying this moves ailment.
     * @return the ailment this move can apply.
     */
    public int getAilmentChance() {
        return ailmentChance;
    }
}
