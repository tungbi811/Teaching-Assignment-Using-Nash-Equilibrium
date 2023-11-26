package Code.TabuSearch;

import Code.utils.Data;

/**
 *
 * @author tungd
 */
public class TabuSearch {

    private Data data;
    private int maxIteration;
    private int maxTabuSize;
    private int neighborsSize;

    public TabuSearch(Data data, int maxIteration, int maxTabuSize, int neighborsSize) {
        this.data = data;
        this.maxIteration = maxIteration;
        this.maxTabuSize = maxTabuSize;
        this.neighborsSize = neighborsSize;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public int getMaxIteration() {
        return maxIteration;
    }

    public void setMaxIteration(int maxIteration) {
        this.maxIteration = maxIteration;
    }

    public int getMaxTabuSize() {
        return maxTabuSize;
    }

    public void setMaxTabuSize(int maxTabuSize) {
        this.maxTabuSize = maxTabuSize;
    }

    public int getNeighborsSize() {
        return neighborsSize;
    }

    public void setNeighborsSize(int neighborsSize) {
        this.neighborsSize = neighborsSize;
    }
}
