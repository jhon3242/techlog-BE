package won.techlog.blog.domain.crawler.rss

import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class RssCrawler(
    private val rssWebClient: WebClient
) {
    private val mapper =
        XmlMapper().apply {
            registerKotlinModule()
        }

    fun fetchPosts(feedUrl: String): List<RssItem> {
        val rawXml =
            rssWebClient.get()
                .uri(feedUrl)
                .retrieve()
                .bodyToMono(String::class.java)
                .block() ?: throw RuntimeException("Failed to fetch feed")

        val cleanedXml = rawXml.filter { it.code >= 32 || it == '\n' || it == '\r' || it == '\t' }

        val rss = mapper.readValue(cleanedXml, RssFeed::class.java)
        return rss.channel.items
    }
}
