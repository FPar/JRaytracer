package de.fabianparzefall.jraytracer.image;

import de.fabianparzefall.jraytracer.raster.Raster;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * This class saves a raster as a PNG file.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
class PNGImage implements Image {
    /**
     * The minimum brightness of a pixel.
     */
    private static final int MIN_BRIGHTNESS = 0;

    /**
     * The maximum brightness of a pixel.
     */
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * The filename of the image.
     */
    private final String filename;

    /**
     * Constructs a PNGImage with the filename of the image.
     *
     * @param filename The filename of the image.
     */
    public PNGImage(final String filename) {
        assert filename != null;
        this.filename = filename;
    }

    /**
     * Saves a raster to as a PNG file.
     *
     * @param raster The raster.
     */
    @Override
    public void save(final Raster raster) throws IOException {
        assert raster != null;

        if (raster.getWidth() < 1 || raster.getHeight() < 1)
            throw new IllegalArgumentException("raster resolution must be at least 1x1");

        final BufferedImage image = new BufferedImage(raster.getWidth(), raster.getHeight(), TYPE_INT_ARGB);

        for (int yCoordinate = 0; yCoordinate < raster.getHeight(); yCoordinate++) {
            for (int xCoordinate = 0; xCoordinate < raster.getWidth(); xCoordinate++) {
                final int brightness = raster.getPixel(yCoordinate, xCoordinate);
                if (brightness < MIN_BRIGHTNESS || brightness > MAX_BRIGHTNESS)
                    throw new IllegalArgumentException("raster must only contain values between 0 and 255.");
                image.setRGB(xCoordinate, raster.getHeight() - yCoordinate - 1, byteToARGB(brightness));
            }
        }

        ImageIO.write(image, "png", new File(filename));
    }

    /**
     * Transforms a brightness value to an ARGB value.
     *
     * @param brightness A brightness value between 0 (black) and 255 (white).
     * @return An ARGB value.
     */
    private int byteToARGB(final int brightness) {
        assert brightness >= MIN_BRIGHTNESS && brightness <= MAX_BRIGHTNESS;
        final int bitsInByte = 8;
        final int opaqueBitmask = 0xFF;
        return ((opaqueBitmask << bitsInByte | brightness) << bitsInByte | brightness) << bitsInByte | brightness;
    }
}
