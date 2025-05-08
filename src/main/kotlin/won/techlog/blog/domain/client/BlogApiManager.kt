package won.techlog.blog.domain.client

import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient

@Service
class BlogApiManager(
    private val clientMap: Map<String, WebClient>
)
