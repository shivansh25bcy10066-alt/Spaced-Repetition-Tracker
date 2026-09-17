package main;

import engine.SessionRunner;

/**
 * Application entry point. Delegates all control flow to the
 * SessionRunner (the Session Runner console UI module).
 */
public class Main {
    public static void main(String[] args) {
        SessionRunner sessionRunner = new SessionRunner();
        sessionRunner.run();
    }
}
