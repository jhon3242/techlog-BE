package won.techlog.blog.domain.crawler

import won.techlog.blog.domain.BlogMetaData

interface BlogAsyncCrawler {
    suspend fun crawlBlogs(url: String): List<BlogMetaData>

    suspend fun crawlBlog(url: String): BlogMetaData
}
