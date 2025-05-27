package won.techlog.blog.domain.relay

import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.company.KakaoPayBlogCrawler

@Component
class KakaoPayRelay(
    private val kakaoPayBlogCrawler: KakaoPayBlogCrawler
) : Relayable {
    override fun getBlogs(): List<BlogMetaData> {
        return kakaoPayBlogCrawler.crawlBlogs(BlogType.KAKAO_PAY)
    }

    override fun getBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.KAKAO_PAY
    }
}
