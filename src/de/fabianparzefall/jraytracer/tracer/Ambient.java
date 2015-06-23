package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;
import de.fabianparzefall.jraytracer.scene.primitive.Surface;

import java.util.Optional;

/**
 * Calculates ambient light.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
public class Ambient implements LightModel {
    @Override
    public double calculate(final Scene scene, final Optional<Intersection> intersection) {
        assert scene != null;
        assert intersection != null;

        return intersection
                .map(intersectionValue -> intersectionValue.getIntersectedPrimitive().getSurface().get(Surface.Property.AmbientRatio))
                .orElse(0D);
    }
}
