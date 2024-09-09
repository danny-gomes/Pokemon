package Entidades;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Trainer {
    protected String name;
    protected Bag inventory;
    ArrayList<Pokemon> party;
    ArrayList<Pokemon> pc;
    protected int money;
    private boolean isPlayer;


    /**
     * Method that creates a trainer only with one starter.
     * @param name the name of the trainer.
     * @param starter the trainers starter pokemon.
     * @param money the trainers starting money.
     */
    public Trainer(String name, Pokemon starter, int money) {
        this.name = name;
        this.inventory = new Bag();
        this.money = money;
        this.party = new ArrayList<>();
        this.pc = new ArrayList<>();

        this.party.add(starter);
    }

    /**
     * Method that creates a trainer with set amount of pokemon already in the party.
     * @param name the name of the trainer.
     * @param inventory the trainers bag with items.
     * @param party the trainers party of pokemon.
     * @param pc the trainers pokemon storage.
     * @param money the trainers money.
     * @param isPlayer true if the trainer is a player, or false if is NPC.
     */
    public Trainer(String name, Bag inventory, ArrayList<Pokemon> party, ArrayList<Pokemon> pc, int money, boolean isPlayer) {
        this.name = name;
        this.inventory = inventory;
        this.party = party;
        this.pc = pc;
        this.money = money;
        this.isPlayer = isPlayer;
    }

    /**
     * Method that returns the trainers non fainted pokemon.
     * @return the list of trainers non fainted pokemon.
     */
    public ArrayList<Pokemon> nonFaintedPokemon() {
        ArrayList<Pokemon> nonFaintedPokemon = new ArrayList<>();

        for(Pokemon p : party){
            if(p.getCurrentHp() > 0){
                nonFaintedPokemon.add(p);
            }
        }

        return nonFaintedPokemon;
    }

    /**
     * Method that checks if a given pokemon is fainted.
     * @param name the name of the pokemon to check if its fainted.
     * @return the fainted pokemon or null if the the pokemon passed as paramater is not fainted.
     */
    public Pokemon getNonFaintedPokemon(String name){
        ArrayList<Pokemon> nonFaintedPokemon = nonFaintedPokemon();

        for(Pokemon p : nonFaintedPokemon){
            if(p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }

        return null;
    }

    /**
     * Method that gets the pokemon name.
     * @return the pokemon name.
     */
    public String getName() {
        return name;
    }

    /**
     * Method that returns true if the trainer is the player.
     * @return true if the trainer is a player or false if NPC.
     */
    public boolean isPlayer() {
        return isPlayer;
    }
}
