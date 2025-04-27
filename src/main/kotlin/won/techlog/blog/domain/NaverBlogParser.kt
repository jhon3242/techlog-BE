package won.techlog.blog.domain

import com.microsoft.playwright.BrowserType
import com.microsoft.playwright.Playwright
import com.microsoft.playwright.options.LoadState
import org.springframework.stereotype.Component

@Component
class NaverBlogParser: BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        val result = mutableListOf<BlogMetaData>()
        Playwright.create().use { playwright ->
            val browser = playwright.chromium().launch(
                BrowserType.LaunchOptions().setHeadless(true)
            )
            val page = browser.newPage()
            page.navigate(url)
            page.waitForLoadState(LoadState.NETWORKIDLE)
            val list = page.locator("a.post_txt_wrap")
                .all()
                .map { it.getAttribute("href") }
                .map { parseBlog("https://d2.naver.com$it") }
            result.addAll(list)
        }
        return result
    }

    override fun parseBlog(url: String): BlogMetaData {
        var title = ""
        var content = ""
        var thumbnail: String? = ""
        Playwright.create().use { playwright ->
            // 브라우저 실행 (headless 모드)
            val browser = playwright.chromium().launch(
                BrowserType.LaunchOptions().setHeadless(true)
            )

            val page = browser.newPage()

            // 페이지 이동
            page.navigate(url)

            // 페이지 로드 기다리기 (옵션)
            page.waitForLoadState(LoadState.NETWORKIDLE)

            // 데이터 추출
            title = page.textContent("h1.posting_tit") ?: "제목 없음"
            content = page.textContent("div.con_view").take(300) ?: "본문 없음"
            thumbnail = page.locator("div.con_view img")
                .first()
                .getAttribute("src")
                .ifBlank { null }
                .let {"https://d2.naver.com${it}"}

            browser.close()
        }

        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
