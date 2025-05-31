package won.techlog.blog.domain.crawler

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Component
import won.techlog.blog.domain.Blog
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.poster.domain.Poster
import won.techlog.poster.domain.PosterDao
import won.techlog.poster.exception.NotFoundException

@Component
class BlogCrawlerManager(
    private val crawlers: List<BlogCrawler>,
    private val posterDao: PosterDao
) {
    private val crawlerMap: Map<BlogType, BlogCrawler> = initMap()
    private val log = KotlinLogging.logger { }

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

    fun fetchBlogs(url: String) {
        val blogType = BlogType.getByUrl(url)
        val crawler = findCrawler(blogType)
        val posters: List<BlogMetaData> = crawler.crawlBlogs(url)
        posters.map { Poster(blogType = blogType, blogMetaData = it) }
            .let { posterDao.savePosters(it) }
        log.info { "${posters.size}개 저장 완료, url=$url" }
    }

    fun fetchBlogs(blogType: BlogType) {
        val crawler = findCrawler(blogType)
        val posters: List<BlogMetaData> = crawler.crawlBlogs()
        posters.map { Poster(blogType = blogType, blogMetaData = it) }
            .let { posterDao.savePosters(it) }
        log.info { "${posters.size}개 저장 완료, blogType=$blogType" }
    }

    fun fetchBlog(url: String) {
        val blogType = BlogType.getByUrl(url)
        val crawler = findCrawler(blogType)
        Poster(blogType = blogType, blogMetaData = crawler.crawlBlog(url))
            .let { posterDao.save(it) }
    }

    fun canHandle(blogType: BlogType): Boolean {
        return crawlerMap.containsKey(blogType)
    }

    private fun initMap(): Map<BlogType, BlogCrawler> {
        val result = mutableMapOf<BlogType, BlogCrawler>()
        crawlers.forEach { crawler ->
            val blogType =
                BlogType.entries
                    .find { crawler.isSupportType(it) }
                    ?: throw IllegalArgumentException("일치하는 블로그 타입이 없습니다.")
            result[blogType] = crawler
        }
        return result.toMap()
    }

    private fun findCrawler(blogType: BlogType): BlogCrawler {
        return crawlers.find { it.isSupportType(blogType) }
            ?: throw NotFoundException()
    }
}
