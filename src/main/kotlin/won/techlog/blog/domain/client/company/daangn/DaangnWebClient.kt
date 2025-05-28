package won.techlog.blog.domain.client.company.daangn

import com.fasterxml.jackson.databind.ObjectMapper
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class DaangnWebClient(
    private val daangnBlogWebClient: WebClient,
    private val mapper: ObjectMapper
) : FetchClient {
    private val startIdx = 1

//    private val endIdx = 1
    private val endIdx = 5

    override suspend fun fetchBlog(uri: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        return (startIdx..endIdx).map { idx ->
            daangnBlogWebClient.get()
                .uri("?page=$idx")
                .retrieve()
                .bodyToMono(String::class.java)
                .map { it.substringAfter("])}while(1);</x>") }
                .map { mapper.readValue(it, DaangnBlogContentsResponse::class.java) }
                .awaitSingle()
                .getBlogMetaData()
        }.flatten()
    }

    override fun supportType(): BlogType {
        return BlogType.DAANGN
    }
}
