package won.techlog.blog.domain.client

import won.techlog.blog.domain.BlogMetaData

interface FetchClient {
    suspend fun fetchData(): BlogMetaData

    suspend fun fetchBlogs(): List<BlogMetaData>
}
