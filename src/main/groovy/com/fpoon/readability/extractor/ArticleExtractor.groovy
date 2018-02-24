package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article

/**
 * Main article extractor
 */
class ArticleExtractor extends Extractor {

    ArticleExtractor() {
        configure()
    }

    /**
     * Configures extractor chain
     */
    def configure() {
        this
                .link(new TitleExtractor())
                .link(new PageExtractor())
                .link(new StripUnlikelyExtractor())
                .link(new ContentExtractor())
    }

    /**
     * Overrides default extractor link. Inserts link as last one in chain
     *
     * @param extractor link to insert
     * @return          newly inserted extractor
     */
    Extractor link(Extractor extractor) {
        Extractor ex = this;
        while (ex.next) {
            ex = ex.next
        }

        ex.next = extractor

        return extractor
    }

    /**
     * Does nothing
     * @param article
     * @return article
     */
    Article doExtract(Article article) {
        return article
    }
}
