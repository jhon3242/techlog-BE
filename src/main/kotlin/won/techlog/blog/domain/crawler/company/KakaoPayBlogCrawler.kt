package won.techlog.blog.domain.crawler.company

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.blog.domain.crawler.BlogCrawler
import won.techlog.common.TimeProvider

@Component
class KakaoPayBlogCrawler : BlogCrawler {
    private val BASE_URL = "https://tech.kakaopay.com/page/"
    private val startIdx = 1
    private val endIdx = 28

    override fun crawlBlogs(url: String): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true)
                )
            val page = browser.newPage()
            page.navigate(url)
            page.waitForLoadState(LoadState.NETWORKIDLE)

            val urls: List<String> =
                page.locator("div._postList_1cl5f_34 ul > li > a")
                    .evaluateAll("nodes => nodes.map(n => n.href)")
                    as List<String>

            val list =
                urls.map { extractBlogMetaData(page, it) }
                    .toList()
            result.addAll(list)
        }
        return result
    }

    override fun crawlBlogs(blogType: BlogType): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        for (i in startIdx..endIdx) {
            val url = "${BASE_URL}/$i"
            val blogMetaDataList = crawlBlogs(url)
            result.addAll(blogMetaDataList)
        }
        return result
    }

    override fun crawlBlog(url: String): BlogMetaData {
        var result: BlogMetaData? = null
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true)
                )

            val page = browser.newPage()

            result = extractBlogMetaData(page, url)

            browser.close()
        }

        return result
            ?: throw IllegalArgumentException("파싱 과정에서 에러가 발생했습니다.")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.KAKAO_PAY
    }

    private fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData {
        // 페이지 이동
        page.navigate(url)

        // 페이지 로드 기다리기 (옵션)
        page.waitForLoadState(LoadState.NETWORKIDLE)

        val html = page.content()
        val doc = Jsoup.parse(html)

        val title =
            doc.selectFirst("meta[property='og:title']")
                .attr("content")
                .split("|")
                .first()
        val content =
            doc.selectFirst("meta[property='og:description']")
                .attr("content")
                .take(300)

        val thumbnail =
            doc.selectFirst("meta[property='og:image']")
                .attr("content")

        val publishedAtStr =
            doc.selectFirst("time") // 2025. 5. 23
                .text()

        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnail,
            content = content,
            url = url,
            publishedAt = TimeProvider.parseByString(publishedAtStr, BlogType.KAKAO_PAY)
        )
    }
}
