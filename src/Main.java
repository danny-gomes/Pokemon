import Entidades.Pokedex;
import Entidades.Pokemon;

import java.util.ArrayList;
import java.util.Random;

import static Ficheiros.LerPokemons.readAllPokemon;

public class Main {
    public static void main(String[] args) {
        Pokedex pokedex = new Pokedex();

        System.out.println(pokedex.getPokemonByName("applin"));
    }
}
