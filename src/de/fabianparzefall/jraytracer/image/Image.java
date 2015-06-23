package de.fabianparzefall.jraytracer.image;

import de.fabianparzefall.jraytracer.raster.Raster;

import java.io.IOException;

/**
 * An interface for saving a raster to a target.
 *
 * @author Fabian Parzefall
 * @version 15-03-06
 */
public interface Image {
    /**
     * Creates an image by an array of strings.
     * <p>
     * The first element of the array is the class of the image, the other elements are the parameters for the image. If
     * the array contains only one element with an empty string, a default image is created.
     *
     * @param args An array with the class name as first element and the parameters as the other elements.
     * @return An instance of an image.
     * @throws ClassNotFoundException If the specified class cannot be found.
     */
    static Image make(final String... args) throws ClassNotFoundException {
        assert args != null;
        if (args.length == 0)
            throw new IllegalArgumentException("args has no elements.");

        final String imageType = args[0];

        switch (imageType) {
            case "":
            case "PGMOut":
                return new PGMOut();
            case "PNGImage":
                if (args.length != 2)
                    throw new IllegalArgumentException("PNGImage doesn't have exactly one argument.");
                return new PNGImage(args[1]);
            default:
                throw new ClassNotFoundException(String.format("Could not find image class \"%s.\"", imageType));
        }
    }

    /**
     * Saves a raster.
     *
     * @param raster The raster.
     * @throws IOException If the image cannot be saved.
     */
    void save(Raster raster) throws IOException;
}
