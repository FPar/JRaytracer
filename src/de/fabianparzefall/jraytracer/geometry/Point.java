package de.fabianparzefall.jraytracer.geometry;

/**
 * Class representing a point in 3 dimensional room.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public final class Point extends Coordinates3D {
    /**
     * The origin of the coordinate system.
     */
    public static final Point ORIGIN = new Point(0, 0, 0);

    /**
     * Constructs the point from coordinates.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public Point(final double x, final double y, final double z) {
        super(x, y, z);
    }

    /**
     * Adds a vector to the point.
     *
     * @param vector The vector to add.
     * @return A new point.
     */
    public Point add(final Vector vector) {
        assert vector != null;

        return new Point(getX() + vector.getX(), getY() + vector.getY(), getZ() + vector.getZ());
    }

    /**
     * Calculates a vector from this point to another.
     *
     * @param other The other point.
     * @return The vector between the points.
     */
    public Vector vectorTo(final Point other) {
        assert other != null;

        return new Vector(other.getX() - getX(), other.getY() - getY(), other.getZ() - getZ());
    }
}
