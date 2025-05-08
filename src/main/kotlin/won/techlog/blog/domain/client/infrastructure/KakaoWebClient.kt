package won.techlog.blog.domain.client.infrastructure

import kotlinx.coroutines.reactor.awaitSingle
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class KakaoWebClient(
    private val kakaoBlogWebClient: WebClient
): FetchClient {
    override suspend fun fetchBlog(url: String): BlogMetaData =
        kakaoBlogWebClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(KakaoBlogContentResponse::class.java)
            .map {
                BlogMetaData(
                    title = it.title,
                    thumbnailUrl = it.thumbnailUri,
                    content = Jsoup.parse(it.content).select("p").text().take(300),
                    url = url
                )
            }
            .awaitSingle()


    override suspend fun fetchBlogs(): List<BlogMetaData> {
        TODO()
    }

    override fun supportType(): BlogType = BlogType.KAKAO
}
