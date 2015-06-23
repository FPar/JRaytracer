package de.fabianparzefall.jraytracer.common;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Wraps a boolean in a promise.
 *
 * @author Fabian Parzefall
 * @version 15-06-15
 */
public class BooleanPromise {
    /**
     * The value supplier.
     */
    private final Supplier<Boolean> valueSupplier;

    /**
     * The lazy evaluated bool.
     */
    private Optional<Boolean> value = Optional.empty();

    /**
     * Constructs the promise from a supplier.
     *
     * @param valueSupplier The supplier, which provides the bool.
     */
    public BooleanPromise(final Supplier<Boolean> valueSupplier) {
        assert valueSupplier != null;
        this.valueSupplier = valueSupplier;
    }

    /**
     * Gets the value.
     *
     * @return true or false.
     */
    public boolean get() {
        if (!value.isPresent()) {
            value = Optional.of(valueSupplier.get());
        }
        return value.get();
    }
}
