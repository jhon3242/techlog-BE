package won.techlog.blog.domain.relay

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

private const val X_ADMIN_HEADER = "X-Admin-Header"
private const val REMOTE_SERVER_HOST = "https://techlog.p-e.kr"

@Configuration
class RemoteServerConfig(
    @Value("\${admin.header}")
    private val adminHeader: String
) {
    @Bean
    fun remoteServerClient(builder: WebClient.Builder): WebClient =
        builder.baseUrl(REMOTE_SERVER_HOST)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .defaultHeader(X_ADMIN_HEADER, adminHeader)
            .build()
}
