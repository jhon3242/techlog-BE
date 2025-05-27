package won.techlog.blog.domain

import org.springframework.stereotype.Service
import won.techlog.blog.domain.client.BlogApiManager
import won.techlog.blog.domain.crawler.BlogCrawlerManager
import won.techlog.blog.domain.recommendation.BlogRecommendation
import won.techlog.blog.domain.recommendation.BlogRecommendationDao

@Service
class BlogService(
    private val blogCrawlerManager: BlogCrawlerManager,
    private val blogApiManager: BlogApiManager,
    private val blogRecommendationDao: BlogRecommendationDao
) {
    fun parseBlog(url: String): Blog = blogCrawlerManager.crawlBlog(url)

    fun parseBlogs(url: String): List<Blog> = blogCrawlerManager.crawlBlogs(url)

    fun saveBlogRecommendation(url: String) = blogRecommendationDao.save(url)

    fun findAllBlogRecommendations(): List<BlogRecommendation> = blogRecommendationDao.findAll()

    fun deleteBlogRecommendation(id: Long) = blogRecommendationDao.delete(id)

    suspend fun fetchBlogs(blogType: String) {
        val findBlogType = BlogType.valueOf(blogType)
        if (blogApiManager.canHandle(findBlogType)) {
            blogApiManager.fetchBlogs(findBlogType)
            return
        }
        if (blogCrawlerManager.canHandle(findBlogType)) {
            blogCrawlerManager.fetchBlogs(findBlogType)
            return
        }
    }

    suspend fun fetchBlog(url: String) {
        val blogType = BlogType.getByUrl(url)
        if (blogApiManager.canHandle(blogType)) {
            blogApiManager.fetchBlog(url)
            return
        }
        if (blogCrawlerManager.canHandle(blogType)) {
            blogCrawlerManager.fetchBlog(url)
            return
        }
    }

    fun fetchBlogsByUrl(url: String) {
        blogCrawlerManager.fetchBlogs(url)
    }

    fun fetchBlogByUrl(url: String) {
        blogCrawlerManager.fetchBlog(url)
    }
}
