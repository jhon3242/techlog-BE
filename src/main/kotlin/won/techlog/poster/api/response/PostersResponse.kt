package won.techlog.poster.api.response

data class PostersResponse(
    val posters: List<PosterResponse>,
    val nextCursor: String? = null,
    val hasNext: Boolean = true,
    val size: Int = 0
)
