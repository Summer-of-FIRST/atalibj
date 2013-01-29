package edu.ATA.autonomous;

import ATA.gordian.Script;
import edu.ATA.main.Logger;
import java.io.IOException;
import javax.microedition.io.Connector;

/**
 * Static class meant to keep Gordian in a state where it can run the current
 * script. To make sure the script uses all of the methods given, the
 * {@link Gordian#ensureInit()} method makes sure that all storage of methods
 * and variables are stored. {@code ensureInit()} is called every time
 * {@link Gordian#run(java.lang.String)} is called, so you usually don't have to
 * worry about it.
 *
 * @author Joel Gallant <joelgallant236@gmail.com>
 */
public final class Gordian {

    private static boolean init = false;

    private Gordian() {
    }

    /**
     * Makes sure that everything is ready to be run in Gordian. This includes
     * methods, variables, etc.
     */
    public static void ensureInit() {
        if (!init) {
            init();
            init = true;
        }
    }

    /**
     * Makes sure Gordian is ready to run ({@link Gordian#ensureInit()}) and
     * then runs the script given.
     *
     * @param fileName name of the file to retrieve text from
     * @throws IOException thrown when accessing file fails
     */
    public static void run(String fileName) throws IOException {
        ensureInit();
        String script = Logger.getTextFromFile(Connector.openDataInputStream("file:///" + fileName));
        Script.run(script);
    }

    private static void init() {
    }
}
