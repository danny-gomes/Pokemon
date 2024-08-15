package Entidades;

import java.util.ArrayList;

import static Ficheiros.LerPokemons.readAllPokemon;

public class Pokedex {
    private ArrayList<Pokemon> allPokemon;

    public Pokedex() {
        this.allPokemon = readAllPokemon("src/pokemon_data.csv");
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
