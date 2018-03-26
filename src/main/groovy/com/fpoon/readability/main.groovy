package com.fpoon.readability

import com.fpoon.readability.extractor.ArticleExtractor
import com.fpoon.readability.resource.Article
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import groovyx.net.http.*
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import groovyx.net.http.*
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import groovyx.net.http.*
@Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.7.1' )
import groovyx.net.http.*
import org.jsoup.Jsoup

import static groovyx.net.http.ContentType.JSON
import static groovyx.net.http.Method.POST

def cli = new CliBuilder(usage: 'readability [options] <url>')
cli.h(longOpt: 'help', 'Print his message')
cli.u(longOpt: 'user', args: 1, 'Username to log in');
cli.p(longOpt: 'password', args: 1, 'Password to log in')
cli._(longOpt: 'url', args: 1, 'Url of JayBB forum')

def options = cli.parse(this.args)
if (options.h || !options.arguments()) {
    cli.usage()
    return
}

url = options.arguments()[0]
user = options.u
password = options.p
token = "${user}:${password}".bytes.encodeBase64()
extractor = new ArticleExtractor()


getFeedsList().each {
    def forumId = it.forumId

    def pubs = getPublishedList(it.forumId)
    println "${pubs.size} articles published in forum ${it.forumId}"
    def pubsUrl = pubs.collect {it.link}

    def rss = new XmlSlurper().parse(it.link)
    println "Reading news from ${it.link} - ${rss.channel.title}"
    def items = rss.channel.item.collect {[title: it.title, link: it.link]}

    items.each {
        if (!it.title)
            return

        if (pubsUrl.contains(it.link)) {
            println "Already published ${it.title} | ${it.link} | Skipping..."
            return
        }

        Article article = new Article(it.link.toURL())
        extractor.extract(article)

        println "Publishing ${it.title} | ${it.link}"

        def bodyMap = [title: "${it.title}".toString(), content: "${article.output}".toString(), url: "${it.link}".toString()]
        def path = "/forum/${forumId}/thread/bot"
        def http = new HTTPBuilder("${url}")
        http.request(POST, JSON) {
            uri.path = path
            body = bodyMap
            headers.'Authorization' = "Basic ${token}"
            response.success = {resp -> println "Article published"}
            response.failure = {
                resp -> println "Failed! ${resp.responseBase}"
            }
        }
    }
}

def getFeedsList() {
    def feeds = []

    println "Downloading feeds from ${url}/forum/feeds, with login '${user}'"

    def rss = Jsoup
            .connect("${url}/forum/feeds")
            .header("Authorization", "Basic ${token}")
            .get()

    def table = rss.select("table")[0].select("tbody")[0]
    table.select("tr").each {
        def row = [:]
        def td = it.select('td')
        row.id = td[0].text()
        row.forumId = td[1].text()
        row.link = td[2].text()

//        println row

        feeds += row
    }

    return feeds
}

def getPublishedList(forumId) {
    def pubs = []

    println "Downloading published list from ${url}/forum/${forumId}/published, with login '${user}'"

    def rss = Jsoup
            .connect("${url}/forum/${forumId}/published")
            .header("Authorization", "Basic ${token}")
            .get()

    def table = rss.select("table")[0].select("tbody")[0]
    table.select("tr").each {
        def row = [:]
        def td = it.select('td')
        row.id = td[0].text()
        row.forumId = td[1].text()
        row.link = td[2].text()
        row.title = td[3].text()
        row.createDate = td[4].text()

//        println row

        pubs += row
//        println it
    }

    return pubs
}
