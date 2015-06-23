package de.fabianparzefall.jraytracer.geometry;

/**
 * Class representing a ray in 3 dimensional room.
 *
 * @author Fabian Parzefall
 * @version 15-06-03
 */
public final class Ray {
    /**
     * The default weight for rays.
     */
    private static final double DEFAULT_WEIGHT = 1;

    /**
     * The start start point of the ray.
     */
    private final Point startPoint;

    /**
     * The normalized direction of the ray.
     */
    private final Vector direction;

    /**
     * The weight of the ray.
     */
    private final double weight;

    /**
     * Constructs the ray from a start startPoint and a direction vector. The direction mustn't be the null vector.
     *
     * @param startPoint Start startPoint of ray.
     * @param direction  Direction vector of ray.
     */
    public Ray(final Point startPoint, final Vector direction) {
        this(startPoint, direction, DEFAULT_WEIGHT);
    }

    /**
     * Constructs the ray from a start startPoint and a direction vector. The direction mustn't be the null vector.
     *
     * @param startPoint Start startPoint of ray.
     * @param direction  Direction vector of ray.
     * @param weight     The weight of the ray.
     */
    public Ray(final Point startPoint, final Vector direction, final double weight) {
        assert startPoint != null;
        assert direction != null;
        if (direction.equals(Vector.NULL_VECTOR))
            throw new IllegalArgumentException("direction is the 0-vector.");

        this.startPoint = startPoint;
        this.direction = direction.normalize();
        this.weight = weight;
    }

    public Point getStartPoint() {
        return startPoint;
    }

    public Vector getDirection() {
        return direction;
    }

    public double getWeight() {
        return weight;
    }

    /**
     * Gets a point on the ray, that has a distance to the start point.
     *
     * @param distance The distance to the start point.
     * @return A point on the ray.
     */
    public Point getPoint(final double distance) {
        final Vector scaledDirection = direction.scalarProduct(distance);
        return startPoint.add(scaledDirection);
    }

    @Override
    public String toString() {
        return String.format("Ray{startPoint=%s, direction=%s}", startPoint, direction);
    }

    @Override
    public boolean equals(final Object obj) {
        assert obj != null;
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final Ray ray = (Ray) obj;

        return startPoint.equals(ray.startPoint)
                && direction.equals(ray.direction);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
