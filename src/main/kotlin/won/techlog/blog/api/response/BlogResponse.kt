package won.techlog.blog.api.response

import won.techlog.blog.domain.Blog

data class BlogResponse(
    val title: String,
    val thumbnail: String?,
    val content: String,
    val url: String,
    val blogType: String
) {
    constructor(blog: Blog) : this(
        title = blog.metaData.title,
        thumbnail = blog.metaData.thumbnailUrl,
        content = blog.metaData.content,
        url = blog.metaData.url,
        blogType = blog.type.name
    )
}
