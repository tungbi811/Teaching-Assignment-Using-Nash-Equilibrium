package Code.GeneticAlgorithm;

import Code.utils.Data;
import Code.utils.Solution;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author tungd
 */
public class MainGA {

    public static void main(String[] args) throws IOException {
        Data data = Data.readDataFromFrile();

        int numberPopulation = 500;
        int numberIteration = 10000;
        int numberCrossOver =250;
        int numberMutation = 50;
        GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(data, numberPopulation, numberIteration, numberCrossOver, numberMutation);
        GAImplement gaImplement = new GAImplement();

        int numberRunTime = 1;
        
        for (int i = 0; i < numberRunTime; i++) {
            ArrayList gaResults = gaImplement.implement(geneticAlgorithm);
            Solution.writeSolutions(gaResults, data, "GA", "PayoffResult" + i);
        }
    }
}
