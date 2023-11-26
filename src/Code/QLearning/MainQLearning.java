package Code.QLearning;

import Code.utils.Data;
import Code.utils.Solution;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author tungd
 */
public class MainQLearning {

    public static void main(String[] args) throws IOException {
        Data data = Data.readDataFromFrile();

        int episodes = 30000;
        double alpha = 0.001;
        int gamma = 1;
        double epsilon = 1;
        double max_epsilon = 1;
        double min_epsilon = 0.01;
        double epsilon_decay_rate = 0.0002;

        Q_learning q_learning = new Q_learning(data, episodes, alpha, gamma, epsilon, max_epsilon, min_epsilon, epsilon_decay_rate);

        ArrayList<int[]> q_result = q_learning.implement();
        Solution.display(q_learning.bestD);
    }
}
