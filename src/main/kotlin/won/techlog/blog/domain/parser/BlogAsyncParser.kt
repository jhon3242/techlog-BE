package won.techlog.blog.domain.parser

import won.techlog.blog.domain.BlogMetaData

interface BlogAsyncParser {
    suspend fun parseBlogs(url: String): List<BlogMetaData>

    suspend fun parseBlog(url: String): BlogMetaData
}
