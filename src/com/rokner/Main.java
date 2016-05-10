package com.rokner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> topology = new ArrayList<>();
        topology.add(9);
        topology.add(10);
        topology.add(9);


//        List<Double> we = net.getAllWeights();
//        System.out.println(we.size());
//        net.setAllWeights(we);
//        we = net.getAllWeights();

        DiferentialEvolution learningAlgo = new DiferentialEvolution(4, topology);
        NeuralNetwork best = learningAlgo.evolve(1000);

        int wins = 0;
        try {
            for (int i = 0; i < 100; i++) {
                NeuralNetwork net = new NeuralNetwork(topology);
                Game testGame = new Game(best, net, false);
                NeuralNetwork winner = testGame.play();

                if(winner == best){
                    wins++;
                }

                Game testGame2 = new Game(net, best, false);
                winner = testGame2.play();

                if(winner == best){
                    wins++;
                }
            }
        } catch (InvalidInputsException e) {
            e.printStackTrace();
        }

        System.out.println(wins);
    }
}
