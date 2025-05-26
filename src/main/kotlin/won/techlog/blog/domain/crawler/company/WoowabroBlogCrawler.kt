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

@Component
class WoowabroBlogCrawler : BlogCrawler {
    companion object {
        private const val BASE_URL = "https://techblog.woowahan.com"
        private const val INVALID_BASE_URL = "https://techblog.woowa.in"
    }

    override fun crawlBlogs(url: String): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true).setTimeout(60000.0)
                )
            val page = browser.newPage()
            page.navigate(url)
            page.waitForLoadState(LoadState.NETWORKIDLE)
            page.setDefaultTimeout(60000.0) // 60초
            val doc = Jsoup.parse(page.content())
            println("이것이 HTML이다 = $doc")
            TODO()
        }
        return result
    }

    override fun crawlBlog(url: String): BlogMetaData {
        var result: BlogMetaData? = null
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true).setTimeout(60000.0)
                )

            val page = browser.newPage()
            page.setExtraHTTPHeaders(
                mapOf(
                    "User-Agent" to "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) A" +
                        "ppleWebKit/537.36 (KHTML, like Gecko) Chrome/122.0.0.0 Safari/537.36"
                )
            )
            page.setDefaultTimeout(60000.0) // 60초
            result = extractBlogMetaData(page, url)

            browser.close()
        }

        return result
            ?: throw IllegalArgumentException("파싱 과정에서 에러가 발생했습니다.")
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return blogType == BlogType.WOOWABRO
    }

    private fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData {
        // 페이지 이동
        page.navigate(url)

        // 페이지 로드 기다리기 (옵션)
        page.waitForLoadState(LoadState.NETWORKIDLE)
        page.setDefaultTimeout(60000.0) // 60초

        val html = page.content()
        val doc = Jsoup.parse(html)

        val title =
            doc.select("h1")[1].text()
                ?: doc.select("meta[property=og:title]").attr("content")
                    .ifBlank { doc.title() }

        // 썸네일: 본문 내 첫 번째 이미지
        val rawImageUrl = doc.selectFirst("meta[property=og:image]")?.attr("content")
        val thumbnailUrl = normalizeImageUrl(rawImageUrl)

        val content =
            doc.select("div.post-content p")
                .take(3) // 앞 2문단 추출
                .joinToString(" ") { it.text() }

        TODO()
//        return BlogMetaData(title = title, thumbnailUrl = thumbnailUrl, content = content, url = url)
    }

    private fun normalizeImageUrl(imageUrl: String?): String? =
        if (imageUrl != null && imageUrl.startsWith(INVALID_BASE_URL)) {
            BASE_URL + imageUrl.substringAfterLast(INVALID_BASE_URL)
        } else {
            imageUrl
        }
}
