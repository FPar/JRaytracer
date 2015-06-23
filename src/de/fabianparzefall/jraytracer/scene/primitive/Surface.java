package de.fabianparzefall.jraytracer.scene.primitive;

import java.util.HashMap;
import java.util.Map;

/**
 * The surface of a primitive.
 *
 * @author Fabian Parzefall
 * @version 15-03-06
 */
public class Surface {
    /**
     * This map contains the properties and values.
     */
    private final Map<Property, Double> propertyDoubleMap = new HashMap<>();

    /**
     * Get a property.
     *
     * @param property The property.
     * @return The value of the property.
     */
    public double get(final Property property) {
        assert property != null;

        if (propertyDoubleMap.containsKey(property)) {
            return propertyDoubleMap.get(property);
        } else {
            return property.getDefaultValue();
        }
    }

    /**
     * Set a property. A property can be only set once.
     *
     * @param property The property to set.
     * @param newValue The value of the property.
     */
    public void set(final Property property, final double newValue) {
        assert property != null;
        if (!property.isValidValue(newValue))
            throw new IllegalArgumentException(String.format("newValue is not between %f and %f.", property.minValue, property.maxValue));

        if (propertyDoubleMap.containsKey(property))
            throw new IllegalStateException(String.format("Key \"%s\" is already set.", property));

        propertyDoubleMap.put(property, newValue);
    }

    /**
     * An enum with the possible properties of the surface.
     */
    public enum Property {
        /**
         * The ratio of ambient light on the surface.
         */
        AmbientRatio(0.0, 1.0, 0.05),
        /**
         * The ratio of diffuse light on the surface.
         */
        DiffuseRatio(0.0, 1.0, 0.95),

        /**
         * The ratio of specular light on the surface.
         */
        SpecularRatio(0, 1, 0),

        /**
         * The intensity of the specular lights.
         */
        SpecularExponent(0, 1000, 30),

        /**
         * The ratio of a reflexion.
         */
        ReflexionRatio(0, 1, 0);

        /**
         * The default value of a property.
         */
        private final double defaultValue;

        /**
         * The minimum value of a property.
         */
        private final double minValue;

        /**
         * The maximum value of a property.
         */
        private final double maxValue;

        /**
         * Constructs a property.
         *
         * @param minValue     The minimum value of the property.
         * @param maxValue     The maximum value of the property.
         * @param defaultValue The default value of the property.
         */
        Property(final double minValue, final double maxValue, final double defaultValue) {
            this.minValue = minValue;
            this.maxValue = maxValue;
            this.defaultValue = defaultValue;
        }

        public double getDefaultValue() {
            return defaultValue;
        }

        /**
         * Checks, if the given value is in the range of the properties allowed values.
         *
         * @param value The value to test.
         * @return True, if the value is valid, otherwise false.
         */
        public boolean isValidValue(final double value) {
            return minValue <= value && value <= maxValue;
        }
    }
}
