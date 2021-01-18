import java.util.Random;
import java.util.Arrays;

public class HillClimbing {

    private static final int ROW = 8;
    private static final int COLUMN = 8;
    private static int changes = 0;
    private static int restarts = 0;
    private static int firstH = 0;

    public static void main(String[] args) {
        int h;
        int firstBoard[][] = new int[ROW][COLUMN];

        firstBoard = generateBoard();
        firstH = heuristic(firstBoard);
        h = firstH;


        while (h != 0)
        {
            firstBoard = checkBoard(firstBoard);
            h = firstH;
        }

        System.out.println();
        printState(firstBoard);
        System.out.println("Solution Found!");
        System.out.println("State Changes: " + changes);
        System.out.println("Restarts: " + restarts);

        System.out.println();
    }

    private static void printState(int arr[][]) {
        System.out.println("Current State");

        for (int r = 0; r < ROW; r++)
        {
            for (int c = 0; c < COLUMN; c++)
            {
                System.out.print(arr[r][c] + ",");
            }
            System.out.println();
        }
    }

    private static int[][] generateBoard() {
        int index;
        int tempBoard[][] = new int[ROW][COLUMN];
        Random rd = new Random();

        for (int r = 0; r < ROW; r++)
        {
            index = rd.nextInt(8);

            for (int c = 0; c < COLUMN; c++)
            {
                if (c == index)
                {
                    tempBoard[r][c] = 1;
                }
                else
                {
                    tempBoard[r][c] = 0;
                }
            }
        }
        return tempBoard;
    }

    // Heuristic count of how many queen interact with each other
    private static int heuristic(int arr[][])
    {
        int newArray[][] = new int [ROW][COLUMN];
       // int h = 0;

        for(int row0 = 0; row0 < ROW; row0++)
        {
            for (int column0 = 0; column0 < COLUMN; column0++)
            {
                newArray[row0][column0] = arr[row0][column0];

            }
        }

        int h = 0;


        for (int r = 0; r < ROW; r++)
        {
            for (int c = 0; c < COLUMN; c++)
            {
                if (newArray[r][c] == 1)
                {

                    // up and down conflicts check ( NOTE: left and right do not matter because queens are places by row )
                    for (int down = ROW - 1; down > r; down--)
                    {
                        if (newArray[down][c] == 1)
                        {
                            h++;
                        }
                    }
                    for (int up = 0; up < r; up++)
                    {
                        if (newArray[up][c] == 1)
                        {
                            h++;
                        }
                    }

                    //TODO: Fix. The diagonal loops are not working properly.
                    // quadrant diagonal conflicts check
                    if (r != 0 || c != 7)
                    {
                        for (int topright = 1, b = (c + 1); b < r; topright++, b++)
                        {
                            if (arr[r - topright][b] == 1)
                            {
                                h++;
                            }
                        }
                    }

                    if (r != 7 || c != 0)
                    {
                        for (int bottomleft = 0; bottomleft > r; bottomleft++)
                        {
                            if (newArray[r + bottomleft][c - bottomleft] == 1)
                            {
                                h++;
                            }
                        }
                    }

                    if (r != 0 || c != 0)
                    {
                        for (int topleft = 0; topleft > r; topleft++)
                        {
                            if (newArray[r - topleft][c - topleft] == 1)
                            {
                                h++;
                            }
                        }
                    }

                    if (r != 7 || c != 7)
                    {
                        for (int bottomright = 0; bottomright > r; bottomright++)
                        {
                            if (newArray[r - bottomright][c + bottomright] == 1)
                            {
                                h++;
                            }
                        }
                    }
                }
            }
        }
        return h;
    }

    private static int[][] checkBoard(int currentBoard[][])
    {
        int boardH = heuristic(currentBoard);
        int betterH = boardH;
        int tempH;
        int bestBoard[][] = new int[ROW][COLUMN];
        int tempBoard[][] = new int[ROW][COLUMN];
        int neighborsOnBoard = 0;


        // Copy to 2 separate 2d arrays to compare the original or the modified one. Current Board assumed best until proven wrong in the other for loop.
        for(int row0 = 0; row0 < ROW; row0++)
        {
            for (int column0 = 0; column0 < COLUMN; column0++)
            {
                bestBoard[row0][column0] = currentBoard[row0][column0];
                tempBoard[row0][column0] = bestBoard[row0][column0];
            }
        }

        //check if the modified board is better than the original brought in. If it is, replace it, if not, do nothing.
        for(int r = 0; r < ROW; r++)
        {
            for(int col = 0; col < COLUMN; col++)
            {
                tempBoard = moveLeft(bestBoard, r);
                tempH = heuristic(tempBoard);

                if (tempH < betterH)
                {
                    neighborsOnBoard++;
                    betterH = tempH;

                    for (int row = 0; row < ROW; row++)
                    {
                        for (int column = 0; column < COLUMN; column++)
                        {
                            bestBoard[row][column] = tempBoard[row][column];
                        }
                    }
                }
            }
        }

        System.out.println();
        System.out.println("Current h: " + boardH);
        printState(currentBoard);
        System.out.println("Neighbors found with lower h: " + neighborsOnBoard);
        System.out.println("Setting new current state");

        if( betterH == boardH)
        {
            System.out.println("RESTART");
            bestBoard = generateBoard();
            firstH = heuristic(bestBoard);
            restarts++;
        }
        else
        {
            firstH = betterH;
        }

        changes++;
        return bestBoard;
    }

    // Moves the specified column indexes one place to the left
    private static int[][] moveLeft (int arr[][], int row)
    {
        int[][] newArray = new int [ROW][COLUMN];

        // Copies arr into newArray
        for(int r = 0; r < ROW; r++)
        {
            for(int col = 0; col < COLUMN; col++)
            {
                newArray[r][col] = arr[r][col];
            }
        }

        // Changes specified column indexes one place to the left
        for(int c = 0; c < COLUMN; c++)
        {
            if(c < COLUMN - 1)
            {
                newArray[row][c] = arr[row][c+1];
            }
            else
                newArray[row][c] = arr[row][0];
        }

        return newArray;
    }
}