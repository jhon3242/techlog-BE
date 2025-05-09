package won.techlog.blog.domain.client.infrastructure.line

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import org.jsoup.Jsoup
import won.techlog.blog.domain.BlogMetaData

@JsonIgnoreProperties(ignoreUnknown = true)
data class LineBlogContentsResponse(
    val result: Result
) {
    fun getSlugs(): List<String> {
        return result.data.posts.edges.map { it.node.slug }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Result(
        val data: Data
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Data(
        @JsonProperty("BlogsQuery")
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
        @JsonProperty("opengraph_image")
        val opengraphImage: List<OpengraphImage>,
        val pubdate: String,
        val slug: String,
    )

    data class OpengraphImage(
        val localFile: LocalFile
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class LocalFile(
        val childImageSharp: ChildImageSharp
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChildImageSharp(
        val gatsbyImageData: GatsbyImageData
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GatsbyImageData(
        val images: Images
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Images(
        val fallback: Fallback
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Fallback(
        val src: String
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class LineBlogContentResponse(
    val result: DetailResult
) {
    fun getBlogMetaData(): BlogMetaData {
        return BlogMetaData(
            title = result.data.blog.title,
            thumbnailUrl = "https://techblog.lycorp.co.jp${result.data.blog.opengraphImage[0].localFile
                .childImageSharp.gatsbyImageData.images.fallback.src}",
            content = Jsoup.parse(result.data.blog.content).text().take(300),
            url = "https://techblog.lycorp.co.jp/ko/${result.data.blog.slug}"
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DetailResult(
        val data: DetailData
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class DetailData(
        @JsonProperty("blogDetail")
        val blog: BlogDetail
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class BlogDetail(
        val id: String,
        val content: String,
        val createdAt: String,
        val landPressId: String,
        val locale: String,
        val postId: Int,
        val primaryLocale: Boolean,
        val slug: String,
        val title: String,
        val pubdate: String,
        @JsonProperty("opengraph_image")
        val opengraphImage: List<OgImage>
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class OgImage(
        val localFile: LocalFile
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class LocalFile(
        val childImageSharp: ChildImageSharp
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ChildImageSharp(
        val gatsbyImageData: GatsbyImageData
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class GatsbyImageData(
        val layout: String,
        val backgroundColor: String,
        val images: ImageFallbackContainer
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class ImageFallbackContainer(
        val fallback: FallbackImage
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class FallbackImage(
        val src: String
    )
}

