package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import spock.lang.Specification

class ArticleExtractorTest extends Specification {

    def "can link and call extractors"() {
        given:
        Extractor ex1 = Spy(EmptyExtractor)
        ex1.doExtract(_) >> { args -> args[0] }
        Extractor ex2 = Spy(EmptyExtractor)
        ex2.doExtract(_) >> { args -> args[0] }

        def extractor = new ArticleExtractor()
        extractor.next = null
        def param = new Article('')

        when:

        extractor.link(ex1).link(ex2)

        def result = extractor.extract(param)

        then:
        result == param
        1 * ex1.doExtract(_)
        1 * ex2.doExtract(_)
    }
}
