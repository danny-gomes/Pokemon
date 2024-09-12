package Entidades;

import Entidades.AbilitiesStructure.Ability;
import Entidades.Items.Item;
import Entidades.Moves.Move;
import Enums.Ailment;
import Enums.MoveCategory;
import Enums.Nature;
import Enums.PokemonType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class Pokemon {
    private int dexNumber;
    private String name;
    private String gender;
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
    private int[] statModifiers;
    public static final int ATTACK_INDEX = 0;
    public static final int DEFENSE_INDEX = 1;
    public static final int SPECIAL_ATTACK_INDEX = 2;
    public static final int SPECIAL_DEFENSE_INDEX = 3;
    public static final int SPEED_INDEX = 4;
    public static final int ACCURACY_INDEX = 5;
    public static final int EVASIVENESS_INDEX = 6;
    private boolean isShiny;
    private Item heldItem;
    private Move[] currentMoves;
    private ArrayList<Move> learnSet;
    private ArrayList<Ailment> ailments;
    private int confusionTurns;
    private int sleepTurns;
    private int toxicCounter;

    /**
     * Method that creates Pokemon instance.
     * @param dexNumber the pokemons pokedex number.
     * @param name the name of the pokemon.
     * @param type1 the first type of the pokemon.
     * @param type2 the second type of the pokemon if they have one.
     * @param level the pokemons level.
     * @param baseHp the pokemons base HP.
     * @param baseAttack the pokemons base attack.
     * @param baseDefense the pokemons base defense.
     * @param baseSpecialAttack the pokemons base special attack.
     * @param baseSpecialDefense the pokemons base special defense.
     * @param baseSpeed the pokemons base speed.
     * @param possibleAbilitiesString the pokemons list of possible abilities.
     * @param hiddenAbilityString the pokemons hidden ability.
     * @param height the pokemons height.
     * @param weight the pokemons weight.
     * @param evolvesAt the level the pokemon evolves at if it does.
     * @param moves the pokemons current learnt moves.
     * @param learnSet all the moves the pokemon can learn.
     */
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
        this.confusionTurns = 0;
        this.sleepTurns = 0;
        this.toxicCounter = 0;


        // 0- Attack 1- Defense 2- SpAttack 3- SpDef 4- Speed 5- Accuracy 6- Evassiveness
        this.statModifiers = new int[7];
        Arrays.fill(statModifiers, 0);

        Nature[] natures = Nature.values();
        Random rd = new Random();
        int randomIndex = rd.nextInt(natures.length);
        this.nature = natures[randomIndex];

        if (rd.nextBoolean()) {
            this.gender = "Male";
        } else {
            this.gender = "Female";
        }

        calculateCurrentStats();

        this.currentMoves = moves;
        this.learnSet = learnSet;
        this.ailments = new ArrayList<>();
    }

    /**
     * Method that calculates the pokemons current stats based on nature and base stats, IVs and EVs not implemented.
     */
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

    /**
     * Method that checks if a pokemon has a held item.
     * @return true if the pokemon has a held item.
     */
    public boolean hasHeldItem() {
        return heldItem != null;
    }

    /**
     * Method that adds a possible evolution to the pokemon.
     * @param evo The pokemon this pokemon will evolve to.
     */
    public void addPossibleEvolution(Pokemon evo) {
        this.possibleEvolutions.add(evo);
    }

    /**
     * Method that returns the pokemons name.
     * @return the pokemons name.
     */
    public String getName() {
        return name;
    }

    /**
     * Method that returns the list of all moves that the pokemon can learn.
     * @return the list of all moves the pokemon can learn.
     */
    public ArrayList<Move> getLearnSet() {
        return learnSet;
    }

    /**
     * Method that sets the level this pokemon evolves at.
     * @param evolvesAt the level the pokemon will evolve at.
     */
    public void setEvolvesAt(int evolvesAt) {
        this.evolvesAt = evolvesAt;
    }

    /**
     * Method that heals a pokemons current HP.
     * @param hpToHeal the amount of hp to heal.
     */
    public void healCurrentHP(int hpToHeal) {
        if (this.currentHp + hpToHeal > this.currentMaxHp) {
            this.currentHp = this.currentMaxHp;
        } else {
            this.currentHp = this.currentMaxHp + hpToHeal;
        }
    }

    /**
     * Method that returns the array with the moves that the pokemon currently knows.
     * @return the list of currently learnt moves.
     */
    public Move[] getMoves() {
        return this.currentMoves;
    }

    /**
     * Return the moves learn in string format.
     * @return the String with the names of the current learnt moves.
     */
    public String getMovesString() {
        String moves = "";
        for (int i = 0; i < currentMoves.length; i++) {
            if (currentMoves[i] != null) {
                moves = moves + (i + 1) + " - " + currentMoves[i].getName() + "\n";
            }
        }

        return moves;
    }

    /**
     * Gets a pokemons current hp.
     * @return the pokemons current hp.
     */
    public int getCurrentHp() {
        return this.currentHp;
    }

    /**
     * Converts the pokemons info into string format.
     * @return the pokemon moves info in String.
     */
    @Override
    public String toString() {
        String shinyStatus = isShiny ? "Yes" : "No";
        String typeInfo = type2 != null ? type1 + " / " + type2 : type1.toString();
        String evolutions = "Fully Evolved";
        for (int i = 0; i < possibleEvolutions.size(); i++) {
            if (i == 0) {
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
                evolvesAt == 0 ? "Fully Evolved" : evolvesAt, evolutions, getAilmentsString(), shinyStatus
        );
    }

    /**
     * Returns pokemons info that is more relevant to a battle in string format.
     * @return the pokemons info that is more relevant to battle in string format.
     */
    public String pokemonBattleInfo() {
        String typeInfo = type2 != null ? type1 + " / " + type2 : type1.toString();

        // Determine gender symbol
        String genderSymbol = switch (gender) {
            case "Male" -> "♂";
            case "Female" -> "♀";
            default -> "";
        };

        // Check for shininess
        String shinyText = isShiny ? "⭐ Shiny" : "";

        // Prepare moves information
        StringBuilder movesInfo = new StringBuilder();
        for (int i = 0; i < currentMoves.length && currentMoves[i] != null; i++) {
            movesInfo.append(String.format(
                    "║ %-7s PP: %2d / %-24d ║\n",
                    currentMoves[i].getName(), currentMoves[i].getPP(), currentMoves[i].getCurrentPowerPoints()
            ));
        }

        return String.format(
                "╔═══════════════════════════════════════════╗\n" +
                        "║ %4d. Pokémon: %-26s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ Type: %-35s ║\n" +
                        "║ Level: %-34d ║\n" +
                        "║ Nature: %-33s ║\n" +
                        "║ Gender: %-33s ║\n" +
                        "║ %-41s ║\n" +
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
                        "╠═══════════════════════════════════════════╣\n" +
                        "%s" +
                        "╚═══════════════════════════════════════════╝\n",
                dexNumber, name, typeInfo, level, nature, genderSymbol, shinyText,
                currentHp, currentMaxHp, attack, baseAttack, defense, baseDefense,
                specialAttack, baseSpecialAttack, specialDefense,
                baseSpecialDefense, speed, baseSpeed, ability,
                getAilmentsString(), movesInfo
        );
    }

    /**
     * Method that returns a pokemons current ailments as a String.
     * @return the pokemons current ailments as string.
     */
    public String getAilmentsString() {
        StringBuilder sb = new StringBuilder();

        for (Ailment ailment : ailments) {
            if (ailment != Ailment.NONE) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(ailment.name().replace("_", " "));
            }
        }

        return sb.length() > 0 ? sb.toString() : "No Ailments";
    }

    /**
     * Method that returns a pokemons current max hp.
     * @return the pokemons current max hp.
     */
    public int getCurrentMaxHp() {
        return currentMaxHp;
    }

    /**
     * Method that returns the pokemons current leve.
     * @return the pokemons level.
     */
    public int getLevel() {
        return this.level;
    }

    /**
     * Method that returns the stat that will be used to calculate damage dealt
     * @param defender the pokemon is defending from the move.
     * @param move the move used.
     * @return the stat that will be used to calculate damage.
     */
    public int getStatForAttackMove(Pokemon defender, Move move) {
        if (move.getName().equalsIgnoreCase("body-press")) {
            return (int) (this.defense * getStatMultiplier(statModifiers[DEFENSE_INDEX]));
        }

        if (move.getName().equalsIgnoreCase("foul-play")) {
            return (int) (this.attack * getStatMultiplier(statModifiers[DEFENSE_INDEX]));
        }

        if (move.getCategory().equals(MoveCategory.PHYSICAL)) {
            return (int) (this.attack * getStatMultiplier(statModifiers[ATTACK_INDEX]));
        } else {
            return (int) (this.specialAttack * getStatMultiplier(statModifiers[SPECIAL_ATTACK_INDEX]));
        }
    }

    /**
     * Method that returns the defense stat to be used to calculate damage received.
     * @param attacker the pokemon that attacked.
     * @param moveUsed the move used by the attacker.
     * @return the stat to be used to calculate damage taken.
     */
    public int getStatForDefense(Pokemon attacker, Move moveUsed) {
        if (moveUsed.getName().equalsIgnoreCase("secret-sword")) {
            return (int) (this.defense * getStatMultiplier(statModifiers[DEFENSE_INDEX]));
        }

        if (moveUsed.getCategory().equals(MoveCategory.SPECIAL)) {
            return (int) (this.specialDefense * getStatMultiplier(statModifiers[SPECIAL_DEFENSE_INDEX]));
        } else {
            return (int) (this.defense * getStatMultiplier(statModifiers[DEFENSE_INDEX]));
        }
    }


    /**
     * Method that checks if a pokemon will have the STAB boost (Same Type Attack Bonus)
     * @param moveUsed the move used.
     * @return true if move used is the same as the pokemon type using it.
     */
    public double getStab(Move moveUsed) {
        PokemonType moveType = moveUsed.getType();
        if (moveType.equals(this.type1) || moveType.equals(this.type2)) {
            return 1.5;
        }

        return 1;
    }

    /**
     * Method that checks how effective a move is against this pokemons types.
     * @param moveType the move type to check effectivess
     * @return the effectiveness of the move on that pokemon.
     */
    public double getEffectiveness(PokemonType moveType) {

        double effectiveness = getEffectivenessAgainstType(moveType, this.type1);

        if (this.type2 != null) {
            effectiveness *= getEffectivenessAgainstType(moveType, this.type2);
        }

        return effectiveness;
    }

    /**
     * Method that checks the effectiveness from one type to another.
     * @param moveType The attacking moveType
     * @param targetType the type that is receiving the attack.
     * @return the effectivess for this combo of moves.
     */
    public double getEffectivenessAgainstType(PokemonType moveType, PokemonType targetType) {
        switch (moveType) {
            case NORMAL:
                if (targetType == PokemonType.ROCK || targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.GHOST) return 0.0;
                break;
            case FIRE:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.ICE || targetType == PokemonType.BUG || targetType == PokemonType.STEEL)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || targetType == PokemonType.ROCK || targetType == PokemonType.DRAGON)
                    return 0.5;
                break;
            case WATER:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.GROUND || targetType == PokemonType.ROCK)
                    return 2.0;
                if (targetType == PokemonType.WATER || targetType == PokemonType.GRASS || targetType == PokemonType.DRAGON)
                    return 0.5;
                break;
            case ELECTRIC:
                if (targetType == PokemonType.WATER || targetType == PokemonType.FLYING) return 2.0;
                if (targetType == PokemonType.ELECTRIC || targetType == PokemonType.GRASS || targetType == PokemonType.DRAGON)
                    return 0.5;
                if (targetType == PokemonType.GROUND) return 0.0;
                break;
            case GRASS:
                if (targetType == PokemonType.WATER || targetType == PokemonType.GROUND || targetType == PokemonType.ROCK)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.GRASS || targetType == PokemonType.POISON || targetType == PokemonType.FLYING || targetType == PokemonType.BUG || targetType == PokemonType.DRAGON || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
            case ICE:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.GROUND || targetType == PokemonType.FLYING || targetType == PokemonType.DRAGON)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || targetType == PokemonType.ICE || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
            case FIGHTING:
                if (targetType == PokemonType.NORMAL || targetType == PokemonType.ICE || targetType == PokemonType.ROCK || targetType == PokemonType.DARK || targetType == PokemonType.STEEL)
                    return 2.0;
                if (targetType == PokemonType.POISON || targetType == PokemonType.FLYING || targetType == PokemonType.PSYCHIC || targetType == PokemonType.BUG || targetType == PokemonType.FAIRY)
                    return 0.5;
                if (targetType == PokemonType.GHOST) return 0.0;
                break;
            case POISON:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.FAIRY) return 2.0;
                if (targetType == PokemonType.POISON || targetType == PokemonType.GROUND || targetType == PokemonType.ROCK || targetType == PokemonType.GHOST)
                    return 0.5;
                if (targetType == PokemonType.STEEL) return 0.0;
                break;
            case GROUND:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.ELECTRIC || targetType == PokemonType.POISON || targetType == PokemonType.ROCK || targetType == PokemonType.STEEL)
                    return 2.0;
                if (targetType == PokemonType.GRASS || targetType == PokemonType.BUG) return 0.5;
                if (targetType == PokemonType.FLYING) return 0.0;
                break;
            case FLYING:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.FIGHTING || targetType == PokemonType.BUG)
                    return 2.0;
                if (targetType == PokemonType.ELECTRIC || targetType == PokemonType.ROCK || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
            case PSYCHIC:
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.POISON) return 2.0;
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.DARK) return 0.0;
                break;
            case BUG:
                if (targetType == PokemonType.GRASS || targetType == PokemonType.PSYCHIC || targetType == PokemonType.DARK)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.FIGHTING || targetType == PokemonType.POISON || targetType == PokemonType.FLYING || targetType == PokemonType.GHOST || targetType == PokemonType.STEEL || targetType == PokemonType.FAIRY)
                    return 0.5;
                break;
            case ROCK:
                if (targetType == PokemonType.FIRE || targetType == PokemonType.ICE || targetType == PokemonType.FLYING || targetType == PokemonType.BUG)
                    return 2.0;
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.GROUND || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
            case GHOST:
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.GHOST) return 2.0;
                if (targetType == PokemonType.DARK) return 0.5;
                if (targetType == PokemonType.NORMAL) return 0.0;
                break;
            case DRAGON:
                if (targetType == PokemonType.DRAGON) return 2.0;
                if (targetType == PokemonType.STEEL) return 0.5;
                if (targetType == PokemonType.FAIRY) return 0.0;
                break;
            case DARK:
                if (targetType == PokemonType.PSYCHIC || targetType == PokemonType.GHOST) return 2.0;
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.DARK || targetType == PokemonType.FAIRY)
                    return 0.5;
                break;
            case STEEL:
                if (targetType == PokemonType.ICE || targetType == PokemonType.ROCK || targetType == PokemonType.FAIRY)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.WATER || targetType == PokemonType.ELECTRIC || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
            case FAIRY:
                if (targetType == PokemonType.FIGHTING || targetType == PokemonType.DRAGON || targetType == PokemonType.DARK)
                    return 2.0;
                if (targetType == PokemonType.FIRE || targetType == PokemonType.POISON || targetType == PokemonType.STEEL)
                    return 0.5;
                break;
        }

        return 1.0;
    }

    /**
     * Method that deals damage to a pokemon.
     * @param finalDamage that damage to be dealt to a pokemon.
     */
    public void takeDamage(int finalDamage) {
        this.currentHp = this.currentHp - finalDamage;
        if (this.currentHp < 0) {
            this.currentHp = 0;
        }
    }

    /**
     * Method that teaches a pokemon a new move.
     * @param m the move to learn.
     */
    public void learnNewMove(Move m) {
        Scanner in = new Scanner(System.in);
        String learntMoves = "";
        for (int i = 0; i < currentMoves.length; i++) {
            if (currentMoves[i] == null) {
                currentMoves[i] = m;
                return;
            }
            learntMoves = learntMoves + (i + 1) + " - " + currentMoves[i].getName() + "\n";
        }

        System.out.println(learntMoves);

        System.out.println("Choose a move to replace:");
        int moveToReplace = in.nextInt();
        while (moveToReplace < 1 || moveToReplace > 4) {
            System.out.println("Choose a move to replace: (1-4)");
            moveToReplace = in.nextInt();
        }

        currentMoves[moveToReplace] = m;
    }

    /**
     * Method that returns the pokemons current evasioness modifier.
     * @return
     */
    public double getEvasiness() {
        return statModifiers[EVASIVENESS_INDEX];
    }

    /**
     * Method that returns the current accuracy modifier.
     * @return the current accuracy modifier.
     */
    public double getAccuracy() {
        return statModifiers[ACCURACY_INDEX];
    }

    /**
     * Method that updates a pokemons stats based on the change recieved as parameter.
     * @param attackChange the attackChange to be implemented, 0 if should be unchanged.
     * @param defenseChange the defenseChange to be implemented, 0 if should be unchanged.
     * @param spAttackChange the spAttackChange to be implemented, 0 if should be unchanged.
     * @param spDefChange the spDefChange to be implemented, 0 if should be unchanged.
     * @param speedChange the speedChange to be implemented, 0 if should be unchanged.
     * @param accuracyChange the accuracyChange to be implemented, 0 if should be unchanged.
     * @param evasionChange the evasionChange to be implemented, 0 if should be unchanged.
     */
    public void updateStatModifiers(int attackChange, int defenseChange, int spAttackChange, int spDefChange, int speedChange, int accuracyChange, int evasionChange) {
        for (int i = 0; i < statModifiers.length; i++) {
            switch (i) {
                case 0:
                    updateStatModifier(0, attackChange, "Attack");
                    break;
                case 1:
                    updateStatModifier(1, defenseChange, "Defense");
                    break;
                case 2:
                    updateStatModifier(2, spAttackChange, "Special Attack");
                    break;
                case 3:
                    updateStatModifier(3, spDefChange, "Special Defense");
                    break;
                case 4:
                    updateStatModifier(4, speedChange, "Speed");
                    break;
                case 5:
                    updateStatModifier(5, accuracyChange, "Accuracy");
                    break;
                case 6:
                    updateStatModifier(6, evasionChange, "Evasion");
                    break;
            }
        }
    }

    /**
     * Method that updates the array with the current stat modifiers and makes sure they dont go higher or lower than the max or min amount.
     * @param i the index of the stat to be changed 0 - attack, 1 - defense etc...
     * @param change the change the stat is going to be updated.
     * @param stat the name of the stat being changed.
     */
    private void updateStatModifier(int i, int change, String stat) {
        if (statModifiers[i] + change > 6) {
            statModifiers[i] = 6;
            System.out.println(stat + " maxed out!!!");
        } else if (statModifiers[i] + change < -6) {
            statModifiers[i] = -6;
            System.out.println(stat + " is at it's lowest...");
        } else if (statModifiers[i]  == 6) {
            System.out.println("Stat won't go higher!");
        } else if (statModifiers[i] == -6) {
            System.out.println("Stat won't go lower...");
        } else {
            statModifiers[i] = statModifiers[i] + change;
            if (change > 0) {
                System.out.println(stat + " raised by " + change);
            }

            if (change < 0) {
                System.out.println(stat + " lowered by " + change);
            }
        }
    }

    /**
     * Method that clears all stat modifiers and sets them back to neutral.
     */
    public void clearStatModifiers() {
        for (int i = 0; i < statModifiers.length; i++) {
            statModifiers[i] = 0;
        }
    }

    /**
     * Method that gets the current stat modifiers as a String.
     * @return the current stat modifiers as a String.
     */
    public String getCurrentStatsString() {
        return String.format(
                "╔═══════════════════════════════════════════╗\n" +
                        "║ %-40s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ %-40s ║\n" +
                        "║ %-40s ║\n" +
                        "╠═══════════════════════════════════════════╣\n" +
                        "║ HP:           %-4d / %-25d ║\n" +
                        "║ Attack:       %-4d                       ║\n" +
                        "║ Defense:      %-4d                       ║\n" +
                        "║ Sp. Attack:   %-4d                       ║\n" +
                        "║ Sp. Defense:  %-4d                       ║\n" +
                        "║ Speed:        %-4d                       ║\n" +
                        "╚═══════════════════════════════════════════╝",
                "Pokémon Stats",
                "Name: " + name,
                "Level: " + level + "   Nature: " + nature,
                (int) currentHp, (int) currentMaxHp,
                (int) (attack * getStatMultiplier(statModifiers[ATTACK_INDEX])),
                (int) (defense * getStatMultiplier(statModifiers[DEFENSE_INDEX])),
                (int) (specialAttack * getStatMultiplier(statModifiers[SPECIAL_ATTACK_INDEX])),
                (int) (specialDefense * getStatMultiplier(statModifiers[SPECIAL_DEFENSE_INDEX])),
                (int) (speed * getStatMultiplier(statModifiers[SPEED_INDEX]))
        );
    }

    /**
     * Method that return the current speed of a pokemon including stat modifiers.
     * @return the current speed of a pokemon.
     */
    public int getCurrentSpeed() {
        return (int) Math.round(this.speed * getStatMultiplier(statModifiers[SPEED_INDEX]));
    }

    /**
     * Method that returns the nerf or boost for a stat taking into account the stat modifiers a stat has on it.
     * @param statModifier the stat to be changed by a modifier.
     * @return the amount a stat should be changed.
     */
    private double getStatMultiplier(int statModifier) {
        switch (statModifier) {
            case -6:
                return (double) 1 / 4;
            case -5:
                return 1 / 3.5;
            case -4:
                return 1 / 3;
            case -3:
                return 1 / 2.5;
            case -2:
                return (double) 1 / 2;
            case -1:
                return 1 / 1.5;
            case 0:
                return 1;
            case 1:
                return 1.5;
            case 2:
                return 2;
            case 3:
                return 2.5;
            case 4:
                return 3;
            case 5:
                return 3.5;
            case 6:
                return 4;
            default:
                return 0;
        }
    }

    /**
     * Method that adds an ailment to a pokemon.
     * @param attacker the pokemon that attacked to check for infatuation.
     * @param ailment the ailment to be added.
     * @return true if ailment was added successfully.
     */
    public boolean addAilment(Pokemon attacker, Ailment ailment) {
        if (ailment == Ailment.NONE || ailment == Ailment.UNKNOWN) {
            return false;
        }

        if (isPrimaryStatusAilment(ailment)) {
            for (Ailment a : ailments) {
                if (isPrimaryStatusAilment(a)) {
                    return false;
                }
            }

            if (ailment.equals(Ailment.PARALYSIS)) {
                this.updateStatModifiers(0,0,0,0,-2,0,0);
            }

            if (ailment.equals(Ailment.BURN)) {
                this.updateStatModifiers(-2,0,0,0,0,0,0);
            }
        } else {
            ailments.remove(ailment);
        }

        if (ailment.equals(Ailment.INGRAIN)) {
            ailments.add(ailment);
            this.addAilment(this, Ailment.TRAP);
        } else {
            if (ailments.contains(Ailment.INFATUATION)) {
                System.out.println("Pokemon is already infatuated.");
                return false;
            }

            if (ailment.equals(Ailment.INFATUATION)) {
                String attackerGender = attacker.gender;
                String defenderGender = this.gender;

                if (attackerGender.equals(defenderGender)) {
                    System.out.println("Infatuation can not be added to same gender pokemon.");
                    return false;
                } else {
                    System.out.println(this.getName() + " fell in love!");
                }
            }

            ailments.add(ailment);
        }

        return true;
    }

    /**
     * Method that checks if the ailment is a primary status ailment (pokemon can only have one of these at a time).
     * @param ailment the ailment to be checked.
     * @return true if it is a primayStatus ailment.
     */
    private boolean isPrimaryStatusAilment(Ailment ailment) {
        return ailment == Ailment.PARALYSIS || ailment == Ailment.SLEEP || ailment == Ailment.FREEZE ||
                ailment == Ailment.BURN || ailment == Ailment.POISON || ailment == Ailment.TOXIC;
    }

    /**
     * Method that checks ailments and applies their effects.
     * @param beforeMove this paramater defines if the ailments are ailments that are applied before a move, true to check ailments before a move is done.
     * @return true if pokemon can attack, false if not.
     */
    public boolean checkAilments(boolean beforeMove) {
        if (beforeMove) {
            for (Ailment ailment : ailments) {
                switch (ailment) {
                    case PARALYSIS:
                        if (Math.random() < 0.5) {
                            return true;
                        } else {
                            System.out.println("The Pokemon is paralyzed.");
                            return false;
                        }
                    case BURN:
                        this.attack /= 2;
                        break;
                    case FREEZE:
                        if (Math.random() < 0.50) {
                            System.out.println("The Pokemon thawed out!");
                            ailments.remove(Ailment.FREEZE);
                            return true;
                        } else {
                            System.out.println("The Pokemon is frozen and cannot move!");
                            return false;
                        }
                    case SLEEP:
                        sleepTurns++;
                        if (sleepTurns == 1) {
                            System.out.println("The Pokemon is fast asleep.");
                            return false;
                        } else if (sleepTurns == 2) {
                            if (Math.random() < 0.5) {
                                System.out.println("The Pokemon woke up!");
                                ailments.remove(Ailment.SLEEP);
                                return true;
                            } else {
                                System.out.println("The Pokemon is fast asleep.");
                                return false;
                            }
                        } else if (sleepTurns >= 3) {
                            System.out.println("The Pokemon woke up!");
                            ailments.remove(Ailment.SLEEP);
                            return true;
                        }
                        break;
                    case CONFUSION:
                        if (Math.random() < 0.50) {
                            System.out.println("The Pokemon snapped out of its confusion!");
                            ailments.remove(Ailment.CONFUSION);
                            return true;
                        } else {
                            if (Math.random() < 0.50) {
                                int confusionDamage = currentMaxHp * 4 / 16;
                                System.out.println("The Pokemon hurt itself in its confusion!");
                                currentHp -= confusionDamage;
                                if (currentHp <= 0) {
                                    currentHp = 0;
                                    System.out.println("The Pokémon fainted!");
                                }
                                return false;
                            } else {
                                System.out.println("The Pokemon overcame its confusion!");
                                return true;
                            }
                        }
                    case INFATUATION:
                        if (Math.random() < 0.50) {
                            System.out.println("The Pokemon snapped out of its infatuation!");
                            ailments.remove(Ailment.INFATUATION);
                            return true;
                        } else {
                            if (Math.random() < 0.50) {
                                System.out.println("The Pokemon is immobilized by love!");
                                return false;
                            }
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            for (Ailment ailment : ailments) {
                switch (ailment) {
                    case BURN:
                        int burnDamage = currentMaxHp / 16;
                        currentHp -= burnDamage;
                        System.out.println("The Pokemon is hurt by its burn!");
                        if (currentHp <= 0) {
                            currentHp = 0;
                            System.out.println("The Pokemon fainted!");
                        }
                        break;
                    case POISON:
                        int poisonDamage = currentMaxHp / 16;
                        currentHp -= poisonDamage;
                        System.out.println("The Pokemon is hurt by poison!");
                        if (currentHp <= 0) {
                            currentHp = 0;
                            System.out.println("The Pokemon fainted!");
                        }
                        break;
                    case TOXIC:
                        toxicCounter++;
                        int toxicDamage = currentMaxHp * toxicCounter / 16;
                        currentHp -= toxicDamage;
                        System.out.println("The Pokemon is hurt by toxic poison!");
                        if (currentHp <= 0) {
                            currentHp = 0;
                            System.out.println("The Pokemon fainted!");
                        }
                        break;
                    case NIGHTMARE:
                        if (ailments.contains(Ailment.SLEEP)) {
                            int nightmareDamage = currentMaxHp / 4;
                            currentHp -= nightmareDamage;
                            System.out.println("The Pokemon is tormented by a nightmare!");
                            if (currentHp <= 0) {
                                currentHp = 0;
                                System.out.println("The Pokemon fainted!");
                            }
                        }
                        break;
                    case LEECH_SEED:
                        int leechSeedDamage = currentMaxHp / 16;
                        currentHp -= leechSeedDamage;
                        System.out.println("The Pokémon's energy is drained by Leech Seed!");
                        this.healCurrentHP(leechSeedDamage / 2);
                        if (currentHp <= 0) {
                            currentHp = 0;
                            System.out.println("The Pokemon fainted!");
                        }
                        break;
                    case INGRAIN:
                        int ingrainHeal = currentMaxHp / 16;
                        currentHp += ingrainHeal;
                        if (currentHp > currentMaxHp) currentHp = currentMaxHp;
                        System.out.println("The Pokémon heals due to Ingrain!");
                        break;
                    // Add additional end-of-turn effects here as needed
                    default:
                        break;
                }
            }
        }
        return true;
    }

    /**
     * Method that checks if a pokemon has the TRAP ailment.
     * @return true if the pokemon is trapped.
     */
    public boolean isTrapped() {
        for (Ailment a : ailments) {
            if (a.equals(Ailment.TRAP)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Method that returns the gender of a pokemon.
     * @return the gender of a pokemon.
     */
    public String getGender() {
        return gender;
    }

    /**
     * Method that displays damage dealt to a pokemon.
     * @param damageDealt the damage dealt to a pokemon.
     */
    public void displayDamageDealt(int damageDealt) {
        if (damageDealt > 0) {
            if (this.getCurrentHp() - damageDealt < 0) {
                System.out.println(this.getCurrentHp() + "/" + this.getCurrentMaxHp() + " --> " + 0 + "/" + this.getCurrentMaxHp());
            } else {
                System.out.println(this.getCurrentHp() + "/" + this.getCurrentMaxHp() + " --> " + (this.getCurrentHp() - damageDealt) + "/" + this.getCurrentMaxHp());
            }
        }
    }

    /**
     * Method that gets the pokemons current attack stat including boosts.
     * @return the pokemons current attack stat.
     */
    public int getCurrentAttack() {
        return (int) Math.round(this.attack * getStatMultiplier(statModifiers[ATTACK_INDEX]));
    }

    /**
     * Method that gets the pokemons current defense stat including boosts.
     * @return the pokemons current defense stat.
     */
    public int getCurrentDefense() {
        return (int) Math.round(this.defense * getStatMultiplier(statModifiers[DEFENSE_INDEX]));
    }

    /**
     * Method that gets the pokemons current special attack stat including boosts.
     * @return the pokemons current special attack stat.
     */
    public int getCurrentSpecialAttack() {
        return (int) Math.round(this.specialAttack * getStatMultiplier(statModifiers[SPECIAL_ATTACK_INDEX]));
    }

    /**
     * Method that gets the pokemons current special defense stat including boosts.
     * @return the pokemons current special defense stat.
     */
    public int getCurrentSpecialDefense() {
        return (int) Math.round(this.specialDefense * getStatMultiplier(statModifiers[SPECIAL_DEFENSE_INDEX]));
    }

    /**
     * Method that gets the pokemons current accuracy modifier.
     * @return the pokemons current accuracy modifier.
     */
    public int getAccuracyModifier() {
        return statModifiers[ACCURACY_INDEX];
    }

    /**
     * Method that gets the pokemons current evasioness modifier.
     * @return the pokemons current evasioness modifier.
     */
    public int getEvasivenessModifier() {
        return statModifiers[EVASIVENESS_INDEX];
    }

    public ArrayList<PokemonType> getTypes(){
        ArrayList<PokemonType> types = new ArrayList<>();
        types.add(this.type1);
        if(this.type2 != null){
            types.add(this.type2);
        }

        return types;
    }

    public void changeEntireMoveSet(Move[] moves){
        System.arraycopy(moves, 0, this.currentMoves, 0, moves.length);
    }
}
