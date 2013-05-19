package edu.first.util;

import com.sun.squawk.util.StringTokenizer;
import java.io.IOException;

/**
 * The class representation of files on the cRIO that contain "properties". This
 * means that there are different values that are accessible by a key. Property
 * files contain the values separated by lines, with an equals sign to indicate
 * what the value is.
 *
 * Ex.
 * <pre>
 * Key = Example Value
 * ShooterSpeed = 42
 * ShooterSpeedFar = 60
 * EncoderDistance = 12.32
 * </pre>
 *
 * Properties are stored as strings only, and cannot be verified as
 * doubles/ints/longs/etc. That has to be done in client code.
 *
 * @since May 13 13
 * @author Joel Gallant
 */
public final class Properties {

    private final String fileName;
    // These are lazily intialized
    private String propertiesContent;
    private Property[] properties;

    /**
     * Constructs the object that will read the properties file. No processing
     * or I/O is done inside of the constructor, so construction is not resource
     * heavy.
     *
     * For all intents and purposes, {@code fileName} should be the same as the
     * argument in {@link TextFiles#getTextFromFile(java.lang.String)}. That
     * definition is subject to change outside of this class' control.
     *
     * @param fileName name of the file with properties in it
     */
    public Properties(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Returns the string with the file's entire contents. Does no parse it into
     * properties.
     *
     * @return the contents of the file
     * @throws IOException when reading from the file throws an exception
     */
    public String getFileContents() throws IOException {
        // lazy intialization so construction isn't intense
        if (propertiesContent == null) {
            propertiesContent = TextFiles.getTextFromFile(fileName);
        }
        return propertiesContent;
    }

    /**
     * Returns an array of properties which are suitable for using.
     *
     * Specifications of property files that are needed for this method to
     * return valid results:
     * <ul>
     * <li> All properties are separated by new lines (Windows / Unix Line
     * Endings)
     * <li> All keys are separated by their values using an equals sign
     * <li> No other equals signs are present
     * </ul>
     *
     * Spaces around the key and value (ex. key = value) are are disregarded.
     *
     * @return all properties in the file
     * @throws IOException when reading from the file throws an exception
     */
    public Property[] getProperties() throws IOException {
        if (properties == null) {
            StringTokenizer tokenizer = new StringTokenizer(getFileContents(), "\n\r=");
            Property[] p = new Property[tokenizer.countTokens() / 2];
            for (int x = 0; x < p.length; x++) {
                p[x] = new Property(tokenizer.nextToken(), tokenizer.nextToken());
            }
            // Buffer the array so less chance of accessing it mid-construction
            properties = p;
        }
        return properties;
    }

    /**
     * Returns the property object that has the specific key.
     *
     * Returns null if no property under the name exists.
     *
     * @param key string used to declare the property
     * @return {@code Property} object corresponding to key
     */
    public Property getProperty(String key) {
        for (int x = 0; x < properties.length; x++) {
            if (properties[x].key.equals(key)) {
                return properties[x];
            }
        }
        return null;
    }

    /**
     * Returns the actual value designated in the file under the key.
     *
     * @param key string used to declare the property
     * @return value given to the key in the file
     * @throws NullPointerException when key does not exist
     */
    public String getValue(String key) {
        return getProperty(key).value;
    }

    /**
     * Returns the actual value designated in the file under the key.
     *
     * @param key string used to declare the property
     * @param backup value used if it did not exist in the file
     * @return value given to the key in the file
     */
    public String getValue(String key, String backup) {
        Property p;
        return (p = getProperty(key)) == null ? backup : p.value;
    }

    /**
     * A class used to represent properties inside of a file. Uses an internal
     * key to identify itself.
     */
    public static final class Property {

        private final String key;
        private final String value;

        private Property(String key, String value) {
            this.key = key.trim();
            this.value = value.trim();
        }

        /**
         * Returns the key that was used to store this value.
         *
         * @return key that property is saved as
         */
        public String getKey() {
            return key;
        }

        /**
         * Returns the value that is saved.
         *
         * @return value that property is in the file
         */
        public String getValue() {
            return value;
        }

        /**
         * Returns the equivalent of {@code key} + " =  " + {@code value}.
         *
         * @return string as would appear in a file
         */
        public String toString() {
            return key + " = " + value;
        }
    }
}
