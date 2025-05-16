package won.techlog.blog.domain.crawler

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType

@Component
class WoowabroBlogCrawler : BlogCrawler {
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

            val urls: List<String> =
                page.locator(
                    "body > div.content.vuejs > div.content-wrap > div.page-main > " +
                        "div.post-main > div.post-list > div > a"
                )
                    .evaluateAll("nodes => nodes.map(n => n.href)") as List<String>

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
                    BrowserType.LaunchOptions().setHeadless(true).setTimeout(60000.0)
                )

            val page = browser.newPage()
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

        val title = page.textContent("div.post-header").trim().split("\n").first()
        val content =
            page.textContent("div.post-content-body")
                .trim()
                .take(300)
        val thumbnail =
            page.locator("div.content-single .post-content-body img")
                .first()
                .getAttribute("src")
                .ifBlank { null }
                .let { it?.replace("https://techblog.woowahan.com", "") }
                .let { "https://techblog.woowahan.com$it" }
        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
