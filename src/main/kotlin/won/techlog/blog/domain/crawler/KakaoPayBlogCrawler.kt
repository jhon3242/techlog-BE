package won.techlog.blog.domain.crawler

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData

@Component
class KakaoPayBlogCrawler : BlogCrawler {
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

    private fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData {
        // 페이지 이동
        page.navigate(url)

        // 페이지 로드 기다리기 (옵션)
        page.waitForLoadState(LoadState.NETWORKIDLE)

        val title =
            page.locator("head meta[property='og:title']")
                .getAttribute("content")
                .split("|")
                .first()
        val content =
            page.locator("head meta[property='og:description']")
                .getAttribute("content")
                .take(300)

        val thumbnail =
            page.locator("article img")
                .first()
                .getAttribute("src")
                .ifBlank { null }
                .let { "https://tech.kakaopay.com$it" }
        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
