package de.fabianparzefall.jraytracer.scene;

import de.fabianparzefall.jraytracer.common.Strings;
import de.fabianparzefall.jraytracer.geometry.Point;
import de.fabianparzefall.jraytracer.geometry.Ray;
import de.fabianparzefall.jraytracer.geometry.Vector;
import de.fabianparzefall.jraytracer.scene.primitive.*;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;


/**
 * A scene that is created by a string.
 *
 * @author Fabian Parzefall
 * @version 15-06-03
 */
class ScriptedScene implements Scene {
    /**
     * The minimum distance of an intersection.
     */
    private static final double MINIMUM_DISTANCE = 1E-10;

    /**
     * This map contains relations between and instruction name and it's action.
     */
    private final Map<String, Consumer<ParameterIterator>> actionMap = new HashMap<>();

    /**
     * A list with all primitives in the scene.
     */
    private final List<Primitive> primitives = new ArrayList<>();
    /**
     * The looker of the scene.
     */
    private Optional<Looker> looker = Optional.empty();
    /**
     * The light of the scene.
     */
    private Optional<Point> light = Optional.empty();
    /**
     * This value is used by the parser, to check, if there is a property allowed at the current position.
     */
    private boolean propertyAllowed;

    /**
     * Constructs a scene from an array of instructions.
     * <p>
     * It supports currently the following instructions:
     * <pre>
     *     looker [x y z] [x y z] w h
     *     light [x y z]
     *     sphere [x y z] r
     *     plane [x y z] <x y z>
     * </pre>
     *
     * @param instructions An array with instructions.
     */
    public ScriptedScene(final String... instructions) {
        assert instructions != null;

        setupParserMap();

        Stream.of(instructions)
                .map(String::trim)
                .filter(instruction -> !"".equals(instruction) && instruction.charAt(0) != '#')
                .map(ParameterIterator::new)
                .forEach(this::parseParameters);

        // looker must be present.
        if (!looker.isPresent())
            throw new IllegalArgumentException("instructions must contain a definition for looker.");
    }

    /**
     * Sets up the parser map.
     */
    private void setupParserMap() {
        actionMap.put("looker", parameters -> {
            if (looker.isPresent())
                throw new IllegalArgumentException("second looker defined.");
            looker = Optional.of(new Looker(parameters.nextPoint(), parameters.nextPoint(), parameters.nextDouble(), parameters.nextDouble()));
            propertyAllowed = false;
        });

        actionMap.put("light", parameters -> {
            if (light.isPresent())
                throw new IllegalArgumentException("second light defined.");
            light = Optional.of(parameters.nextPoint());
            propertyAllowed = false;
        });

        actionMap.put("sphere", parameters -> addPrimitive(new Sphere(parameters.nextPoint(), parameters.nextDouble())));
        actionMap.put("plane", parameters -> addPrimitive(new Plane(parameters.nextPoint(), parameters.nextVector())));

        actionMap.put("ambient", parameters -> setPropertyForLastPrimitive(Surface.Property.AmbientRatio, parameters.nextDouble()));
        actionMap.put("diffuse", parameters -> setPropertyForLastPrimitive(Surface.Property.DiffuseRatio, parameters.nextDouble()));
        actionMap.put("specular", parameters -> {
            setPropertyForLastPrimitive(Surface.Property.SpecularRatio, parameters.nextDouble());
            setPropertyForLastPrimitive(Surface.Property.SpecularExponent, parameters.nextDouble());
        });
        actionMap.put("reflexion", parameters -> setPropertyForLastPrimitive(Surface.Property.ReflexionRatio, parameters.nextDouble()));
    }

    /**
     * Adds a primitive to the primitives list.
     *
     * @param primitive The primitive.
     */
    private void addPrimitive(final Primitive primitive) {
        assert primitive != null;

        primitives.add(primitive);
        propertyAllowed = true;
    }

    /**
     * Sets a property for the last primitive.
     *
     * @param property The property to set.
     * @param value    The value of the property.
     */
    private void setPropertyForLastPrimitive(final Surface.Property property, final double value) {
        assert property != null;

        // Properties are only allowed after primitives and other properties.
        if (!propertyAllowed)
            throw new IllegalArgumentException("Property not allowed here.");

        primitives.get(primitives.size() - 1).getSurface().set(property, value);
    }

    /**
     * Parses a parameter from a ParameterIterator.
     *
     * @param parameters A ParameterIterator.
     */
    private void parseParameters(final ParameterIterator parameters) {
        assert parameters != null;

        // The first parameter is always the type of the instruction.
        final String type = parameters.next();

        if (actionMap.containsKey(type)) {
            actionMap.get(type).accept(parameters);
        } else {
            throw new IllegalArgumentException(String.format("Unknown type \"%s\"", type));
        }
    }

    @Override
    public Looker getLooker() {
        return looker.get();
    }

    @Override
    public Optional<Point> getLight() {
        return light;
    }

    @Override
    public Optional<Intersection> findIntersection(final Ray ray) {
        assert ray != null;

        final ArrayList<Intersection> intersections = new ArrayList<>();
        for (final Primitive primitive : primitives) {
            intersections.addAll(primitive.intersections(ray));
        }

        if (intersections.isEmpty())
            return Optional.empty();

        // Make sure to get the intersection with the shortest distance.
        Collections.sort(intersections);
        return intersections.stream()
                .filter(intersection -> intersection.getDistance() > MINIMUM_DISTANCE)
                .findFirst();
    }

    /**
     * Splits an instruction up into parameters.
     */
    private static class ParameterIterator implements Iterator<String> {
        /**
         * The instruction.
         */
        private final String[] parameters;

        /**
         * The current position at the instruction string.
         */
        private int position;

        /**
         * Constructs a parameter iterator from an instruction.
         *
         * @param instruction An instruction string.
         */
        ParameterIterator(final String instruction) {
            assert instruction != null;
            parameters = Strings.split(Strings.remove(instruction, '[', ']', '<', '>'), ' ');
        }

        @Override
        public boolean hasNext() {
            return position < parameters.length;
        }

        @Override
        public String next() {
            if (!hasNext())
                throw new NoSuchElementException("No more parameters.");

            return parameters[position++];
        }

        /**
         * Parses the next parameter as a double.
         *
         * @return A double.
         */
        public Double nextDouble() {
            return Double.parseDouble(next());
        }

        /**
         * Parses the next 3 parameters as a point.
         *
         * @return A point.
         */
        public Point nextPoint() {
            return new Point(nextDouble(), nextDouble(), nextDouble());
        }

        /**
         * Parses the next 3 parameters as a vector.
         *
         * @return A vector.
         */
        public Vector nextVector() {
            return new Vector(nextDouble(), nextDouble(), nextDouble());
        }
    }
}
