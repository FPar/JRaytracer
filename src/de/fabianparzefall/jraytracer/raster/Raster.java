package de.fabianparzefall.jraytracer.raster;

import de.fabianparzefall.jraytracer.tracer.Raytracer;

import java.util.Arrays;

/**
 * A raster that renders with a raytracer.
 *
 * @author Fabian Parzefall
 * @version 15-06-18
 */
public interface Raster {
    /**
     * Creates a raster by an array of strings.
     * <p>
     * The first element of the array is the class of the raster,
     * the other elements are the parameters for the raster.
     * If the array contains only one element with an empty string,
     * a default raster is created.
     *
     * @param args An array with the class name as first element and the parameters as the other elements.
     * @return An instance of a raster.
     * @throws ClassNotFoundException If the specified class cannot be found.
     */
    static Raster make(final String... args) throws ClassNotFoundException {
        assert args != null;

        if (args.length == 0)
            throw new IllegalArgumentException("args must contain at least one element.");

        final String rasterType = args[0];
        // This variable is used to count more easily through the argument array.
        int argumentPosition = 1;

        switch (rasterType) {
            case "":
                return new ArrayRaster();
            case "ArrayRaster":
                return new ArrayRaster(Integer.parseInt(args[argumentPosition++]), Integer.parseInt(args[argumentPosition]));
            case "Supersampled":
                return new Supersampled(make(Arrays.copyOfRange(args, argumentPosition, args.length)));
            case "ParallelRaster":
                return new ParallelRaster(Integer.parseInt(args[argumentPosition++]), Integer.parseInt(args[argumentPosition++]), Integer.parseInt(args[argumentPosition]));
            case "ThreadIdRaster":
                return new ThreadIdRaster(Integer.parseInt(args[argumentPosition++]), Integer.parseInt(args[argumentPosition++]), Integer.parseInt(args[argumentPosition]));
            default:
                throw new ClassNotFoundException(String.format("Could not find raster class \"%s.\"", rasterType));
        }
    }

    /**
     * Gets the width of the raster.
     *
     * @return The width.
     */
    int getWidth();

    /**
     * Gets the height of the raster.
     *
     * @return The height.
     */
    int getHeight();

    /**
     * Gets a pixel from the raster by the coordinates.
     *
     * @param yCoordinate The y coordinate.
     * @param xCoordinate The x coordinate.
     * @return The brightness value.
     */
    int getPixel(int yCoordinate, int xCoordinate);

    /**
     * Uses a raytracer to fill the raster with values.
     *
     * @param raytracer An instance of Raytracer.
     * @return This with the rendered image.
     */
    Raster render(Raytracer raytracer);
}
