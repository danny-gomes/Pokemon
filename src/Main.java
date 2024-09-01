import Entidades.*;
import Entidades.Moves.Move;
import Enums.Ailment;
import Enums.MoveCategory;
import Enums.MoveType;
import Ficheiros.ReadPokemons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Pokedex pokedex = new Pokedex();
        ArrayList<Move> allMoves = pokedex.getAllMoves();
        Pokemon turtwig = pokedex.getPokemonByName("chimchar");
        ArrayList<Pokemon> playerParty = new ArrayList<>();
        playerParty.add(turtwig);
        ArrayList<Pokemon> pc = new ArrayList<>();

        Pokemon piplup = pokedex.getPokemonByName("piplup");
        ArrayList<Pokemon> opponentParty = new ArrayList<>();
        opponentParty.add(piplup);

        int count = 0;
        for (Move m : allMoves) {
            if (m.getName().equalsIgnoreCase("amnesia")) {
                turtwig.learnNewMove(m);
            }
            if (m.getMoveInfo().getAilment().equals(Ailment.TRAP)) {
                System.out.println(m);
            }
        }
        System.out.println("TOTAL: " + count);

        Bag playerBag = new Bag();

        Trainer player = new Trainer("Danny", playerBag, playerParty, pc, 500);
        Trainer opponent = new Trainer("Isa", playerBag, opponentParty, pc, 500);

        Battle battle = new Battle(player, playerParty.get(0), opponent, opponentParty.get(0), 0);

        int battleOver = 0;
        System.out.println(battle);

        System.out.println(turtwig.getCurrentStatsString());
        while (battleOver == 0) {

            battleOver = battle.turn();
            System.out.println(turtwig.getCurrentStatsString());
            Thread.sleep(2000);
            System.out.println(turtwig.getCurrentStatsString());

            System.out.println(battle);
        }

        if (battleOver == -1) {
            System.out.println(opponent.getName() + " wins.");
        } else {
            System.out.println(player.getName() + " wins.");
        }

    }
}
