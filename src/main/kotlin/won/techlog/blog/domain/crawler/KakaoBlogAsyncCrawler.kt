package won.techlog.blog.domain.crawler

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Page
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.springframework.stereotype.Component
import won.techlog.blog.domain.BlogMetaData

@Component
class KakaoBlogAsyncCrawler : BlogAsyncCrawler {
    override suspend fun crawlBlogs(url: String): List<BlogMetaData> {
        return coroutineScope {
            val result = mutableListOf<BlogMetaData>()
            launch {
                Playwright.create().use { playwright ->
                    val browser = playwright.chromium().launch(BrowserType.LaunchOptions().setHeadless(true))
                    val page = browser.newPage()
                    page.navigate(url)

                    page.waitForLoadState(LoadState.DOMCONTENTLOADED)
                    page.locator("ul.list_post li").first().waitFor()

                    val postUrls =
                        page.locator("a.link_post")
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
            }
            result
        }
    }

    override suspend fun crawlBlog(url: String): BlogMetaData {
        TODO("Not yet implemented")
    }

    private suspend fun extractBlogMetaData(
        page: Page,
        url: String
    ): BlogMetaData =
        withContext(Dispatchers.IO) {
            page.navigate(url)
            page.waitForSelector(
                "div.inner_content div.preview",
                Page.WaitForSelectorOptions().setTimeout(10000.0)
            )
            val html = page.content()
            val doc = Jsoup.parse(html)
            val title = doc.selectFirst("h1.tit_post")?.text()?.trim() ?: "제목 없음"
            val fullContent =
                doc.selectFirst("div.preview")?.text()?.trim()
                    ?: doc.selectFirst("div.inner_content")?.text()?.trim()
                    ?: "본문 없음"
            val content = if (fullContent.length > 300) fullContent.substring(0, 300) else fullContent
            val thumbnail = doc.selectFirst("meta[property=og:image]")?.attr("content")?.takeIf { it.isNotBlank() }

            BlogMetaData(
                title = title,
                thumbnailUrl = thumbnail,
                content = content,
                url = url
            )
        }
}
