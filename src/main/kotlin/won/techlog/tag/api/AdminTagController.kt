package won.techlog.tag.api

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.common.admin.AdminCheck
import won.techlog.tag.api.request.TagRequest
import won.techlog.tag.api.response.TagResponse
import won.techlog.tag.domain.TagService

@AdminCheck
@RestController
@RequestMapping("/api")
class AdminTagController(
    private val tagService: TagService
) {
    @PostMapping("/tag")
    fun save(
        @RequestBody request: TagRequest
    ): TagResponse = TagResponse(tagService.save(request.name))

    @DeleteMapping("/tag")
    fun delete(
        @RequestBody request: TagRequest
    ) = tagService.delete(request.name)
}
