package won.techlog.blog.domain.crawler

import won.techlog.blog.domain.BlogMetaData

interface BlogCrawler {
    fun crawlBlogs(url: String): List<BlogMetaData>

    fun crawlBlog(url: String): BlogMetaData
}
