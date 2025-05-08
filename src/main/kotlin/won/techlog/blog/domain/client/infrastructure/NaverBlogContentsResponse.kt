package won.techlog.blog.domain.client.infrastructure

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverBlogContentsResponse(
    val content: List<NaverBlogContentResponse>
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class NaverBlogContentResponse(
    val postTitle: String,
    val postImage: String,
    val postHtml: String,
    val postPublishedAt: Long,
    val url: String,
    val viewCount: Int,
    val author: String
)
