package com.gilpratte;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class Main {

    Map<String, List<Entry>> data = new HashMap<>();
    Map<String, Double> fundTotal = new HashMap<>();

    void process() {
        try (Stream<String> stream = Files.lines(Paths.get("columbia.csv"))) {
            stream.forEach(s -> {
                addEntry(s);
            });
            //printData();
            total();
            printFundTotals();
        } catch (IOException e) {
            System.out.println("\nThere was a problem reading the columbia.csv file");
        }
    }

    void addEntry(String csv) {
        String[] columns = csv.split(",");
        Entry entry = new Entry();
        for (int i = 0; i < columns.length; i++) {
            switch (i) {
                case 0:
                    List<Entry> entries = data.get(columns[i]);
                    if (entries == null) {
                        entries = new LinkedList<>();
                        data.put(columns[i], entries);
                    }
                    entries.add(entry);
                    break;
                case 1:
                    entry.date = LocalDate.parse(columns[i]);
                    break;
                case 2:
                    entry.price = Double.parseDouble(columns[i]);
                    break;
                case 3:
                    entry.shares = Double.parseDouble(columns[i]);
                    break;
            }
        }
    }

    void total() {
        for (String key : data.keySet()) {
            double total = 0;
            List<Entry> entries = data.get(key);
            for (Entry entry : entries) {
                total += entry.price * entry.shares;
            }
            fundTotal.put(key, new Double(total));
        }
    }

    void printData() {
        for (String key : data.keySet()) {
            System.out.println(key);
            List<Entry> entries = data.get(key);
            for (Entry entry : entries) {
                System.out.println("\t" + entry);
            }
        }
    }

    void printFundTotals() {
        System.out.println("Fund totals");
        double allFundsTotal = 0;
        for (String key : fundTotal.keySet()) {
            String totalRounded = String.format("%.2f", fundTotal.get(key));
            allFundsTotal += Double.parseDouble(totalRounded);
            System.out.println("\t" + key + " $" + totalRounded);
        }
        System.out.println("All funds: " + allFundsTotal);
    }


    class Entry {
        LocalDate date;
        double price;
        double shares;

        @Override
        public String toString() {
            return "Entry{" +
                "date=" + date +
                ", $price=" + price +
                ", shares=" + shares +
                '}';
        }
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.process();
    }
}
