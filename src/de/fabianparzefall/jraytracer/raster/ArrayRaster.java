package de.fabianparzefall.jraytracer.raster;

import de.fabianparzefall.jraytracer.tracer.Raytracer;

/**
 * A raster, that uses a raytracer to calculate an array with brightness values.
 *
 * @author Fabian Parzefall
 * @version 15-06-18
 */
class ArrayRaster implements Raster {
    /**
     * The resolution used for default constructor.
     */
    private static final int DEFAULT_RESOLUTION = 128;

    /**
     * The maximum brightness of the scene.
     */
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * The width in pixels of the scene.
     */
    private final int width;

    /**
     * The height in pixels of the scene.
     */
    private final int height;

    /**
     * The array in which the values get saved into.
     */
    private final int[][] raster;

    /**
     * Constructs an ArrayRaster with a resolution of 128x128.
     */
    public ArrayRaster() {
        this(DEFAULT_RESOLUTION, DEFAULT_RESOLUTION);
    }

    /**
     * Constructs the raster with the given width and height.
     *
     * @param width  Width of the raster, must be greater or equal 1.
     * @param height Height of the raster, must be greater or equal 1.
     */
    public ArrayRaster(final int width, final int height) {
        if (width < 1)
            throw new IllegalArgumentException("width is less than 1.");
        if (height < 1)
            throw new IllegalArgumentException("height is less than 1.");

        this.width = width;
        this.height = height;
        raster = new int[height][width];
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public int getPixel(final int yCoordinate, final int xCoordinate) {
        if (!(0 <= xCoordinate && xCoordinate < width) && !(0 <= yCoordinate && yCoordinate < height))
            throw new IllegalArgumentException("point is not within the resolution.");

        return raster[yCoordinate][xCoordinate];
    }

    /**
     * Sets the pixel in raster at the given coordinates.
     *
     * @param yCoordinate The y coordinate of the pixel.
     * @param xCoordinate The x coordinate of the pixel.
     * @param brightness  The brightness of the pixel.
     */
    void setPixel(final int yCoordinate, final int xCoordinate, final int brightness) {
        assert 0 <= xCoordinate && xCoordinate < width;
        assert 0 <= yCoordinate && yCoordinate < height;
        assert 0 <= brightness && brightness <= MAX_BRIGHTNESS;

        raster[yCoordinate][xCoordinate] = brightness;
    }

    @Override
    public Raster render(final Raytracer raytracer) {
        assert raytracer != null;

        final CoordinateConverter coordinateConverter = new CoordinateConverter(width, height);

        for (int yCoordinate = 0; yCoordinate < height; yCoordinate++) {
            for (int xCoordinate = 0; xCoordinate < width; xCoordinate++) {
                setPixel(yCoordinate, xCoordinate,
                        (int) (raytracer.tracePrimary(
                                coordinateConverter.calculateHorizontalCoordinate(xCoordinate),
                                coordinateConverter.calculateVerticalCoordinate(yCoordinate)
                        ) * MAX_BRIGHTNESS));
            }
        }

        return this;
    }


}
