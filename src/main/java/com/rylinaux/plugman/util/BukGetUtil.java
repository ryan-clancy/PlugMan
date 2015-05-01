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

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utilities for dealing with the Bukget API.
 *
 * @author rylinaux
 */
public class BukGetUtil {

    /**
     * The base URL for the Bukget API.
     */
    public static final String API_BASE_URL = "http://api.bukget.org/3/";

    public static String getPluginSlug(String name) {

        HttpClient client = HttpClients.createMinimal();
        HttpGet get = new HttpGet(API_BASE_URL + "search/slug/like/" + name + "?fields=plugin_name,slug");

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            JSONArray jsonArray = new JSONArray(body);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject json = jsonArray.getJSONObject(i);
                if (json.getString("plugin_name").equalsIgnoreCase(name))
                    return json.getString("slug");
            }

        } catch (IOException e) {

        }

        return null;

    }

    /**
     * Get the plugin data for the latest version.
     *
     * @param slug the plugin slug.
     * @return the JSON encoded data.
     */
    public static JSONObject getPluginData(String slug) {
        return getPluginData(slug, "latest");
    }

    /**
     * Get the plugin data for a certain version.
     *
     * @param slug the plugin slug.
     * @return the JSON encoded data.
     */
    public static JSONObject getPluginData(String slug, String version) {

        HttpClient client = HttpClients.createMinimal();
        HttpGet get = new HttpGet(API_BASE_URL + "plugins/bukkit/" + slug + "/" + version);

        try {

            HttpResponse response = client.execute(get);
            String body = IOUtils.toString(response.getEntity().getContent());

            JSONObject json = new JSONObject(body);

            return new JSONObject(body);

        } catch (IOException e) {

        }

        return null;

    }

}
