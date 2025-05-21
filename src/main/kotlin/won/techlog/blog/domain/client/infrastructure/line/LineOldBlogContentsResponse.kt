package won.techlog.blog.domain.client.infrastructure.line

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import won.techlog.blog.domain.BlogMetaData
import won.techlog.blog.domain.BlogType
import won.techlog.common.TimeProvider

@JsonIgnoreProperties(ignoreUnknown = true)
data class LineOldBlogContentsResponse(
    val result: Result
) {
    fun getBlogMetaData(): List<BlogMetaData> {
        return result.data.posts.edges.map {
            BlogMetaData(
                title = it.node.title,
                thumbnailUrl = it.node.opengraphImage.get(0).fullUrl,
                content = Jsoup.parse(it.node.content).text().take(300),
                url = "https://engineering.linecorp.com/ko/blog/${it.node.slug}",
                publishedAt = TimeProvider.parseByString(it.node.pubdate, BlogType.LINE_OLD)
            )
        }
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Result(
    val data: Data
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Data(
    @JsonProperty("allLandPressBlogPosts")
    val posts: AllPosts
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class AllPosts(
    val edges: List<Edge>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Edge(
    val node: BlogPost
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class BlogPost(
    val postId: Int,
    val title: String,
    val content: String,
    @JsonProperty("opengraph_image")
    val opengraphImage: List<OpengraphImage>,
    val authors: Authors,
    val pubdate: String,
    val slug: String,
    val locale: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class OpengraphImage(
    val fullUrl: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Authors(
    @JsonProperty("display_name")
    val displayName: String,
    val description: String?
)
