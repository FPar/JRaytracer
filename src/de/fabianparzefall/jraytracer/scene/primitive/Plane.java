package de.fabianparzefall.jraytracer.scene.primitive;

import de.fabianparzefall.jraytracer.common.Doubles;
import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a plane in the 3 dimensional space by a point which is in the plane and a vector which is
 * normal to the plane.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public class Plane implements Primitive {
    /**
     * An vector to an arbitrary point in the plane.
     */
    private final Vector startVector;

    /**
     * A normal vector to the plane.
     */
    private final Vector normalVector;

    /**
     * The shortest distance between the plane and the origin.
     */
    private final double originDistance;

    /**
     * The surface for this primitive.
     */
    private final Surface surface = new Surface();

    /**
     * Constructs the plane.
     *
     * @param point        An arbitrary point in the plane.
     * @param normalVector A normalized vector normal to the plane. It must not equals the null vector.
     */
    public Plane(final Point point, final Vector normalVector) {
        assert point != null;
        assert normalVector != null;
        if (normalVector.equals(Vector.NULL_VECTOR))
            throw new IllegalArgumentException("normalVector is the null vector.");

        this.startVector = Point.ORIGIN.vectorTo(point);
        this.normalVector = normalVector.normalize();

        originDistance = calculateOriginDistance();
    }

    /**
     * Calculates intersections with a ray. The list contains only elements, if there
     * are intersections in view direction.
     *
     * @param ray The ray.
     * @return A list with 0 or 1 elements.
     * @see <a href="http://www.siggraph.org/education/materials/HyperGraph/raytrace/rayplane_intersection.htm">SIGGRAPH</a>
     */
    @Override
    public List<Intersection> intersections(final Ray ray) {
        assert ray != null;

        final double rayPlaneDotProduct = normalVector.dotProduct(ray.getDirection());
        // This value is 0 if the ray is parallel to the plane. This is compared to an epsilon, because there may be
        // rounding errors caused by the normalization of the vectors.
        final ArrayList<Intersection> intersections = new ArrayList<>(1);

        if (!Doubles.equals(rayPlaneDotProduct, 0)) {

            // This is the formula from the SIGGRAPH paper.
            final Vector originRayVector = Point.ORIGIN.vectorTo(ray.getStartPoint());
            final double rayOriginPlaneDotProduct = normalVector.dotProduct(originRayVector);

            final double distance = -(rayOriginPlaneDotProduct + originDistance) / rayPlaneDotProduct;

            // If the distance is less than zero, the ray intersects the plane behind origin. For the raytracer that
            // means, there is no intersection.
            if (distance > 0) {
                intersections.add(new Intersection(ray.getPoint(distance), this, distance, true));
            }
        }
        return intersections;
    }

    @Override
    public Vector getNormal(final Point point) {
        // Even it doesn't make any difference for the method, this point shouldn't be null, because it makes no sense.
        assert point != null;

        return normalVector;
    }

    public Surface getSurface() {
        return surface;
    }

    /**
     * Calculates the shortest distance between the plane and the origin.
     *
     * @return The distance.
     */
    private double calculateOriginDistance() {
        return -startVector.dotProduct(normalVector);
    }
}
