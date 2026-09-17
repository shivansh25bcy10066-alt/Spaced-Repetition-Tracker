# Problem Statement — Spaced Repetition Skill & Habit Tracker

## Problem Statement

Learners who are simultaneously developing multiple unrelated skills —
for instance, a technical skill like algorithmic problem solving, a
motor/musical skill like playing an instrument, a fine-motor skill like
handwriting, and a physical fitness habit — struggle to decide **when**
to revisit each one. Practising too frequently wastes time on skills
that are already well-retained, while practising too infrequently
causes forgetting and progress loss. Existing to-do list and habit
tracker applications rely on fixed, arbitrary schedules (e.g. "every
day" or "every Monday") and do not adapt to how well the learner is
actually performing.

Spaced repetition — the technique behind flashcard systems such as
Anki — solves an analogous problem for memorization by expanding or
contracting the interval between reviews based on recall quality. This
project applies the same principle, generalized beyond flashcards, to
**skills and habits**, using a SuperMemo-2 (SM-2) derived scheduling
algorithm to decide the next practice date for each tracked item based
on a simple 1–5 self-rating.

## Scope of the Project

The project is a **pure Java, console-based** application. It is
intentionally scoped to run without any external frameworks, build
tools, or database servers, so that it can be compiled and executed
with nothing beyond a standard JDK (`javac` / `java`). In scope:

- Managing a personal collection of Skills and Habits (create, read,
  update, delete).
- Computing the next review date for each item using an SM-2 derived
  scheduling algorithm driven by a 1–5 quality rating.
- Running an interactive review session that queues only the items
  currently due and collects feedback for each.
- Persisting all application state between runs using Java
  serialization, and exporting review history to CSV for external
  analysis.

Out of scope: graphical user interfaces, networked/multi-user
synchronization, mobile notifications, and integration with any
third-party calendar or database service. These are noted as
candidates for future work.

## Target Users

- Undergraduate students and self-learners who are cultivating more
  than one skill or habit at a time and want a lightweight, adaptive
  way to schedule practice.
- Users who prefer a fast, distraction-free command-line tool over a
  heavyweight GUI or mobile app.
- Anyone wanting a concrete, extensible reference implementation of
  the SM-2 spaced repetition algorithm in idiomatic, object-oriented
  Java.

## High-Level Features

1. **Deck & Card Manager** — Add, edit, delete, and list Skills and
   Habits, organized by category (Technical Skill, Musical Skill,
   Handwriting, Physical Fitness).
2. **Scheduling Engine** — An SM-2 derived algorithm that adjusts each
   item's easiness factor, repetition count, and interval based on a
   1–5 recall/performance rating, and computes the next review date.
3. **Session Runner** — A console-driven review session that pulls all
   currently due items into a `PriorityQueue` (ordered by next review
   date), presents each one in turn, and records the user's rating.
4. **Persistence** — Full application state is saved to and loaded
   from disk via Java object serialization; review history can be
   exported to a CSV file.
5. **Seed Data** — The application ships with four pre-populated
   entries — Java DSA Problem Solving, Harmonica Practice, Continuous
   Cursive Handwriting, and Calisthenics (Pull-up progression) — so it
   is immediately usable and demonstrable.
