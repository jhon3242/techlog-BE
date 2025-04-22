package won.techlog.blog.domain

import org.springframework.stereotype.Component
import won.techlog.poster.exception.NotFoundException

@Component
class BlogParserManager(
    private val parserMap: Map<String, BlogParser>
) {
    fun parseBlog(url: String): Blog {
        val blogType = BlogType.getByUrl(url)
        val parser = findParser(blogType)
        val blogMetaData = parser.parseBlog(url)
        return Blog(blogType, blogMetaData)
    }

    fun parseBlogs(url: String): List<Blog> {
        val blogType = BlogType.getByUrl(url)
        val parser = findParser(blogType)
        return parser.parseBlogs(url)
            .map { Blog(blogType, it) }
    }

    private fun findParser(blogType: BlogType): BlogParser {
        return parserMap.get(blogType.beanName)
            ?: throw NotFoundException()
    }
}
