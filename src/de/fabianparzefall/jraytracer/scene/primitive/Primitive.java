package de.fabianparzefall.jraytracer.scene.primitive;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;

import java.util.List;

/**
 * A primitive is generic object in the scene.
 *
 * @author Fabian Parzefall
 * @version 15-05-06
 */
public interface Primitive {
    /**
     * Gets the normal on the surface at the given point.
     *
     * @param point A point on the surface.
     * @return A vector normal to the surface.
     */
    Vector getNormal(Point point);

    /**
     * Gets the surface of the primitive.
     *
     * @return The surface of the primitive.
     */
    Surface getSurface();

    /**
     * A function calculating all intersections with the given ray and this primitive and returning them in a list of
     * intersection objects.
     *
     * @param ray The ray.
     * @return A list of intersections.
     */
    List<Intersection> intersections(Ray ray);
}
