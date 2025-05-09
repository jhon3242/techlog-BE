package won.techlog.blog.domain.client.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.core.codec.DecodingException
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class KakaoWebClient(
    private val kakaoBlogWebClient: WebClient
): FetchClient {
    private val startIdx = 306
    private val endIdx = 702

    override suspend fun fetchBlog(url: String): BlogMetaData =
        kakaoBlogWebClient.get()
            .uri(url)
            .retrieve()
            .onStatus({ it.is4xxClientError }, { resp ->                                             // ★ 4xx 에러 처리
                Mono.error(IllegalArgumentException("Client Error: ${resp.statusCode()} url=${url}"))
            })
            .onStatus({ it.is5xxServerError }, { resp ->                                             // ★ 5xx 에러 처리
                Mono.error(IllegalArgumentException("Server Error: ${resp.statusCode()} url${url}"))
            })
            .bodyToMono(KakaoBlogContentResponse::class.java)
            .map {
                BlogMetaData(
                    title = it.title,
                    thumbnailUrl = it.thumbnailUri,
                    content = Jsoup.parse(it.content).select("p").text().take(300),
                    url = "https://tech.kakao.com/posts/${it.id}"
                )
            }
            .awaitSingle()


    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val baseUrl = "https://tech.kakao.com/api/v1/posts/"
        val result = mutableListOf<BlogMetaData>()
        withContext(Dispatchers.IO) {
            for (idx in startIdx..endIdx) {
                try {
                    result.add(async { fetchBlog("${baseUrl}${idx}") }
                        .await()
                    )
                } catch (e: DecodingException) {
                    println(e)
                }
            }
        }
        return result
    }

    override fun supportType(): BlogType = BlogType.KAKAO
}
