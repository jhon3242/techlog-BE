package won.techlog.blog.domain.crawler

import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType

interface BlogCrawler {
    fun crawlBlogs(url: String): List<BlogMetaData>

    fun crawlBlogs(): List<BlogMetaData>

    fun crawlBlog(url: String): BlogMetaData

    fun isSupportType(blogType: BlogType): Boolean
}
