package de.fabianparzefall.jraytracer.raster;

import de.fabianparzefall.jraytracer.tracer.Raytracer;

import java.util.Optional;

/**
 * A raster, which you uses threads for optimal parallelism.
 *
 * @author Fabian Parzefall
 * @version 15-06-18
 */
public class ParallelRaster extends ArrayRaster {
    /**
     * The maximum brightness of the scene.
     */
    private static final int MAX_BRIGHTNESS = 255;

    /**
     * The count of threads.
     */
    private final int threadCount;

    /**
     * This zero-based int contains the highest row number, which is not calculated yet.
     */
    private int remainingRows;

    /**
     * Constructs a ParallelRaster from width, height and the count of threads.
     *
     * @param width       The width of the raster, must be positive.
     * @param height      The height of the raster, must be positive.
     * @param threadCount The number of threads to use, must be positive or 0. If it's 0, than the count of available
     *                    processors is used.
     */
    public ParallelRaster(final int width, final int height, final int threadCount) {
        super(width, height);

        if (threadCount < 0) {
            throw new IllegalArgumentException("threadCount must be at least 0.");
        }

        // If threadCount is 0, get the count of available processors.
        if (threadCount == 0) {
            this.threadCount = Runtime.getRuntime().availableProcessors();
        } else {
            this.threadCount = threadCount;
        }

        remainingRows = height - 1;
    }

    public int getThreadCount() {
        return threadCount;
    }

    @Override
    public Raster render(final Raytracer raytracer) {
        assert raytracer != null;

        final Thread[] threads = new Thread[threadCount];
        for (int index = 0; index < threadCount; index++) {
            threads[index] = new LineProcessor(raytracer);
            // The thread gets as its name an id, that can be used by the Thread ID Raster.
            threads[index].setName(Integer.toString(index));
            threads[index].start();
        }

        for (final Thread processor : threads) {
            try {
                // Wait for all threads.
                processor.join();
            } catch (final InterruptedException exception) {
                // This should never be hit.
                assert false;
            }
        }

        return this;
    }

    /**
     * Fetches the vertical coordinate of the next row to process. This method is thread safe.
     *
     * @return An optional with an int value or an empty optional, if there are no more rows to process.
     */
    private synchronized Optional<Integer> fetchNextRow() {
        if (remainingRows < 0) {
            return Optional.empty();
        } else {
            // Decrement after creating optional.
            return Optional.of(remainingRows--);
        }
    }

    /**
     * This class renders one line after another.
     */
    private class LineProcessor extends Thread {
        /**
         * The raytracer to raytrace points.
         */
        private final Raytracer raytracer;

        /**
         * Creates a LineProcessor with the given raytracer.
         *
         * @param raytracer The raytracer.
         */
        public LineProcessor(final Raytracer raytracer) {
            this.raytracer = raytracer;
        }

        @Override
        public void run() {
            Optional<Integer> currentRow = fetchNextRow();

            final CoordinateConverter coordinateConverter = new CoordinateConverter(getWidth(), getHeight());

            while (currentRow.isPresent()) {
                // Iterate through a row.
                for (int xCoordinate = 0; xCoordinate < getWidth(); xCoordinate++) {
                    setPixel(currentRow.get(), xCoordinate,
                            (int) (raytracer.tracePrimary(
                                    coordinateConverter.calculateHorizontalCoordinate(xCoordinate),
                                    coordinateConverter.calculateVerticalCoordinate(currentRow.get())
                            ) * MAX_BRIGHTNESS));
                }

                currentRow = fetchNextRow();
            }
        }
    }
}
