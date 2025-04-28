package won.techlog.blog.domain

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class KakaoPayBlogParser : BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        val doc: Document =
            Jsoup.connect(url)
                .userAgent("Mozilla/5.0") // 일부 사이트는 User-Agent 체크함
                .get()

        return doc.select("li._postListItem_1cl5f_66")
            .map { it.select("a").attr("href") }
            .map { parseBlog("https://tech.kakaopay.com/$it") }
            .toList()
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
            doc.selectFirst("div.content")
                .text()
                .take(300)

        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
