package org.tuc.spatial;

/**

 * A class representing a Monster. It has the coordinates of it's location

 * (the class extends TucPoint) and additionally has a scary and frightening name!

 */

public class Monster extends TucPoint {

    private String name;

    public Monster(int x, int y, String name) {

        super(x, y);

        this.name = name;

    }

    public String getName() {

        return name;

    }

}