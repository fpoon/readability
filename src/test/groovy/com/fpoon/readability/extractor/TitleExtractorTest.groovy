package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import spock.lang.Specification

class TitleExtractorTest extends Specification {
    /**
     * This test requires internet connection
     */
    def "can extract title from #address"() {
        given:
        def extractor = new TitleExtractor()
        def article = new Article(new URL(address))

        when:
        article = extractor.extract(article)

        then:
        article.title == title

        where:
        address                                                      || title
        'http://spockframework.org/spock/docs/1.1/spock_primer.html' || 'Spock Primer'
        'https://spring.io/blog/2018/02/22/spring-for-apache-kafka-2-1-3-spring-integration-kafka-3-0-2-are-available' || 'Spring for Apache Kafka 2.1.3, Spring Integration Kafka 3.0.2 are Available'
    }
}
