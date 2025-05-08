package won.techlog.blog.domain.client.infrastructure

import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class NaverWebClient(
    private val naverBlogWebClient: WebClient
) : FetchClient {
    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val response =
            naverBlogWebClient
                .get()
                .retrieve()
                .bodyToMono(NaverWebClientResponse::class.java)
                .map { it.content }
                .awaitSingle()
        return response.map {
            BlogMetaData(
                title = it.postTitle,
                thumbnailUrl = "https://d2.naver.com${it.postImage}",
                content = it.postHtml,
                url = it.url
            )
        }
    }

    override fun supportType(): BlogType = BlogType.NAVER
}
