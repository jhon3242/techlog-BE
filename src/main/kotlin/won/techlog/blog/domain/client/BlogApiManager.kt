package won.techlog.blog.domain.client

import org.springframework.stereotype.Service
import won.techlog.blog.domain.Blog
import won.techlog.blog.domain.BlogType

@Service
class BlogApiManager(
    private val clients: List<FetchClient>
) {
    private val clientMap: Map<BlogType, FetchClient> = clients.associateBy { it.supportType()}

    fun canHandle(url: String): Boolean {
        val blogType = BlogType.getByUrl(url)
        return clientMap.containsKey(blogType)
    }

    fun fetchBlogs(url: String): List<Blog> {
        TODO()
    }
}
