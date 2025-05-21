package won.techlog.blog.domain.client.infrastructure.naver

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient
import won.techlog.common.TimeProvider

@Component
class NaverWebClient(
    private val naverBlogWebClient: WebClient
) : FetchClient {
    private val maxPage = 6
    private val size = 100

    override suspend fun fetchBlog(uri: String): BlogMetaData {
        val idx = uri.substringAfterLast("/")
        val response =
            naverBlogWebClient.get()
                .uri("/$idx")
                .retrieve()
                .bodyToMono(NaverBlogContentResponse::class.java)
                .awaitSingle()
        return BlogMetaData(
            title = response.postTitle,
            thumbnailUrl = response.postImage?.let { "https://d2.naver.com$it" },
            content = response.postHtml,
            url = "https://d2.naver.com${response.url}",
            publishedAt = TimeProvider.parseByLong(response.postPublishedAt)
        )
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        withContext(Dispatchers.IO) {
            for (page in 0..maxPage) {
                val response =
                    async {
                        naverBlogWebClient
                            .get()
                            .uri("?page=$page&size=$size")
                            .retrieve()
                            .bodyToMono(NaverBlogContentsResponse::class.java)
                            .map { it.content }
                            .awaitSingle()
                    }
                        .await()
                        .filter { it.url.startsWith("/helloworld") }
                        .map {
                            BlogMetaData(
                                title = it.postTitle,
                                thumbnailUrl = it.postImage?.let { "https://d2.naver.com$it" },
                                content = it.postHtml,
                                url = "https://d2.naver.com${it.url}",
                                publishedAt = TimeProvider.parseByLong(it.postPublishedAt)
                            )
                        }
                result.addAll(response)
            }
        }
        return result
    }

    override fun supportType(): BlogType = BlogType.NAVER
}
