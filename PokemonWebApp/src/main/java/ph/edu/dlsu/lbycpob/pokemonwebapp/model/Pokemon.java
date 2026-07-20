package ph.edu.dlsu.lbycpob.pokemonwebapp.model;

// Concrete Pokemon implementation
public class Pokemon extends AbstractPokemon {
    public Pokemon(int instanceId, String name, double weight, double height,
                   double attack, double defense, double stamina, String type) {
        super(instanceId, name, weight, height, attack, defense, stamina, type);
    }
    @Override
    public String getSpecialAbility() {
        return "Standard Pokemon ability for " + type + " type";
    }
    @Override
    public String getTypeBackground() {
        String[] types = type.split("-");
        String primaryType = types[0].toUpperCase();
        return getTypeBackground(primaryType);
    }
    // Follow the first type by default
    public String getTypeBackground(String customType) {
        return switch (customType) {
            case "WATER" -> "linear-gradient(to bottom, #6BB6FF, #0066CC)";
            case "FIRE" -> "linear-gradient(to bottom, #FF6B6B, #CC0000)";
            case "GRASS" -> "linear-gradient(to bottom, #4CAF50, #2E7D32)";
            case "ELECTRIC" -> "linear-gradient(to bottom, #FFD700, #FFA500)";
            case "PSYCHIC" -> "linear-gradient(to bottom, #FF69B4, #8B008B)";
            case "ICE" -> "linear-gradient(to bottom, #87CEEB, #4169E1)";
            case "DRAGON" -> "linear-gradient(to bottom, #9370DB, #4B0082)";
            case "DARK" -> "linear-gradient(to bottom, #696969, #2F2F2F)";
            case "FIGHTING" -> "linear-gradient(to bottom, #CD853F, #8B4513)";
            case "POISON" -> "linear-gradient(to bottom, #9932CC, #4B0082)";
            case "GROUND" -> "linear-gradient(to bottom, #DEB887, #8B7355)";
            case "FLYING" -> "linear-gradient(to bottom, #87CEEB, #6495ED)";
            case "BUG" -> "linear-gradient(to bottom, #9ACD32, #556B2F)";
            case "ROCK" -> "linear-gradient(to bottom, #A0522D, #654321)";
            case "GHOST" -> "linear-gradient(to bottom, #9370DB, #483D8B)";
            case "STEEL" -> "linear-gradient(to bottom, #C0C0C0, #708090)";
            case "FAIRY" -> "linear-gradient(to bottom, #FFB6C1, #FF69B4)";
            case "NORMAL" -> "linear-gradient(to bottom, #F5F5DC, #D2B48C)";
            default -> "linear-gradient(to bottom, #F0F0F0, #D0D0D0)";
        };
    }
    public String getSecondBackground() {
        String[] types = type.split("-");
        if (types.length < 2) {
            return "linear-gradient(to bottom, #F0F0F0, #D0D0D0)";
        }
        return getTypeBackground(types[1].toUpperCase());
    }
    public boolean isDualType() {
        return type.contains("-");
    }

    public String getFirstType() {
        return type.split("-")[0];
    }
    public String getSecondType() {
        String[] types = type.split("-");
        return types.length > 1 ? types[1] : "";
    }
    /** Web-relative image filename, e.g. "squirtle.gif" - used by Thymeleaf to build the <img> src. */
    public String getImageFileName() {
        return name.toLowerCase() + ".gif";
    }
}