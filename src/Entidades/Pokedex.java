package Entidades;

import Entidades.Moves.Move;

import java.util.ArrayList;

import static Ficheiros.ReadPokemons.readAllPokemon;
import static Ficheiros.ReadPokemons.readMoves;

public class Pokedex {
    private final ArrayList<Pokemon> allPokemon;
    private ArrayList<Move> allMoves;

    public Pokedex() {
        this.allMoves = readMoves();
        this.allPokemon = readAllPokemon("pokemon_data.csv", allMoves);

        for(Move m : allMoves){
            if(m.getName().equalsIgnoreCase("fly")){
                m.setChargeMove(true);
                m.setEvasionOnCharge(true);
            }
        }
    }

    public Pokemon getPokemonByName(String pokemonName) {
        for(Pokemon p : allPokemon){
            if(p.getName().equalsIgnoreCase(pokemonName)) {
                return p;
            }
        }

        return null;
    }

    public ArrayList<Move> getAllMoves() {
        return allMoves;
    }
}
