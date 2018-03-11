package com.fpoon.readability.extractor

import com.fpoon.readability.resource.Article
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.nodes.Node
import org.jsoup.select.NodeVisitor

/**
 * Extracts content from article
 */
class ContentExtractor extends Extractor {

    Article doExtract(Article article) {
        Document doc = article.document

        def nodes = scoreNodes(article) // Map to hold score for each element

        nodes.keySet().each {n ->
            println "${n.nodeName()}: ${nodes[n]}"
        }

        def max = nodes.keySet().max { nodes[it] }

        println "Max candidate (score ${nodes[max]}):\n\n${max}"

        return article;
    }

    def scoreNodes(Article article) {
        def nodes = [:]

        article.document.body().traverse(new NodeVisitor() {
            @Override
            void head(Node node, int i) {
                if (node instanceof Element) {
                    println "${" " * i}entering ${node.nodeName()}"
                    scoreElement((Element) node, nodes);
                }
            }

            @Override
            void tail(Node node, int i) {
                if (node instanceof Element) {
                    println "${" " * i}exiting ${node.nodeName()}"
                }
            }
        })

        return nodes
    }

    def scoreElement(Element e, def nodes) {
        if (e.text().size() < 25)
            return null

        def score = nodes[e] ?: 0

        score += e.text().count(',')

        score += Math.min((int) (e.text().size() / 100), 3)

        nodes[e] = score

        if (e.parent() && e.tagName() != 'body') {
            def parentScore = nodes[e.parent()] ?: 0
            nodes[e.parent()] = parentScore + score
        }

        if (e.parent()?.parent() && e.tagName() != 'body' && e.parent()?.tagName() != 'body') {
            def grandparentScore = nodes[e.parent()?.parent()] ?: 0
            nodes[e.parent()?.parent()] = grandparentScore + score / 2
        }
    }
}
