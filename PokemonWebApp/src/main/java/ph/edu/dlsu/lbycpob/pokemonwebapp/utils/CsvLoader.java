package ph.edu.dlsu.lbycpob.pokemonwebapp.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CsvLoader {

    private int totalLines = 0;
    private int skippedLines = 0;
    private final List<String> skippedLineDetails = new ArrayList<>();

    public static String[] loadCSVFromResources(String resourcePath) {
        CsvLoader loader = new CsvLoader();
        return loader.loadCSVFromResourcesInternal(resourcePath);
    }

    public static String[] loadCSV(String filePath) {
        return loadCSV(new File(filePath));
    }

    public static String[] loadCSV(File file) {
        CsvLoader loader = new CsvLoader();
        return loader.loadCSVToStringArray(file);
    }

    private String[] loadCSVFromResourcesInternal(String resourcePath) {
        List<String> validLines = new ArrayList<>();
        totalLines = 0;
        skippedLines = 0;
        skippedLineDetails.clear();

        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(resourcePath)) {
            if (inputStream == null) {
                System.err.println("Resource file not found: " + resourcePath);
                return new String[0];
            }
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    totalLines++;
                    if (line.trim().isEmpty()) {
                        skippedLines++;
                        skippedLineDetails.add("Line " + totalLines + ": Empty line");
                        continue;
                    }
                    String cleanedLine = cleanCSVLine(line);
                    if (isValidCSVLine(cleanedLine)) {
                        validLines.add(cleanedLine);
                    } else {
                        skippedLines++;
                        skippedLineDetails.add("Line " + totalLines + ": " + line);
                        System.out.println("Skipped malformed line " + totalLines + ": " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading resource file: " + e.getMessage());
        }

        return validLines.toArray(new String[0]);
    }

    public String[] loadCSVToStringArray(File file) {
        List<String> validLines = new ArrayList<>();
        totalLines = 0;
        skippedLines = 0;
        skippedLineDetails.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                totalLines++;
                if (line.trim().isEmpty()) {
                    skippedLines++;
                    skippedLineDetails.add("Line " + totalLines + ": Empty line");
                    continue;
                }
                String cleanedLine = cleanCSVLine(line);
                if (isValidCSVLine(cleanedLine)) {
                    validLines.add(cleanedLine);
                } else {
                    skippedLines++;
                    skippedLineDetails.add("Line " + totalLines + ": " + line);
                    System.out.println("Skipped malformed line " + totalLines + ": " + line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }

        return validLines.toArray(new String[0]);
    }

    private String cleanCSVLine(String line) {
        line = line.trim();
        if (line.endsWith(",")) {
            line = line.substring(0, line.length() - 1);
        }
        if (line.startsWith("\"") && line.endsWith("\"")) {
            line = line.substring(1, line.length() - 1);
        }
        return line;
    }

    private boolean isValidCSVLine(String line) {
        if (line == null || line.trim().isEmpty()) return false;
        String[] fields = line.split(",");
        if (fields.length != 7) return false;

        try {
            if (fields[0].trim().isEmpty()) return false;

            String weight = fields[1].trim();
            if (!weight.endsWith("kg")) return false;
            Double.parseDouble(weight.substring(0, weight.length() - 2));

            String height = fields[2].trim();
            if (!height.endsWith("m")) return false;
            Double.parseDouble(height.substring(0, height.length() - 1));

            Double.parseDouble(fields[3].trim());
            Double.parseDouble(fields[4].trim());
            Double.parseDouble(fields[5].trim());

            return !fields[6].trim().isEmpty();
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public int getTotalLines() { return totalLines; }
    public int getSkippedLines() { return skippedLines; }
    public int getValidLines() { return totalLines - skippedLines; }
    public List<String> getSkippedLineDetails() { return new ArrayList<>(skippedLineDetails); }

    public String getLoadingSummary() {
        return String.format("Loaded %d valid lines, skipped %d malformed lines out of %d total lines",
                getValidLines(), skippedLines, totalLines);
    }
}