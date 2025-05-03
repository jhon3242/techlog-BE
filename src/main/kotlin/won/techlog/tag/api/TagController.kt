package won.techlog.tag.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.tag.api.response.TagResponse
import won.techlog.tag.domain.TagService

@RestController
@RequestMapping("/api")
class TagController(
    private val tagService: TagService
) {
    @GetMapping("/tags")
    fun findAllTags(): List<TagResponse> =
        tagService.findAll()
            .map { TagResponse(it) }
}
