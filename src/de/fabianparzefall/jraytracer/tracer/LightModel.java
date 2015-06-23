package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;

import java.util.Optional;

/**
 * The interface for different light models.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
interface LightModel {
    /**
     * Calculates the brightness for the given (optional) intersection.
     *
     * @param scene        The scene to work with.
     * @param intersection The optional intersection for which the value should be calculated.
     * @return The (positive) brightness value.
     */
    double calculate(Scene scene, Optional<Intersection> intersection);
}
