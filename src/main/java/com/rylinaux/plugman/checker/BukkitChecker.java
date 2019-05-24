package com.rylinaux.plugman.checker;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rylinaux.plugman.PlugMan;
import net.gravitydevelopment.updater.Updater;
import org.json.JSONObject;

public class BukkitChecker implements Checker {

    private static final String CURSE_PROJECTS_BASE_URL = "https://api.curseforge.com/servermods/projects";

    private final PlugMan plugin;

    public BukkitChecker(PlugMan plugin) {
        this.plugin = plugin;
    }

    @Override
    public int getId(String pluginName) {

        int id = -1;

        try {

            HttpResponse<JsonNode> response = Unirest.get(CURSE_PROJECTS_BASE_URL + "?search=" + pluginName).asJson();
            JSONObject json = response.getBody().getObject();

            id = json.getInt("id");

        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return id;

    }

    @Override
    public Updater.UpdateResult fetchStatus(int id) {
        return new Updater(plugin, id, plugin.getFile(), Updater.UpdateType.NO_DOWNLOAD, false).getResult();
    }

}