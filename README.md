# Hex Game (Java)

**Date:** December 2022

## Summary

This project is a Java implementation of the game Hex. It features:

*   **Playable Hex game:** Implemented with both text-based and Graphical User Interfaces (GUI).
*   **AI Opponent:**  Includes an AI player using the Negamax algorithm to play against a human.
*   **Heuristic AI:** AI decision-making is based on a heuristic function that calculates shortest paths on the board.

## Key Files

*   **`Main.java`**:  Core game logic, text interface, and AI implementation.
*   **`HexGUI.java`**:  Graphical User Interface using Swing.
*   **`Node.java`**:  Represents a hexagonal cell on the game board.

## AI Strategy

The AI uses the **Negamax** algorithm with **alpha-beta pruning** and a heuristic based on shortest path calculations to determine its moves.

## Potential Improvements

Future development could include:

*   Stronger AI (deeper search, refined heuristics).
*   More game features (different board sizes, save/load, undo).
*   Network multiplayer.