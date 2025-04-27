package won.techlog.blog.domain

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class WoowabroBlogParser : BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        val doc: Document =
            Jsoup.connect(url)
                .userAgent("Mozilla/5.0") // 일부 사이트는 User-Agent 체크함
                .get()
        return doc.select("div[class=post-item firstpaint]")
            .map { it.select("a").attr("href") }
            .map { parseBlog(it) }
            .toList()
    }

    override fun parseBlog(url: String): BlogMetaData {
        val doc: Document =
            Jsoup.connect(url)
                .userAgent("Mozilla/5.0") // 일부 사이트는 User-Agent 체크함
                .get()

        val title =
            doc.select("h1")[1].text()
                ?: doc.select("meta[property=og:title]").attr("content")
                    .ifBlank { doc.title() }

        // 썸네일: 본문 내 첫 번째 이미지
        val thumbnail =
            doc.select("img[decoding]").first()?.absUrl("src")
                ?: doc.select("meta[property=og:image]").attr("content")
                    .ifBlank { null }

        val content =
            doc.select("div.post-content p")
                .take(2) // 앞 2문단 추출
                .joinToString(" ") { it.text() }

        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
