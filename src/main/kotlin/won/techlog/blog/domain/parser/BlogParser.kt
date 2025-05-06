package won.techlog.blog.domain.parser

import won.techlog.blog.domain.BlogMetaData

interface BlogParser {
    fun parseBlogs(url: String): List<BlogMetaData>

    fun parseBlog(url: String): BlogMetaData
}
