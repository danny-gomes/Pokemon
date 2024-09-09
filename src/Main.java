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

        Pokemon garchomp = pokedex.getPokemonByName("garchomp");
        Pokemon spiritomb = pokedex.getPokemonByName("spiritomb");
        Pokemon togekiss = pokedex.getPokemonByName("togekiss");
        Pokemon lucario = pokedex.getPokemonByName("lucario");
        Pokemon roserade = pokedex.getPokemonByName("roserade");
        Pokemon milotic = pokedex.getPokemonByName("milotic");

        ArrayList<Pokemon> cynthiaParty = new ArrayList<>();
        cynthiaParty.add(garchomp);
        cynthiaParty.add(spiritomb);
        cynthiaParty.add(togekiss);
        cynthiaParty.add(lucario);
        cynthiaParty.add(roserade);
        cynthiaParty.add(milotic);

        Pokemon pikachu = pokedex.getPokemonByName("pikachu");
        Pokemon charizard = pokedex.getPokemonByName("charizard");
        Pokemon snorlax = pokedex.getPokemonByName("snorlax");
        Pokemon sceptile = pokedex.getPokemonByName("sceptile");
        Pokemon infernape = pokedex.getPokemonByName("infernape");
        Pokemon greninja = pokedex.getPokemonByName("greninja");

        ArrayList<Pokemon> ashParty = new ArrayList<>();
        ashParty.add(pikachu);
        ashParty.add(charizard);
        ashParty.add(snorlax);
        ashParty.add(sceptile);
        ashParty.add(infernape);
        ashParty.add(greninja);

        garchomp.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("earthquake"),
                pokedex.getMoveByName("dragon-claw"),
                pokedex.getMoveByName("stone-edge"),
                pokedex.getMoveByName("swords-dance")
        });

        spiritomb.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("shadow-ball"),
                pokedex.getMoveByName("dark-pulse"),
                pokedex.getMoveByName("will-o-wisp"),
                pokedex.getMoveByName("nasty-plot")
        });

        togekiss.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("air-slash"),
                pokedex.getMoveByName("aura-sphere"),
                pokedex.getMoveByName("thunder-wave"),
                pokedex.getMoveByName("roost")
        });

        lucario.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("close-combat"),
                pokedex.getMoveByName("bullet-punch"),
                pokedex.getMoveByName("extreme-speed"),
                pokedex.getMoveByName("swords-dance")
        });

        roserade.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("sludge-bomb"),
                pokedex.getMoveByName("energy-ball"),
                pokedex.getMoveByName("shadow-ball"),
                pokedex.getMoveByName("toxic-spikes")
        });

        milotic.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("scald"),
                pokedex.getMoveByName("ice-beam"),
                pokedex.getMoveByName("recover"),
                pokedex.getMoveByName("toxic")
        });

        pikachu.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("thunderbolt"),
                pokedex.getMoveByName("iron-tail"),
                pokedex.getMoveByName("quick-attack"),
                pokedex.getMoveByName("electro-ball")
        });

        charizard.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("flamethrower"),
                pokedex.getMoveByName("dragon-claw"),
                pokedex.getMoveByName("air-slash"),
                pokedex.getMoveByName("earthquake")
        });

        snorlax.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("body-slam"),
                pokedex.getMoveByName("rest"),
                pokedex.getMoveByName("crunch"),
                pokedex.getMoveByName("earthquake")
        });

        sceptile.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("leaf-blade"),
                pokedex.getMoveByName("dragon-claw"),
                pokedex.getMoveByName("earthquake"),
                pokedex.getMoveByName("swords-dance")
        });

        infernape.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("flare-blitz"),
                pokedex.getMoveByName("close-combat"),
                pokedex.getMoveByName("mach-punch"),
                pokedex.getMoveByName("thunder-punch")
        });

        greninja.changeEntireMoveSet(new Move[]{
                pokedex.getMoveByName("water-shuriken"),
                pokedex.getMoveByName("dark-pulse"),
                pokedex.getMoveByName("ice-beam"),
                pokedex.getMoveByName("hydro-pump")
        });

        Bag playerBag = new Bag();
        Trainer ash = new Trainer("Ash", playerBag, ashParty, new ArrayList<>(), 1000, true);
        Trainer cynthia = new Trainer("Cynthia", playerBag, cynthiaParty, new ArrayList<>(), 1000, false);

        Battle battle = new Battle(ash, ashParty.get(0), cynthia, cynthiaParty.get(0), 0);

        int battleOver = 0;
        System.out.println(battle);

        while (battleOver == 0) {
            battleOver = battle.turn();
            Thread.sleep(100);

            System.out.println(battle);
        }

        if (battleOver == -1) {
            System.out.println(cynthia.getName() + " wins.");
        } else {
            System.out.println(ash.getName() + " wins.");
        }

    }
}
