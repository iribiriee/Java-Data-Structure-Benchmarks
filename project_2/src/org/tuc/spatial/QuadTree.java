package org.tuc.spatial;

import org.tuc.spatial.util.AccessCounter;

import java.util.ArrayList;

public class QuadTree implements SpatialStructure {

    private final int K; // World size
    private final int D; // Nearness distance
    private QuadTreeNode root;

    public QuadTree(int K, int D) {
        this.K = K;
        this.D = D;
        this.root = new QuadTreeNode(0, 0, K - 1, K - 1); // Whole world as root
    }

    // =============================
    // Inner class: QuadTreeNode
    // =============================
    private class QuadTreeNode {
        int x1, y1, x2, y2; // Bounds of the region this node represents
        Monster point = null; // The point this leaf stores (if any)

        QuadTreeNode nw = null, ne = null, sw = null, se = null; // Children

        public QuadTreeNode(int x1, int y1, int x2, int y2) {
            this.x1 = x1;
            this.y1 = y1;
            this.x2 = x2;
            this.y2 = y2;
        }

        public boolean isLeaf() {
            return nw == null && ne == null && sw == null && se == null;
        }

        // Subdivide the region into 4 children
        public void subdivide() {
            int midX = (x1 + x2) / 2;
            int midY = (y1 + y2) / 2;

            nw = new QuadTreeNode(x1, y1, midX, midY);
            ne = new QuadTreeNode(midX + 1, y1, x2, midY);
            sw = new QuadTreeNode(x1, midY + 1, midX, y2);
            se = new QuadTreeNode(midX + 1, midY + 1, x2, y2);
        }

        // Find the correct child for a given point
        public QuadTreeNode getChildForPoint(int x, int y) {
            if (x <= (x1 + x2) / 2) {
                if (y <= (y1 + y2) / 2)
                    return nw;
                else
                    return sw;
            } else {
                if (y <= (y1 + y2) / 2)
                    return ne;
                else
                    return se;
            }
        }
    }

    @Override
    public boolean insert(TucPoint tucPoint) {
        return insertRecursive(root, (Monster) tucPoint);
    }

    private boolean insertRecursive(QuadTreeNode node, Monster monster) {
        AccessCounter.increment();

        if (node.isLeaf()) {
            if (node.point == null) {
                node.point = monster;
                return true;
            } else {
                // Leaf is full, subdivide and re-insert the existing point
                Monster existing = node.point;
                node.point = null;
                node.subdivide();
                insertRecursive(node.getChildForPoint(existing.getX(), existing.getY()), existing);
                return insertRecursive(node.getChildForPoint(monster.getX(), monster.getY()), monster);
            }
        } else {
            return insertRecursive(node.getChildForPoint(monster.getX(), monster.getY()), monster);
        }
    }

    @Override
    public TucPoint search(int x, int y) {
        return searchRecursive(root, x, y);
    }

    private TucPoint searchRecursive(QuadTreeNode node, int x, int y) {
        AccessCounter.increment();

        if (node == null) return null;
        if (node.isLeaf()) {
            if (node.point != null && node.point.getX() == x && node.point.getY() == y) {
                return node.point;
            }
            return null;
        }
        return searchRecursive(node.getChildForPoint(x, y), x, y);
    }

    @Override
    public ArrayList<TucPoint> rangeSearch(int x1, int y1, int x2, int y2) {
        ArrayList<TucPoint> result = new ArrayList<>();
        rangeSearchRecursive(root, x1, y1, x2, y2, result);
        return result;
    }

    private void rangeSearchRecursive(QuadTreeNode node, int x1, int y1, int x2, int y2, ArrayList<TucPoint> result) {
        AccessCounter.increment();

        if (node == null) return;

        // If this region doesn't intersect with the search area, stop
        if (node.x2 < x1 || node.x1 > x2 || node.y2 < y1 || node.y1 > y2)
            return;

        if (node.isLeaf()) {
            if (node.point != null) {
                int px = node.point.getX(), py = node.point.getY();
                if (px >= x1 && px <= x2 && py >= y1 && py <= y2) {
                    result.add(node.point);
                }
            }
        } else {
            rangeSearchRecursive(node.nw, x1, y1, x2, y2, result);
            rangeSearchRecursive(node.ne, x1, y1, x2, y2, result);
            rangeSearchRecursive(node.sw, x1, y1, x2, y2, result);
            rangeSearchRecursive(node.se, x1, y1, x2, y2, result);
        }
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
