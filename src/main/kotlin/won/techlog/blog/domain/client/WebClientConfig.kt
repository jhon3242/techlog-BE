package won.techlog.blog.domain.client

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

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
        builder.baseUrl("https://techblog.woowahan.com/")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
}
