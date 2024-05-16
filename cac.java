import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class cac {
    public static void main(String[] args) {
        String csvFile = "TV_Final.csv";
        String line = "";
        String cvsSplitBy = ",";

        // Variables for statistical analysis
        List<Double> sellingPrices = new ArrayList<>();
        List<Double> originalPrices = new ArrayList<>();
        List<Double> sizes = new ArrayList<>();
        List<Double> ratings = new ArrayList<>();
        Map<String, Integer> brandFrequency = new HashMap<>();
        Map<String, Integer> resolutionFrequency = new HashMap<>();
        Map<String, Integer> osFrequency = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
            // Read the header to skip it
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] data = line.split(cvsSplitBy);

                // Check if data array has enough elements
                if (data.length < 7) {
                    System.err.println("Error: Incomplete data in CSV line, skipping line: " + line);
                    continue;  // Skip this line and continue with the next one
                }

                try {
                    double sellingPrice = Double.parseDouble(data[3]);
                    double originalPrice = Double.parseDouble(data[4]);
                    double size = Double.parseDouble(data[2]);
                    double rating = Double.parseDouble(data[6]);
                    String brand = data[0];
                    String resolution = data[1];
                    String os = data[5];

                    // Data cleaning and preprocessing
                    if (sellingPrice > 0 && originalPrice > 0 && size > 0 && rating >= 0 && rating <= 5) {
                        sellingPrices.add(sellingPrice);
                        originalPrices.add(originalPrice);
                        sizes.add(size);
                        ratings.add(rating);

                        // Update frequency maps
                        updateFrequencyMap(brand, brandFrequency);
                        updateFrequencyMap(resolution, resolutionFrequency);
                        updateFrequencyMap(os, osFrequency);
                    } else {
                        System.err.println("Error: Invalid data in CSV line, skipping line: " + line);
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Error: Invalid numeric data in CSV line, skipping line: " + line);
                }
            }

            // Calculate mean, median, mode, etc.
            double meanSellingPrice = calculateMean(sellingPrices);
            double medianSellingPrice = calculateMedian(sellingPrices);
            double modeSellingPrice = calculateMode(sellingPrices);
            // Similar calculations for other variables

            // Print results
            System.out.println("Mean Selling Price: " + meanSellingPrice);
            System.out.println("Median Selling Price: " + medianSellingPrice);
            System.out.println("Mode Selling Price: " + modeSellingPrice);
            // Print results for other variables

            // Print frequency distributions
            System.out.println("Brand Frequency:");
            printFrequencyMap(brandFrequency);
            System.out.println("Resolution Frequency:");
            printFrequencyMap(resolutionFrequency);
            System.out.println("OS Frequency:");
            printFrequencyMap(osFrequency);

            // Perform statistical tests
            performStatisticalTests(sellingPrices, originalPrices, sizes, ratings);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to update frequency map
    private static void updateFrequencyMap(String key, Map<String, Integer> frequencyMap) {
        frequencyMap.put(key, frequencyMap.getOrDefault(key, 0) + 1);
    }

    // Helper method to calculate mean
    private static double calculateMean(List<Double> values) {
        double sum = 0;
        for (double value : values) {
            sum += value;
        }
        return sum / values.size();
    }

    // Helper method to calculate median
    private static double calculateMedian(List<Double> values) {
        values.sort(null);
        int size = values.size();
        if (size % 2 == 0) {
            return (values.get(size / 2 - 1) + values.get(size / 2)) / 2.0;
        } else {
            return values.get(size / 2);
        }
    }

    // Helper method to calculate mode
    private static double calculateMode(List<Double> values) {
        Map<Double, Integer> frequencyMap = new HashMap<>();
        for (double value : values) {
            frequencyMap.put(value, frequencyMap.getOrDefault(value, 0) + 1);
        }
        double mode = 0;
        int maxFrequency = 0;
        for (Map.Entry<Double, Integer> entry : frequencyMap.entrySet()) {
            if (entry.getValue() > maxFrequency) {
                mode = entry.getKey();
                maxFrequency = entry.getValue();
            }
        }
        return mode;
    }

    // Helper method to print frequency map
    private static void printFrequencyMap(Map<String, Integer> frequencyMap) {
        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }

    // Method to perform statistical tests
    private static void performStatisticalTests(List<Double> sellingPrices, List<Double> originalPrices,
                                                 List<Double> sizes, List<Double> ratings) {
        // Perform Chi-square test
        double chiSquare = chiSquareTest(sellingPrices, originalPrices);
        System.out.println("Chi-square value: " + chiSquare);

        // Perform t-test
        double tStatistic = tTest(sellingPrices, originalPrices);
        System.out.println("t-Statistic value: " + tStatistic);

        // Perform ANOVA test
        double fStatistic = ANOVATest(sizes, ratings);
        System.out.println("F-Statistic value: " + fStatistic);

        // Perform Pearson correlation coefficient test
        double correlationCoefficient = pearsonCorrelationCoefficient(sellingPrices, originalPrices);
        System.out.println("Pearson Correlation Coefficient: " + correlationCoefficient);
    }

    // Chi-square test
    private static double chiSquareTest(List<Double> observedValues, List<Double> expectedValues) {
        double chiSquare = 0.0;
        for (int i = 0; i < observedValues.size(); i++) {
            double observed = observedValues.get(i);
            double expected = expectedValues.get(i);
            chiSquare += Math.pow((observed - expected), 2) / expected;
        }
        return chiSquare;
    }

    // Two-sample t-test (assuming equal variances)
    private static double tTest(List<Double> sample1, List<Double> sample2) {
        double mean1 = calculateMean(sample1);
        double mean2 = calculateMean(sample2);
        double variance1 = calculateVariance(sample1);
        double variance2 = calculateVariance(sample2);
        int n1 = sample1.size();
        int n2 = sample2.size();

        double pooledVariance = ((n1 - 1) * variance1 + (n2 - 1) * variance2) / (n1 + n2 - 2);
        double tStatistic = (mean1 - mean2) / Math.sqrt(pooledVariance * (1.0 / n1 + 1.0 / n2));

        return tStatistic;
    }

    // ANOVA test
    private static double ANOVATest(List<Double> group1, List<Double> group2) {
        // Perform ANOVA for simplicity, considering only two groups
        double fStatistic = 0.0;

        double mean1 = calculateMean(group1);
        double mean2 = calculateMean(group2);

        double variance1 = calculateVariance(group1);
        double variance2 = calculateVariance(group2);

        fStatistic = (variance1 / variance2);

        return fStatistic;
    }

    // Pearson correlation coefficient
    private static double pearsonCorrelationCoefficient(List<Double> x, List<Double> y) {
        double sumXY = 0.0;
        double sumX = 0.0;
        double sumY = 0.0;
        double sumXSquare = 0.0;
        double sumYSquare = 0.0;

        int n = x.size();

        for (int i = 0; i < n; i++) {
            sumXY += x.get(i) * y.get(i);
            sumX += x.get(i);
            sumY += y.get(i);
            sumXSquare += Math.pow(x.get(i), 2);
            sumYSquare += Math.pow(y.get(i), 2);
        }

        double numerator = n * sumXY - sumX * sumY;
        double denominator = Math.sqrt((n * sumXSquare - Math.pow(sumX, 2)) * (n * sumYSquare - Math.pow(sumY, 2)));

        return numerator / denominator;
    }

    // Helper method to calculate variance
    private static double calculateVariance(List<Double> values) {
        double mean = calculateMean(values);
        double sumSquareDiff = 0;
        for (double value : values) {
            sumSquareDiff += Math.pow(value - mean, 2);
        }
        return sumSquareDiff / (values.size() - 1);
    }
}
