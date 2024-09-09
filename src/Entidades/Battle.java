package Entidades;

import Entidades.FieldEffects.Barrier;
import Entidades.FieldEffects.EntryHazard;
import Entidades.FieldEffects.FieldEffect;
import Entidades.FieldEffects.Weather;
import Entidades.Moves.Move;
import Enums.Ailment;
import Enums.MoveCategory;
import Enums.Target;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    /**
     * The players Trainer object will be store here.
     */
    private Trainer player;
    /**
     * The players current Pokemon in battle.
     */
    private Pokemon playerCurrentPokemon;
    /**
     * The challengers Trainer object.
     */
    private Trainer challenger;
    /**
     * The challengers current Pokemon.
     */
    private Pokemon challengerCurrentPokemon;
    /**
     * The battles turn count.
     */
    private int turnCount;
    /**
     *
     */
    private ArrayList<FieldEffect> playerFieldEffects;
    /**
     * The list of the challengers field effects.
     */
    private ArrayList<FieldEffect> challengerFieldEffects;
    /**
     * The battles current weather.
     */
    private Weather weather;

    /**
     * Constructor to initialize a battle.
     * @param player The players Trainer object will be store here.
     * @param playerCurrentPokemon The players starting pokemon in battle.
     * @param challenger The challengers Trainer object.
     * @param challengerCurrentPokemon The challengers starting Pokemon in battle.
     * @param turnCount The list with the players field effects.
     */
    public Battle(Trainer player, Pokemon playerCurrentPokemon, Trainer challenger, Pokemon challengerCurrentPokemon, int turnCount) {
        this.player = player;
        this.playerCurrentPokemon = playerCurrentPokemon;
        this.challenger = challenger;
        this.challengerCurrentPokemon = challengerCurrentPokemon;
        this.turnCount = turnCount;

        this.playerFieldEffects = new ArrayList<>();
        this.challengerFieldEffects = new ArrayList<>();
        this.weather = new Weather("None", null, 0, 0);
    }

    /**
     * Method that starts a battle turn, player can select one of 4 options.
     * @return 0 if battle should continue, -1 if challenger wins, 1 if player wins.
     * @throws InterruptedException Exception needed to use thread.sleep
     */
    public int turn() throws InterruptedException {
        this.turnCount = this.turnCount + 1;

        Scanner in = new Scanner(System.in);
        int option;
        boolean hasNotSelected = true;
        int outcome = 0;
        do {
            do {
                System.out.println("1 - Fight 2 - Bag \n3 - Pokemon 4 - Run");
                option = in.nextInt();
            } while (option < 1 || option > 4);

            switch (option) {
                case 1:
                    Move playerMove = selectPlayerMove();
                    if (playerMove != null) {
                        Move challengerMove = selectChallengerMove();
                        outcome = battleTurn(playerMove, challengerMove);
                        hasNotSelected = false;
                    }
                    break;
                case 2:
                    System.out.println("BAG STUFF");
                    break;
                case 3:
                    boolean switched = switchOut();
                    if (switched) {
                        hasNotSelected = false;
                        Move challengerMove = selectChallengerMove();
                        outcome = battleTurn(null, challengerMove);
                    }
                    break;
                case 4:
                    System.out.println("TRY RUN");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        } while (hasNotSelected);

        updateFieldEffects(player);
        updateFieldEffects(challenger);

        return outcome;
    }
    // Had to use iterator because of concurrentModificationException, was removing element from array while using it in for loop above

    /**
     * Method that updates a given trainers field effects.
     * @param trainer The trainer to have its field effects updated.
     */
    private void updateFieldEffects(Trainer trainer) {
        Iterator<FieldEffect> iterator = getTrainerFieldEffects(trainer).iterator();
        while (iterator.hasNext()) {
            FieldEffect fe = iterator.next();
            if (fe instanceof Barrier b) {
                if (b.updateTurnsOnField()) {
                    iterator.remove();
                    b.removeStats(trainer, this);
                }
            }
        }
    }

    /**
     * Method to handle the players switch out mechanic.
     * @return false if player could not switch out or if they ended up choosing not to switch out, true if switch out successful.
     */
    private boolean switchOut() {
        Scanner in = new Scanner(System.in);
        if (playerCurrentPokemon.isTrapped()) {
            System.out.println("Player is trapped.");
            return false;
        }

        ArrayList<String> pokemonNames = new ArrayList<>();
        int count = 1;
        for (Pokemon p : player.nonFaintedPokemon()) {
            if (!p.getName().equalsIgnoreCase(playerCurrentPokemon.getName())) {
                System.out.println("\n" + count + ".\n" + p.pokemonBattleInfo());
                pokemonNames.add(p.getName());
                count++;
            }
        }
        int pokemonOption = 0;

        if (count == 1) {
            System.out.println("Party empty.");
        } else {
            do {
                System.out.println("Choose pokemon to switch in. 0 to go back");
                pokemonOption = in.nextInt();
            } while (pokemonOption < -1 && pokemonOption > count);
        }

        if (pokemonOption != 0) {
            playerCurrentPokemon.clearStatModifiers();
            playerCurrentPokemon = player.getNonFaintedPokemon(pokemonNames.get(pokemonOption - 1));
            System.out.println("Go, " + playerCurrentPokemon.getName() + "!");
            for (FieldEffect fe : playerFieldEffects) {
                if (fe instanceof EntryHazard eh) {
                    eh.checkEntryHazards(playerCurrentPokemon);
                }

                if (fe instanceof Barrier) {
                    checkBarriers(player);
                }
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * Method that handles the actual turn of the fight.
     * @param playerMove The move selected by the player.
     * @param challengerMove The move selected by the challenger.
     * @return 0 if no one lost, -1 if challenger wins, 1 if player wins.
     * @throws InterruptedException Exception to use thread.sleep
     */
    private int battleTurn(Move playerMove, Move challengerMove) throws InterruptedException {
        boolean playerWin = false;
        boolean challengerWin = false;
        boolean isPlayerFirst = playerAttackFirst(playerMove, challengerMove);
        int playerDamageDealt = 0;
        int challengerDamageDealt = 0;

        if (playerMove == null) {
            if (challengerCurrentPokemon.checkAilments(true)) {
                executeMove(challenger, player, challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
            }
            challengerCurrentPokemon.checkAilments(false);
            playerWin = checkChallengerFaint();
            if (!playerWin) {
                challengerWin = checkPlayerFaint();

            }
            if (challengerWin) {
                return -1;
            } else {
                return 0;
            }
        }

        if (isPlayerFirst) {
            if (playerCurrentPokemon.checkAilments(true)) {
                playerDamageDealt = executeMove(player, challenger, playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
            }

            playerWin = checkChallengerFaint();

            if (playerWin) {
                return 1;
            }

            playerCurrentPokemon.checkAilments(false);
            challengerWin = checkPlayerFaint();

            if (challengerWin) {
                return -1;
            }

            Thread.sleep(2000);

            if (playerDamageDealt > 0 && playerMove.getMoveInfo().isFlinch()) {
                System.out.println(challengerCurrentPokemon.getName() + " flinched!");
            } else {
                if (challengerCurrentPokemon.checkAilments(true)) {
                    executeMove(challenger, player, challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
                }

                challengerWin = checkPlayerFaint();

                if (challengerWin) {
                    return -1;
                }

                challengerCurrentPokemon.checkAilments(false);
                playerWin = checkChallengerFaint();
                if (playerWin) {
                    return 1;
                }
                Thread.sleep(2000);
            }

        } else {
            if (challengerCurrentPokemon.checkAilments(true)) {
                challengerDamageDealt = executeMove(challenger, player, challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
            }

            challengerWin = checkPlayerFaint();

            if (challengerWin) {
                return -1;
            }

            challengerCurrentPokemon.checkAilments(false);
            playerWin = checkChallengerFaint();
            if (playerWin) {
                return 1;
            }

            Thread.sleep(2000);


            if (challengerDamageDealt > 0 && challengerMove.getMoveInfo().isFlinch()) {
                System.out.println(playerCurrentPokemon.getName() + " flinched!");
            } else {
                if (playerCurrentPokemon.checkAilments(true)) {
                    executeMove(player, challenger, playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
                }
                playerWin = checkChallengerFaint();
                if(playerWin){
                    return 1;
                }

                playerCurrentPokemon.checkAilments(true);
                challengerWin = checkPlayerFaint();
                if (challengerWin) {
                    return -1;
                }

                Thread.sleep(2000);
            }
        }

       return 0;
    }

    /**
     * Method that checks if the player should attack first.
     * @param playerMove The move selected by the player.
     * @param challengerMove The move selected by the challenger.
     * @return true if player should attack first.
     */
    public boolean playerAttackFirst(Move playerMove, Move challengerMove) {
        if (playerMove == null) {
            return false;
        }
        if (playerMove.getPriority() > challengerMove.getPriority()) {
            return true;
        } else if (challengerMove.getPriority() > playerMove.getPriority()) {
            return false;
        }

        if (playerCurrentPokemon.getCurrentSpeed() > challengerCurrentPokemon.getCurrentSpeed()) {
            return true;
        } else if (challengerCurrentPokemon.getCurrentSpeed() > playerCurrentPokemon.getCurrentSpeed()) {
            return false;
        } else {
            if (new Random().nextBoolean()) {
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Move that checks if the player lost after having a pokemon faint.
     * @return true if player has no more pokemons to switch in, false if they do.
     */
    private boolean checkPlayerFaint() {
        if (playerCurrentPokemon.getCurrentHp() <= 0) {
            Pokemon sentOutPokemon = pokemonFaint(player);
            if (sentOutPokemon == null) {
                return true;
            } else {
                playerCurrentPokemon = sentOutPokemon;
            }
        }

        return false;
    }

    /**
     * Method that checks if the challenger has more pokemon to switch in after one faints.
     * @return true if challenger has no more pokemon to swithc in, false if they do.
     */
    private boolean checkChallengerFaint() {
        if (challengerCurrentPokemon.getCurrentHp() <= 0) {
            Pokemon sentOutPokemon = pokemonFaint(challenger);
            if (sentOutPokemon == null) {
                return true;
            } else {
                challengerCurrentPokemon = sentOutPokemon;
            }
        }

        return false;
    }

    /**
     * Method that handles the pokemon to switch in for the a player when one faints.
     * @param trainer the trainer to have pokemon switched in.
     * @return the pokemon to switch in or null if no more pokemon.
     */
    private Pokemon pokemonFaint(Trainer trainer) {
        if (trainer.nonFaintedPokemon().size() > 0) {
            int i = 1;
            for (Pokemon p : trainer.nonFaintedPokemon()) {
                System.out.println(i + ".\n");
                System.out.println(p.pokemonBattleInfo());
                i++;
            }
            System.out.println("Select Pokemon:");
            Scanner in = new Scanner(System.in);

            int selection = in.nextInt();
            while (selection < 1 || selection > i) {
                System.out.println("Select a Pokemon");
                selection = in.nextInt();
            }

            return trainer.nonFaintedPokemon().get(selection);
        }

        return null;
    }

    /**
     * method that handles a moves execution for a Trainer.
     * @param attacker the trainer that is attacking.
     * @param defender the trainer that is defending.
     * @param attackerPokemon the attacker pokemon.
     * @param defenderPokemon the defending pokemon.
     * @param moveUsed the move used by the attacker.
     * @param opponentsMove the move used by the defender.
     * @return the damage dealt by the move.
     * @throws InterruptedException exception to use thread.sleep.
     */
    private int executeMove(Trainer attacker, Trainer defender, Pokemon attackerPokemon, Pokemon defenderPokemon, Move moveUsed, Move opponentsMove) throws InterruptedException {
        String effectivenessString = "";
        double effectiveness = defenderPokemon.getEffectiveness(moveUsed.getType());
        int defenderHpBeginTurn = defenderPokemon.getCurrentHp();
        int damageDealt = 0;

        if (!(moveUsed.getCategory().equals(MoveCategory.STATUS))) {
            if (effectiveness == 0.5) {
                effectivenessString = "it's not very effective.";
            } else if (effectiveness == 2) {
                effectivenessString = "it's super effective!";
            } else if (effectiveness == 4) {
                effectivenessString = "it's extremely effective!!!";
            }
        }


        System.out.println("\n" + attackerPokemon.getName() + " used " + moveUsed.getName() + " " + effectivenessString + "\n");
        Random rd = new Random();
        int attackChange = moveUsed.getMoveInfo().getStatChange(0);
        int defenseChange = moveUsed.getMoveInfo().getStatChange(1);
        int spAttackChange = moveUsed.getMoveInfo().getStatChange(2);
        int spDefChange = moveUsed.getMoveInfo().getStatChange(3);
        int speedChange = moveUsed.getMoveInfo().getStatChange(4);
        int accuracyChange = moveUsed.getMoveInfo().getStatChange(5);
        int evasionChange = moveUsed.getMoveInfo().getStatChange(6);

        switch (moveUsed.getMoveType()) {
            case DAMAGE -> {
                damageDealt = executeDamageMove(attackerPokemon, defenderPokemon, moveUsed, opponentsMove);
                System.out.println("\n" + defenderPokemon.getName() + ": " + defenderHpBeginTurn + "/" + defenderPokemon.getCurrentMaxHp() + " --> " + defenderPokemon.getCurrentHp() + "/" + defenderPokemon.getCurrentMaxHp() + "\n");
            }
            case DAMAGE_HEAL -> {
                damageDealt = executeDamageMove(attackerPokemon, defenderPokemon, moveUsed, opponentsMove);
                System.out.println("\n" + defenderPokemon.getName() + ": " + defenderHpBeginTurn + "/" + defenderPokemon.getCurrentMaxHp() + " --> " + defenderPokemon.getCurrentHp() + "/" + defenderPokemon.getCurrentMaxHp() + "\n");
                if (damageDealt > 0) {
                    drainMove(attackerPokemon, moveUsed, damageDealt);
                }
            }
            case DAMAGE_LOWER -> {
                damageDealt = executeDamageMove(attackerPokemon, defenderPokemon, moveUsed, opponentsMove);
                System.out.println("\n" + defenderPokemon.getName() + ": " + defenderHpBeginTurn + "/" + defenderPokemon.getCurrentMaxHp() + " --> " + defenderPokemon.getCurrentHp() + "/" + defenderPokemon.getCurrentMaxHp() + "\n");
                int statLowerChance = moveUsed.getMoveInfo().getStatChance();
                int randomValue = rd.nextInt(100);
                if (randomValue < statLowerChance && damageDealt > 0) {
                    defenderPokemon.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                }
            }
            case NET_GOOD_STATS -> {
                if (moveUsed.getTarget().equals(Target.USER) || moveUsed.getTarget().equals(Target.ALLY)) {
                    attackerPokemon.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                } else if (moveUsed.getTarget().equals(Target.SELECTED_POKEMON)) {
                    defenderPokemon.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                }
            }
            case DAMAGE_AILMENT -> {
                damageDealt = executeDamageMove(attackerPokemon, defenderPokemon, moveUsed, opponentsMove);
                System.out.println("\n" + defenderPokemon.getName() + ": " + defenderHpBeginTurn + "/" + defenderPokemon.getCurrentMaxHp() + " --> " + defenderPokemon.getCurrentHp() + "/" + defenderPokemon.getCurrentMaxHp() + "\n");
                int ailmentChance = moveUsed.getMoveInfo().getAilmentChance();
                if (rd.nextInt(101) < ailmentChance && damageDealt > 0) {
                    Ailment moveAilment = moveUsed.getMoveInfo().getAilment();
                    boolean addedAilment = defenderPokemon.addAilment(attackerPokemon, moveAilment);
                    if (addedAilment) {
                        System.out.println(defenderPokemon.getName() + " was inflicted with " + moveAilment);
                    } else {
                        System.out.println(defenderPokemon.getName() + " can not be inflicted with " + moveAilment);
                    }
                }
            }
            case DAMAGE_RAISE -> {
                damageDealt = executeDamageMove(attackerPokemon, defenderPokemon, moveUsed, opponentsMove);
                System.out.println("\n" + defenderPokemon.getName() + ": " + defenderHpBeginTurn + "/" + defenderPokemon.getCurrentMaxHp() + " --> " + defenderPokemon.getCurrentHp() + "/" + defenderPokemon.getCurrentMaxHp() + "\n");
                if (rd.nextInt(101) < moveUsed.getEffectChance() && damageDealt > 0) {
                    attackerPokemon.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                }
            }
            case AILMENT -> {
                if (rd.nextInt(101) < moveUsed.getAccuracy()) {
                    if (defenderPokemon.getEffectiveness(moveUsed.getType()) != 0) {
                        defenderPokemon.addAilment(attackerPokemon, moveUsed.getMoveInfo().getAilment());
                    } else {
                        System.out.println("Pokemon is not affected by this type.");
                    }
                } else {
                    System.out.println("Move missed.");
                }
            }
            case FIELD_EFFECT -> {
                if (addFieldEffect(attacker, moveUsed)) {
                    checkBarriers(attacker);
                }
            }
            case UNIQUE -> {
                executeUniqueMove(attackerPokemon, defenderPokemon, moveUsed);
            }
        }

        return damageDealt;
    }

    /**
     * Method that checks for a players barriers.
     * @param trainer the trainer to have its barriers checked.
     */
    private void checkBarriers(Trainer trainer) {
        for (FieldEffect fe : getTrainerFieldEffects(trainer)) {
            if (fe instanceof Barrier b) {
                b.checkBarriers(trainer, this);
            }
        }
    }

    /**
     * Method to execute Unique moves with very specific behaviours.
     * @param attacker the attaciking pokemon.
     * @param defender the defending pokemon.
     * @param moveUsed the move used by the attacker.
     */
    private void executeUniqueMove(Pokemon attacker, Pokemon defender, Move moveUsed) {
        switch (moveUsed.getName()) {
            case "acupressure":
                int[] statArray = new int[7];
                Random rd = new Random();
                int randomStatIndex = rd.nextInt(7);
                if (statArray[randomStatIndex] == 7) {
                    System.out.println("Acupressure failed.");
                } else {
                    statArray[randomStatIndex] += 2;
                }
                attacker.updateStatModifiers(statArray[0], statArray[1], statArray[2], statArray[3], statArray[4], statArray[5], statArray[6]);
                break;
            default:
                System.out.println("Not implemented sorry, I'll try implement more unique moves next time :)");
        }
    }

    /**
     * Method that handles moves that have a drain to them recoil or healing.
     * @param attacker the attacking pokemon.
     * @param moveUsed the move used by the attacker.
     * @param damageDealt the damage dealt by the move.
     */
    private void drainMove(Pokemon attacker, Move moveUsed, int damageDealt) {
        double drain = ((double) moveUsed.getDrain() / 100) * damageDealt;
        if (drain > 0) {
            attacker.healCurrentHP((int) Math.floor(drain));
            System.out.println(attacker.getName() + " healed.");
        } else {
            attacker.takeDamage((int) Math.floor(drain));
            System.out.println(attacker.getName() + " took recoil.");
        }
    }

    /**
     * Method that handles moves that deal damage.
     * @param attacker the attacking pokemon.
     * @param defender the defending pokemon.
     * @param moveUsed the move used by the attacker.
     * @param opponentsMove the move used by the opponent.
     * @return the damage dealt.
     * @throws InterruptedException exception to use thread.sleep
     */
    private int executeDamageMove(Pokemon attacker, Pokemon defender, Move moveUsed, Move opponentsMove) throws InterruptedException {
        int damageDealt = 0;

        if (moveUsed.getMoveInfo().getMinHits() > 0) {
            Random rd = new Random();
            int hits = rd.nextInt(moveUsed.getMoveInfo().getMaxHits() - moveUsed.getMoveInfo().getMinHits() + 1) + moveUsed.getMoveInfo().getMinHits();
            int totalDamage = 0;
            for (int i = 0; i < hits; i++) {
                System.out.println("Hit " + (i + 1));
                Thread.sleep(1000);
                damageDealt = calculateDamage(attacker, defender, moveUsed);
                totalDamage = totalDamage + damageDealt;
                int defenderHpBeforeHit = defender.getCurrentHp();
                defender.takeDamage(damageDealt);
                System.out.println("\n" + defenderHpBeforeHit + "/" + defender.getCurrentMaxHp() + " --> " + defender.getCurrentHp() + "/" + defender.getCurrentMaxHp() + "\n");
                if (damageDealt == 0) {
                    break;
                }
            }

            return totalDamage;
        } else {
            damageDealt = calculateDamage(attacker, defender, moveUsed);
            if (moveUsed.getName().equalsIgnoreCase("acrobatics")) {
                if (!attacker.hasHeldItem()) {
                    defender.takeDamage(damageDealt * 2);
                    return damageDealt * 2;
                }
            }
            defender.takeDamage(damageDealt);
        }

        return damageDealt;
    }

    /**
     * Method that calulates damage dealt by moves that deal damage.
     * @param attacker the attacking pokemon.
     * @param defender the defending pokemon.
     * @param moveUsed the move used by the attacker.
     * @return the damage dealt.
     */
    private int calculateDamage(Pokemon attacker, Pokemon defender, Move moveUsed) {
        Random rand = new Random();
        int crit = moveUsed.getCrit();
        int baseAccuracy = moveUsed.getAccuracy();
        double attackerAccuracy = attacker.getAccuracy();
        double defenderEvasiness = defender.getEvasiness();

        boolean hits = true;
        if (baseAccuracy != -1) {
            double effectiveAccuracy = calculateEffectiveAccuracy(baseAccuracy, attackerAccuracy, defenderEvasiness);
            hits = isHit(effectiveAccuracy);
        }

        if (!hits) {
            System.out.println("The attack missed!");
            return 0;
        }

        double randomMultiplier = (rand.nextInt(16) + 85) / 100.0;
        double baseDamage = ((((double) (((2 * attacker.getLevel() * crit / 5) + 2) * moveUsed.getPower() * attacker.getStatForAttackMove(defender, moveUsed)) / defender.getStatForDefense(attacker, moveUsed) / 50) + 2) * attacker.getStab(moveUsed) * defender.getEffectiveness(moveUsed.getType()) * randomMultiplier);

        int finalDamage = (int) Math.floor(baseDamage);

        if (crit == 2) {
            System.out.println("It's a critical hit!");
        }

        return finalDamage;
    }

    /**
     * Method that calulates the chances of a move hitting taking into account the moves base accuracy and accuracy modifiers.
     * @param baseAccuracy
     * @param attackerAccuracy
     * @param defenderEvasiness
     * @return
     */
    private double calculateEffectiveAccuracy(int baseAccuracy, double attackerAccuracy, double defenderEvasiness) {
        double accuracyModifier = (0.1 * (attackerAccuracy + 6)) / (0.1 * (defenderEvasiness + 6));
        double effectiveAccuracy = baseAccuracy * accuracyModifier;

        // Ensure effective accuracy is within the range [0, 100]
        effectiveAccuracy = Math.max(0, Math.min(100, effectiveAccuracy));

        return effectiveAccuracy;
    }

    /**
     * Method that checks if a move hits.
     * @param effectiveAccuracy the effective accuracy of a move hitting.
     * @return true if move hits, false if not.
     */
    private boolean isHit(double effectiveAccuracy) {
        Random rand = new Random();
        int roll = rand.nextInt(101);
        return roll <= effectiveAccuracy;
    }

    /**
     * Method that selects a move for the challenger.
     * @return The move selected by the challenger.
     */
    private Move selectChallengerMove() {
        Move[] moves = challengerCurrentPokemon.getMoves();
        Move selectedMove = null;

        while (selectedMove == null) {
            Random random = new Random();
            int randomNumber = random.nextInt(4);
            selectedMove = moves[randomNumber];
        }

        return selectedMove;
    }

    /**
     * Method that handles move selection for the player.
     * @return The move selected by the player, or null if player decided to go back.
     */
    private Move selectPlayerMove() {
        Scanner in = new Scanner(System.in);
        Move[] moves = playerCurrentPokemon.getMoves();
        System.out.println("Choose move 1, 2, 3 or 4. 5 to go Back");
        System.out.println(playerCurrentPokemon.getMovesString());
        int moveSelection = in.nextInt() - 1;
        while (moveSelection < 0 || moveSelection > 4) {
            System.out.println("Choose move 1, 2, 3 or 4. 5 to go Back");
            moveSelection = in.nextInt() - 1;
        }

        return moveSelection == 4 ? null : moves[moveSelection];
    }

    /**
     * Method that converts battle info into a string.
     * @return the battle info as a String.
     */
    @Override
    public String toString() {
        StringBuilder battleDisplay = new StringBuilder();

        // Challenger's information
        battleDisplay.append("~~~~~~~~~~~~~~ OPPONENT ~~~~~~~~~~~~~~\n");
        battleDisplay.append(String.format("Trainer: %s\n", challenger.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", challengerCurrentPokemon.getName()));
        battleDisplay.append(String.format("Gender: %s\n", challengerCurrentPokemon.getGender()));
        battleDisplay.append(String.format("HP: %d/%d\n", challengerCurrentPokemon.getCurrentHp(), challengerCurrentPokemon.getCurrentMaxHp()));
        battleDisplay.append("Current Stats:\n");
        battleDisplay.append(String.format("%-15s %-15s\n", "Attack", "Defense"));
        battleDisplay.append(String.format("%-15d %-15d\n", challengerCurrentPokemon.getCurrentAttack(), challengerCurrentPokemon.getCurrentDefense()));
        battleDisplay.append(String.format("%-15s %-15s\n", "Special Attack", "Special Defense"));
        battleDisplay.append(String.format("%-15d %-15d\n", challengerCurrentPokemon.getCurrentSpecialAttack(), challengerCurrentPokemon.getCurrentSpecialDefense()));
        battleDisplay.append(String.format("%-15s %-15s\n", "Speed", "Accuracy"));
        battleDisplay.append(String.format("%-15d %-15d\n", challengerCurrentPokemon.getCurrentSpeed(), challengerCurrentPokemon.getAccuracyModifier()));
        battleDisplay.append(String.format("%-15s\n", "Evasiveness"));
        battleDisplay.append(String.format("%-15d\n", challengerCurrentPokemon.getEvasivenessModifier()));
        battleDisplay.append(String.format("Status: %s\n", challengerCurrentPokemon.getAilmentsString()));
        battleDisplay.append("Field Effects:\n");
        if (challengerFieldEffects.isEmpty()) {
            battleDisplay.append("None\n");
        } else {
            for (FieldEffect effect : challengerFieldEffects) {
                battleDisplay.append("- ").append(effect.getName()).append("\n");
            }
        }
        battleDisplay.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        // Player's information
        battleDisplay.append("~~~~~~~~~~~~~~~ PLAYER ~~~~~~~~~~~~~~~\n");
        battleDisplay.append(String.format("Trainer: %s\n", player.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", playerCurrentPokemon.getName()));
        battleDisplay.append(String.format("Gender: %s\n", playerCurrentPokemon.getGender()));
        battleDisplay.append(String.format("HP: %d/%d\n", playerCurrentPokemon.getCurrentHp(), playerCurrentPokemon.getCurrentMaxHp()));
        battleDisplay.append("Current Stats:\n");
        battleDisplay.append(String.format("%-15s %-15s\n", "Attack", "Defense"));
        battleDisplay.append(String.format("%-15d %-15d\n", playerCurrentPokemon.getCurrentAttack(), playerCurrentPokemon.getCurrentDefense()));
        battleDisplay.append(String.format("%-15s %-15s\n", "Special Attack", "Special Defense"));
        battleDisplay.append(String.format("%-15d %-15d\n", playerCurrentPokemon.getCurrentSpecialAttack(), playerCurrentPokemon.getCurrentSpecialDefense()));
        battleDisplay.append(String.format("%-15s %-15s\n", "Speed", "Accuracy"));
        battleDisplay.append(String.format("%-15d %-15d\n", playerCurrentPokemon.getCurrentSpeed(), playerCurrentPokemon.getAccuracyModifier()));
        battleDisplay.append(String.format("%-15s\n", "Evasiveness"));
        battleDisplay.append(String.format("%-15d\n", playerCurrentPokemon.getEvasivenessModifier()));
        battleDisplay.append(String.format("Status: %s\n", playerCurrentPokemon.getAilmentsString()));
        battleDisplay.append("Field Effects:\n");
        if (playerFieldEffects.isEmpty()) {
            battleDisplay.append("None\n");
        } else {
            for (FieldEffect effect : playerFieldEffects) {
                battleDisplay.append("- ").append(effect.getName()).append("\n");
            }
        }
        battleDisplay.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n\n");

        // Weather and Turn Count
        battleDisplay.append(String.format("Weather: %s\n", weather.getName()));
        battleDisplay.append(String.format("Turn: %d\n", turnCount));
        battleDisplay.append("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~\n");

        return battleDisplay.toString();
    }


    /**
     * Method that adds a field effect to a given trainer.
     * @param trainer The trainer that used the field effect.
     * @param moveUsed The move associated with the field effect.
     * @return true if field effect added successfully false if not.
     */
    public boolean addFieldEffect(Trainer trainer, Move moveUsed) {
        String moveName = moveUsed.getName();
        FieldEffect fe = checkIfFieldEffectHasBeenAdded(trainer, moveUsed.getName());

        switch (moveName) {
            case "spikes", "toxic-spikes":
                if (fe != null) {
                    EntryHazard spikes = (EntryHazard) fe;
                    spikes.addLayer();
                } else {
                    EntryHazard spikes = new EntryHazard(moveUsed.getName(), moveUsed);
                    this.getTrainerFieldEffects(trainer).add(spikes);
                }

                break;
            case "sticky-web":
                if (fe != null) {
                    System.out.println("But it failed.");
                    return false;
                } else {
                    EntryHazard stickyWeb = new EntryHazard(moveUsed.getName(), moveUsed);
                    this.getTrainerFieldEffects(trainer).add(stickyWeb);
                }
                break;
            case "aurora-veil":
                if (fe != null) {
                    System.out.println("But it failed.");
                    return false;
                } else {
                    if (this.weather.getName().equalsIgnoreCase("hail")) {
                        FieldEffect reflect = checkIfFieldEffectHasBeenAdded(trainer, "reflect");
                        FieldEffect lightScreen = checkIfFieldEffectHasBeenAdded(trainer, "light-screen");
                        if (reflect == null && lightScreen == null) {
                            Barrier barrier = new Barrier(moveUsed.getName(), moveUsed, 5);
                            this.getTrainerFieldEffects(trainer).add(barrier);
                            System.out.println("Aurora veil protects your team.");
                        } else {
                            System.out.println("But it failed.");

                        }
                    } else {
                        System.out.println("But it failed.");
                    }
                }
                break;
            case "reflect", "light-screen":
                if (fe != null) {
                    System.out.println("But it failed.");
                    return false;
                } else {
                    FieldEffect auroraVeil = checkIfFieldEffectHasBeenAdded(trainer, "aurora-veil");
                    if (auroraVeil == null) {
                        Barrier barrier = new Barrier(moveUsed.getName(), moveUsed, 5);
                        this.getTrainerFieldEffects(trainer).add(barrier);
                        System.out.println("Barrier protects your team.");
                    } else {
                        System.out.println("But it failed.");
                        return false;
                    }
                }
                break;
        }

        return true;
    }

    /**
     * Method that checks if field effect has been added before.
     * @param trainer The trainer to have their field effects checked.
     * @param moveName The name of the field effect.
     * @return null if it has not been added, or the field effect if it has been added.
     */
    public FieldEffect checkIfFieldEffectHasBeenAdded(Trainer trainer, String moveName) {
        for (FieldEffect fe : this.getTrainerFieldEffects(trainer)) {
            if (fe.getName().equalsIgnoreCase(moveName)) {
                return fe;
            }
        }

        return null;
    }

    /**
     * Method that returns a trainers field effects.
     * @param trainer The trainer to have its field effects returned.
     * @return the list of a trainers field effects.
     */
    public ArrayList<FieldEffect> getTrainerFieldEffects(Trainer trainer) {
        if (trainer.isPlayer()) {
            return playerFieldEffects;
        } else {
            return challengerFieldEffects;
        }
    }

    /**
     * Method that returns a trainers current pokemon.
     * @param trainer the trainer to have their current pokemon returned.
     * @return the trainers current pokemon.
     */
    public Pokemon getTrainerCurrentPokemon(Trainer trainer) {
        if (trainer.isPlayer()) {
            return playerCurrentPokemon;
        } else {
            return challengerCurrentPokemon;
        }
    }
}
