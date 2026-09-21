package org.tuc.spatial;

import org.tuc.spatial.util.AccessCounter;

import java.util.ArrayList;
import java.util.List;

public class SpatialHash implements SpatialStructure {

    private final int K; // World size
    private final int D; // Nearness distance
    private final int B; // Subregion box size (B x B)
    private final int numCells; // Total number of cells in hash table
    private final List<Monster>[] hashTable;

    public SpatialHash(int K, int D, int B) {
        this.K = K;
        this.D = D;
        this.B = B;
        this.numCells = (K / B) * (K / B);
        this.hashTable = new ArrayList[numCells];
        for (int i = 0; i < numCells; i++) {
            hashTable[i] = new ArrayList<>();
        }
    }

    private int getCellIndex(int x, int y) {
        int col = x / B;
        int row = y / B;
        return row * (K / B) + col;
    }

    @Override
    public boolean insert(TucPoint tucPoint) {
        Monster monster = (Monster) tucPoint;
        int index = getCellIndex(monster.getX(), monster.getY());
        hashTable[index].add(monster);
        return true;
    }

    @Override
    public TucPoint search(int x, int y) {
        int index = getCellIndex(x, y);
        AccessCounter.increment(); // count cell access
        for (TucPoint point : hashTable[index]) {
            AccessCounter.increment(); // count each list element access
            if (point.getX() == x && point.getY() == y) {
                return point;
            }
        }
        return null;
    }

    @Override
    public ArrayList<TucPoint> rangeSearch(int x1, int y1, int x2, int y2) {
        ArrayList<TucPoint> result = new ArrayList<>();

        int minCol = Math.max(0, x1 / B);
        int maxCol = Math.min((K / B) - 1, x2 / B);
        int minRow = Math.max(0, y1 / B);
        int maxRow = Math.min((K / B) - 1, y2 / B);

        for (int row = minRow; row <= maxRow; row++) {
            for (int col = minCol; col <= maxCol; col++) {
                int index = row * (K / B) + col;
                AccessCounter.increment(); // cell access

                for (TucPoint point : hashTable[index]) {
                    AccessCounter.increment(); // list item access
                    int x = point.getX();
                    int y = point.getY();
                    if (x >= x1 && x <= x2 && y >= y1 && y <= y2) {
                        result.add(point);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public ArrayList<TucPoint> findNearPoints(int x, int y) {
        int x1 = Math.max(0, x - D);
        int y1 = Math.max(0, y - D);
        int x2 = Math.min(K - 1, x + D);
        int y2 = Math.min(K - 1, y + D);
        return rangeSearch(x1, y1, x2, y2);
    }
}
