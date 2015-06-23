package de.fabianparzefall.jraytracer.geometry;

import de.fabianparzefall.jraytracer.common.Doubles;

/**
 * Class representing coordinates in 3 dimensional space.
 *
 * @author Fabian Parzefall
 * @version 15-05-25
 */
abstract class Coordinates3D {
    /**
     * X coordinate.
     */
    private final double xCoordinate;

    /**
     * Y coordinate.
     */
    private final double yCoordinate;

    /**
     * Z coordinate.
     */
    private final double zCoordinate;

    /**
     * Constructs from coordinates.
     *
     * @param x X coordinate.
     * @param y Y coordinate.
     * @param z Z coordinate.
     */
    protected Coordinates3D(final double x, final double y, final double z) {
        xCoordinate = x;
        yCoordinate = y;
        zCoordinate = z;
    }

    protected double getX() {
        return xCoordinate;
    }

    protected double getY() {
        return yCoordinate;
    }

    protected double getZ() {
        return zCoordinate;
    }

    @Override
    public String toString() {
        return String.format("Coordinates3D{xCoordinate=%s, yCoordinate=%s, zCoordinate=%s}", xCoordinate, yCoordinate, zCoordinate);
    }

    @Override
    public boolean equals(final Object obj) {
        assert obj != null;
        if (this == obj) return true;
        if (getClass() != obj.getClass()) return false;

        final Coordinates3D that = (Coordinates3D) obj;

        return Doubles.equals(that.xCoordinate, xCoordinate)
                && Doubles.equals(that.yCoordinate, yCoordinate)
                && Doubles.equals(that.zCoordinate, zCoordinate);
    }

    @Override
    public int hashCode() {
        throw new UnsupportedOperationException();
    }
}
