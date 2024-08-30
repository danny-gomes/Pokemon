import Entidades.Moves.Move;
import Entidades.Pokedex;
import Entidades.Pokemon;
import Ficheiros.ReadPokemons;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        Pokedex pokedex = new Pokedex();

        Pokemon dreepy = pokedex.getPokemonByName("turtwig");

        System.out.println(dreepy);

        ArrayList<Move> learnSet = dreepy.getLearnSet();

        for(Move m : learnSet){
            System.out.println(m.getName());
        }

        Move[] moves = dreepy.getMoves();

        for(int i = 0; i < moves.length; i++){
            if(moves[i] != null){
                System.out.println(i + " - " + moves[i].getName());
            }
        }
    }
}
