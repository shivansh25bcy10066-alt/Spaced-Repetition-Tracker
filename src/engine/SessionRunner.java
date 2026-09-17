package engine;

import models.Category;
import models.HabitTask;
import models.SkillTask;
import models.Task;
import utils.FileManager;
import utils.InputValidator;
import utils.SeedDataInitializer;

import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * The console-based "Session Runner" module. Owns the main menu loop,
 * builds the priority queue of due tasks for a review session, and routes
 * user feedback into the SchedulingAlgorithm. Also hosts the Deck & Card
 * Manager (CRUD) submenu.
 */
public class SessionRunner {

    private final DeckManager deckManager;
    private final SchedulingAlgorithm schedulingAlgorithm;
    private final FileManager fileManager;
    private final Scanner scanner;

    public SessionRunner() {
        this.fileManager = new FileManager();
        DeckManager loaded = fileManager.loadDeckManager();
        if (loaded != null) {
            this.deckManager = loaded;
            for (Task task : deckManager.getAllTasks()) {
                Task.ensureIdCounterAbove(task.getId());
            }
            System.out.println("[System] Existing tracker data loaded successfully.\n");
        } else {
            this.deckManager = new DeckManager();
            SeedDataInitializer.populate(this.deckManager);
            System.out.println("[System] No saved data found. Initialized with seed data.\n");
        }
        this.schedulingAlgorithm = new SM2SchedulingAlgorithm();
        this.scanner = new Scanner(System.in);
    }

    public void run() {
        printBanner();
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputValidator.readIntInRange(scanner, "Enter choice: ", 1, 5);
            switch (choice) {
                case 1:
                    startReviewSession();
                    break;
                case 2:
                    deckManagementMenu();
                    break;
                case 3:
                    viewAllTasks();
                    break;
                case 4:
                    fileManager.exportHistoryToCsv(deckManager);
                    break;
                case 5:
                    fileManager.saveDeckManager(deckManager);
                    System.out.println("Goodbye! Keep practicing.");
                    running = false;
                    break;
                default:
                    break;
            }
        }
        scanner.close();
    }

    private void printBanner() {
        System.out.println("=========================================================");
        System.out.println("   SPACED REPETITION SKILL & HABIT TRACKER (SM-2 Engine)");
        System.out.println("=========================================================");
    }

    private void printMainMenu() {
        System.out.println("\n--- MAIN MENU ---");
        System.out.println("1. Start Review Session");
        System.out.println("2. Manage Skills/Habits (CRUD)");
        System.out.println("3. View All Tasks");
        System.out.println("4. Export Review History (CSV)");
        System.out.println("5. Save & Exit");
    }

    // ------------------------------------------------------------------
    // Module: Session Runner (queues due tasks, processes feedback)
    // ------------------------------------------------------------------
    private void startReviewSession() {
        PriorityQueue<Task> dueQueue = deckManager.getDueTasksQueue();
        if (dueQueue.isEmpty()) {
            System.out.println("\n[Session] No tasks are due for review today. Great job staying ahead!");
            return;
        }

        System.out.println("\n[Session] " + dueQueue.size() + " task(s) due for review.");
        int sessionCount = 0;
        while (!dueQueue.isEmpty()) {
            Task current = dueQueue.poll();
            sessionCount++;
            System.out.println("\n--- Reviewing Task " + sessionCount + " ---");
            System.out.println(current);
            System.out.println("Type        : " + current.getTaskType());
            System.out.println("Description : " + current.getDescription());
            System.out.println("Progress    : " + current.getProgressSummary());

            int rating = InputValidator.readIntInRange(
                    scanner, "Rate your recall/performance (1=Poor ... 5=Excellent): ", 1, 5);

            schedulingAlgorithm.schedule(current, rating);

            System.out.println("[Scheduled] Next review for \"" + current.getName()
                    + "\" is on " + current.getNextReviewDate()
                    + " (interval: " + current.getCurrentInterval() + " day(s)).");
        }
        System.out.println("\n[Session] Review session complete. " + sessionCount + " task(s) processed.");
    }

    // ------------------------------------------------------------------
    // Module: Deck & Card Manager (CRUD)
    // ------------------------------------------------------------------
    private void deckManagementMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- MANAGE SKILLS/HABITS ---");
            System.out.println("1. Add New Skill");
            System.out.println("2. Add New Habit");
            System.out.println("3. Edit Existing Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Back to Main Menu");
            int choice = InputValidator.readIntInRange(scanner, "Enter choice: ", 1, 5);
            switch (choice) {
                case 1:
                    addSkill();
                    break;
                case 2:
                    addHabit();
                    break;
                case 3:
                    editTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    break;
            }
        }
    }

    private Category chooseCategory() {
        System.out.println("Select Category:");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }
        int idx = InputValidator.readIntInRange(scanner, "Enter choice: ", 1, categories.length);
        return categories[idx - 1];
    }

    private void addSkill() {
        String name = InputValidator.readNonEmptyLine(scanner, "Enter skill name: ");
        String desc = InputValidator.readNonEmptyLine(scanner, "Enter description: ");
        Category category = chooseCategory();
        String level = InputValidator.readNonEmptyLine(scanner, "Enter current proficiency level: ");
        SkillTask task = new SkillTask(name, desc, category, level);
        deckManager.addTask(task);
        System.out.println("[System] Skill added: " + task);
    }

    private void addHabit() {
        String name = InputValidator.readNonEmptyLine(scanner, "Enter habit name: ");
        String desc = InputValidator.readNonEmptyLine(scanner, "Enter description: ");
        Category category = chooseCategory();
        HabitTask task = new HabitTask(name, desc, category);
        deckManager.addTask(task);
        System.out.println("[System] Habit added: " + task);
    }

    private void editTask() {
        viewAllTasks();
        if (deckManager.totalTaskCount() == 0) {
            return;
        }
        int id = InputValidator.readIntInRange(scanner, "Enter Task ID to edit: ", 1, Integer.MAX_VALUE);
        deckManager.findTaskById(id).ifPresentOrElse(task -> {
            String name = InputValidator.readNonEmptyLine(
                    scanner, "Enter new name (current: " + task.getName() + "): ");
            String desc = InputValidator.readNonEmptyLine(scanner, "Enter new description: ");
            task.setName(name);
            task.setDescription(desc);
            System.out.println("[System] Task updated: " + task);
        }, () -> System.out.println("[Error] Task ID not found."));
    }

    private void deleteTask() {
        viewAllTasks();
        if (deckManager.totalTaskCount() == 0) {
            return;
        }
        int id = InputValidator.readIntInRange(scanner, "Enter Task ID to delete: ", 1, Integer.MAX_VALUE);
        boolean removed = deckManager.removeTaskById(id);
        System.out.println(removed ? "[System] Task removed." : "[Error] Task ID not found.");
    }

    private void viewAllTasks() {
        List<Task> all = deckManager.getAllTasks();
        if (all.isEmpty()) {
            System.out.println("\n[Info] No tasks currently tracked.");
            return;
        }
        System.out.println("\n--- ALL TRACKED SKILLS & HABITS ---");
        for (Task task : all) {
            System.out.println(task);
        }
    }
}
