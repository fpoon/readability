package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import org.jsoup.select.Elements

/**
 * Extracts title from article
 */
class TitleExtractor extends Extractor {
    Article doExtract(Article article) {

        def title = getElementWithTitleId(article) ?: // First, try to get element with id="title"
                getH1(article) ?:                     // If there is none, try to find H1 tag
                getDocumentTitle(article)             // As last resort get page title

        System.err << "Found title: ${title}\n"

        article.title = title;

        return article
    }

    private def getDocumentTitle(Article article) {
        return article.document.title()
    }

    private def getElementWithTitleId(Article article) {
        return article.document.getElementById("title")?.text()
    }

    private def getH1(Article article) {
        Elements els =  article.document.getElementsByTag('h1')
        return els.size()? els.get(0).text() : null
    }
}
