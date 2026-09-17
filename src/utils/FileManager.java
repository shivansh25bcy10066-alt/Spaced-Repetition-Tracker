package utils;

import engine.DeckManager;
import models.ReviewRecord;
import models.Task;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;

/**
 * Handles all persistence concerns for the application using pure Java
 * File I/O: binary object serialization for saving/restoring full
 * application state, and a human-readable CSV export of review history
 * (useful for external analysis or for grading/verification purposes).
 */
public class FileManager {

    private static final String DATA_FILE = "tracker_data.ser";
    private static final String CSV_LOG_FILE = "review_history.csv";

    public void saveDeckManager(DeckManager manager) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(DATA_FILE))) {
            oos.writeObject(manager);
            System.out.println("[System] Tracker state saved successfully to " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("[Error] Could not save tracker state: " + e.getMessage());
        }
    }

    public DeckManager loadDeckManager() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return null;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            return (DeckManager) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("[Error] Could not load saved state, starting fresh: " + e.getMessage());
            return null;
        }
    }

    public void exportHistoryToCsv(DeckManager manager) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(CSV_LOG_FILE))) {
            writer.println("TaskID,TaskName,Category,ReviewDate,QualityRating,ResultingIntervalDays");
            for (Task task : manager.getAllTasks()) {
                for (ReviewRecord record : task.getHistory()) {
                    writer.printf("%d,%s,%s,%s,%d,%d%n",
                            task.getId(),
                            escapeCsv(task.getName()),
                            task.getCategory(),
                            record.getReviewDate(),
                            record.getQualityRating(),
                            record.getResultingIntervalDays());
                }
            }
            System.out.println("[System] Review history exported to " + CSV_LOG_FILE);
        } catch (IOException e) {
            System.out.println("[Error] Could not export CSV: " + e.getMessage());
        }
    }

    private String escapeCsv(String value) {
        if (value.contains(",")) {
            return "\"" + value + "\"";
        }
        return value;
    }
}
