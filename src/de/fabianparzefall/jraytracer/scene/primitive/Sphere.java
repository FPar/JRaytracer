package de.fabianparzefall.jraytracer.scene.primitive;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing a sphere in 3 dimensional space.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public class Sphere implements Primitive {
    /**
     * The center of the sphere.
     */
    private final Point center;

    /**
     * The radius of the sphere.
     */
    private final double radius;

    /**
     * The surface for this primitive.
     */
    private final Surface surface = new Surface();

    /**
     * Constructs a sphere from a center point and the radius.
     *
     * @param center Center of the sphere.
     * @param radius Radius of the sphere.
     */
    public Sphere(final Point center, final double radius) {
        assert center != null;
        if (radius <= 0)
            throw new IllegalArgumentException("radius is less or equal 0.");

        this.center = center;
        this.radius = radius;
    }

    /**
     * Calculates all intersections (in view direction) of a ray (point and vector) with this sphere. If the distance of
     * a intersection is zero, it will be treated as no intersection. The intersections in this list are already ordered
     * from lowest to highest.
     *
     * @param ray The ray.
     * @return A list of intersection objects ordered by their distance ascending.
     * @see <a
     * href="http://www.siggraph.org/education/materials/HyperGraph/raytrace/rtinter1.htm">SIGGRAPH</a>
     */
    @Override
    public List<Intersection> intersections(final Ray ray) {
        assert ray != null;

        // The is a vector from the the sphere center to the start point on the ray.
        final Vector deltaR = center.vectorTo(ray.getStartPoint());

        // Coefficients for quadratic for equation.
        // We don't need to calculate the a coefficient, because the direction vector is always normalized and thus a is
        // 1 (see SIGGRAPH paper).
        final double b = deltaR.dotProduct(ray.getDirection()) * 2;
        final double c = deltaR.dotProduct(deltaR) - radius * radius;

        // The discriminant of the equation.
        double discriminant = b * b - 4 * c;

        // No intersection.
        if (discriminant < 0) {
            return new ArrayList<>(0);
        }

        // Now calculate the square root of the discriminant to get the distances. This is always a positive value.
        discriminant = Math.sqrt(discriminant);

        // Calculate the first distance value (the bigger one).
        double distance = (-b + discriminant) * 0.5;

        // If it is less or equal zero, we have no intersection (in view direction), because in the second case we
        // subtract the discriminant from b and this will also be negative.
        if (distance <= 0) {
            return new ArrayList<>(0);
        }

        // Create an array list with an initial length of 2. In most cases there will be 2 elements (that is also the
        // maximum number of elements) in the list, so checking for the rare case of 1 element won't improve the memory
        // consumption significantly.
        final List<Intersection> intersections = new ArrayList<>(2);

        // And add the first intersection. It's either more far away than the other or the ray starts within the sphere
        // so it's always not entering the sphere.
        intersections.add(new Intersection(ray.getPoint(distance), this, distance, false));

        // The rare case of an tangent.
        if (discriminant == 0) {
            return intersections;
        }

        // Now check for the second possible value.
        distance = (-b - discriminant) * 0.5;
        if (distance > 0) {
            // Insert the intersection before the other value to get it directly sorted.
            intersections.add(0, new Intersection(ray.getPoint(distance), this, distance, true));
        }

        return intersections;
    }

    @Override
    public Vector getNormal(final Point point) {
        assert point != null;

        return center.vectorTo(point);
    }

    public Surface getSurface() {
        return surface;
    }
}