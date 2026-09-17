package engine;

import models.Task;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;

/**
 * Owns the in-memory "deck" of all tracked Tasks, organized as a
 * Map<String, List<Task>> keyed by Category name. Provides full CRUD
 * operations and exposes due tasks via a PriorityQueue ordered by
 * next review date (earliest due first).
 */
public class DeckManager implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<String, List<Task>> deck;

    public DeckManager() {
        this.deck = new HashMap<>();
    }

    // ---- Create ----
    public void addTask(Task task) {
        String key = task.getCategory().name();
        deck.computeIfAbsent(key, k -> new ArrayList<>()).add(task);
    }

    // ---- Read ----
    public Optional<Task> findTaskById(int id) {
        return getAllTasks().stream().filter(t -> t.getId() == id).findFirst();
    }

    public List<Task> getAllTasks() {
        List<Task> all = new ArrayList<>();
        for (List<Task> categoryTasks : deck.values()) {
            all.addAll(categoryTasks);
        }
        return all;
    }

    public List<Task> getTasksByCategory(String categoryKey) {
        return deck.getOrDefault(categoryKey, Collections.emptyList());
    }

    public Map<String, List<Task>> getDeck() {
        return deck;
    }

    public int totalTaskCount() {
        return getAllTasks().size();
    }

    // ---- Update ---- (mutations are performed directly on the returned Task
    // reference by the caller, e.g. SessionRunner#editTask)

    // ---- Delete ----
    public boolean removeTaskById(int id) {
        for (List<Task> categoryTasks : deck.values()) {
            Iterator<Task> iterator = categoryTasks.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getId() == id) {
                    iterator.remove();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Builds a PriorityQueue of every Task whose next review date is
     * today or earlier, ordered earliest-due-first via Task's natural
     * Comparable ordering.
     */
    public PriorityQueue<Task> getDueTasksQueue() {
        PriorityQueue<Task> dueQueue = new PriorityQueue<>();
        LocalDate today = LocalDate.now();
        for (Task task : getAllTasks()) {
            if (!task.getNextReviewDate().isAfter(today)) {
                dueQueue.add(task);
            }
        }
        return dueQueue;
    }
}
