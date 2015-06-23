package de.fabianparzefall.jraytracer.scene;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A scene, that loads instructions from a file.
 *
 * @author Fabian Parzefall.
 * @version 15-05-09
 */
class LoadedScene extends ScriptedScene {
    /**
     * Created a loaded scene from the filename.
     *
     * @param filename The path to a file containing scene instructions.
     * @throws IOException If the file cannot be found.
     */
    public LoadedScene(final String filename) throws IOException {
        super(Files.lines(Paths.get(filename)).toArray(String[]::new));
    }
}
