package won.techlog.poster.api.request

data class PosterUpdateRequest(
    val title: String = "",
    val thumbnail: String? = null,
    val content: String = "",
    val url: String = "",
    val blogType: String = "",
    val tags: List<String> = mutableListOf()
)
