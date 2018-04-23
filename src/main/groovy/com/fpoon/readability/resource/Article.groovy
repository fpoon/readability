package com.fpoon.readability.resource

import groovy.transform.Canonical
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

/**
 * Class to hold all article related data
 */
@Canonical
class Article extends WebResource {

    /**
     * Collection of all images. Key is unique identifier of each resource
     */
    Map<String, WebResource> resources = [:]

    /**
     * Article title
     */
    String title;

    /**
     * Raw input data from page
     */
    String input;

    /**
     * Output of algorithm
     */
    String output;

    /**
     * Input parsed by JSoup
     */
    Document document;

    /**
     * Creates article from content downloaded from URL
     * @param url to download data
     */
    Article(URL url) {
        url = findRealUrl(url)
        this.input = url.text
        document = Jsoup.parse(this.input)
        this.source = url
    }

    /**
     * Creates article from content passed as parameter
     * @param input data to parse
     */
    Article(String input) {
        this.input = input
        document = Jsoup.parse(this.input);
    }

    URL findRealUrl(URL url) {
        HttpURLConnection conn = url.openConnection()
        conn.followRedirects = false
        conn.requestMethod = 'HEAD'
        if(conn.responseCode in [301,302]) {
            if (conn.headerFields.'Location') {
                return findRealUrl(conn.headerFields.Location.first().toURL())
            } else {
                throw new RuntimeException('Failed to follow redirect')
            }
        }
        return url
    }
}
