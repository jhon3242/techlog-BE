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
import kotlin.use

@Component
class KurlyBlogCrawler : BlogCrawler {
    private val baseUrl = "https://helloworld.kurly.com"

    override fun crawlBlogs(url: String): List<BlogMetaData> {
        TODO("Not yet implemented")
    }

    override fun crawlBlogs(): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(true)
                )
            val page = browser.newPage()
            page.navigate(baseUrl)
            page.waitForLoadState(LoadState.NETWORKIDLE)
            val doc = Jsoup.parse(page.content())

            val blogUrls =
                doc.select("a.post-link")
                    .map { baseUrl + it.attr("href") }

            blogUrls.forEach { url ->
                val metadata = crawlBlog(page, url)
                result.add(metadata)
            }

            browser.close()
        }
        return result
    }

    fun crawlBlog(
        page: Page,
        url: String
    ): BlogMetaData {
        page.navigate(url)
        page.waitForLoadState(LoadState.NETWORKIDLE)
        val doc = Jsoup.parse(page.content())

        val title = doc.selectFirst("h1.page-title").text() ?: "No title"
        val thumbnailUrl =
            doc.select("img")
                .map { it.attr("src") }
                .firstOrNull { it.startsWith("/post-img") }
                ?.let { baseUrl + it }
        val content =
            doc.select("p")
                .map { it.text() }
                .drop(2)
                .joinToString()
                .take(1000)
        val publishedAt =
            doc.selectFirst("span.post-date")
                .text()
                .substring(7, 17)
                .let { TimeProvider.parseByString(it) }

        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnailUrl,
            content = content,
            url = url,
            publishedAt = publishedAt
        )
    }

    override fun crawlBlog(url: String): BlogMetaData {
        TODO()
    }

    override fun isSupportType(blogType: BlogType): Boolean {
        return BlogType.KURLY == blogType
    }
}
