package won.techlog.blog.domain.client.infrastructure.line

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class LineWebClient(
    private val lineBlogWebClient: WebClient
) : FetchClient {
    private val startIdx = 1
    private val endIdx = 9

    override suspend fun fetchBlog(slug: String): BlogMetaData {
        val uri = "/$slug/page-data.json"
        return lineBlogWebClient.get()
            .uri(uri)
            .retrieve()
            .bodyToMono(LineBlogContentResponse::class.java)
            .awaitSingle()
            .getBlogMetaData()
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> =
        withContext(Dispatchers.IO) {
            val deferreds =
                (startIdx..endIdx).map { idx ->
                    async {
                        val url = getUrl(idx)
                        lineBlogWebClient.get()
                            .uri(url)
                            .retrieve()
                            .bodyToMono(LineBlogContentsResponse::class.java)
                            .awaitSingle()
                            .getSlugs()
                            .map { fetchBlog(it) }
                    }
                }
            deferreds.awaitAll().flatten()
        }

    private fun getUrl(idx: Int): String {
        if (idx == startIdx) {
            return "/page-data.json"
        }
        return "/page/$idx/page-data.json"
    }

    override fun supportType(): BlogType {
        return BlogType.LINE
    }
}
