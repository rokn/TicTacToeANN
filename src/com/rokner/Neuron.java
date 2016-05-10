package com.rokner;

import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * Created by amind on 5/9/2016.
 */
public class Neuron {
    private static final Random rand = new Random();
    private static final double randomWeightMultiplier = 1.0;
    private static final double biasValue = 1.0;
    private List<Double> weights;

    public Neuron(int inputs) {
        weights = new Vector<>();

        for(int i = 0; i < inputs + 1; i++){
            weights.add(getRandomWeight());
        }
    }

    public double fire(List<Double> inputs) throws InvalidInputsException {
        if(inputs.size() != (weights.size() - 1)) {
            throw new InvalidInputsException("Invalid size of inputs");
        }

        double sum = 0.0;

        for (int i = 0; i < inputs.size(); i++) {
            sum += inputs.get(i) * weights.get(i);
        }

        sum += biasValue * weights.get(weights.size() - 1);

        return sigmoid(sum);
    }

    public List<Double> getWeights(){
        return weights;
    }

    public void setWeights(List<Double> newWeights){
        weights = newWeights;
    }

    public int getWeightsCount(){
        return weights.size();
    }

    private static double sigmoid(double input){
        return 1 / (1 + Math.exp(-input));
    }

    private static double getRandomWeight() {
        return randomWeightMultiplier * (rand.nextDouble() * 2 - 1);
    }
}
