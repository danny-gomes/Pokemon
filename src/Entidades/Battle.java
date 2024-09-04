package Entidades;

import Entidades.FieldEffects.FieldEffect;
import Entidades.Moves.Move;
import Enums.Ailment;
import Enums.MoveCategory;
import Enums.Target;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Trainer player;
    private Pokemon playerCurrentPokemon;
    private Trainer challenger;
    private Pokemon challengerCurrentPokemon;
    private int turnCount;
    private ArrayList<FieldEffect> playerFieldEffects;
    private ArrayList<FieldEffect> challengerFieldEffects;


    public Battle(Trainer player, Pokemon playerCurrentPokemon, Trainer challenger, Pokemon challengerCurrentPokemon, int turnCount) {
        this.player = player;
        this.playerCurrentPokemon = playerCurrentPokemon;
        this.challenger = challenger;
        this.challengerCurrentPokemon = challengerCurrentPokemon;
        this.turnCount = turnCount;

        this.playerFieldEffects = new ArrayList<>();
        this.challengerFieldEffects = new ArrayList<>();
    }

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

        return outcome;
    }

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
            playerCurrentPokemon = player.getNonFaintedPokemon(pokemonNames.get(pokemonOption - 1));
            System.out.println("Go, " + playerCurrentPokemon.getName() + "!");
            for(FieldEffect fe : playerFieldEffects){
                fe.checkEntryHazards(playerCurrentPokemon, challengerCurrentPokemon);
            }
        } else {
            return false;
        }

        return true;
    }

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
                playerDamageDealt = executeMove(challenger, player, playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
            }
            playerCurrentPokemon.checkAilments(true);
            challengerWin = checkPlayerFaint();
            if (!challengerWin) {
                playerWin = checkPlayerFaint();
            }
            Thread.sleep(2000);
            if (!playerWin) {
                if (playerDamageDealt > 0 && playerMove.getMoveInfo().isFlinch()) {
                    System.out.println(challengerCurrentPokemon.getName() + " flinched!");
                } else {
                    if (challengerCurrentPokemon.checkAilments(true)) {
                        executeMove(challenger, player, challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
                    }
                    challengerCurrentPokemon.checkAilments(false);
                    playerWin = checkChallengerFaint();
                    if (!playerWin) {
                        challengerWin = checkPlayerFaint();

                    }
                    Thread.sleep(2000);
                }
            }
        } else {
            if (challengerCurrentPokemon.checkAilments(true)) {
                challengerDamageDealt = executeMove(challenger, player, challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
            }
            challengerCurrentPokemon.checkAilments(false);
            playerWin = checkChallengerFaint();
            if (!playerWin) {
                challengerWin = checkPlayerFaint();

            }
            Thread.sleep(2000);
            if (!challengerWin) {
                if (challengerDamageDealt > 0 && challengerMove.getMoveInfo().isFlinch()) {
                    System.out.println(playerCurrentPokemon.getName() + " flinched!");
                } else {
                    if (playerCurrentPokemon.checkAilments(true)) {
                        executeMove(challenger, player, playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
                    }
                    playerCurrentPokemon.checkAilments(true);
                    challengerWin = checkPlayerFaint();
                    if (!challengerWin) {
                        playerWin = checkPlayerFaint();
                    }
                }
                Thread.sleep(2000);
            }
        }

        if (!challengerWin && !playerWin) {
            return 0;
        } else if (challengerWin) {
            return -1;
        } else {
            return 1;
        }
    }

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
                addFieldEffect(attacker, defender, moveUsed);
            }
            case UNIQUE -> {
                executeUniqueMove(attackerPokemon, defenderPokemon, moveUsed);
            }
        }

        return damageDealt;
    }

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

    private int calculateDamage(Pokemon attacker, Pokemon defender, Move moveUsed) {
        Random rand = new Random();
        int crit = moveUsed.getCrit();
        int baseAccuracy = moveUsed.getAccuracy();
        double attackerAccuracy = attacker.getAccuracy();
        double defenderEvasiness = defender.getEvasiness();

        // Determine if the attack hits
        boolean hits = true;
        if (baseAccuracy != -1) {
            double effectiveAccuracy = calculateEffectiveAccuracy(baseAccuracy, attackerAccuracy, defenderEvasiness);
            hits = isHit(effectiveAccuracy);
        }

        if (!hits) {
            System.out.println("The attack missed!");
            return 0;
        }

        // Calculate base damage
        double randomMultiplier = (rand.nextInt(16) + 85) / 100.0;
        double baseDamage = ((((double) (((2 * attacker.getLevel() * crit / 5) + 2) * moveUsed.getPower() * attacker.getStatForAttackMove(defender, moveUsed)) / defender.getStatForDefense(attacker, moveUsed) / 50) + 2) * attacker.getStab(moveUsed) * defender.getEffectiveness(moveUsed.getType()) * randomMultiplier);

        int finalDamage = (int) Math.floor(baseDamage);

        if (crit == 2) {
            System.out.println("It's a critical hit!");
        }

        return finalDamage;
    }

    private double calculateEffectiveAccuracy(int baseAccuracy, double attackerAccuracy, double defenderEvasiness) {
        double accuracyModifier = (0.1 * (attackerAccuracy + 6)) / (0.1 * (defenderEvasiness + 6));
        double effectiveAccuracy = baseAccuracy * accuracyModifier;

        // Ensure effective accuracy is within the range [0, 100]
        effectiveAccuracy = Math.max(0, Math.min(100, effectiveAccuracy));

        return effectiveAccuracy;
    }

    private boolean isHit(double effectiveAccuracy) {
        Random rand = new Random();
        int roll = rand.nextInt(101); // Roll between 0 and 100
        return roll <= effectiveAccuracy;
    }

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

    @Override
    public String toString() {
        StringBuilder battleDisplay = new StringBuilder();

        battleDisplay.append(String.format("Opponent: %s\n", challenger.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", challengerCurrentPokemon.getName()));
        battleDisplay.append(String.format("Gender: %s\n", challengerCurrentPokemon.getGender()));
        battleDisplay.append(String.format("HP: %d/%d\n", challengerCurrentPokemon.getCurrentHp(), challengerCurrentPokemon.getCurrentMaxHp()));
        battleDisplay.append(String.format("Status: %s\n", challengerCurrentPokemon.getAilmentsString()));
        battleDisplay.append("\n");

        battleDisplay.append("----------------------------------------------------\n");
        battleDisplay.append(String.format("Player: %s\n", player.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", playerCurrentPokemon.getName()));
        battleDisplay.append(String.format("Gender: %s\n", playerCurrentPokemon.getGender()));
        battleDisplay.append(String.format("HP: %d/%d\n", playerCurrentPokemon.getCurrentHp(), playerCurrentPokemon.getCurrentMaxHp()));
        battleDisplay.append(String.format("Status: %s\n", playerCurrentPokemon.getAilmentsString()));
        battleDisplay.append("\n");

        battleDisplay.append(String.format("Turn: %d\n", turnCount));
        battleDisplay.append("----------------------------------------------------\n");

        return battleDisplay.toString();
    }


    public void addFieldEffect(Trainer attacker, Trainer defender, Move moveUsed) {
        FieldEffect fe = new FieldEffect(moveUsed.getName(), 5, moveUsed);
        if (moveUsed.getTarget().equals(Target.USERS_FIELD)) {
            if (attacker.isPlayer()) {
                playerFieldEffects.add(fe);
            } else {
                challengerFieldEffects.add(fe);
            }
        } else if (moveUsed.getTarget().equals(Target.OPPONENTS_FIELD)) {
            if (attacker.isPlayer()) {
                challengerFieldEffects.add(fe);
            } else {
                playerFieldEffects.add(fe);
            }
        }
    }
}
