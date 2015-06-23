package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.common.BooleanPromise;
import de.fabianparzefall.jraytracer.geometry.Vector;
import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;
import de.fabianparzefall.jraytracer.scene.primitive.Primitive;
import de.fabianparzefall.jraytracer.scene.primitive.Surface;

import java.util.Optional;

/**
 * Calculates diffuse light.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public class Diffuse implements LightModel {
    /**
     * Is this in the shadows?
     */
    private final BooleanPromise isShadowed;

    /**
     * Constructs the Diffuse model.
     *
     * @param isShadowed A promise, if there is shadow.
     */
    public Diffuse(final BooleanPromise isShadowed) {
        this.isShadowed = isShadowed;
    }

    @Override
    public double calculate(final Scene scene, final Optional<Intersection> optionalIntersection) {
        assert scene != null;
        assert optionalIntersection != null;

        if (!optionalIntersection.isPresent() || !scene.getLight().isPresent() || isShadowed.get()) {
            return 0;
        }

        final Intersection primaryIntersection = optionalIntersection.get();
        final Primitive intersectedPrimitive = primaryIntersection.getIntersectedPrimitive();
        final double diffuseRatio = intersectedPrimitive.getSurface().get(Surface.Property.DiffuseRatio);

        if (diffuseRatio == 0) {
            return 0;
        }

        // A vector normal to the primitive at the point the ray intersected the primitive.
        final Vector primitiveNormalVector = intersectedPrimitive.getNormal(primaryIntersection.getIntersectionPoint()).normalize();
        final Vector primitiveToLightVector = primaryIntersection.getIntersectionPoint().vectorTo(scene.getLight().get()).normalize();

        // Because both vectors a normalized, only the dot product has to be applied to get the cosinus.
        final double brightness = diffuseRatio * primitiveNormalVector.dotProduct(primitiveToLightVector);
        return brightness < 0 ? 0 : brightness;
    }
}
