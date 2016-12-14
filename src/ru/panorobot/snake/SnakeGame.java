package ru.panorobot.snake;

import java.util.ArrayList;

import android.content.Context;

public class SnakeGame {

    // класс определяющий позицию
    public class pos {
        int x;
        int y;

        //конструктор
        pos(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    // Константы направления
    public static final int DIR_NORTH = 1;
    public static final int DIR_EAST = 2;
    public static final int DIR_SOUTH = 3;
    public static final int DIR_WEST = 4;

    // ширина и высота игрового поля
    // подбиралось исходя из пропорций экрана моего девайса
    public static int mFieldX = 18;
    public static int mFieldY = 30;

    // Очки в игре
    public int mScore=0;

    // Матрица - игровое поле
    private int mField[][] = new int[mFieldX][mFieldY];

    // Сама змея - массив двумерных координат каждого сегмента. -1-змея, 0-пусто, 1 -фрукт
    private ArrayList<pos> mSnake = new ArrayList<pos>();

    // Текущее напраление движения змеи
    int mDirection = SnakeGame.DIR_EAST;

    // пераметр по которому определяется должна ли
    // змейка расти или нет смотрите ниже там понятно
    int isGrowing = 0;

    // Собственно конструктор
    SnakeGame() {
        // очищаем игровое поле
        for (int i = 0; i < mFieldX; i++)
            for (int j = 0; j < mFieldY; j++) {
                mField[i][j] = 0;
            }
        // создаем змею
        mSnake.add(new pos(2, 2));
        // каждая клетка поля в которой
        // находится змея - отмечается -1
        mField[2][2] = -1;
        mSnake.add(new pos(2, 3));
        mField[2][3] = -1;
        mSnake.add(new pos(2, 4));
        mField[2][4] = -1;
        // добавляем на игровое поле фрукт
        addFruite();
    }

    // метод добавляет фрукт на игровое поле
    // поскольку игра у нас самая простая то
    // и фрукт только один а его код на поле - 2
    private void addFruite() {
        boolean par = false;
        while (!par) {
            int x = (int) (Math.random() * mFieldX);
            int y = (int) (Math.random() * mFieldY);
            if (mField[x][y] == 0) {
                mField[x][y] = 2;
                par = true;
            }
        }
    }

    // Этот метод соержит в себе всю логику игры
    // здесь опиываются все действия которые должны происходить
    // при каждом перемещении змеи
    // при этом, учитывается текущее направление и
    // проверяется, может ли змея ходить в указанном направлении
    // собственно вся игровая логика заключена в этом методе
    public boolean nextMove() {
        // смотрим, куда у нас направлена змея сейчас
        switch (this.mDirection) {
            // если на север
            case DIR_NORTH: {
                // тогда рассчитываем координаты в которые попадет
                // голова змеи на следуюзщем ходу
                int nextX = mSnake.get(mSnake.size() - 1).x;
                int nextY = mSnake.get(mSnake.size() - 1).y - 1;
                // если мы не утыкаемся в верхнюю стенку
                // и если клетка куда мы идем пуста (о чем нам говорит
                // нулевое значение в указанной клетке поля)
                if ((nextY >= 0) && mField[nextX][nextY] == 0) {
                    // то мы проверяем, растет ли в данный момент змея
                    if (isGrowing > 0) {
                        // если растет, уменьшаем запас роста и не
                        // двигаем хвост змеи
                        isGrowing--;
                    } else {
                        // если не растет, то передвигаем хвост змеи
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    //Затем перемещаем голову змеи
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    // и на этом все закончилось, возвращаем истину
                    return true;
                } else if ((nextY >= 0) && mField[nextX][nextY] == 1) {
                    // если мы уткнулись в препятствие возвращаем ложь
                    return false;
                } else if ((nextY >= 0) && mField[nextX][nextY] > 1) {
                    // А вот если мы уткнулись во фрукт,
                    // тогда увеличиваем запас роста
                    isGrowing = isGrowing + 2;
                    // добавляем очков!
                    mScore=mScore+10;
                    // и переносим голову змеи
                    // на соответствующую клетку поля
                    mField[nextX][nextY] = 0;
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    // ну и соответственно добавляем на поле новый фрукт!
                    addFruite();
                    return true;
                } else {
                    // во всех остальных случаях возвращаем false
                    return false;
                }
            }
            // Здесь все то же самое, только
            // для других направлений
            case DIR_EAST: {
                int nextX = mSnake.get(mSnake.size() - 1).x + 1;
                int nextY = mSnake.get(mSnake.size() - 1).y;
                if ((nextX < mFieldX) && mField[nextX][nextY] == 0) {
                    if (isGrowing > 0) {
                        isGrowing--;
                    } else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else if ((nextX < mFieldX) && mField[nextX][nextY] == 1) {
                    return false;
                } else if ((nextX < mFieldX) && mField[nextX][nextY] > 1) {
                    isGrowing = isGrowing + 2;
                    mScore=mScore+10;
                    mField[nextX][nextY] = 0;
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    addFruite();
                    return true;
                } else {
                    return false;
                }
            }
            case DIR_SOUTH: {
                int nextX = mSnake.get(mSnake.size() - 1).x;
                int nextY = mSnake.get(mSnake.size() - 1).y + 1;
                if ((nextX < mFieldX) && mField[nextX][nextY] == 0) {
                    if (isGrowing > 0) {
                        isGrowing--;
                    } else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else if ((nextX < mFieldX) && mField[nextX][nextY] == 1) {
                    return false;
                } else if ((nextX < mFieldX) && mField[nextX][nextY] > 1) {
                    isGrowing = isGrowing + 2;
                    mScore=mScore+10;
                    mField[nextX][nextY] = 0;
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    addFruite();
                    return true;
                } else {
                    return false;
                }
            }
            case DIR_WEST: {
                int nextX = mSnake.get(mSnake.size() - 1).x - 1;
                int nextY = mSnake.get(mSnake.size() - 1).y;
                if ((nextX >= 0) && mField[nextX][nextY] == 0) {
                    if (isGrowing > 0) {
                        isGrowing--;
                    } else {
                        mField[mSnake.get(0).x][mSnake.get(0).y] = 0;
                        mSnake.remove(0);
                    }
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    return true;
                } else if ((nextX >= 0) && mField[nextX][nextY] == 1) {
                    return false;
                } else if ((nextX >= 0) && mField[nextX][nextY] > 1) {
                    isGrowing = isGrowing + 2;
                    mScore=mScore+10;
                    mField[nextX][nextY] = 0;
                    mSnake.add(new pos(nextX, nextY));
                    mField[nextX][nextY] = -1;
                    addFruite();
                    return true;
                } else {
                    return false;
                }
            }
        }
        return false;
    }

    // здесь и нижу всякие геттеры и сеттеры
    // думаю тут и объяснять нечего
    public int getDirection() {
        return mDirection;
    }

    public void clearScore(){
        this.mScore=0;
    }

    public void setDirection(int direction) {
        this.mDirection = direction;
    }

    public int[][] getmField() {
        return mField;
    }

    public int getSnakeLength() {
        return  mSnake.size();
    }

    public ArrayList<pos> getmSnake() {
        return mSnake;
    }
}

