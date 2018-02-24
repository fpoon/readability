package com.fpoon.readability.resource

import spock.lang.Specification

class ArticleTest extends Specification {

    /**
     * this test requires internet connection
     */
    def "can read some data from #address"() {
        when:
            def url = new URL(address)
            def article = new Article(url)

        then:
            article.source == url
            article.input.contains(phrase)

        where:
            address                     || phrase
            'https://google.pl'         || 'html'
            'https://github.com'        || 'html'
    }
}
