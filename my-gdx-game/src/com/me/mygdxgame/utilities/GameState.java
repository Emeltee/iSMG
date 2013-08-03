package com.me.mygdxgame.utilities;

/**
 * Contains different possible states that Screens may declare themselves to be
 * in to the application.
 */
public enum GameState {

    /**
     * Indicates that the game has not yet reached a win or lose state. Further
     * updating is still required.
     */
    Running,
    /**
     * Indicates that the game has completed running, and is in the winning
     * state. No further calls to render() are required.
     */
    Win,
    /**
     * Indicates that the game has completed running, and is in the losing
     * state. No further calls to render() are required.
     */
    Lose
}
