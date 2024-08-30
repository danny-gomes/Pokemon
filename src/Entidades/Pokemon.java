package Entidades;

import Entidades.AbilitiesStructure.Ability;
import Entidades.Items.Item;
import Entidades.Moves.Move;
import Enums.Nature;
import Enums.PokemonType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Pokemon {
    private int dexNumber;
    private String name;
    private PokemonType type1;
    private PokemonType type2;
    private int level;
    private Nature nature;
    private int baseHp;
    private int currentMaxHp;
    private int currentHp;
    private int attack;
    private int baseAttack;
    private int defense;
    private int baseDefense;
    private int specialAttack;
    private int baseSpecialAttack;
    private int specialDefense;
    private int baseSpecialDefense;
    private int speed;
    private int baseSpeed;
    private Ability ability;
    private ArrayList<String> possibleAbilitiesString;
    private String hiddenAbilityString;
    private double height;
    private double weight;
    private int evolvesAt;
    private ArrayList<Pokemon> possibleEvolutions;
    private double[] statModifiers;
    public static final int ATTACK = 0;
    public static final int DEFENSE = 1;
    public static final int SPECIAL_ATTACK = 2;
    public static final int SPECIAL_DEFENSE = 3;
    public static final int SPEED = 4;
    public static final int ACCURACY = 5;
    public static final int EVASIVENESS = 6;
    private StatusEffect statusEffect;
    private boolean isShiny;
    private Item heldItem;
    private Move[] currentMoves;
    private ArrayList<Move> learnSet;

    public Pokemon(int dexNumber, String name, PokemonType type1, PokemonType type2, int level, int baseHp, int baseAttack, int baseDefense, int baseSpecialAttack, int baseSpecialDefense, int baseSpeed, ArrayList<String> possibleAbilitiesString, String hiddenAbilityString, double height, double weight, int evolvesAt, Move[] moves, ArrayList<Move> learnSet) {
        this.dexNumber = dexNumber;
        this.name = name;
        this.type1 = type1;
        this.type2 = type2;
        this.level = level;
        this.baseHp = baseHp;
        this.baseAttack = baseAttack;
        this.baseDefense = baseDefense;
        this.baseSpecialAttack = baseSpecialAttack;
        this.baseSpecialDefense = baseSpecialDefense;
        this.baseSpeed = baseSpeed;
        this.possibleAbilitiesString = possibleAbilitiesString;
        this.hiddenAbilityString = hiddenAbilityString;
        this.height = height;
        this.weight = weight;
        this.evolvesAt = evolvesAt;
        this.possibleEvolutions = new ArrayList<>();

        // 0- Attack 1- Defense 2- SpAttack 3- SpDef 4- Speed 5- Accuracy 6- Evassiveness
        this.statModifiers = new double[7];
        Arrays.fill(statModifiers, 1);

        Nature[] natures = Nature.values();
        Random rd = new Random();
        int randomIndex = rd.nextInt(natures.length);
        this.nature = natures[randomIndex];

        calculateCurrentStats();

        this.currentMoves = moves;
        this.learnSet = learnSet;
    }

    private void calculateCurrentStats() {
        //Other Stats = (floor(0.01 x (2 x Base + IV + floor(0.25 x EV)) x Level) + 5) x Nature.
        this.currentMaxHp = (int) Math.floor(0.01 * (2 * this.baseHp) * this.level) + this.level + 10;
        this.currentHp = this.currentMaxHp;
        this.attack = (int) ((Math.floor(0.01 * (2 * this.baseAttack) * this.level) + 5) * this.nature.getAttackMultiplier());
        this.defense = (int) ((Math.floor(0.01 * (2 * this.baseDefense) * this.level) + 5) * this.nature.getDefenseMultiplier());
        this.specialAttack = (int) ((Math.floor(0.01 * (2 * this.baseSpecialAttack) * this.level) + 5) * this.nature.getSpecialAttackMultiplier());
        this.specialDefense = (int) ((Math.floor(0.01 * (2 * this.baseDefense) * this.level) + 5) * this.nature.getSpecialDefenseMultiplier());
        this.speed = (int) ((Math.floor(0.01 * (2 * this.baseSpeed) * this.level) + 5) * this.nature.getSpeedMultiplier());
    }

    public int getCurrentSpeed(){
        return (int) Math.round(this.speed * statModifiers[SPEED]);
    }

    public boolean hasHeldItem(){
        return heldItem != null;
    }

    public void addPossibleEvolution(Pokemon evo) {
        this.possibleEvolutions.add(evo);
    }

    public String getName() {
        return name;
    }

    public ArrayList<Move> getLearnSet() {
        return learnSet;
    }

    public void setEvolvesAt(int evolvesAt) {
        this.evolvesAt = evolvesAt;
    }

    public void healCurrentHP(int hpToHeal){
        if(this.currentHp + hpToHeal > this.currentMaxHp){
            this.currentHp = this.currentMaxHp;
        } else {
            this.currentHp = this.currentMaxHp + hpToHeal;
        }
    }

    public Move[] getMoves() {
        return this.currentMoves;
    }

    public String getMovesString(){
        String moves = "";
        for(int i = 0; i < currentMoves.length; i++){
            moves = moves + i + " - " + currentMoves[i].getName() + "\n";
        }

        return moves;
    }

    public int getCurrentHp() {
        return this.currentHp;
    }

    @Override
    public String toString() {
        String shinyStatus = isShiny ? "Yes" : "No";
        String typeInfo = type2 != null ? type1 + " / " + type2 : type1.toString();
        String evolutions = "Fully Evolved";
        for (int i = 0; i < possibleEvolutions.size(); i++) {
            if (i == 0){
                evolutions = "";
            }
            if (i == possibleEvolutions.size() - 1) {
                evolutions = evolutions + possibleEvolutions.get(i).getName();
                break;
            }
            evolutions = evolutions + possibleEvolutions.get(i).getName() + " | ";
        }

        return String.format(
                "╔═══════════════════════════════════════════╗\n" +
                        "║ %4d. Pokémon: %-26s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Type: %-35s ║\n" +
                        "║ Level: %-34d ║\n" +
                        "║ Nature: %-33s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ HP: %4d / %-30d ║\n" +
                        "║ Attack: %-20d (Base: %4d) ║\n" +
                        "║ Defense: %-19d (Base: %4d) ║\n" +
                        "║ Sp. Atk: %-19d (Base: %4d) ║\n" +
                        "║ Sp. Def: %-19d (Base: %4d) ║\n" +
                        "║ Speed: %-21d (Base: %4d) ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Ability: %-32s ║\n" +
                        "║ Height: %6.2fm %-25s ║\n" +
                        "║ Weight: %6.2fkg %-24s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Evolves At: Lv. %-25s ║\n" +
                        "║ Evolves To: %-29s ║\n" +
                        "║ Status Effect: %-26s ║\n" +
                        "║ Shiny: %-34s ║\n" +
                        "╚═══════════════════════════════════════════╝",
                dexNumber, name, typeInfo, level, nature,
                currentHp, currentMaxHp,
                attack, baseAttack,
                defense, baseDefense,
                specialAttack, baseSpecialAttack,
                specialDefense, baseSpecialDefense,
                speed, baseSpeed,
                ability, height, "",
                weight, "",
                evolvesAt == 0 ? "Fully Evolved" : evolvesAt, evolutions, statusEffect == null ? "None" : statusEffect, shinyStatus
        );
    }

    public String pokemonBattleInfo(){
        String typeInfo = type2 != null ? type1 + " / " + type2 : type1.toString();

        return String.format(
                        "╔═══════════════════════════════════════════╗\n" +
                        "║ %4d. Pokémon: %-26s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Type: %-35s ║\n" +
                        "║ Level: %-34d ║\n" +
                        "║ Nature: %-33s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ HP: %4d / %-30d ║\n" +
                        "║ Attack: %-20d (Base: %4d) ║\n" +
                        "║ Defense: %-19d (Base: %4d) ║\n" +
                        "║ Sp. Atk: %-19d (Base: %4d) ║\n" +
                        "║ Sp. Def: %-19d (Base: %4d) ║\n" +
                        "║ Speed: %-21d (Base: %4d) ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Ability: %-32s ║\n" +
                        "║ Status Effect: %-26s ║\n" +
                        "╚═══════════════════════════════════════════╝\n",
                        dexNumber, name, typeInfo, level, nature,currentHp,
                        currentMaxHp, attack, baseAttack, defense, baseDefense,
                        specialAttack, baseSpecialAttack, specialDefense,
                        baseSpecialDefense, speed, baseSpeed, ability,
                        statusEffect == null ? "None" : statusEffect
        );
    }
}
