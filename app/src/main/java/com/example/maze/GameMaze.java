package com.example.maze;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class GameMaze {
    private final int COLS = 10;
    private final int ROWS = 10;
    private int CELL_SIZE;
    private int SCORE;

    private Cell[][] maze;
    private Point ballPoint;
    private Point endPoint;
    private final Paint wall, ball, end, score;

    public enum DIRECTION {
        T, R, B, L
    }

    public GameMaze() {
        initMaze();

        SCORE = 0;

        wall = new Paint();
        wall.setColor(Color.BLACK);
        wall.setStrokeWidth(4);

        ball = new Paint();
        ball.setColor(Color.RED);

        end = new Paint();
        end.setColor(Color.DKGRAY);

        score = new Paint();
        score.setColor(Color.BLACK);
        score.setTextSize(50);
    }

    private void reset() {
        SCORE += 1;
        initMaze();
    }

    private void initMaze() {
        maze = new Cell[COLS][ROWS];
        Stack<Cell> stack = new Stack<>();
        Cell currCell, nextCell;

        for (int y = 0; y < COLS; y++) {
            for (int x = 0; x < ROWS; x++) {
                maze[y][x] = new Cell(x, y);
            }
        }

        currCell = maze[0][0];
        currCell.checked = true;

        do {
            nextCell = getNeighbour(maze, currCell);

            if (nextCell != null) {
                clearWall(currCell, nextCell);
                stack.push(currCell);
                currCell = nextCell;
                currCell.checked = true;
            } else {
                currCell = stack.pop();
            }
        } while (!stack.isEmpty());

        ballPoint = new Point(0, 0);
        endPoint = new Point(COLS - 1, ROWS - 1);
    }

    private Cell getNeighbour(Cell[][] maze, Cell cell) {
        Random random = new Random();
        ArrayList<Cell> neighbours = new ArrayList<>();

        if (cell.point.y > 0) {
            Cell cellT = maze[cell.point.y - 1][cell.point.x];
            if (!cellT.checked) neighbours.add(cellT);
        }

        if (cell.point.x < COLS - 1) {
            Cell cellR = maze[cell.point.y][cell.point.x + 1];
            if (!cellR.checked) neighbours.add(cellR);
        }

        if (cell.point.y < ROWS - 1) {
            Cell cellB = maze[cell.point.y + 1][cell.point.x];
            if (!cellB.checked) neighbours.add(cellB);
        }

        if (cell.point.x > 0) {
            Cell cellL = maze[cell.point.y][cell.point.x - 1];
            if (!cellL.checked) neighbours.add(cellL);
        }

        return neighbours.size() > 0
                ? neighbours.get(random.nextInt(neighbours.size()))
                : null;
    }

    public void clearWall(Cell cellA, Cell cellB) {
        if (cellA.point.y == cellB.point.y && cellA.point.x == cellB.point.x + 1) {
            cellA.tWall = false;
            cellB.bWall = false;
        }

        if (cellA.point.y == cellB.point.y && cellA.point.x == cellB.point.x - 1) {
            cellA.bWall = false;
            cellB.tWall = false;
        }

        if (cellA.point.y == cellB.point.y + 1 && cellA.point.x == cellB.point.x) {
            cellA.lWall = false;
            cellB.rWall = false;
        }

        if (cellA.point.y == cellB.point.y - 1 && cellA.point.x == cellB.point.x) {
            cellA.rWall = false;
            cellB.lWall = false;
        }
    }

    public void draw(Canvas canvas) {
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        // Set canvas in the center
        canvas.translate((width - COLS * CELL_SIZE) / 2f, (height - ROWS * CELL_SIZE) / 2f);

        CELL_SIZE = width / (COLS + 1);

        // Draw maze
        for (int x = 0; x < COLS; x++) {
            for (int y = 0; y < ROWS; y++) {
                Cell cell = maze[x][y];
                float xLeft = x * CELL_SIZE;
                float xRight = (x + 1) * CELL_SIZE;
                float yTop = y * CELL_SIZE;
                float yBottom = (y + 1) * CELL_SIZE;

                if (cell.tWall) canvas.drawLine(xLeft, yTop, xRight, yTop, wall);
                if (cell.rWall) canvas.drawLine(xRight, yTop, xRight, yBottom, wall);
                if (cell.bWall) canvas.drawLine(xLeft, yBottom, xRight, yBottom, wall);
                if (cell.lWall) canvas.drawLine(xLeft, yTop, xLeft, yBottom, wall);
            }
        }

        // Draw ball and end points
        float offset = CELL_SIZE / 2f;
        canvas.drawCircle(CELL_SIZE * ballPoint.x + offset, CELL_SIZE * ballPoint.y + offset, CELL_SIZE / 2.5f, ball);
        canvas.drawCircle(CELL_SIZE * endPoint.x + offset, CELL_SIZE * endPoint.y + offset, CELL_SIZE / 2.5f, end);

        canvas.drawText("Score: " + SCORE, 0, -20, score);
    }

    public void move(DIRECTION direction) {
        if (direction == DIRECTION.T && ballPoint.y > 0 && !maze[ballPoint.x][ballPoint.y].tWall) {
            ballPoint.y -= 1;
        } else if (direction == DIRECTION.R && ballPoint.x < ROWS - 1 && !maze[ballPoint.x][ballPoint.y].rWall) {
            ballPoint.x += 1;
        } else if (direction == DIRECTION.B && ballPoint.y < COLS - 1 && !maze[ballPoint.x][ballPoint.y].bWall) {
            ballPoint.y += 1;
        } else if (direction == DIRECTION.L && ballPoint.x > 0 && !maze[ballPoint.x][ballPoint.y].lWall) {
            ballPoint.x -= 1;
        }

        if (ballPoint.x == endPoint.x && ballPoint.y == endPoint.y) {
            reset();
        }
    }

    private class Cell {
        public final Point point;
        public boolean tWall = true;
        public boolean rWall = true;
        public boolean bWall = true;
        public boolean lWall = true;
        public boolean checked = false;

        public Cell(int x, int y) {
            point = new Point(x, y);
        }
    }

}
