package won.techlog.blog.domain.client.infrastructure.woowabro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class WoowabroWebClient(
    private val woowabroBlogWebClient: WebClient
): FetchClient {
    private val startIdx = 1
    private val endIdx = 2
//    private val endIdx = 46
    override suspend fun fetchBlog(uri: String): BlogMetaData {
        val html = woowabroBlogWebClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(String::class.java)
            .awaitSingle()
        return parseBlogMetaData(html)
    }

    private fun parseBlogMetaData(html: String): BlogMetaData {
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("meta[property=og:title]")?.attr("content") ?: "제목 없음"
        val thumbnail = doc.selectFirst("meta[property=og:image]")?.attr("content")?.takeIf { it.isNotBlank() }
        val content = doc.selectFirst("meta[property=og:description]")?.attr("content") ?: "본문 없음"
        val url = doc.selectFirst("meta[property=og:url]")?.attr("content") ?: ""
        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnail,
            content = content,
            url = url
        )
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> = withContext(Dispatchers.IO) {
        val deferreds = (startIdx..endIdx).map { idx ->
            async {
                val html = woowabroBlogWebClient.get()
                    .uri("/?paged=${idx}")
                    .retrieve()
                    .bodyToMono(String::class.java)
                    .awaitSingle()
                val doc = Jsoup.parse(html)
                doc.select("body > div.content.vuejs > div.content-wrap > div.page-main > " +
                        "div.post-main > div.post-list > div > a")
                    .map { it.absUrl("href") }
                    .map { it.substringAfterLast("/") }
                    .map { fetchBlog(it) }
            }
        }
        deferreds.awaitAll().flatten()
    }

    override fun supportType(): BlogType {
        return BlogType.WOOWABRO
    }
}
