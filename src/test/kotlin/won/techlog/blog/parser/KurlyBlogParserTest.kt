package won.techlog.blog.parser

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import won.techlog.blog.domain.crawler.company.KurlyBlogCrawler
import won.techlog.support.BaseServiceTest

class KurlyBlogParserTest : BaseServiceTest() {
    @Autowired
    lateinit var kurlyBlogCrawler: KurlyBlogCrawler

    @Test
    fun `블로그들을 파싱한다`() {
        // when
        kurlyBlogCrawler.crawlBlogs()

        // then
    }
}
