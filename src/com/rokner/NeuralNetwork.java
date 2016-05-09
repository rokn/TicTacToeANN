package com.rokner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by amind on 5/9/2016.
 */
public class NeuralNetwork {

    private Neuron neurons[][];
    private int layers;


    public NeuralNetwork(List<Integer> topology) {
        layers = topology.size() - 1;
        neurons = new Neuron[layers][];

        for(int i = 0; i < neurons.length; i++){
            neurons[i] = new Neuron[topology.get(i + 1)];

            for (int j = 0; j < topology.get(i + 1); j++) {
                neurons[i][j] = new Neuron(topology.get(i));
            }
        }
    }

    public List<Double> process(List<Double> inputs) throws InvalidInputsException {
        List<Double> nextInputs;

        for (int i = 0; i < layers; i++) {
            nextInputs = new ArrayList<>();

            for (int j = 0; j < neurons[i].length; j++) {
                nextInputs.add(neurons[i][j].fire(inputs));
            }

            inputs = new ArrayList<>(nextInputs);
        }

        return inputs;
    }
}
