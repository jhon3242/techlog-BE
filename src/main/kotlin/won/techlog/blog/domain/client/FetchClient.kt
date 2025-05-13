package won.techlog.blog.domain.client

import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType

interface FetchClient {
    suspend fun fetchBlog(uri: String): BlogMetaData

    suspend fun fetchBlogs(): List<BlogMetaData>

    fun supportType(): BlogType
}
