package Entidades;

import java.util.ArrayList;

import static Ficheiros.ReadPokemons.readAllPokemon;

public class Pokedex {
    private ArrayList<Pokemon> allPokemon;

    public Pokedex() {
        this.allPokemon = readAllPokemon("pokemon_data.csv");
    }

    public Pokemon getPokemonByName(String pokemonName) {
        for(Pokemon p : allPokemon){
            if(p.getName().equalsIgnoreCase(pokemonName)) {
                return p;
            }
        }

        return null;
    }
}
