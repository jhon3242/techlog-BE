package won.techlog.blog.domain.client.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import won.techlog.blog.domain.BlogMetaData

@JsonIgnoreProperties(ignoreUnknown = true)
data class LineOldBlogContentsResponse(
    val result: Result
) {
    fun getBlogMetaData(): List<BlogMetaData> {
        return result.data.posts.edges.map {
            BlogMetaData(
                title = it.node.title,
                thumbnailUrl = it.node.opengraphImage.get(0).fullUrl,
                content = it.node.content,
                url = "https://engineering.linecorp.com/ko/blog/${it.node.slug}"
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
