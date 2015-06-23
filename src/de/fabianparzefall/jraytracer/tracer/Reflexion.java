package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;
import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;
import de.fabianparzefall.jraytracer.scene.primitive.Primitive;
import de.fabianparzefall.jraytracer.scene.primitive.Surface;

import java.util.Optional;

/**
 * Calculates reflexions.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public class Reflexion implements LightModel {
    /**
     * This value is used, to stop reflection, if it ping pongs between two primitives.
     */
    private static final double REFLEXION_WEIGHT_STOP = 0x1.0p-8;

    /**
     * The primary ray.
     */
    private final Ray primaryRay;

    /**
     * The raytracer itself.
     */
    private final Raytracer raytracer;

    /**
     * Constructs the reflexion model from a primary ray and the raytracer itself.
     *
     * @param primaryRay The primary ray.
     * @param raytracer  The raytracer itself.
     */
    public Reflexion(final Ray primaryRay, final Raytracer raytracer) {
        assert primaryRay != null;
        assert raytracer != null;

        this.primaryRay = primaryRay;
        this.raytracer = raytracer;
    }

    @Override
    public double calculate(final Scene scene, final Optional<Intersection> optionalIntersection) {
        assert scene != null;
        assert optionalIntersection != null;

        if (!optionalIntersection.isPresent()) {
            return 0;
        }

        // Remark: this isn't necessarily the primary intersection, because the ray could also be a reflected one. But
        // for consistency (because other light models treat it as well as primary intersection, although it isn't)
        // it is named primaryIntersection.
        final Intersection primaryIntersection = optionalIntersection.get();
        final Primitive intersectedPrimitive = primaryIntersection.getIntersectedPrimitive();
        final Point intersectionPoint = primaryIntersection.getIntersectionPoint();

        // Mirror the vector at the intersection point.
        final Vector mirroredVector = primaryRay.getDirection().mirror(intersectedPrimitive.getNormal(intersectionPoint));

        // Create the new mirrored ray and reduce it's weight by multiplying it with the reflexion ratio.
        final Ray newMirroredRay = new Ray(intersectionPoint, mirroredVector, primaryRay.getWeight() * intersectedPrimitive.getSurface().get(Surface.Property.ReflexionRatio));

        // If the new ray is below a specific weight, it will be treated as 0.
        if (newMirroredRay.getWeight() < REFLEXION_WEIGHT_STOP) {
            return 0;
        }

        // Trace the new ray.
        return raytracer.traceRay(newMirroredRay);
    }
}
