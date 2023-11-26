package Code.Tabu;

import Code.utils.Data;
import Code.utils.Solution;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author tungd
 */
public class MainTabu {

    public static void main(String[] args) throws IOException {
        Data data = Data.readDataFromFrile();

        int maxIteration = 3000;
        int maxTabuSize = 100;
        int neighborsSize = 5000;
        TabuSearch tabuSearch = new TabuSearch(data, maxIteration, maxTabuSize, neighborsSize);
        TabuSearchImplement tabuSearchImplement = new TabuSearchImplement(tabuSearch);
        
        for (int i = 0; i < 1; i++) {
            ArrayList tabuSearchResult = tabuSearchImplement.implement();
            Solution.writeSolutions(tabuSearchResult, data, "Tabu", "PayoffResult" + i);
        }
    }

}
