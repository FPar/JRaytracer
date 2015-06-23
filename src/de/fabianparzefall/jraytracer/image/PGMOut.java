package de.fabianparzefall.jraytracer.image;

import de.fabianparzefall.jraytracer.raster.Raster;

/**
 * Saves an array of pixels to a PGM file.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
class PGMOut implements Image {
    /**
     * The minimum brightness of a pixel.
     */
    private static final int MIN_BRIGHTNESS = 0;

    /**
     * The maximum brightness of a pixel.
     */
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * Saves a raster to console output.
     *
     * @param raster The raster.
     */
    public void save(final Raster raster) {
        assert raster != null;

        System.out.println(asString(raster));
    }

    /**
     * Converts a raster to a pgm string.
     *
     * @param raster The raster.
     * @return A pgm formatted string.
     * @see <a href="http://en.wikipedia.org/wiki/Netpbm_format">PGM</a>
     */
    public String asString(final Raster raster) {
        assert raster != null;

        if (raster.getWidth() < 1 || raster.getHeight() < 1)
            throw new IllegalArgumentException("raster resolution must be at least 1x1");

        final int yResolution = raster.getHeight();
        final int xResolution = raster.getWidth();

        final StringBuilder builder = new StringBuilder();

        builder.append(String.format("P2%n%d %d%n%d%n", xResolution, yResolution, MAX_BRIGHTNESS));
        for (int yCoordinate = yResolution - 1; yCoordinate >= 0; yCoordinate--) {
            for (int xCoordinate = 0; xCoordinate < xResolution; xCoordinate++) {
                final int brightness = raster.getPixel(yCoordinate, xCoordinate);
                if (brightness < MIN_BRIGHTNESS || brightness > MAX_BRIGHTNESS)
                    throw new IllegalArgumentException("raster must only contain values between 0 and 255.");

                builder.append(String.format("%d ", brightness));
            }
            builder.append('\n');
        }

        return builder.toString();
    }
}
