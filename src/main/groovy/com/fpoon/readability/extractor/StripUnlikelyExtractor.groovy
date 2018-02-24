package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

/**
 * Removes unlikely candidates
 */
class StripUnlikelyExtractor extends Extractor {

    String unlikely = /^.*\b(combx|comment|community|disqus|content|extra|foot|header|menu|remark|rss|shoutbox|sidebar|sponsor|ad-break|agegate|pagination|pager|popup|tweet|twitter)\b.*$/

    String skipped = /^.*\b(body|pre|panel-content|panel-header)\b.*$/

    Article doExtract(Article article) {
        def doc = article.document

        Elements elements = doc.body().select("*") // Get all tags from body (including body)

        elements.each {e ->
            if (isSkipped(e)) // Skip over some stuff
                return

            if (isUnlikely(e)) { // If tag is unlikely candidate, remove it from DOM
//                println "Removed ${e.className()} ${e.id()}"
                e.remove()
            }
        }

        return article
    }

    /**
     * Decides if element is unlikely candidate
     * @param e HTML element
     * @return decision
     */
    def isUnlikely(Element e) {
        def str = "${e.className()} ${e.id()}"
        return str ==~ unlikely
    }

    /**
     * Decides if element is skipped
     * @param e HTML element
     * @return
     */
    def isSkipped(Element e) {
        def str = "${e.tagName()} ${e.className()} ${e.id()}"
        return str ==~ skipped
    }
}
