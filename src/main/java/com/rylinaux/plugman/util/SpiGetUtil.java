package com.rylinaux.plugman.util;

/*
 * #%L
 * PlugMan
 * %%
 * Copyright (C) 2010 - 2015 PlugMan
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import com.rylinaux.plugman.pojo.UpdateResult;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

/**
 * Utilities for dealing with the SpiGet API.
 *
 * @author rylinaux
 */
public class SpiGetUtil {

    /**
     * The base URL for the SpiGet API.
     */
    public static final String API_BASE_URL = "https://api.spiget.org/v2/";

    /**
     * Check which plugins are up-to-date or not.
     *
     * @return a map of the plugins and the results.
     */
    public static Map<String, UpdateResult> checkUpToDate() {
        Map<String, UpdateResult> results = new TreeMap<>();
        for (Plugin plugin : Bukkit.getPluginManager().getPlugins())
            results.put(plugin.getName(), checkUpToDate(plugin.getName()));
        return results;
    }

    /**
     * Check if the installed plugin version is up-to-date with the Spigot version.
     *
     * @param pluginName the plugin name.
     * @return the reflective UpdateResult.
     */
    public static UpdateResult checkUpToDate(String pluginName) {

        long pluginId = SpiGetUtil.getPluginId(pluginName);

        if (pluginId < 0) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            return new UpdateResult(UpdateResult.ResultType.INVALID_PLUGIN, plugin.getDescription().getVersion());
        }

        JSONArray versions = SpiGetUtil.getPluginVersions(pluginId);

        if (versions.size() == 0) {
            Plugin plugin = Bukkit.getPluginManager().getPlugin(pluginName);
            return new UpdateResult(UpdateResult.ResultType.INVALID_PLUGIN, plugin.getDescription().getVersion());
        }

        JSONObject latest = (JSONObject) versions.get(0);

        String currentVersion = PluginUtil.getPluginVersion(pluginName);
        String latestVersion = (String) latest.get("name");

        if (currentVersion == null) {
            return new UpdateResult(UpdateResult.ResultType.NOT_INSTALLED, currentVersion, latestVersion);
        } else if (currentVersion.equalsIgnoreCase(latestVersion)) {
            return new UpdateResult(UpdateResult.ResultType.UP_TO_DATE, currentVersion, latestVersion);
        } else {
            return new UpdateResult(UpdateResult.ResultType.OUT_OF_DATE, currentVersion, latestVersion);
        }

    }

    /**
     * Get the id of the plugin.
     *
     * @param name the name of the plugin.
     * @return the id of the plugin.
     */
    public static long getPluginId(String name) {

        HttpClient client = HttpClients.createMinimal();

        HttpGet get = new HttpGet(API_BASE_URL + "search/resources/" + name + "?field=name&fields=id%2Cname");
        get.setHeader("User-Agent", "PlugMan");

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            Object object = JSONValue.parse(body);

            if (object instanceof JSONArray) {

                JSONArray array = (JSONArray) JSONValue.parse(body);

                for (int i = 0; i < array.size(); i++) {
                    JSONObject json = (JSONObject) array.get(i);
                    String pluginName = (String) json.get("name");
                    if (name.equalsIgnoreCase(pluginName)) {
                        return (long) json.get("id");
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return -1;

    }

    /**
     * Get the versions for a given plugin.
     *
     * @param id the plugin id.
     * @return the JSON encoded data.
     */
    public static JSONArray getPluginVersions(long id) {

        HttpClient client = HttpClients.createMinimal();

        HttpGet get = new HttpGet(API_BASE_URL + "/resources/" + id + "/versions?sort=-releaseDate");
        get.setHeader("User-Agent", "PlugMan");

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            return (JSONArray) JSONValue.parse(body);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }

}
