package Ficheiros;

import Entidades.Pokemon;
import Enums.PokemonType;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class LerPokemons {
    public static ArrayList<Pokemon> readAllPokemon(String path) {
        Pokemon foundPokemon = null;
        ArrayList<Pokemon> allPokemon = new ArrayList<>();
        ArrayList<String> pokemonNames = new ArrayList<>();

        try {
            Scanner readFile = new Scanner(new File(path));

            readFile.nextLine();

            while (readFile.hasNextLine()) {
                String entireLine = readFile.nextLine();
                String[] lineArray = entireLine.split(",");

                int dexNumber = Integer.parseInt(lineArray[0]);
                String name = lineArray[1];
                PokemonType type1 = PokemonType.typeString(lineArray[2]);
                PokemonType type2 = null;
                if (!lineArray[3].equalsIgnoreCase("null")) {
                    type2 = PokemonType.typeString(lineArray[3]);
                }
                double maleRatio = Double.parseDouble(lineArray[4]);
                double femaleRatio = Double.parseDouble(lineArray[5]);
                int hp = Integer.parseInt(lineArray[6]);
                int attack = Integer.parseInt(lineArray[7]);
                int defense = Integer.parseInt(lineArray[8]);
                int specialAttack = Integer.parseInt(lineArray[9]);
                int specialDefense = Integer.parseInt(lineArray[10]);
                int speed = Integer.parseInt(lineArray[11]);

                int firstColonIndex = lineArray[12].indexOf(":");
                int secondColonIndex = lineArray[12].indexOf(":", firstColonIndex + 1);
                int thirdColonIndex = lineArray[12].indexOf(":", secondColonIndex + 1);

                ArrayList<String> normalAbilities = new ArrayList<>();
                String ability1 = "";
                String ability2 = "";
                String hiddenAbility = "";
                if (secondColonIndex == -1) {
                    ability1 = lineArray[12].substring(firstColonIndex + 2);
                    normalAbilities.add(ability1);
                } else {
                    ability1 = lineArray[12].substring(firstColonIndex + 2, secondColonIndex - 2);
                    normalAbilities.add(ability1);
                    if (thirdColonIndex == -1) {
                        if (lineArray[12].contains("1:")) {
                            ability2 = lineArray[12].substring(secondColonIndex + 2);
                            normalAbilities.add(ability2);
                        } else if (lineArray[12].contains("H:")) {
                            hiddenAbility = lineArray[12].substring(secondColonIndex + 2);
                        }
                    } else {
                        ability2 = lineArray[12].substring(secondColonIndex + 2, thirdColonIndex - 2);
                        normalAbilities.add(ability2);

                        hiddenAbility = lineArray[12].substring(thirdColonIndex + 2);
                    }
                }
                double height = Double.parseDouble(lineArray[13]);
                double weight = Double.parseDouble(lineArray[14]);
                String color = lineArray[15];
                String preEvo = lineArray[16];
                int evoLevel = lineArray[17].equalsIgnoreCase("Base") ? 0 : Integer.parseInt(lineArray[17]);
                String evolvesTo = lineArray[18];

                if(evolvesTo.equalsIgnoreCase("None")) {
                    evoLevel = 0;
                }

                foundPokemon = new Pokemon(dexNumber, name, type1, type2, 50, hp, attack, defense, specialAttack, specialDefense, speed,normalAbilities, hiddenAbility, height, weight, evoLevel);
                pokemonNames.add(name);
                allPokemon.add(foundPokemon);

                if(pokemonNames.contains(preEvo)) {
                    for(Pokemon p : allPokemon){
                        if(p.getName().equalsIgnoreCase(preEvo)) {
                             p.addPossibleEvolution(foundPokemon);
                             p.setEvolvesAt(evoLevel == 0 ? 30 : evoLevel);
                        }
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return allPokemon;
    }

}
