package com.rylinaux.plugman.utilities;

public class StringUtil {

    public static String consolidateStrings(String[] args, int start) {
        String plugin = args[start];
        if (args.length > (start + 1)) {
            for (int i = (start + 1); i < args.length; i++)
                plugin = plugin + " " + args[i];
        }
        return plugin;
    }

}
