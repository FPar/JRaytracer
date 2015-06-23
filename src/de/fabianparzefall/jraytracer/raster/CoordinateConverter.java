package de.fabianparzefall.jraytracer.raster;

/**
 * This class provides methods to convert resolution based coordinates to viewport coordinates.
 *
 * @author Fabian Parzefall
 * @version 15-06-16
 */
class CoordinateConverter {
    /**
     * The factor horizontal coordinates get scaled with.
     */
    private final double horizontalFactor;

    /**
     * The value used to align the coordinate to the viewport horizontally.
     */
    private final double horizontalShift;

    /**
     * The factor vertical coordinates get scaled with.
     */
    private final double verticalFactor;

    /**
     * The value used to align the coordinate to the viewport vertically.
     */
    private final double verticalShift;

    /**
     * Constructs the CoordinateConverter with the resolution of the raster.
     *
     * @param horizontalResolution The horizontal resolution of the raster.
     * @param verticalResolution   The vertical resoultion of the raster.
     */
    public CoordinateConverter(final int horizontalResolution, final int verticalResolution) {
        horizontalFactor = calculateScaleFactor(horizontalResolution);
        horizontalShift = calculateShift(horizontalResolution);
        verticalFactor = calculateScaleFactor(verticalResolution);
        verticalShift = calculateShift(verticalResolution);
    }

    /**
     * Calculates the horizontal viewport coordinate component.
     *
     * @param xCoordinate The horizontal coordinate component.
     * @return The horizontal viewport coordinate component.
     */
    public double calculateHorizontalCoordinate(final int xCoordinate) {
        return xCoordinate * horizontalFactor - horizontalShift;
    }

    /**
     * Calculates the vertical viewport coordinate component.
     *
     * @param yCoordinate The vertical coordinate component.
     * @return The vertical viewport coordinate component.
     */
    public double calculateVerticalCoordinate(final int yCoordinate) {
        return yCoordinate * verticalFactor - verticalShift;
    }

    /**
     * Calculates a factor to convert pixel coordinates to viewport coordinates.
     *
     * @param resolution The resolution of the raster.
     * @return A factor.
     */
    private double calculateScaleFactor(final int resolution) {
        if (resolution == 1)
            return 1;
        else
            return (double) 2 / resolution;
    }

    /**
     * Calculates a minuend to shift the viewport with the origin as center.
     *
     * @param resolution The resolution of the raster.
     * @return A minuend.
     */
    private double calculateShift(final int resolution) {
        return 1 - (double) 1 / resolution;
    }
}
