package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article

/**
 * Base for extractor class
 */
abstract class Extractor {

    Extractor next

    Extractor link(Extractor extractor) {
        this.next = extractor
        return extractor
    }

    /**
     * Call me maybe? Main method to call in extractor
     * Internally calls {@link #doExtract(Article) doExtract} method for article operations
     * and pass result to the next link in extractor chain
     *
     * @param article  article to transform
     * @return article transformed by all extractors beneath current
     */
    Article extract(Article article) {
        System.err << "Calling extractor: ${this.class.name}\n"
        Article art = this.doExtract(article)

        art = next?.extract(article) ?: art

        return art
    }

    /**
     * Main method for operations on {@link com.fpoon.readability.resource.Article Article}
     *
     * @param article
     * @return
     */
    abstract Article doExtract(Article article)
}
