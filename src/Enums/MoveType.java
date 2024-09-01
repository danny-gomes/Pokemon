package Enums;

public enum MoveType {
    DAMAGE,
    AILMENT,
    NET_GOOD_STATS,
    HEAL,
    DAMAGE_AILMENT,
    SWAGGER,
    DAMAGE_LOWER,
    DAMAGE_RAISE,
    DAMAGE_HEAL,
    OHKO,
    WHOLE_FIELD_EFFECT,
    FIELD_EFFECT,
    FORCE_SWITCH,
    UNIQUE;

    public static MoveType fromCsv(String field) {
        switch (field.trim().toUpperCase()) {
            case "DAMAGE":
                return DAMAGE;
            case "AILMENT":
                return AILMENT;
            case "NET-GOOD-STATS":
                return NET_GOOD_STATS;
            case "HEAL":
                return HEAL;
            case "DAMAGE+AILMENT":
                return DAMAGE_AILMENT;
            case "SWAGGER":
                return SWAGGER;
            case "DAMAGE+LOWER":
                return DAMAGE_LOWER;
            case "DAMAGE+RAISE":
                return DAMAGE_RAISE;
            case "DAMAGE+HEAL":
                return DAMAGE_HEAL;
            case "OHKO":
                return OHKO;
            case "WHOLE-FIELD-EFFECT":
                return WHOLE_FIELD_EFFECT;
            case "FIELD-EFFECT":
                return FIELD_EFFECT;
             case "FORCE-SWITCH":
                return FORCE_SWITCH;
            case "UNIQUE":
                return UNIQUE;
            default:
                throw new IllegalArgumentException("Unknown MoveType value: " + field);
        }
    }
}



