package de.fabianparzefall.jraytracer.scene;

import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.scene.primitive.Intersection;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * The interface for a scene.
 *
 * @author Fabian Parzefall
 * @version 15-03-06
 */
public interface Scene {
    /**
     * Creates a scene by an array of strings.
     * <p>
     * The first element of the array is the class of the scene,
     * the other elements are the parameters for the scene.
     * If the array contains only one element with an empty string,
     * a default scene is created.
     *
     * @param args An array with the class name as first element and the parameters as the other elements.
     * @return An instance of a scene.
     * @throws ClassNotFoundException If the specified class cannot be found.
     * @throws IOException            If a LoadedScene cannot find a file.
     */
    static Scene make(final String... args) throws ClassNotFoundException, IOException {
        assert args != null;
        if (args.length == 0)
            throw new IllegalArgumentException("args must contain at least one element.");

        final String sceneType = args[0];

        switch (sceneType) {
            case "":
                return new ScriptedScene("looker [0 0 5] [0 0 0] 2 2",
                        "sphere [0 0 -5] 1",
                        "ambient 1");
            case "ScriptedScene":
                return new ScriptedScene(Arrays.copyOfRange(args, 1, args.length));
            case "LoadedScene":
                if (args.length != 2)
                    throw new IllegalArgumentException("Need exactly one parameter for a Loaded Scene.");
                return new LoadedScene(args[1]);
            default:
                throw new ClassNotFoundException(String.format("Could not find scene class \"%s.\"", sceneType));
        }
    }

    /**
     * Gets the looker in the scene.
     *
     * @return The looker.
     */
    Looker getLooker();

    /**
     * Gets the light source of the scene.
     *
     * @return The position of the light source as point.
     */
    Optional<Point> getLight();

    /**
     * Finds intersection of the given ray in the scene.
     *
     * @param ray The ray.
     * @return An intersection or null.
     */
    Optional<Intersection> findIntersection(Ray ray);
}
