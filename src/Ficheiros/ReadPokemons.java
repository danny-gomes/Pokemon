package Ficheiros;

import Entidades.Moves.Move;
import Entidades.Moves.MoveInfo;
import Entidades.Pokemon;
import Enums.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class ReadPokemons {
    public static ArrayList<Pokemon> readAllPokemon(String pokemonFilePath, ArrayList<Move> allMoves) {
        Pokemon foundPokemon = null;
        ArrayList<Pokemon> allPokemon = new ArrayList<>();
        ArrayList<String> pokemonNames = new ArrayList<>();
        Map<String, ArrayList<String>> learnSetsString = readLearnSets();

        try {
            Scanner readPokemonFile = new Scanner(new File(pokemonFilePath));

            readPokemonFile.nextLine();

            while (readPokemonFile.hasNextLine()) {
                String entireLine = readPokemonFile.nextLine();
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

                if (evolvesTo.equalsIgnoreCase("None")) {
                    evoLevel = 0;
                }

                ArrayList<Move> learnSet = readPokemonLearnSet(learnSetsString, name, allMoves);

                Move[] startingMoves = getStartingMoves(learnSet);
                foundPokemon = new Pokemon(dexNumber, name, type1, type2, 50, hp, attack, defense, specialAttack, specialDefense, speed, normalAbilities, hiddenAbility, height, weight, evoLevel, startingMoves, learnSet);
                pokemonNames.add(name);
                allPokemon.add(foundPokemon);

                if (pokemonNames.contains(preEvo)) {
                    for (Pokemon p : allPokemon) {
                        if (p.getName().equalsIgnoreCase(preEvo)) {
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

    private static Move[] getStartingMoves(ArrayList<Move> learnSet) {

        if (learnSet == null) {
            throw new IllegalArgumentException("The learn set cannot be null.");
        }

        Move powerMove = null;
        Move statusMove = null;
        Random random = new Random();

        for (Move move : learnSet) {
            if (move.getPower() <= 60 && move.getPower() > 0 && powerMove == null) {
                powerMove = move;
            }
            if (move.getCategory() == MoveCategory.STATUS && statusMove == null) {
                ArrayList<Integer> statChanges = move.getMoveInfo().getStatChanges();
                for(Integer i : statChanges){
                    if(i > 0){
                        break;
                    } else {
                        statusMove = move;
                    }
                }
            }
            if (powerMove != null && statusMove != null) {
                break;
            }
        }

        Move[] startingMoves = new Move[4];
        if (powerMove != null && statusMove != null) {
            startingMoves[0] = powerMove;
            startingMoves[1] = statusMove;
        } else {

            ArrayList<Move> availableMoves = new ArrayList<>(learnSet);

            while (availableMoves.size() < 2) {
                availableMoves.add(availableMoves.get(random.nextInt(learnSet.size())));
            }

            startingMoves[0] = powerMove;
            startingMoves[1] = statusMove;


            for (int i = 2; i < startingMoves.length; i++) {
                startingMoves[i] = null;
            }
        }

        return startingMoves;
    }

    private static ArrayList<Move> readPokemonLearnSet(Map<String, ArrayList<String>> learnSetsString, String name, ArrayList<Move> allMoves) {
        ArrayList<String> movesString = new ArrayList<>();
        ArrayList<Move> moveSet = new ArrayList<>();

        for (Map.Entry<String, ArrayList<String>> entry : learnSetsString.entrySet()) {
            if (entry.getKey().equalsIgnoreCase(name)) {
                movesString = entry.getValue();
                break;
            }
        }

        for (Move m : allMoves) {
            for (String s : movesString) {
                if (m.getName().equalsIgnoreCase(s)) {
                    moveSet.add(m);
                    break;
                }
            }
        }

        return moveSet;
    }

    private static Map<String, ArrayList<String>> readLearnSets() {
        Map<String, ArrayList<String>> learnSets = new HashMap<>();

        try {
            Scanner readPokemonLearnSets = new Scanner(new File("pokemon_learnsets.csv"));

            while (readPokemonLearnSets.hasNextLine()) {
                String line = readPokemonLearnSets.nextLine();
                String[] lineArray = line.split(",");

                for (int i = 0; i < lineArray.length; i++) {
                    ArrayList<String> moves = new ArrayList<>();
                    if (i == 0) {
                        learnSets.put(lineArray[0], moves);
                    } else {
                        learnSets.get(lineArray[0]).add(lineArray[i]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        return learnSets;
    }

    public static ArrayList<Move> readMoves() {
        ArrayList<Move> movesList = new ArrayList<>();
        Map<String, MoveInfo> moveInfoMap = new HashMap<>();

        try (Scanner scanner = new Scanner(new File("moves_info.csv"))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                if (fields.length < 20) continue;
                // Move Name,Ailment,Ailment Chance,Move Type3,Crit Rate4,Drain5,Flinch Chance6,Healing7,Max Hits8,Min Hits9,Min Turns10,Max Turns11,Stat Chance12,Attack Change, Defense Change, SpAttack Change, SpDef Change, Speed Change, Accuracy Change, Evasiness Change
                String moveName = fields[0];
                Ailment ailment = Ailment.fromCsv(fields[1]);
                int ailmentChance = parseInt(fields[2]);
                MoveType moveCategory = MoveType.fromCsv(fields[3]);
                int critRate = parseInt(fields[4]);
                int drain = parseInt(fields[5]);
                int flinchChance = parseInt(fields[6]);
                int healing = parseInt(fields[7]);
                int maxHits = parseInt(fields[8]);
                int minHits = parseInt(fields[9]);
                int minTurns = parseInt(fields[10]);
                int maxTurns = parseInt(fields[11]);
                int statChance = parseInt(fields[12]);
                int attackChange = parseInt(fields[13]);
                int defenseChange = parseInt(fields[14]);
                int spAttackChange = parseInt(fields[15]);
                int spDefenseChange = parseInt(fields[16]);
                int speedChange = parseInt(fields[17]);
                int accuracyChange = parseInt(fields[18]);
                int evasinessChange = parseInt(fields[19]);
                ArrayList<Integer> statChanges = new ArrayList<>();

                statChanges.add(attackChange);
                statChanges.add(defenseChange);
                statChanges.add(spAttackChange);
                statChanges.add(spDefenseChange);
                statChanges.add(speedChange);
                statChanges.add(accuracyChange);
                statChanges.add(evasinessChange);

                MoveInfo moveInfo = new MoveInfo(
                        ailment, moveCategory, minHits, maxHits, minTurns, maxTurns,
                        drain, healing, critRate, ailmentChance, flinchChance, statChance, statChanges
                );
                moveInfoMap.put(moveName, moveInfo);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (Scanner scanner = new Scanner(new File("pokemon_moves.csv"))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] fields = line.split(",");

                if (fields.length < 11) continue; // Skip incomplete lines

                // Move Name,Type,Accuracy,Move Category,Effect Chance,PP,Priority,Power,HP Based Damage,Target,Effect Entry

                String moveName = fields[0];
                PokemonType type = PokemonType.typeString(fields[1]);
                int accuracy = parseInt(fields[2]);
                MoveCategory category = MoveCategory.valueOf(fields[3].toUpperCase());
                int effectChance = parseInt(fields[4]);
                int powerpoints = parseInt(fields[5]);
                int priority = parseInt(fields[6]);
                int power = parseInt(fields[7]);
                boolean isHpBasedDmg = fields[8].equalsIgnoreCase("Yes");
                Target target = Target.fromCsv(fields[9]);
                String description = fields[10];

                MoveInfo moveInfo = moveInfoMap.get(moveName);

                Move move = new Move(
                        moveName, category, power, powerpoints, accuracy, effectChance, priority,
                        description, moveInfo, target, type
                );

                movesList.add(move);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return movesList;
    }

    private static int parseInt(String value) {
        return value == null || value.equals("null") ? 0 : Integer.parseInt(value);
    }
}


