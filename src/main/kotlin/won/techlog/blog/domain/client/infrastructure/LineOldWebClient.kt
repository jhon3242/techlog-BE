package won.techlog.blog.domain.client.infrastructure

import kotlinx.coroutines.reactor.awaitSingle
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

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        for (idx in startIdx..endIdx) {
            val url = getUrl(idx)
            val response = lineOleBlogWebClient.get()
                .uri(url)
                .retrieve()
                .bodyToMono(LineOldBlogContentsResponse::class.java)
                .map { it.getBlogMetaData() }
                .awaitSingle()
            result.addAll(response)
        }
        return result
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
