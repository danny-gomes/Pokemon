package Entidades;

import Entidades.Moves.Move;

import java.util.ArrayList;

import static Ficheiros.ReadPokemons.readAllPokemon;
import static Ficheiros.ReadPokemons.readMoves;

public class Pokedex {
    /**
     * List of all pokemon.
     */
    private final ArrayList<Pokemon> allPokemon;
    /**
     * List of all moves.
     */
    private ArrayList<Move> allMoves;

    /**
     * Method that creates a pokedex that stores all Pokemon and Moves.
     */
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

    /**
     * Method that returns pokemon by a given name.
     * @param pokemonName The pokemons name.
     * @return the pokemon or null if pokemon was not found.
     */
    public Pokemon getPokemonByName(String pokemonName) {
        for(Pokemon p : allPokemon){
            if(p.getName().equalsIgnoreCase(pokemonName)) {
                return p;
            }
        }

        return null;
    }

    /**
     * Method that returns the list of all moves.
     * @return
     */
    public ArrayList<Move> getAllMoves() {
        return allMoves;
    }
}
