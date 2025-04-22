package won.techlog.blog.domain

interface BlogParser {

    fun parseBlogs(url: String): List<BlogMetaData>

    fun parseBlog(url: String): BlogMetaData
}
