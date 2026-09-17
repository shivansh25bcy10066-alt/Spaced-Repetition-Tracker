# Spaced Repetition Skill & Habit Tracker

## Overview

The **Spaced Repetition Skill & Habit Tracker** is a pure Java,
console-based application that helps a learner schedule practice
sessions for multiple skills and habits using a **SuperMemo-2 (SM-2)
derived spaced repetition algorithm**. Instead of practising on a
fixed, arbitrary calendar, the interval before the next review of each
item grows or shrinks automatically based on how well the user rates
their own recall/performance (on a 1–5 scale) at the end of every
session.

The project was built entirely with the Java Standard Edition (SE)
class library — no external frameworks, build tools, or database
servers are required.

## Features

- **Deck & Card Manager** — full CRUD (Create, Read, Update, Delete)
  for tracked Skills and Habits, organized into categories.
- **SM-2 Scheduling Engine** — computes the next review date, updated
  easiness factor, and repetition interval for a task from a 1–5
  quality rating.
- **Session Runner** — an interactive console review flow that queues
  only the tasks currently due (using a `PriorityQueue` ordered by
  next review date) and processes feedback for each one in turn.
- **Persistent Storage** — the full application state (all tasks and
  their review history) is saved to `tracker_data.ser` via Java object
  serialization and automatically reloaded on the next run.
- **CSV Export** — review history for every task can be exported to
  `review_history.csv` for external analysis or grading verification.
- **Seed Data** — the app ships pre-populated with four sample
  entries: *Java DSA Problem Solving*, *Harmonica Practice*,
  *Continuous Cursive Handwriting*, and *Calisthenics (Pull-up
  progression)*.

## Technologies Used

| Component            | Technology                                   |
|-----------------------|-----------------------------------------------|
| Language              | Java SE (Java 17+ compatible)                 |
| Build/Run             | Standard `javac` / `java` (no Maven/Gradle)   |
| Persistence           | Java Object Serialization + CSV via `java.io`|
| Data Structures       | `PriorityQueue`, `Map<String, List<Task>>`, `List` |
| Date/Time             | `java.time.LocalDate`                         |
| OOP Concepts          | Abstract classes, interfaces, inheritance, polymorphism, encapsulation |

## Project Structure

```
SpacedRepetitionTracker/
├── src/
│   ├── models/
│   │   ├── Category.java
│   │   ├── ReviewRecord.java
│   │   ├── Task.java            (abstract)
│   │   ├── SkillTask.java
│   │   └── HabitTask.java
│   ├── engine/
│   │   ├── SchedulingAlgorithm.java   (interface — Strategy pattern)
│   │   ├── SM2SchedulingAlgorithm.java
│   │   ├── DeckManager.java
│   │   └── SessionRunner.java
│   ├── utils/
│   │   ├── InputValidator.java
│   │   ├── FileManager.java
│   │   └── SeedDataInitializer.java
│   └── main/
│       └── Main.java
├── statement.md
├── README.md
└── PROJECT_REPORT.md
```

## Steps to Compile and Run

This project has **no external dependencies**. Any standard JDK
(version 11 or later; developed against Java 17+) is sufficient.

1. Clone or download the repository and navigate to its root folder:

   ```bash
   cd SpacedRepetitionTracker
   ```

2. Compile all source files into an `out` directory:

   ```bash
   javac -d out $(find src -name "*.java")
   ```

   On Windows (PowerShell), use:

   ```powershell
   javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src).FullName
   ```

3. Run the application:

   ```bash
   java -cp out main.Main
   ```

4. On first run, the application will report that no saved data was
   found and will initialize the four seed entries automatically. On
   subsequent runs, it will load `tracker_data.ser` from the directory
   in which it was launched.

## Instructions for Testing

Because this is a pure console application with no external test
framework dependency, testing is performed through a combination of
**manual functional testing** and **algorithmic verification**:

1. **Manual functional walkthrough** — Launch the application and
   exercise each menu option in turn (Start Review Session, Manage
   Skills/Habits, View All Tasks, Export CSV, Save & Exit) to confirm
   expected console behaviour.
2. **Scheduling correctness checks** — After rating a task, verify
   that:
   - A rating of 1–2 resets the task's repetition count to 0 and its
     next interval to 1 day.
   - A rating of 3–5 advances the repetition count and follows the
     1 day → 6 day → `interval × easiness factor` progression.
   - The easiness factor never falls below `1.3`.
3. **Persistence checks** — Save and exit, then relaunch the
   application and confirm via "View All Tasks" that all previously
   entered data (including updated review dates) was restored
   correctly.
4. **CSV verification** — After exporting review history, open
   `review_history.csv` in a spreadsheet application and confirm one
   row exists per review event, with correct task IDs, ratings, and
   resulting intervals.
5. **Edge-case input testing** — Deliberately enter non-numeric text
   or out-of-range numbers at menu prompts to confirm the application
   re-prompts gracefully instead of crashing.

See Section 11 ("Testing Approach") of `PROJECT_REPORT.md` for a more
detailed discussion of the unit-level test cases devised for the
scheduling algorithm.
