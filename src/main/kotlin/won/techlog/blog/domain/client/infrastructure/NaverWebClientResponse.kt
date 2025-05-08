package won.techlog.blog.domain.client.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverWebClientResponse(
    val content: List<NaverBlogContent>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverBlogContent(
    val postTitle: String,
    val postImage: String,
    val postHtml: String,
    val postPublishedAt: Long,
    val url: String,
    val viewCount: Int,
    val author: String
)
