package com.fpoon.readability.resource

import spock.lang.Specification

class ArticleTest extends Specification {

    /**
     * this test requires internet connection
     */
    def "can read some data from #url"() {
        expect:
            def article = new Article(new URL(url))
            article.input.contains(phrase)

        where:
            url                         || phrase
            'https://google.pl'         || 'html'
            'https://github.com'        || 'html'
    }
}
