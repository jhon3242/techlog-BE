package won.techlog.blog.domain.client.infrastructure.woowabro

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

private const val INVALID_URL_PREFIX = "https://techblog.woowa.in"

//@Component  TODO 로컬에서만 동작
class WoowabroWebClient(
    private val woowabroBlogWebClient: WebClient
) : FetchClient {
    private val startIdx = 1

//    private val endIdx = 3
    private val endIdx = 46

    override suspend fun fetchBlog(uri: String): BlogMetaData {
        val html =
            woowabroBlogWebClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String::class.java)
                .awaitSingle()
        return parseBlogMetaData(html)
    }

    private fun parseBlogMetaData(html: String): BlogMetaData {
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("meta[property=og:title]")?.attr("content") ?: "제목 없음"
        val thumbnail = getThumbnail(doc)
        val content = doc.selectFirst("meta[property=og:description]")?.attr("content") ?: "본문 없음"
        val url = doc.selectFirst("meta[property=og:url]")?.attr("content") ?: ""
        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnail,
            content = content,
            url = url
        )
    }

    private fun getThumbnail(doc: Document): String? {
        val url = doc.selectFirst("meta[property=og:image]")?.attr("content")
        if (url == null) return null
        if (url.startsWith(INVALID_URL_PREFIX)) {
            return "https://techblog.woowahan.com${url.substringAfterLast(INVALID_URL_PREFIX)}"
        }
        return url
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> =
        withContext(Dispatchers.IO) {
            val deferreds =
                (startIdx..endIdx).map { idx ->
                    async {
                        val html =
                            woowabroBlogWebClient.get()
                                .uri(getUriByIdx(idx))
                                .retrieve()
                                .bodyToMono(String::class.java)
                                .awaitSingle()
                        val doc = Jsoup.parse(html)
                        doc.select("div.post-item.firstpaint a")
                            .map { it.absUrl("href") }
                            .map { fetchBlog(it) }
                    }
                }
            deferreds.awaitAll().flatten()
        }

    private fun getUriByIdx(idx: Int): String {
        if (idx == 1) return "/?paged=1"
        return "/page/$idx/?paged=$idx"
    }

    override fun supportType(): BlogType {
        return BlogType.WOOWABRO
    }
}
