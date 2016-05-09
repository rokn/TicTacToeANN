package com.rokner;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Integer> topology = new ArrayList<>();
        topology.add(9);
        topology.add(10);
        topology.add(9);

        List<Double> inputs = new ArrayList<>();

        inputs.add(1.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);
        inputs.add(0.0);

        NeuralNetwork net = new NeuralNetwork(topology);

        try {
            inputs = net.process(inputs);
            System.out.println(inputs.get(getMaxIndex(inputs)));
        } catch (InvalidInputsException e) {
            e.printStackTrace();
        }
    }

    private static int getMaxIndex(List<Double> list){
        int maxIndex = 0;

        for (int i = 0; i < list.size(); i++) {
            if(list.get(maxIndex) < list.get(i)){
                maxIndex = i;
            }
        }

        return maxIndex;
    }
}
