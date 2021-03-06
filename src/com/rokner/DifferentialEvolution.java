package com.rokner;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by amind on 5/10/2016.
 */
public class DifferentialEvolution {
    private final double EvoRate = 2;
    private final double CrossoverRate = 0.8;

    private List<NeuralNetwork> population;
    int populationSize;
    List<Integer> topology;
    Random rand;

    public DifferentialEvolution(int populationSize, List<Integer> topology) {
        population = new ArrayList<>(populationSize);
        this.populationSize = populationSize;
        this.topology = topology;
        reset();
        rand = new Random();
    }

    public NeuralNetwork evolve(int epochs){
        int currEpoch = 0;
        int a,b,c;
        int R;
        NeuralNetwork newCandidate = new NeuralNetwork(topology);

        while(currEpoch < epochs){
            for (int i = 0; i < populationSize; i++) {
                // chose a b c parents
                do {
                    a = rand.nextInt(populationSize);
                }while(a == i);
                do {
                    b = rand.nextInt(populationSize);
                }while(b == i || b == a);
                do {
                    c = rand.nextInt(populationSize);
                }while(c == i || c == a || c == b);

                List<Double> currGenes = population.get(i).getAllWeights();
                List<Double> aGenes = population.get(a).getAllWeights();
                List<Double> bGenes = population.get(b).getAllWeights();
                List<Double> cGenes = population.get(c).getAllWeights();

                R = rand.nextInt(currGenes.size());

                for (int j = 0; j < currGenes.size(); j++) {
                    double crossover = rand.nextDouble();
                    if(crossover < CrossoverRate) {
                        Double newGene = cGenes.get(j) + EvoRate * (bGenes.get(i) - aGenes.get(i));
                        currGenes.set(j, newGene);
                    }
                }

                newCandidate.setAllWeights(currGenes);

                int oldFitness = population.get(i).getFitness();
                int newFitness = getFitness(newCandidate, 20, false);

                if(oldFitness <= newFitness){
                    population.get(i).setAllWeights(currGenes);
                    population.get(i).setFitness(newFitness);
                }
            }

            currEpoch++;
        }

        int maxIndex = 0;
        int maxFitness = -100000;

        for (int i = 0; i < populationSize; i++) {
            int fitness = getFitness(population.get(i), 50, false);
            if(fitness > maxFitness){
                maxIndex = i;
                maxFitness = fitness;
            }
        }

        return population.get(maxIndex);
    }

    public void reset(){
        population.clear();
        for (int i = 0; i < populationSize; i++) {
            NeuralNetwork net = new NeuralNetwork(topology);
            net.setFitness(getFitness(net, 20, true));
            population.add(net);
        }
    }

    private int getFitness(NeuralNetwork net, int testTries, boolean dummyNet){
        NeuralNetwork dummy = (dummyNet) ?
                  new NeuralNetwork(topology)
                : population.get(rand.nextInt(populationSize));
        Game game1 = new Game(net, dummy, false);
        Game game2 = new Game(dummy, net, false);

        int fitness = 0;

        for (int i = 0; i < testTries; i++) {
            try {
                NeuralNetwork winner = game1.play();

                if(winner == net){
                    fitness++;
                }else if(winner == dummy){
                    fitness--;
                }

                winner = game2.play();

                if(winner == net){
                    fitness++;
                }else if(winner == dummy){
                    fitness--;
                }

            } catch (InvalidInputsException e) {
                e.printStackTrace();
            }
        }

        fitness *= Math.abs(fitness);

        return fitness;
    }
}
