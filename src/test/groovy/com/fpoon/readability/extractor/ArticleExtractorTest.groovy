package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import spock.lang.Specification

class ArticleExtractorTest extends Specification {

    def "can link and call extractors"() {
        given:
        // Extractor type doesn't matter
        Extractor ex1 = Spy(ArticleExtractor)
        ex1.doExtract(_) >> { args -> args[0] }
        Extractor ex2 = Spy(ArticleExtractor)
        ex2.doExtract(_) >> { args -> args[0] }

        def extractor = new ArticleExtractor()
        def param = new Article('')

        when:

        extractor.link(ex1).link(ex2)

        def result = extractor.extract(param)

        then:
        result == param
        extractor.next == ex1
        ex1.next == ex2
//            1 * ex1.link(_)
//            1 * ex2.link(_)
        1 * ex1.doExtract(_)
        1 * ex2.doExtract(_)
    }
}
