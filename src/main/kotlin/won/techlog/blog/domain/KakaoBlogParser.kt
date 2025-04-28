package won.techlog.blog.domain

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.jsoup.Jsoup
import org.springframework.stereotype.Component

@Component
class KakaoBlogParser : BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(true))
            val page = browser.newPage()
            page.navigate(url)

            page.waitForLoadState(LoadState.DOMCONTENTLOADED)
            page.locator("ul.list_post li").first().waitFor()

            val postUrls = page.locator("a.link_post")
                .all()
                .mapNotNull { it.getAttribute("href") }
                .map { "https://tech.kakao.com$it" }

            for (postUrl in postUrls) {
                val newPage = browser.newPage()
                val blog = extractBlogMetaData(newPage, postUrl)
                result.add(blog)
                newPage.close()
            }
            browser.close()
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

        return result ?: throw IllegalArgumentException("파싱 과정에서 에러가 발생했습니다.")
    }

    private fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData {
        page.navigate(url)
        page.waitForSelector(
            "div.inner_content div.preview",
            Page.WaitForSelectorOptions().setTimeout(10000.0)
        )

        val html = page.content()
        val doc = Jsoup.parse(html)
        val title = doc.selectFirst("h1.tit_post")?.text()?.trim() ?: "제목 없음"
        val fullContent = doc.selectFirst("div.preview")?.text()?.trim()
            ?: doc.selectFirst("div.inner_content")?.text()?.trim()
            ?: "본문 없음"
        val content = if (fullContent.length > 300) fullContent.substring(0, 300) else fullContent
        val thumbnail = doc.selectFirst("meta[property=og:image]")?.attr("content")?.takeIf { it.isNotBlank() }

        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnail,
            content = content,
            url = url
        )
    }
}
