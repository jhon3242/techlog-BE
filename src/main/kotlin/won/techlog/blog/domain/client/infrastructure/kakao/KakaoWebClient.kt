package won.techlog.blog.domain.client.infrastructure.kakao

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.client.FetchClient

@Component
class KakaoWebClient(
    private val kakaoBlogWebClient: WebClient
) : FetchClient {
    private val startIdx = 306
    private val endIdx = 702
    private val exceptTags =
        mutableSetOf(
            "internship",
            "new-krew", "recruitment", "developer relations",
            "event", "internship", "blind-recruitment", "career", "seminar", "code-festival", "programming-contest"
        )

    override suspend fun fetchBlog(uri: String): BlogMetaData =
        kakaoBlogWebClient.get()
            .uri(uri)
            .retrieve()
            .onStatus({ it.is4xxClientError }, { resp -> // ★ 4xx 에러 처리
                Mono.error(IllegalArgumentException("Client Error: ${resp.statusCode()} url=$uri"))
            })
            .onStatus({ it.is5xxServerError }, { resp -> // ★ 5xx 에러 처리
                Mono.error(IllegalArgumentException("Server Error: ${resp.statusCode()} url$uri"))
            })
            .bodyToMono(KakaoBlogContentResponse::class.java)
            .filter { res -> validTags(res.tags) }
            .switchIfEmpty(Mono.error(IllegalArgumentException("제외되는 태그를 포함하고 있습니다.")))
            .map {
                BlogMetaData(
                    title = it.title,
                    thumbnailUrl = it.thumbnailUri,
                    content = Jsoup.parse(it.content).select("p").text().take(300),
                    url = "https://tech.kakao.com/posts/${it.id}"
                )
            }
            .awaitSingle()

    private fun validTags(tags: List<Tag>?): Boolean {
        if (tags.isNullOrEmpty()) {
            return true
        }
        val tagNames = tags.map { it.name }
        return tagNames.none { exceptTags.contains(it) }
    }

    override suspend fun fetchBlogs(): List<BlogMetaData> {
        val baseUrl = "https://tech.kakao.com/api/v1/posts/"
        val result = mutableListOf<BlogMetaData>()
        withContext(Dispatchers.IO) {
            supervisorScope {
                val deferreds =
                    (startIdx..endIdx).map { idx ->
                        async {
                            runCatching { fetchBlog("$baseUrl$idx") } // 예외는 Result.failure에 보관
                        }
                    }
                deferreds.awaitAll().forEachIndexed { offset, resultWrap ->
                    resultWrap
                        .onSuccess { blog ->
                            synchronized(result) { result.add(blog) }
                        }
                        .onFailure { e ->
                            println("Failed idx=${startIdx + offset}: $e")
                        }
                }
            }
        }
        return result
    }

    override fun supportType(): BlogType = BlogType.KAKAO
}
