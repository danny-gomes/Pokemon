package Entidades.FieldEffects;

import Entidades.Battle;
import Entidades.Moves.Move;
import Entidades.Pokemon;
import Enums.PokemonType;
import Enums.WeatherEffect;

public class Weather extends FieldEffect {
    protected int turnsOnField;
    protected int maxTurnsOnField;
    protected WeatherEffect weatherType;
    protected Pokemon playerCurrentBoosted;
    protected Pokemon challengerCurrentBoosted;

    public Weather(WeatherEffect weather, Move moveUsed, int maxTurnsOnField) {
        super(weather.getName(), moveUsed);
        this.turnsOnField = 0;
        this.maxTurnsOnField = maxTurnsOnField;
        this.weatherType = weather;
    }


    public void checkWeatherEffects(Battle b, boolean isEndTurn) {
        Pokemon playerPokemon = b.getPlayerCurrentPokemon();
        Pokemon challengerPokemon = b.getChallengerCurrentPokemon();

        switch (this.name) {
            case "Hail":
                if (this.turnsOnField == 0) {
                    System.out.println(this.weatherType.getDescription());
                }

                if (isEndTurn) {
                    System.out.println("It's hailing!");
                }

                if (playerPokemon.getTypes().contains(PokemonType.ICE) && this.playerCurrentBoosted != playerPokemon) {
                    this.playerCurrentBoosted = playerPokemon;
                    playerPokemon.updateStatModifiers(0, 1, 0, 0, 0, 0, 0);
                } else if (!playerPokemon.getTypes().contains(PokemonType.ICE) && isEndTurn) {
                    System.out.println(playerPokemon.getName() + " is hurt by hail.");
                    playerPokemon.takeDamage(playerPokemon.getCurrentMaxHp() / 16);
                }

                if (challengerPokemon.getTypes().contains(PokemonType.ICE) && this.challengerCurrentBoosted != challengerPokemon) {
                    this.challengerCurrentBoosted = challengerPokemon;
                    challengerPokemon.updateStatModifiers(0, 1, 0, 0, 0, 0, 0);
                } else if (!challengerPokemon.getTypes().contains(PokemonType.ICE) &&isEndTurn) {
                    System.out.println(challengerPokemon.getName() + " is hurt by hail.");
                    challengerPokemon.takeDamage(challengerPokemon.getCurrentMaxHp() / 16);
                }

                break;
            case "sunny-day":
                break;
        }
        this.turnsOnField++;
        if (this.turnsOnField > this.maxTurnsOnField && this.maxTurnsOnField != -1) {
            b.setWeather(new Weather(WeatherEffect.NONE, null, -1));
            clearWeather(playerPokemon, challengerPokemon);
        }
    }

    private void clearWeather(Pokemon playerPokemon, Pokemon challengerPokemon) {
        switch (this.name) {
            case "Hail":
                if (playerPokemon.getTypes().contains(PokemonType.ICE)) {
                    playerPokemon.updateStatModifiers(0, -1, 0, 0, 0, 0, 0);
                }

                if (challengerPokemon.getTypes().contains(PokemonType.ICE)) {
                    challengerPokemon.updateStatModifiers(0, -1, 0, 0, 0, 0, 0);
                }
                break;
            case "sunny-day":
                break;
        }
    }
}
