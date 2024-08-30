package Entidades.Moves;

import Enums.Ailment;
import Enums.MoveType;

import java.util.ArrayList;

public class MoveInfo {
    private Ailment ailment;
    private MoveType moveCategory;
    private int minHits;
    private int maxHits;
    private int min_turns;
    private int max_turns;
    private int drain;
    private int healing;
    private int critRate;
    private int ailmentChance;
    private int flinchChance;
    private int statChance;
    private ArrayList<Integer> statChanges;

    public MoveInfo(Ailment ailment, MoveType moveCategory, int minHits, int maxHits, int min_turns, int max_turns, int drain, int healing, int critRate, int ailmentChance, int flinchChance, int statChance, ArrayList<Integer> statChanges) {
        this.ailment = ailment;
        this.moveCategory = moveCategory;
        this.minHits = minHits;
        this.maxHits = maxHits;
        this.min_turns = min_turns;
        this.max_turns = max_turns;
        this.drain = drain;
        this.healing = healing;
        this.critRate = critRate;
        this.ailmentChance = ailmentChance;
        this.flinchChance = flinchChance;
        this.statChance = statChance;
        this.statChanges = statChanges;
    }

    public ArrayList<Integer> getStatChanges() {
        return statChanges;
    }

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
                moveCategory,
                minHits,
                maxHits,
                min_turns,
                max_turns,
                drain,
                healing,
                critRate,
                ailmentChance,
                flinchChance,
                statChance,
                statChangesStr
        );
    }

    private int getStatChange(int index) {
        return statChanges != null && index < statChanges.size() ? statChanges.get(index) : 0;
    }
}
