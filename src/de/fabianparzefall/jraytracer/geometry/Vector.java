package de.fabianparzefall.jraytracer.geometry;

/**
 * Class representing a vector in 3 dimensional room.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public final class Vector extends Coordinates3D {
    /**
     * The null vector.
     */
    public static final Vector NULL_VECTOR = new Vector(0, 0, 0);

    /**
     * Vector with length 1 in x direction.
     */
    public static final Vector X_VECTOR = new Vector(1, 0, 0);

    /**
     * Vector with length 1 in y direction.
     */
    public static final Vector Y_VECTOR = new Vector(0, 1, 0);

    /**
     * Vector with length 1 in z direction.
     */
    public static final Vector Z_VECTOR = new Vector(0, 0, 1);

    /**
     * The length of the vector.
     */
    private final double length;

    /**
     * Constructs a vector from coordinates.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    public Vector(final double x, final double y, final double z) {
        super(x, y, z);
        length = calculateLength();
    }

    public double getLength() {
        return length;
    }

    /**
     * Adds a vector to this vector.
     *
     * @param other The vector to add.
     * @return The resulting vector.
     */
    public Vector add(final Vector other) {
        assert other != null;

        return new Vector(getX() + other.getX(), getY() + other.getY(), getZ() + other.getZ());
    }

    /**
     * Subtracts a vector from this vector.
     *
     * @param other The vector to subtract.
     * @return The resulting vector.
     */
    public Vector subtract(final Vector other) {
        assert other != null;

        return new Vector(getX() - other.getX(), getY() - other.getY(), getZ() - other.getZ());
    }

    /**
     * Multiplies the vector with a scalar.
     *
     * @param scalar The scalar.
     * @return A new vector.
     */
    public Vector scalarProduct(final double scalar) {
        return new Vector(getX() * scalar, getY() * scalar, getZ() * scalar);
    }

    /**
     * Scales the vector to the given length.
     * Will throw an exception, if the vector length equals 0.
     *
     * @param newLength The new length.
     * @return A vector with the given length.
     */
    public Vector scale(final double newLength) {
        if (length == 0)
            throw new IllegalStateException("Vector has length of 0.");

        final double scaleFactor = newLength / length;
        return scalarProduct(scaleFactor);
    }

    /**
     * Normalizes the vector.
     *
     * @return A vector with the same direction as this but with length 1.
     */
    public Vector normalize() {
        return scale(1);
    }

    /**
     * Calculates the dot product with another vector.
     *
     * @param other The other vector.
     * @return A double with the scalar product.
     */
    public double dotProduct(final Vector other) {
        assert other != null;

        return getX() * other.getX() + getY() * other.getY() + getZ() * other.getZ();
    }

    /**
     * Calculates the cross product with another vector.
     *
     * @param other The other vector.
     * @return A new vector perpendicular to the others.
     */
    public Vector crossProduct(final Vector other) {
        assert other != null;

        final double resultX = getY() * other.getZ() - getZ() * other.getY();
        final double resultY = getZ() * other.getX() - getX() * other.getZ();
        final double resultZ = getX() * other.getY() - getY() * other.getX();

        return new Vector(resultX, resultY, resultZ);
    }

    /**
     * Mirrors a vector with a normal.
     *
     * @param normal The normal vector on the surface.
     * @return The reflected vector.
     */
    public Vector mirror(final Vector normal) {
        assert normal != null;

        final Vector normalN = normal.normalize();

        return subtract(normalN.scalarProduct(2 * dotProduct(normalN)));
    }

    /**
     * Calculates the length of the vector.
     *
     * @return The length.
     */
    private double calculateLength() {
        return Math.sqrt(getX() * getX() + getY() * getY() + getZ() * getZ());
    }
}
