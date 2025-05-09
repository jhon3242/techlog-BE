package won.techlog.blog.domain.client.infrastructure

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
class LineOldWebClient(
    private val lineOleBlogWebClient: WebClient
): FetchClient {
    private val startIdx = 1
    private val endIdx = 2
//    private val endIdx = 38
    override suspend fun fetchBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> = withContext(Dispatchers.IO) {
        val deferreds = (startIdx..endIdx).map { idx ->
            async {
                val url = getUrl(idx)
                val response = lineOleBlogWebClient.get()
                    .uri(url)
                    .retrieve()
                    .bodyToMono(LineOldBlogContentsResponse::class.java)
                    .map { it.getBlogMetaData() }
                    .awaitSingle()
                response  // List<BlogMetaData>
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
        return BlogType.LINE_OLD
    }
}
