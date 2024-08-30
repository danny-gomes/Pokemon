package Enums;

public enum Target {
    SPECIFIC_MOVE,
    SELECTED_POKEMON_ME_FIRST,
    ALLY,
    USERS_FIELD,
    USER_OR_ALLY,
    OPPONENTS_FIELD,
    USER,
    RANDOM_OPPONENT,
    ALL_OTHER_POKEMON,
    SELECTED_POKEMON,
    ALL_OPPONENTS,
    ENTIRE_FIELD,
    USER_AND_ALLIES,
    ALL_POKEMON,
    ALL_ALLIES,
    FAINTING_POKEMON;

    public static Target fromCsv(String field) {
        switch (field.trim().toUpperCase()) {
            case "SPECIFIC-MOVE":
                return SPECIFIC_MOVE;
            case "SELECTED-POKEMON-ME-FIRST":
                return SELECTED_POKEMON_ME_FIRST;
            case "ALLY":
                return ALLY;
            case "USERS-FIELD":
                return USERS_FIELD;
            case "USER-OR-ALLY":
                return USER_OR_ALLY;
            case "OPPONENTS-FIELD":
                return OPPONENTS_FIELD;
            case "USER":
                return USER;
            case "RANDOM-OPPONENT":
                return RANDOM_OPPONENT;
            case "ALL-OTHER-POKEMON":
                return ALL_OTHER_POKEMON;
            case "SELECTED-POKEMON":
                return SELECTED_POKEMON;
            case "ALL-OPPONENTS":
                return ALL_OPPONENTS;
            case "ENTIRE-FIELD":
                return ENTIRE_FIELD;
            case "USER-AND-ALLIES":
                return USER_AND_ALLIES;
            case "ALL-POKEMON":
                return ALL_POKEMON;
            case "ALL-ALLIES":
                return ALL_ALLIES;
            case "FAINTING-POKEMON":
                return FAINTING_POKEMON;
            default:
                throw new IllegalArgumentException("Unknown Target value: " + field);
        }
    }
}

