package won.techlog.tag.api.response

import won.techlog.tag.domain.Tag

data class TagResponse(
    val id: Long,
    val name: String
) {
    constructor(tag: Tag) : this(tag.id, tag.name)
}
