package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article

abstract class Extractor {

    Extractor next

    Extractor link(Extractor extractor) {
        this.next = extractor
        return extractor
    }

    Article extract(Article article) {
        println("Calling extractor: ${this.class.name}")
        Article art = this.doExtract(article)

        art = next?.extract(article) ?: art

        return art
    }

    abstract Article doExtract(Article article)
}
