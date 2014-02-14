package com.rylinaux.plugman.utilities;

/**
 * Utilities for String manipulation.
 *
 * @author rylinaux
 */
public class StringUtil {

    /**
     * Returns an array as a String.
     *
     * @param args  the array
     * @param start the index to start at
     * @return the array as a String
     */
    public static String consolidateStrings(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++)
                plugin = plugin + " " + args[i];
        }
        return plugin;
    }

}
