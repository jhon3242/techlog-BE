package won.techlog.blog.domain.relay

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.MediumBlogCrawler

@Component
class YanoljaCloudRelay : Relayable {
    companion object {
        private const val SLUG = "yanoljacloud-tech"
        private val yearList = listOf(2021, 2022)
    }

    override fun getBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        yearList.forEach { year ->
            result.addAll(MediumBlogCrawler.fetchBlog(SLUG, year))
        }
        return result
    }

    override fun getBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.YANOLJA_CLOUD
    }
}
