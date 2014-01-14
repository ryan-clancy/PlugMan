package com.rylinaux.plugman.updater;

import net.gravitydevelopment.updater.Updater;

import com.rylinaux.plugman.PlugMan;

public class UpdaterHandler {

    private final Updater updater;

    public UpdaterHandler(String updaterType) {

        Updater.UpdateType type;
        
        switch (updaterType.toLowerCase()) {
            case "download":
                type = Updater.UpdateType.DEFAULT;
            

        }

    }

}
