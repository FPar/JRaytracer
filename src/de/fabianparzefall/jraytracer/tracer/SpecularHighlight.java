package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.common.BooleanPromise;
import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;
import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;
import de.fabianparzefall.jraytracer.scene.primitive.Primitive;
import de.fabianparzefall.jraytracer.scene.primitive.Surface;

import java.util.Optional;

/**
 * Calculates specular highlights.
 *
 * @author Fabian Parzefall
 * @version 15-03-15
 */
public class SpecularHighlight implements LightModel {
    /**
     * Is this in the shadows?
     */
    private final BooleanPromise isShadowed;

    /**
     * The primary ray used for calculations.
     */
    private final Ray primaryRay;

    /**
     * Constructs the SpecularHighlight model.
     *
     * @param isShadowed A promise, if there is shadow.
     * @param primaryRay The primary ray used for calculations.
     */
    public SpecularHighlight(final BooleanPromise isShadowed, final Ray primaryRay) {
        this.isShadowed = isShadowed;
        this.primaryRay = primaryRay;
    }

    @Override
    public double calculate(final Scene scene, final Optional<Intersection> optionalIntersection) {
        assert scene != null;
        assert optionalIntersection != null;

        // No intersection, no light or shadow -> no specular highlight.
        if (!optionalIntersection.isPresent() || !scene.getLight().isPresent() || isShadowed.get()) {
            return 0;
        }

        final Intersection primaryIntersection = optionalIntersection.get();
        final Primitive intersectedPrimitive = primaryIntersection.getIntersectedPrimitive();
        final double specularRatio = intersectedPrimitive.getSurface().get(Surface.Property.SpecularRatio);

        // No specular ratio - no reason no continue.
        if (specularRatio == 0) {
            return 0;
        }

        final Point intersectionPoint = primaryIntersection.getIntersectionPoint();
        // A vector, which is mirrored with a vector normal to the primitive at the intersection point.
        final Vector mirroredVector = primaryRay.getDirection().mirror(intersectedPrimitive.getNormal(intersectionPoint).normalize());
        // The cosine between the mirrored vector and vector between intersection point and light source.
        final double specularValue = mirroredVector.dotProduct(intersectionPoint.vectorTo(scene.getLight().get()).normalize());

        if (specularValue < 0) {
            return 0;
        }

        // Calculate the brightness according to the formula.
        return intersectedPrimitive.getSurface().get(Surface.Property.SpecularRatio)
                * Math.pow(specularValue, intersectedPrimitive.getSurface().get(Surface.Property.SpecularExponent));
    }
}
