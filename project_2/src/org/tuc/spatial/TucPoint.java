package org.tuc.spatial;

/**

 * A class modeling a spatial point. Basically provides a x,y coordinate and

 * a method to determine if a given other point is "near" it

 */

public class TucPoint {

    private int x;

    private int y;

    public TucPoint(int x, int y) {

        this.x = x;

        this.y = y;

    }

    public int getX() {

        return x;

    }

    public int getY() {

        return y;

    }

    public boolean isNear(TucPoint tucPoint, int distance) {

        return isNear(tucPoint.getX(), tucPoint.getY(), distance);

    }

    public boolean isNear(int x, int y, int distance) {

        if (Math.abs(x - getX()) <= distance && Math.abs(y - getY()) <= distance ) {

            return true;

        }

        return false;

    }

}