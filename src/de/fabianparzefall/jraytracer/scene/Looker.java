package de.fabianparzefall.jraytracer.scene;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;

/**
 * Calculates the rays.
 *
 * @author Fabian Parzefall
 * @version 15-03-06
 */
public class Looker {
    /**
     * Position of the camera.
     */
    private final Point cameraPosition;

    /**
     * The vector between camera and viewportCenter.
     */
    private final Vector viewVector;

    /**
     * The horizontal Vector in the viewport.
     */
    private final Vector rightVector;

    /**
     * The vertical vector in the viewport.
     */
    private final Vector upVector;

    /**
     * Initializes the Looker.
     *
     * @param cameraPosition The position of the camera.
     * @param viewportCenter The center point of the viewport.
     * @param viewportWidth  The width of the viewport.
     * @param viewportHeight The height of the viewport.
     */
    public Looker(final Point cameraPosition, final Point viewportCenter, final double viewportWidth, final double viewportHeight) {
        assert cameraPosition != null;
        assert viewportCenter != null;
        if (cameraPosition.equals(viewportCenter))
            throw new IllegalArgumentException("cameraPosition equals viewportCenter.");

        if (viewportWidth <= 0)
            throw new IllegalArgumentException("viewportWidth is negative.");
        if (viewportHeight <= 0)
            throw new IllegalArgumentException("viewportHeight is negative.");

        this.cameraPosition = cameraPosition;

        viewVector = cameraPosition.vectorTo(viewportCenter);
        // The viewVector cannot be parallel to the y-Axis because it isn't possible
        // to calculate the right vector then.
        if (viewVector.normalize().equals(Vector.Y_VECTOR))
            throw new IllegalArgumentException("viewVector is parallel to y-Axis.");

        // The order of the cross products is important to have the vectors directly pointing
        // in the right direction according to the right hand rule.
        rightVector = viewVector.crossProduct(Vector.Y_VECTOR).scale(viewportWidth / 2);
        upVector = rightVector.crossProduct(viewVector).scale(viewportHeight / 2);
    }

    /**
     * Calculates a ray based on viewport coordinates between -1 and 1.
     *
     * @param horizontal The horizontal component, ranges between -1 and 1.
     * @param vertical   The vertical component, ranges between -1 and 1.
     * @return A ray.
     */
    public Ray getPrimaryRay(final double horizontal, final double vertical) {
        if (Math.abs(horizontal) > 1)
            throw new IllegalArgumentException("horizontal is not within -1 and 1");
        if (Math.abs(vertical) > 1)
            throw new IllegalArgumentException("vertical is not within -1 and 1");

        // The horizontal component.
        final Vector hVector = rightVector.scalarProduct(horizontal);
        // The vertical component.
        final Vector vVector = upVector.scalarProduct(vertical);
        // And the direction.
        final Vector direction = viewVector.add(hVector).add(vVector);

        return new Ray(cameraPosition, direction);
    }
}