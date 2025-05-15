package won.techlog.poster.api.response

data class PostersResponse(
    val posters: List<PosterResponse>,
    val nextCursor: Long? = null,
    val hasNext: Boolean = true
)
