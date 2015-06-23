package de.fabianparzefall.jraytracer.raster;

/**
 * This class produces a raster, in which you can see the activities of the various threads.
 *
 * @author Fabian Parzefall
 * @version 15-06-18
 */
public class ThreadIdRaster extends ParallelRaster {
    /**
     * The maximum brightness of the scene.
     */
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * Constructs a ParallelRaster from width, height and the count of threads.
     *
     * @param width       The width of the raster, must be positive.
     * @param height      The height of the raster, must be positive.
     * @param threadCount The number of threads to use, must be positive or 0. If it's 0, than the count of available
     */
    public ThreadIdRaster(final int width, final int height, final int threadCount) {
        super(width, height, threadCount);
    }

    @Override
    protected void setPixel(final int yCoordinate, final int xCoordinate, final int brightness) {
        final int brightnessValue = (int) ((double) MAX_BRIGHTNESS * Integer.parseInt(Thread.currentThread().getName()) / (getThreadCount() - 1));
        super.setPixel(yCoordinate, xCoordinate, brightnessValue);
    }
}
