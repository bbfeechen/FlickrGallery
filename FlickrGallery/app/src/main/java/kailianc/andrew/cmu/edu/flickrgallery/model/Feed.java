package kailianc.andrew.cmu.edu.flickrgallery.model;

import java.io.Serializable;

/**
 * Author  : KAILIANG CHEN
 * Version : 1.0
 * Date    : 12/13/15
 *
 * Concrete class for photo's information to be used for creating url.
 * Only used attributes are defined, e.g. photo id, secrete string, server, farm.
 * Other attributes could be added for future use cases.
 *
 * Implementing Serializable is to serialize/deserialize single photo feed when screen
 * transits from FeedActivity(FeedFragment) to PhotoActivity(PhotoFragment).
 *
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

    /**
     * function for single photo id
     * @return : photo id
     */
    public String getId() {
        return id;
    }

    /**
     * function for single photo url
     * @return : photo url for downloading
     */
    public String getUrl() {
        return "http://farm" + farm + ".static.flickr.com/" + server + "/" + id + "_" + secret + ".jpg";
    }
}
