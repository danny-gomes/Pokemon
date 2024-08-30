package Entidades;

import Entidades.Moves.Move;

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

    public int turn() {
        Move playerMove = selectPlayerMove();
        Move challengerMove = selectChallengerMove();
        boolean playerWin = false;
        boolean challengerWin = false;
        boolean isPlayerFirst = playerAttackFirst(playerMove, challengerMove);

        if(isPlayerFirst){
            executeMove(playerCurrentPokemon,challengerCurrentPokemon,playerMove);
            playerWin = checkChallengerFaint();
            if(!playerWin){
                executeMove(challengerCurrentPokemon, playerCurrentPokemon, challengerMove);
                challengerWin = checkPlayerFaint();
            }
        } else {
            executeMove(challengerCurrentPokemon, playerCurrentPokemon, challengerMove);
            challengerWin = checkPlayerFaint();
            if(!challengerWin){
                executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove);
                playerWin = checkChallengerFaint();
            }
        }

        if(!challengerWin && !playerWin){
            return 0;
        } else if(challengerWin) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean playerAttackFirst(Move playerMove, Move challengerMove){
        if (playerMove.getPriority() > challengerMove.getPriority()) {
            executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove);
            return true;
        } else if (challengerMove.getPriority() > playerMove.getPriority()) {
            executeMove(challengerCurrentPokemon, playerCurrentPokemon, playerMove);
            return false;
        }

        if (playerCurrentPokemon.getCurrentSpeed() > challengerCurrentPokemon.getCurrentSpeed()) {
            executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove);
            return true;
        } else if (challengerCurrentPokemon.getCurrentSpeed() > playerCurrentPokemon.getCurrentSpeed()) {
            executeMove(challengerCurrentPokemon, playerCurrentPokemon, playerMove);
            return false;
        } else {
            if (new Random().nextBoolean()) {
                executeMove(playerCurrentPokemon, challengerCurrentPokemon, playerMove);
                return true;
            } else {
                executeMove(challengerCurrentPokemon, playerCurrentPokemon, playerMove);
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

    private void executeMove(Pokemon attacker, Pokemon defender, Move moveUsed) {

    }

    private Move selectChallengerMove() {
        Move[] moves = challengerCurrentPokemon.getMoves();

        Random random = new Random();
        int randomNumber = random.nextInt(4) + 1;

        return moves[randomNumber];
    }

    private Move selectPlayerMove() {
        Scanner in = new Scanner(System.in);
        Move[] moves = playerCurrentPokemon.getMoves();
        System.out.println(playerCurrentPokemon.getMovesString());
        int moveSelection;
        do {
            System.out.println("Choose move 1, 2, 3 or 4");
            moveSelection = in.nextInt();
        } while (moveSelection < 1 || moveSelection > 4);

        return moves[moveSelection];
    }
}
