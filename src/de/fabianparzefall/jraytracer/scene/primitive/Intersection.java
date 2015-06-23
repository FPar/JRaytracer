package de.fabianparzefall.jraytracer.scene.primitive;

import de.fabianparzefall.jraytracer.common.Doubles;
import de.fabianparzefall.jraytracer.geometry.Point;

/**
 * This represents an intersection between a primitive and a ray.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public final class Intersection implements Comparable<Intersection> {
    /**
     * The intersection point.
     */
    private final Point intersectionPoint;

    /**
     * The primitive the ray intersects with.
     */
    private final Primitive intersectedPrimitive;

    /**
     * Distance from ray point to primitive.
     */
    private final double distance;

    /**
     * True if the ray enters the primitive, false if the ray leaves.
     */
    private final boolean entering;

    /**
     * Constructs the intersection.
     *
     * @param intersectionPoint    The position of the the intersection.
     * @param intersectedPrimitive The object the ray intersects with.
     * @param distance             The distance between ray start in intersection. Must be positive.
     * @param entering             If the ray is entering or exiting the primitive.
     */
    public Intersection(final Point intersectionPoint, final Primitive intersectedPrimitive, final double distance, final boolean entering) {
        assert intersectionPoint != null;
        assert intersectedPrimitive != null;
        if (distance <= 0)
            throw new IllegalArgumentException("distance is negative.");

        this.intersectionPoint = intersectionPoint;
        this.intersectedPrimitive = intersectedPrimitive;
        this.distance = distance;
        this.entering = entering;
    }

    public Point getIntersectionPoint() {
        return intersectionPoint;
    }

    public Primitive getIntersectedPrimitive() {
        return intersectedPrimitive;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isEntering() {
        return entering;
    }

    @Override
    public int compareTo(final Intersection other) {
        assert other != null;

        return Double.compare(distance, other.distance);
    }

    @Override
    public boolean equals(final Object obj) {
        assert obj != null;
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final Intersection that = (Intersection) obj;

        return Doubles.equals(that.distance, distance)
                && entering == that.entering
                && intersectionPoint.equals(that.intersectionPoint)
                && intersectedPrimitive.equals(that.intersectedPrimitive);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String toString() {
        return String.format("Intersection{intersectionPoint=%s, intersectedPrimitive=%s, distance=%s, entering=%s}", intersectionPoint, intersectedPrimitive, distance, entering);
    }
}
