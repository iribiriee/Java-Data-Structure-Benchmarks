package org.tuc;
/**
 * An interface describing an Object Pool (collection of unused objects)
 * It is up to the user to make sure the handled objects are instances
 * of desired classes.
 */
public interface ObjectPool {
    /**
     * @return true if the object pool holds at least one unused object
     */
    public boolean hasFreeObject();


    /**
     * Adds the given object to the pool
     * @param object
     */
    public void addObject(Object object);

    /**
     * Returns an object of the pool and removes it from the pool
     * @return object of the pool
     */
    public Object getObject();


    /**
     * Returns the number of objects in the pool
     * @return number of objects in the pool
     */
    public int length();
}
