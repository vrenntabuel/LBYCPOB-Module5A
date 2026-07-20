package ph.edu.dlsu.lbycpob.pokemonwebapp.model;

import java.util.concurrent.atomic.AtomicInteger;

// Pokemon Factory
public class PokemonFactory {
    private static final AtomicInteger counter = new AtomicInteger(0);

    public static Pokemon createPokemon(String name, double weight, double height,
                                        double attack, double defense, double stamina, String type) {
        return new Pokemon(counter.incrementAndGet(), name, weight, height, attack, defense, stamina, type);
    }
    public static Pokemon createPokemonFromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid CSV format");
        }
        String name = parts[0].trim();
        double weight = parseWeight(parts[1].trim());
        double height = parseHeight(parts[2].trim());
        double attack = Double.parseDouble(parts[3].trim());
        double defense = Double.parseDouble(parts[4].trim());
        double stamina = Double.parseDouble(parts[5].trim());
        String type = parts[6].trim();

        return createPokemon(name, weight, height, attack, defense, stamina, type);
    }
    private static double parseWeight(String weightStr) {
        return Double.parseDouble(weightStr.replace("kg", ""));
    }
    private static double parseHeight(String heightStr) {
        return Double.parseDouble(heightStr.replace("m", ""));
    }
}