package won.techlog.blog.domain.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.ExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Configuration
class WebClientConfig {
    @Bean
    fun naverBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://d2.naver.com/api/v1/contents")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun kakaoBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://tech.kakao.com/api/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun lineOleBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://engineering.linecorp.com/page-data/ko/blog")
            .codecs { it.defaultCodecs().maxInMemorySize(5 * 1024 * 1024) }
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun lineBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://techblog.lycorp.co.jp/page-data/ko")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun woowabroBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://techblog.woowahan.com")
            .filter(logRequest())
            .defaultHeader("Accept", MediaType.TEXT_HTML_VALUE)
            .build()

    @Bean
    fun tossBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl(
            "https://api-public.toss.im/api-public/v3/ipd-thor/api/v1/workspaces/15/posts?size=999&categoriesSlug=tech"
        )
            .codecs { it.defaultCodecs().maxInMemorySize(5 * 1024 * 1024) }
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun daangnBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://medium.com/_/api/collections/4505f82a2dbd/stream?to=1744697694782")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun rssWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://medium.com/feed/daangn")
            .build()

    @Bean
    fun nhnBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://meetup.nhncloud.com/tcblog/v1.0/posts")
            .build()

    fun logRequest(): ExchangeFilterFunction =
        ExchangeFilterFunction.ofRequestProcessor { clientRequest ->
            println("▶ Request: ${clientRequest.method()} ${clientRequest.url()}")
            Mono.just(clientRequest)
        }
}
