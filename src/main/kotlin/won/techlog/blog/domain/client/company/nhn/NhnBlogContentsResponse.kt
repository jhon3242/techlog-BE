package won.techlog.blog.domain.client.company.nhn

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import won.techlog.blog.domain.BlogMetaData
import won.techlog.common.TimeProvider

@JsonIgnoreProperties(ignoreUnknown = true)
data class NhnBlogContentsResponse(
    val header: Header,
    val totalCount: Int,
    val posts: List<Post>
) {
    fun getBlogMetaData(): List<BlogMetaData> {
        return posts.map { post ->
            val perLang = post.postPerLang
            BlogMetaData(
                title = perLang.title,
                thumbnailUrl = perLang.repImageUrl,
                content = post.contentPreview,
                url = postUrlFromId(post.postId),
                publishedAt = TimeProvider.parseByString(post.publishTime)
            )
        }
    }

    private fun postUrlFromId(postId: Int): String {
        return "https://meetup.nhncloud.com/posts/$postId"
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Header(
        val isSuccessful: Boolean,
        val resultCode: Int,
        val resultMessage: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class Post(
        val postId: Int,
        val viewCount: Int,
        val delYn: String,
        val regTime: String,
        val regId: String,
        val updTime: String,
        val updId: String,
        val publishYn: String,
        val publishTime: String,
        val publishRegId: String,
        val publishStatus: String,
        val contentStatus: String,
        val postPerLang: PostPerLang,
        val regDt: String,
        val contentPreview: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true)
    data class PostPerLang(
        val postPerLangId: Int,
        val postId: Int,
        val langCd: String,
        val title: String,
        val description: String,
        val repImageUrl: String,
        val tag: String,
        val facebookRegYn: String,
        val twitterRegYn: String,
        val delYn: String,
        val publishYn: String,
        val regTime: String,
        val regId: String,
        val updTime: String,
        val updId: String,
        val attachments: List<Any>,
        val authors: List<Any>
    )

}

