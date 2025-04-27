package won.techlog.blog.domain

import org.springframework.stereotype.Service

@Service
class BlogService(
    private val blogParserManager: BlogParserManager
) {
    fun parseBlog(url: String): Blog = blogParserManager.parseBlog(url)

    fun parseBlogs(url: String): List<Blog> = blogParserManager.parseBlogs(url)
}
