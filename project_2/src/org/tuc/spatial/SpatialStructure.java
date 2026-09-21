package org.tuc.spatial;

import java.util.ArrayList;

public interface SpatialStructure {

    /**

     * Inserts a point into the data structure. At most one point can exist

     * at a given coordinate

     * @param tucPoint

     * @return true if the insertion was successful

     */

    public boolean insert(TucPoint tucPoint);



    /**

     * Searches the structure for a point at the exact given coordinate.

     * @param x

     * @param y

     * @return The point found, or null if no point exists at given coordinates

     */

    public TucPoint search(int x, int y);



    /**

     * Returns an array list of points within the rectangle given by the

     * (x1,y1) and (x2,y2) coordinates

     * A point exists if it's (x,y) coordinate is

     *        x>=x1 AND x<=x2 AND y>=y1 AND y<=y2

     * @param x1

     * @param y1

     * @param x2

     * @param y2

     * @return ArrayList of found points. If no points are found,

     *         the list is empty (NOT null!!)

     */

    public ArrayList<TucPoint> rangeSearch(int x1, int y1, int x2, int y2);


    /**

     * Returns an array list of points "near" the given coordinates.

     * It is up to the implementation to decide what "near" means.

     * @param x

     * @param y

     * @return

     */

    public ArrayList<TucPoint> findNearPoints(int x, int y);

}