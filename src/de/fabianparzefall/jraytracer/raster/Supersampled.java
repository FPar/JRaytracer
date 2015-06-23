package de.fabianparzefall.jraytracer.raster;

import de.fabianparzefall.jraytracer.tracer.Raytracer;

import java.util.stream.Stream;

/**
 * An implementation of a supersampled raster, that works on another raster.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
class Supersampled implements Raster {
    /**
     * The underlying raster.
     */
    private final Raster underlyingRaster;

    /**
     * Constructs the supersampled raster from a raster.
     *
     * @param underlyingRaster The raster to work with.
     */
    public Supersampled(final Raster underlyingRaster) {
        assert underlyingRaster != null;

        if (underlyingRaster.getHeight() % 2 != 0 || underlyingRaster.getWidth() % 2 != 0)
            throw new IllegalArgumentException("underlyingRaster resolution is not even.");

        this.underlyingRaster = underlyingRaster;
    }

    @Override
    public int getWidth() {
        return underlyingRaster.getWidth() / 2;
    }

    @Override
    public int getHeight() {
        return underlyingRaster.getHeight() / 2;
    }

    @Override
    public int getPixel(final int yCoordinate, final int xCoordinate) {
        if (!(0 <= xCoordinate && xCoordinate < getWidth()) && !(0 <= yCoordinate && yCoordinate < getHeight()))
            throw new IllegalArgumentException("point is not within the resolution.");

        final int doubledYCoordinate = yCoordinate * 2;
        final int doubledXCoordinate = xCoordinate * 2;

        final double value = Stream.of(
                underlyingRaster.getPixel(doubledYCoordinate, doubledXCoordinate),
                underlyingRaster.getPixel(doubledYCoordinate, doubledXCoordinate + 1),
                underlyingRaster.getPixel(doubledYCoordinate + 1, doubledXCoordinate),
                underlyingRaster.getPixel(doubledYCoordinate + 1, doubledXCoordinate + 1))
                .mapToDouble(Double::valueOf)
                .average()
                .getAsDouble();

        return (int) Math.round(value);
    }

    @Override
    public Raster render(final Raytracer raytracer) {
        assert raytracer != null;

        underlyingRaster.render(raytracer);

        return this;
    }
}
