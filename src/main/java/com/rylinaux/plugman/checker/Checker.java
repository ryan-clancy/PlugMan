package com.rylinaux.plugman.checker;

import net.gravitydevelopment.updater.Updater.UpdateResult;

public interface Checker {

    int getId(String pluginName);

    UpdateResult fetchStatus(int id);

}