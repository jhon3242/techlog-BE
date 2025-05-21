package won.techlog.blog.domain.relay

import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.poster.api.request.PosterCreateRequest
import won.techlog.poster.api.request.PostersCreateRequest

@Component
class RemoteServerManager(
    private val remoteServerClient: WebClient
) {
    fun saveBlogs(
        blogs: List<BlogMetaData>,
        blogType: BlogType
    ) {
        val request =
            blogs.map { PosterCreateRequest(blogMetaData = it, blogType = blogType) }
                .let { PostersCreateRequest(it) }
        remoteServerClient.post()
            .uri("/api/posters")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), PostersCreateRequest::class.java) // ← 수정된 부분
            .retrieve()
            .bodyToMono<Unit>()
            .block()
    }
}
