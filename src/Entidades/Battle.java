package Entidades;

import Entidades.Moves.Move;
import Enums.Target;

import java.util.Random;
import java.util.Scanner;

public class Battle {
    private Trainer player;
    private Pokemon playerCurrentPokemon;
    private Trainer challenger;
    private Pokemon challengerCurrentPokemon;
    private int turnCount;


    public Battle(Trainer player, Pokemon playerCurrentPokemon, Trainer challenger, Pokemon challengerCurrentPokemon, int turnCount) {
        this.player = player;
        this.playerCurrentPokemon = playerCurrentPokemon;
        this.challenger = challenger;
        this.challengerCurrentPokemon = challengerCurrentPokemon;
        this.turnCount = turnCount;
    }

    public int turn() throws InterruptedException {
        this.turnCount = this.turnCount + 1;
        Move playerMove = selectPlayerMove();
        Move challengerMove = selectChallengerMove();
        boolean playerWin = false;
        boolean challengerWin = false;
        boolean isPlayerFirst = playerAttackFirst(playerMove, challengerMove);

        if (isPlayerFirst) {
            executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
            playerWin = checkChallengerFaint();
            Thread.sleep(2000);
            if (!playerWin) {
                executeMove(challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
                challengerWin = checkPlayerFaint();
                Thread.sleep(2000);
            }
        } else {
            executeMove(challengerCurrentPokemon, playerCurrentPokemon, challengerMove, playerMove);
            challengerWin = checkPlayerFaint();
            Thread.sleep(2000);
            if (!challengerWin) {
                executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove, challengerMove);
                playerWin = checkChallengerFaint();
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

    private void executeMove(Pokemon attacker, Pokemon defender, Move moveUsed, Move opponentsMove) throws InterruptedException {
        String effectivenessString = "";
        double effectiveness = defender.getEffectiveness(moveUsed);
        int defenderHpBeginTurn = defender.getCurrentHp();

        if (effectiveness == 0.5) {
            effectivenessString = "it's not very effective.";
        } else if (effectiveness == 2) {
            effectivenessString = "it's super effective!";
        } else if (effectiveness == 4) {
            effectivenessString = "it's extremely effective!!!";
        }

        System.out.println("\n" + attacker.getName() + " used " + moveUsed.getName() + " " + effectivenessString + "\n");
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
                executeDamageMove(attacker, defender, moveUsed, opponentsMove);
                System.out.println("\n" + defender.getName() + ": " + defenderHpBeginTurn + "/" + defender.getCurrentMaxHp() + " --> " + defender.getCurrentHp() + "/" + defender.getCurrentMaxHp() + "\n");
            }
            case DAMAGE_HEAL -> {
                int damageDealt = executeDamageMove(attacker, defender, moveUsed, opponentsMove);
                if (damageDealt > 0) {
                    drainMove(attacker, moveUsed, damageDealt);
                }
            }
            case DAMAGE_LOWER -> {
                executeDamageMove(attacker, defender, moveUsed, opponentsMove);
                int statLowerChance = moveUsed.getMoveInfo().getStatChance();
                int randomValue = rd.nextInt(100);
                if (randomValue < statLowerChance) {
                    defender.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                }
            }
            case NET_GOOD_STATS -> {
                if (moveUsed.getTarget().equals(Target.USER)) {
                    attacker.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                } else if(moveUsed.getTarget().equals(Target.SELECTED_POKEMON)){
                    defender.updateStatModifiers(attackChange, defenseChange, spAttackChange, spDefChange, speedChange, accuracyChange, evasionChange);
                }
            }
            case UNIQUE -> {
                executeUniqueMove(attacker, defender, moveUsed);
            }
        }
    }

    private void executeUniqueMove(Pokemon attacker, Pokemon defender, Move moveUsed) {
        switch (moveUsed.getName()){
            case "acupressure":
                int[] statArray = new int[7];
                Random rd = new Random();
                int randomStatIndex = rd.nextInt(7);
                if(statArray[randomStatIndex] == 7) {
                    System.out.println("Acupressure failed.");
                } else {
                    statArray[randomStatIndex] += 2;
                }
                attacker.updateStatModifiers(statArray[0], statArray[1], statArray[2], statArray[3],statArray[4],statArray[5],statArray[6]);
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

            if(moveUsed.getName().equalsIgnoreCase("acrobatics")){
                if(!attacker.hasHeldItem()){
                    return totalDamage * 2;
                }
            }

            return totalDamage;
        } else {
            damageDealt = calculateDamage(attacker, defender, moveUsed);
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
        double baseDamage = ((((double) (((2 * attacker.getLevel() * crit / 5) + 2) * moveUsed.getPower() * attacker.getStatForAttackMove(defender, moveUsed)) / defender.getStatForDefense(attacker, moveUsed) / 50) + 2) * attacker.getStab(moveUsed) * defender.getEffectiveness(moveUsed) * randomMultiplier);

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
        System.out.println("Choose move 1, 2, 3 or 4");
        System.out.println(playerCurrentPokemon.getMovesString());
        int moveSelection = in.nextInt() - 1;
        while (moveSelection < 0 || moveSelection > 3) {
            System.out.println("Choose move 1, 2, 3 or 4");
            moveSelection = in.nextInt() - 1;
        }

        return moves[moveSelection];
    }

    @Override
    public String toString() {
        StringBuilder battleDisplay = new StringBuilder();

        battleDisplay.append(String.format("Opponent: %s\n", challenger.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", challengerCurrentPokemon.getName()));
        battleDisplay.append(String.format("HP: %d/%d\n", challengerCurrentPokemon.getCurrentHp(), challengerCurrentPokemon.getCurrentMaxHp()));
        if (challengerCurrentPokemon.getAilment() != null && !challengerCurrentPokemon.getAilment().equals("NONE")) {
            battleDisplay.append(String.format("Status: %s\n", challengerCurrentPokemon.getAilment()));
        }
        battleDisplay.append("\n");

        battleDisplay.append("----------------------------------------------------\n");
        battleDisplay.append(String.format("Player: %s\n", player.getName()));
        battleDisplay.append(String.format("Pokémon: %s\n", playerCurrentPokemon.getName()));
        battleDisplay.append(String.format("HP: %d/%d\n", playerCurrentPokemon.getCurrentHp(), playerCurrentPokemon.getCurrentMaxHp()));
        if (playerCurrentPokemon.getAilment() != null && !playerCurrentPokemon.getAilment().equals("NONE")) {
            battleDisplay.append(String.format("Status: %s\n", playerCurrentPokemon.getAilment()));
        }
        battleDisplay.append("\n");

        battleDisplay.append(String.format("Turn: %d\n", turnCount));
        battleDisplay.append("----------------------------------------------------\n");

        return battleDisplay.toString();
    }
}
