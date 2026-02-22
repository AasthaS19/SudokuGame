# 🧩 Sudoku Game – Java Swing Application

## 📌 Description

A GUI-based Sudoku game built using Java Swing. The game supports multiple difficulty levels, hints, real-time validation, and persistent save functionality that allows users to resume progress even after restarting the application.

This project demonstrates strong understanding of:

- Object-Oriented Programming
- Backtracking Algorithm
- Java Swing (GUI Development)
- File I/O and Serialization
- Event Handling

---

## 🚀 Features

- 🎮 Interactive 9×9 Sudoku GUI
- 🎯 Three difficulty levels:
  - Easy
  - Medium
  - Hard
- 🧠 Backtracking-based puzzle solver
- 💡 Hint system
- ✅ Real-time input validation
- 💾 Save game to disk
- 🔄 Resume previously saved game
- 🏠 Home screen navigation
- 🎉 Win detection with dialog

---

## 🛠️ Tech Stack

- Java
- Java Swing (GUI)
- OOP Principles
- Backtracking Algorithm
- Java File I/O
- Object Serialization

---

## 📁 Project Structure

```
SudokuGame/
│
├── src/
│   ├── Main.java
│   ├── HomeScreen.java
│   ├── SudokuGUI.java
│   ├── Puzzles.java
│   ├── SudokuSolver.java
│   └── GameState.java
│
├── .gitignore
└── README.md
```

---

## 🧠 How It Works

### 🔹 Puzzle Generation

1. Randomly seeds valid numbers in the board.
2. Uses a backtracking solver to generate a complete solution.
3. Removes cells based on difficulty level:
   - Easy → 35 cells removed
   - Medium → 45 cells removed
   - Hard → 55 cells removed

### 🔹 Solving Algorithm

The project uses the **Backtracking Algorithm**:

1. Find an empty cell.
2. Try digits 1–9.
3. Check if the number is valid.
4. Recursively solve the rest of the board.
5. Backtrack if a conflict occurs.

This guarantees a valid Sudoku solution.

---

## 💾 Save & Resume System

The game state is stored at:

```
<user-home>/.sudokuapp/save.dat
```

It stores:
- Current board state
- Solution board
- Remaining hints

Serialization ensures the game persists even after application restarts.

---

## ▶️ How to Run

### Option 1: Using an IDE

1. Open the project in IntelliJ / Eclipse / VS Code
2. Run `Main.java` from the `src/` folder

### Option 2: Using Terminal

```bash
cd src
javac *.java
java Main
```

---

## 🎯 Learning Outcomes

Through this project, I gained hands-on experience in:

- Designing GUI applications using Swing
- Implementing backtracking recursively
- Managing application state
- File persistence using serialization
- Event-driven programming
- Clean OOP architecture

---

## 👩‍💻 Author

**Aastha Sharma**
B.Tech CSE (3rd Year) | DIT University
