package com.rokner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amind on 5/9/2016.
 */
public class Game {
    private final int BoardSize = 3;
    private NeuralNetwork player1;
    private String name1;

    private NeuralNetwork player2;
    private String name2;

    private char board[][];

    private boolean printEnabled;

    public Game(NeuralNetwork net1, NeuralNetwork net2, boolean printEnabled){
        board = new char[BoardSize][BoardSize];
        player1 = net1;
        player2 = net2;
        this.printEnabled = printEnabled;
    }

    public NeuralNetwork play() throws InvalidInputsException {
        NeuralNetwork winner = null;
        int turns = 0;
        boolean turn = true;
        resetBoard();

        while(winner == null && turns < 9){
            List<Double> inputs = getInputs(turn);

            if(turn){
                processOutput(player1.process(inputs), turn);
            }else{
                processOutput(player2.process(inputs), turn);
            }

            winner = checkForWinner();

            if(printEnabled) {
                printBoard();
                System.out.println("Press enter to continue...");
                try
                {
                    System.in.read();
                }
                catch(Exception e)
                {}
            }

            turn = !turn;
            turns++;
        }

        return winner;
    }

    private NeuralNetwork checkForWinner(){

        char diag1 = board[0][0];
        char diag2 = board[BoardSize - 1][0];
        int diag1Sum = 0;
        int diag2Sum = 0;

        for (int i = 0; i < BoardSize; i++){
            char col = board[i][0];
            char row = board[0][i];
            int colSum = 0;
            int rowSum = 0;

            for (int j = 0; j < BoardSize; j++) {
                if(col != ' ') {
                    colSum += board[i][j];
                }
                if(col != ' ') {
                    rowSum += board[j][i];
                }
            }

            if(col == colSum/BoardSize){
                return getPlayer(col);
            } else if (row == rowSum/BoardSize){
                return getPlayer(row);
            }

            if(diag1 != ' '){
                diag1Sum += board[i][i];
            }
            if(diag2 != ' '){
                diag2Sum += board[(BoardSize - i) - 1][i];
            }
        }

        if(diag1 == diag1Sum/BoardSize){
            return getPlayer(diag1);
        } else if (diag2 == diag2Sum/BoardSize){
            return getPlayer(diag2);
        }

        return null;
    }

    private NeuralNetwork getPlayer(char symbol){
        if(symbol == 'X'){
            return player1;
        }else if(symbol == 'O'){
            return player2;
        }

        return null;
    }

    private void processOutput(List<Double> outputs, boolean turn){
        int maxOutput = getMaxIndex(outputs);

        board[maxOutput%BoardSize][maxOutput/BoardSize] = (turn) ? 'X' : 'O';
    }

    private void printBoard(){
        for (int i = 0; i < BoardSize; i++) {
            System.out.print("|");

            for (int j = 0; j < BoardSize; j++) {
                System.out.print(board[j][i] + "|");
            }

            System.out.println();
        }

        System.out.println();
    }

    private int getMaxIndex(List<Double> list){
        int maxIndex = -1;

        for (int i = 0; i < list.size(); i++) {
            if(maxIndex < 0 || list.get(maxIndex) < list.get(i)){
                if(getCharAtIndex(i) == ' ') {
                    maxIndex = i;
                }
            }
        }

        return maxIndex;
    }

    private char getCharAtIndex(int index){
        return board[index%BoardSize][index/BoardSize];
    }

    private List<Double> getInputs(boolean turn){
        List<Double> inputs = new ArrayList<>();
        double xValue = (turn) ? 1.0 : -1.0;
        double oValue = (turn) ? -1.0 : 1.0;

        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                switch (board[j][i]) {
                    case ' ':
                        inputs.add(0.0);
                        break;
                    case 'X':
                        inputs.add(xValue);
                        break;
                    case 'O':
                        inputs.add(oValue);
                        break;
                }
            }
        }

        return inputs;
    }



    private void resetBoard(){
        for (int i = 0; i < BoardSize; i++) {
            for (int j = 0; j < BoardSize; j++) {
                board[i][j] = ' ';
            }
        }
    }
}
