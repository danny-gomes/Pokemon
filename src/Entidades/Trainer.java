package Entidades;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Trainer {
    protected String name;
    protected Bag inventory;
    ArrayList<Pokemon> party;
    ArrayList<Pokemon> pc;
    protected int money;

    public Trainer(String name, Pokemon starter, int money) {
        this.name = name;
        this.inventory = new Bag();
        this.money = money;
        this.party = new ArrayList<>();
        this.pc = new ArrayList<>();

        this.party.add(starter);
    }

    public Trainer(String name, Bag inventory, ArrayList<Pokemon> party, ArrayList<Pokemon> pc, int money) {
        this.name = name;
        this.inventory = inventory;
        this.party = party;
        this.pc = pc;
        this.money = money;
    }

    public ArrayList<Pokemon> nonFaintedPokemon() {
        ArrayList<Pokemon> nonFaintedPokemon = new ArrayList<>();

        for(Pokemon p : party){
            if(p.getCurrentHp() > 0){
                nonFaintedPokemon.add(p);
            }
        }

        return nonFaintedPokemon;
    }

    public String getName() {
        return name;
    }
}
