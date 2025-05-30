package won.techlog.blog.domain.client.company.nhn

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class NhnWebClient(
    private val nhnBlogWebClient: WebClient
): FetchClient {
    override suspend fun fetchBlog(uri: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val response = nhnBlogWebClient.get()
            .uri("?pageNo=1&rowsPerPage=100")
            .retrieve()
            .bodyToMono(NhnBlogContentsResponse::class.java)
            .awaitSingle()
        return response.getBlogMetaData()
    }

    override fun supportType(): BlogType {
        return BlogType.NHN
    }
}
