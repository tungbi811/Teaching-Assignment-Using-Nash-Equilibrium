package Code.Tabu;

import Code.utils.Solution;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author tungd
 */
public class TabuSearchImplement {

    TabuSearch tabuSearch;

    public TabuSearchImplement(TabuSearch tabuSearch) {
        this.tabuSearch = tabuSearch;
    }

    public ArrayList<int[]> implement() {
        int[] initialSolution = Solution.initSolution(tabuSearch.getData());
        int[] bestSolution = initialSolution;
        double bestSolutionFitness = Solution.getFitness(tabuSearch.getData(), bestSolution);
        int[] currentSolution = initialSolution;
        ArrayList<int[]> tabuList = new ArrayList();
        ArrayList<int[]> result = new ArrayList();
        
        int[] bestNeighbor;
        double bestNeighborFitness;

        for (int i = 0; i < tabuSearch.getMaxIteration(); i++) {
            ArrayList<int[]> neighbors = getNeighbors(currentSolution);
            
            bestNeighbor = getBestNeighbor(neighbors, tabuList);
            bestNeighborFitness = Solution.getFitness(tabuSearch.getData(), bestNeighbor);

            // No non-tabu neighbors found, terminate search
            if (bestNeighbor == null) {
                break;
            }

            currentSolution = bestNeighbor.clone();
            tabuList.add(bestNeighbor);
            result.add(bestNeighbor);

            if (tabuList.size() > tabuSearch.getMaxTabuSize()) {
                tabuList.remove(0);
            }

            if (bestNeighborFitness > bestSolutionFitness) {
                bestSolution = bestNeighbor.clone();
                bestSolutionFitness = bestNeighborFitness;
            }

            System.out.println("Iter: " + i + " - Cur Fitness: " + bestNeighborFitness + " - Best Fitness: " + bestSolutionFitness);

        }
        Solution.display(bestSolution);
        return result;
    }

    public ArrayList getNeighbors(int[] solution) {
        ArrayList<int[]> neighbors = new ArrayList<>();
        Random rnd = new Random();
        int course1, course2;

        for (int i = 0; i < tabuSearch.getNeighborsSize(); i++) {
            do {
                course1 = rnd.nextInt(tabuSearch.getData().Nc);
                course2 = rnd.nextInt(tabuSearch.getData().Nc);
            } while (course1 == course2);
            int[] tmp_solution = solution.clone();
            int tmp = tmp_solution[course1];
            tmp_solution[course1] = tmp_solution[course2];
            tmp_solution[course2] = tmp;
            tmp_solution[course1] = rnd.nextInt(tabuSearch.getData().Ng);
            neighbors.add(tmp_solution);
        }

        return neighbors;
    }

    private int[] getBestNeighbor(ArrayList<int[]> neighbors, ArrayList<int[]> tabuList) {
        int[] bestNeighbor = null;
        double bestNeighborFitness = Double.MIN_VALUE;

        for (int[] neighbor : neighbors) {
            if (!contain(tabuList, neighbor)) {
                double neighborFitness = Solution.getFitness(tabuSearch.getData(), neighbor);
                if (neighborFitness > bestNeighborFitness) {
                    bestNeighbor = neighbor.clone();
                    bestNeighborFitness = neighborFitness;
                }
            }
        }
        return bestNeighbor;
    }

    public boolean contain(ArrayList<int[]> list, int[] target) {
        for (int[] array : list) {
            if (Arrays.equals(array, target)) {
                return true;
            }
        }
        return false;
    }
}
