package won.techlog.blog.domain.parser

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData

@Component
class NaverBlogParser : BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true)
                )
            val page = browser.newPage()
            page.navigate(url)
            page.waitForLoadState(LoadState.NETWORKIDLE)

            val list =
                page.locator("a.post_txt_wrap")
                    .all()
                    .map { it.getAttribute("href") }
                    .map { "https://d2.naver.com$it" }
                    .map { url -> extractBlogMetaData(page, url) }
            result.addAll(list)
        }
        return result
    }

    override fun parseBlog(url: String): BlogMetaData {
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

        val title = page.textContent("h1.posting_tit") ?: "제목 없음"
        val content = page.textContent("div.con_view").take(300) ?: "본문 없음"
        val thumbnail =
            page.locator("div.con_view img")
                .first()
                .getAttribute("src")
                .ifBlank { null }
                .let { "https://d2.naver.com$it" }
        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
