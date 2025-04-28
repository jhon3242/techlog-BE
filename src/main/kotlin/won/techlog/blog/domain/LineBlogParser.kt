package won.techlog.blog.domain

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class LineBlogParser : BlogParser {
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
                page.locator("a.link.list_item")
                    .all()
                    .map { it.getAttribute("href") }
                    .map { "https://techblog.lycorp.co.jp$it" }
                    .map { url -> parseBlog(url) }
            result.addAll(list)
        }
        return result
    }

    override fun parseBlog(url: String): BlogMetaData {
        val doc: Document =
            Jsoup.connect(url)
                .userAgent("Mozilla/5.0") // 일부 사이트는 User-Agent 체크함
                .get()

        val title = doc.select("h1").first().text()

        // 썸네일: 본문 내 첫 번째 이미지
        val thumbnail =
            doc.select("img").first()
                .attribute("src")
                .value
                ?: null

        val content =
            doc.select("div.content")
                .take(300)
                .first()
                .text()

        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
