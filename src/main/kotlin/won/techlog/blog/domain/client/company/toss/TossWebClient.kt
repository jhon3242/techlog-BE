package won.techlog.blog.domain.client.company.toss

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class TossWebClient(
    private val tossBlogWebClient: WebClient
) : FetchClient {
    override suspend fun fetchBlog(uri: String): BlogMetaData {
        TODO()
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val response =
            tossBlogWebClient.get()
                .retrieve()
                .bodyToMono(TossBlogContentsResponse::class.java)
                .awaitSingle()
        return response.getBlogMetaDataList()
    }

    override fun supportType(): BlogType = BlogType.TOSS
}
