package won.techlog.blog.domain.crawler

import org.springframework.stereotype.Component
import won.techlog.blog.domain.Blog
import won.techlog.blog.domain.BlogType
import won.techlog.poster.exception.NotFoundException

@Component
class BlogCrawlerManager(
    private val crawlers: List<BlogCrawler>
) {
    fun crawlBlog(url: String): Blog {
        val blogType = BlogType.getByUrl(url)
        val crawler = findCrawler(blogType)
        val blogMetaData = crawler.crawlBlog(url)
        return Blog(blogType, blogMetaData)
    }

    fun crawlBlogs(url: String): List<Blog> {
        val blogType = BlogType.getByUrl(url)
        val crawler = findCrawler(blogType)
        return crawler.crawlBlogs(url)
            .map { Blog(blogType, it) }
    }

    private fun findCrawler(blogType: BlogType): BlogCrawler {
        return crawlers.find { it.isSupportType(blogType) }
            ?: throw NotFoundException()
    }
}
