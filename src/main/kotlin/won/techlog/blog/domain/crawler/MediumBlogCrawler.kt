package won.techlog.blog.domain.crawler

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import won.techlog.blog.domain.BlogMetaData
import won.techlog.common.TimeProvider
import kotlin.random.Random
import kotlin.use

object MediumBlogCrawler {
    fun fetchBlog(
        companySlug: String,
        year: Int
    ): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser =
                playwright.chromium().launch(
                    BrowserType.LaunchOptions().setHeadless(false).setTimeout(60000.0)
                )
            val page = browser.newPage()
            page.navigate("https://medium.com/$companySlug/archive/$year")
            page.waitForLoadState(LoadState.NETWORKIDLE)
            page.setDefaultTimeout(60000.0) // 60초
            Thread.sleep(Random.nextInt(1, 5).toLong() * 1000)
            val doc = Jsoup.parse(page.content())

            doc.select("div.postArticle")
                .map { extractBlogMetaData(it) }
                .let { result.addAll(it) }
        }
        return result.distinctBy { it.url }
    }

    private fun extractBlogMetaData(element: Element): BlogMetaData {
        val title = element.select("h3").text()
        val content = element.select("p").text()
        val thumbnail =
            element.select("img.progressiveMedia-image")
                .attr("data-src")
        val url = element.select("div.ui-caption a").attr("href")
        val dateTimeStr = element.select("time").attr("datetime")
        val publishedAt = TimeProvider.parseByString(dateTimeStr)
        return BlogMetaData(
            title = title,
            thumbnailUrl = thumbnail,
            content = content,
            url = url,
            publishedAt = publishedAt
        )
    }
}
