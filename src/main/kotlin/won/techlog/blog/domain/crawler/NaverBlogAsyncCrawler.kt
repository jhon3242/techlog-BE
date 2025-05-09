package won.techlog.blog.domain.crawler

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData

@Component
class NaverBlogAsyncCrawler : BlogAsyncCrawler {
    override suspend fun crawlBlogs(url: String): List<BlogMetaData> =
        coroutineScope {
            Playwright.create().use { playwright ->
                val browser =
                    playwright.chromium().launch(
                        BrowserType.LaunchOptions().setHeadless(true)
                    )
                val page = browser.newPage()
                page.navigate(url)
                page.waitForLoadState(LoadState.NETWORKIDLE)
                val urls =
                    page.locator("a.post_txt_wrap")
                        .all()
                        .map { it.getAttribute("href") }
                        .map { "https://d2.naver.com$it" }

                urls.map { url ->
                    async {
                        val newPage = browser.newPage()
                        try {
                            extractBlogMetaData(newPage, url)
                        } finally {
                            newPage.close()
                        }
                    }
                }.awaitAll()
            }
        }

    override suspend fun crawlBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    private suspend fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData {
        page.navigate(url)
        page.waitForLoadState(LoadState.NETWORKIDLE)
        // 페이지 로드 기다리기 (옵션)

        val html = page.content()
        val doc = Jsoup.parse(html)
        val title =
            doc.selectFirst("h1.posting_tit")
                .text() ?: "제목 없음"
        val content =
            doc.selectFirst("div.con_view")
                .text()
                .take(300)
        val thumbnail =
            doc.selectFirst("div.con_view img")
                .attribute("src")
                .value
                .let { "https://d2.naver.com$it" }
        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
