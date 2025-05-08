package won.techlog.blog.domain.client.infrastructure

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun naverBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://d2.naver.com/api/v1/contents?size=20")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()

    @Bean
    fun kakaoBlogWebClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl("https://tech.kakao.com/api/v1")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .build()
}
