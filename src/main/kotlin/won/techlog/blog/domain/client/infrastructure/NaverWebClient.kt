package won.techlog.blog.domain.client.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class NaverWebClient(
    private val naverBlogWebClient: WebClient
) : FetchClient {
    private val maxPage = 6
    private val size = 100
    override suspend fun fetchBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        withContext(Dispatchers.IO) {
            for (page in 0..maxPage) {
                val response = async { naverBlogWebClient
                    .get()
                    .uri("?page=${page}&size=${size}")
                    .retrieve()
                    .bodyToMono(NaverBlogContentsResponse::class.java)
                    .map { it.content }
                    .awaitSingle() }
                    .await()
                    .map { BlogMetaData(
                        title = it.postTitle,
                        thumbnailUrl = it.postImage?.let { "https://d2.naver.com${it}" },
                        content = it.postHtml,
                        url = it.url
                    ) }
                result.addAll(response)
            }
        }
        return result
    }

    override fun supportType(): BlogType = BlogType.NAVER
}
