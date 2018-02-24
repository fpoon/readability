package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import spock.lang.Specification

class StripUnlikelyExtractorTest extends Specification {
    /**
     * This test requires internet connection
     */
    def "can remove unlikely candidates"() {
        given:
        def extractor = Spy(StripUnlikelyExtractor)
        extractor.isUnlikely(_) >> true

        def article = new Article(new URL(address))

        when:
        article = extractor.extract(article)

        then:
        !article.document.children().isEmpty()
        article.document.body().children().isEmpty()

        where: //TODO: test real stripping
        address                                                      || content
        'http://spockframework.org/spock/docs/1.1/spock_primer.html' || 'Spock Primer'
        'https://spring.io/blog/2018/02/22/spring-for-apache-kafka-2-1-3-spring-integration-kafka-3-0-2-are-available' || 'Spring for Apache Kafka 2.1.3, Spring Integration Kafka 3.0.2 are Available'
    }
}
