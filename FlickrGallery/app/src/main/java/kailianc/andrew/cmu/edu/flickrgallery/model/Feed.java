package kailianc.andrew.cmu.edu.flickrgallery.model;

import java.io.Serializable;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 */
public class Feed implements Serializable {

    private String id;
    private String secret;
    private String server;
    private String farm;

    public Feed(String id, String secret, String server, String farm) {
        this.id = id;
        this.secret = secret;
        this.server = server;
        this.farm = farm;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }
}
