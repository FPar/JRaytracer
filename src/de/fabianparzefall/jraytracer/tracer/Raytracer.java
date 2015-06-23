package de.fabianparzefall.jraytracer.tracer;

import de.fabianparzefall.jraytracer.common.BooleanPromise;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.scene.Looker;
import de.fabianparzefall.jraytracer.scene.Scene;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;

import java.util.Optional;
import java.util.stream.Stream;

/**
 * The raytracer traces rays through a given scene.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public class Raytracer {
    /**
     * The current scene.
     */
    private final Scene scene;

    /**
     * The looker.
     */
    private final Looker looker;

    /**
     * Initializes the raytracer with a scene.
     *
     * @param scene The scene.
     */
    public Raytracer(final Scene scene) {
        assert scene != null;

        this.scene = scene;
        looker = scene.getLooker();
    }

    /**
     * Traces a ray with the given viewport coordinates.
     *
     * @param horizontal The horizontal viewport coordinate.
     * @param vertical   The vertical viewport coordinate.
     * @return The brightness at the given coordinates.
     */
    public double tracePrimary(final double horizontal, final double vertical) {
        if (Math.abs(horizontal) > 1)
            throw new IllegalArgumentException("horizontal must be within -1 and 1");
        if (Math.abs(vertical) > 1)
            throw new IllegalArgumentException("vertical must be within -1 and 1");

        final Ray primaryRay = looker.getPrimaryRay(horizontal, vertical);
        return traceRay(primaryRay);
    }

    /**
     * Traces a ray through the scene.
     *
     * @param ray The ray.
     * @return A brightness value.
     */
    public double traceRay(final Ray ray) {
        assert ray != null;

        final Optional<Intersection> optionalIntersection = scene.findIntersection(ray);
        final BooleanPromise isShadowed =
                new BooleanPromise(() -> new Shadowed().calculate(scene, optionalIntersection) > 0);
        final double brightness = Stream.of(new Ambient(),
                new Diffuse(isShadowed),
                new SpecularHighlight(isShadowed, ray),
                new Reflexion(ray, this))
                .map(lightModel -> lightModel.calculate(scene, optionalIntersection))
                .mapToDouble(Double::doubleValue)
                .sum();

        // Light models mustn't return a negative value for the brightness.
        assert brightness >= 0;
        // If the brightness is bigger than 1, than normalize the result to one.
        return brightness > 1 ? 1 : brightness;
    }
}
