package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article

class ArticleExtractor extends Extractor {

    ArticleExtractor() {
        configure()
    }

    def configure() {
        this
                .link(new TitleExtractor())
    }

    Article doExtract(Article article) {
        return article
    }
}
