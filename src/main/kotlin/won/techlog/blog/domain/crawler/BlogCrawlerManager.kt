package won.techlog.blog.domain.crawler

import org.springframework.stereotype.Component
import won.techlog.blog.domain.Blog
import won.techlog.blog.domain.BlogType
import won.techlog.poster.exception.NotFoundException

@Component
class BlogCrawlerManager(
    private val crawlerMap: Map<String, BlogCrawler>
) {
    fun crawlBlog(url: String): Blog {
        val blogType = BlogType.getByUrl(url)
        val crawler = findParser(blogType)
        val blogMetaData = crawler.crawlBlog(url)
        return Blog(blogType, blogMetaData)
    }

    fun crawlBlogs(url: String): List<Blog> {
        val blogType = BlogType.getByUrl(url)
        val crawler = findParser(blogType)
        return crawler.crawlBlogs(url)
            .map { Blog(blogType, it) }
    }

    suspend fun crawlBlogsAsync(url: String): List<Blog> {
        val blogType = BlogType.getByUrl(url)
        val crawler = findParser(blogType)
        return crawler.crawlBlogs(url)
            .map { Blog(blogType, it) }
    }

    private fun findParser(blogType: BlogType): BlogCrawler {
        return crawlerMap.get(blogType.beanName)
            ?: throw NotFoundException()
    }
}
