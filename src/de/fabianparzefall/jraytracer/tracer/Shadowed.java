package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;
import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;

import java.util.Optional;

/**
 * Calculates shadows.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public class Shadowed implements LightModel {
    @Override
    public double calculate(final Scene scene, final Optional<Intersection> optionalIntersection) {
        assert scene != null;
        assert optionalIntersection != null;

        // No intersection -> no shadow
        // Shadows emerge from the absence of light, but if there is no light, there cannot be any shadows.
        if (!optionalIntersection.isPresent() || !scene.getLight().isPresent()) {
            return 0;
        }

        final Intersection primaryIntersection = optionalIntersection.get();
        final Point intersectionPoint = primaryIntersection.getIntersectionPoint();

        // A ray to the light.
        final Vector lightVector = intersectionPoint.vectorTo(scene.getLight().get());
        final Ray lightRay = new Ray(intersectionPoint, lightVector);

        // Is there a primitive between the intersection and the intersection?
        final Optional<Intersection> secondaryIntersection = scene.findIntersection(lightRay);
        // If the distance is greater than the length of the light vector, it means, that intersection is behind the
        // lightsource, therefor irrelevant and no shadow.
        return secondaryIntersection.map(intersection -> intersection.getDistance() < lightVector.getLength() ? 1 : 0).orElse(0);
    }
}
