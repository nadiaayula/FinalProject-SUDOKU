package sudoku;

import java.util.*;

public class Puzzle {
    int[][] numbers = new int[9][9];
    boolean[][] isGiven = new boolean[9][9];
    // Solver solver = new Solver();
    Random r = new Random();

    // Constructor
    public Puzzle() {
        super();
    }

    public void newPuzzle(int difficulty, int sourcePuzzle) {
        if (sourcePuzzle == 0) {
            generateValidSudoku(numbers);
        } else if (sourcePuzzle == 1) {
            generateValidSudoku(numbers);
        }

        // solver.solve(numbers);

        isGiven = generateGiven(isGiven, difficulty);
    }

    private void generateValidSudoku(int[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = 0;
            }
        }

        solveSudokuBacktracking(grid);
    }

    private boolean solveSudokuBacktracking(int[][] grid) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                if (grid[row][col] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(grid, row, col, num)) {
                            grid[row][col] = num;
                            if (solveSudokuBacktracking(grid)) {
                                return true;
                            }

                            // Jika gagal, backtrack (hapus angka)
                            grid[row][col] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] grid, int row, int col, int num) {
        // Memeriksa baris
        for (int x = 0; x < 9; x++) {
            if (grid[row][x] == num) {
                return false;
            }
        }

        // Memeriksa kolom
        for (int x = 0; x < 9; x++) {
            if (grid[x][col] == num) {
                return false;
            }
        }

        // Memeriksa subgrid 3x3
        int startRow = row - row % 3;
        int startCol = col - col % 3;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (grid[i + startRow][j + startCol] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean[][] generateGiven(boolean[][] isGiven, int difficulty) {
        int cellsToRemove;
        Random rand = new Random();

        if (difficulty == 0) {
            cellsToRemove = rand.nextInt(15) + 45;
        } else if (difficulty == 1) {
            cellsToRemove = rand.nextInt(15) + 35;
        } else {
            cellsToRemove = rand.nextInt(15) + 20;
        }

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                isGiven[i][j] = false;
            }
        }

        for (int i = 0; i < cellsToRemove; i++) {
            int row = rand.nextInt(9);
            int col = rand.nextInt(9);
            while (isGiven[row][col] || numbers[row][col] == 0) {
                row = rand.nextInt(9);
                col = rand.nextInt(9);
            }
            isGiven[row][col] = true; 
        }
        return isGiven;
    }
}
