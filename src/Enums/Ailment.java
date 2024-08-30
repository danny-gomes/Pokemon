package Enums;

public enum Ailment {
    UNKNOWN,
    NONE,
    PARALYSIS,
    SLEEP,
    FREEZE,
    BURN,
    POISON,
    CONFUSION,
    INFATUATION,
    TRAP,
    NIGHTMARE,
    TORMENT,
    DISABLE,
    YAWN,
    HEAL_BLOCK,
    NO_TYPE_IMMUNITY,
    LEECH_SEED,
    EMBARGO,
    PERISH_SONG,
    SILENCE,
    TAR_SHOT,
    INGRAIN;

    public static Ailment fromCsv(String field) {
        switch (field.trim().toUpperCase()) {
            case "UNKNOWN":
                return UNKNOWN;
            case "NONE":
                return NONE;
            case "PARALYSIS":
                return PARALYSIS;
            case "SLEEP":
                return SLEEP;
            case "FREEZE":
                return FREEZE;
            case "BURN":
                return BURN;
            case "POISON":
                return POISON;
            case "CONFUSION":
                return CONFUSION;
            case "INFATUATION":
                return INFATUATION;
            case "TRAP":
                return TRAP;
            case "NIGHTMARE":
                return NIGHTMARE;
            case "TORMENT":
                return TORMENT;
            case "DISABLE":
                return DISABLE;
            case "YAWN":
                return YAWN;
            case "HEAL-BLOCK":
                return HEAL_BLOCK;
            case "NO-TYPE-IMMUNITY":
                return NO_TYPE_IMMUNITY;
            case "LEECH-SEED":
                return LEECH_SEED;
            case "EMBARGO":
                return EMBARGO;
            case "PERISH-SONG":
                return PERISH_SONG;
            case "SILENCE":
                return SILENCE;
            case "TAR-SHOT":
                return TAR_SHOT;
            case "INGRAIN":
                return INGRAIN;
            default:
                throw new IllegalArgumentException("Unknown Ailment value: " + field);
        }
    }
}
