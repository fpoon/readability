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
    Document inputDocument;

    /**
     * Creates article from content downloaded from URL
     * @param url to download data
     */
    Article(URL url) {
        this(url.text)
        this.source = url
    }

    /**
     * Creates article from content passed as parameter
     * @param input data to parse
     */
    Article(String input) {
        this.input = input
        inputDocument = Jsoup.parse(this.input);
    }
}
