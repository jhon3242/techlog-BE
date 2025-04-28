package won.techlog.blog.domain

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.stereotype.Component

@Component
class CoupangBlogParser : BlogParser {
    override fun parseBlogs(url: String): List<BlogMetaData> {
        TODO("Not yet implemented")
    }

    override fun parseBlog(url: String): BlogMetaData {
        val doc: Document =
            Jsoup.connect(url)
                .userAgent("Mozilla/5.0") // 일부 사이트는 User-Agent 체크함
                .get()

        val title = doc.select("h1").first().text()

        // 썸네일: 본문 내 첫 번째 이미지
        val thumbnail =
            doc.select("picture")[0]
                .select("source")[0]
                .attribute("srcset")
                .value.split(" ")[0]

        val content =
            doc.select("p.pw-post-body-paragraph")[1]
                .text()
                .take(300)

        return BlogMetaData(title = title, thumbnailUrl = thumbnail, content = content, url = url)
    }
}
