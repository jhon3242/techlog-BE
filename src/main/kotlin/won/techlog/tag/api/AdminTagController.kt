package won.techlog.tag.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import won.techlog.common.admin.AdminCheck
import won.techlog.tag.api.request.TagRequest
import won.techlog.tag.domain.Tag
import won.techlog.tag.domain.TagService

@AdminCheck
@RestController
@RequestMapping("/api")
class AdminTagController(
    private val tagService: TagService
) {
    @GetMapping("/tags")
    fun findAllTags(): List<Tag> = tagService.findAll()

    @PostMapping("/tag")
    fun save(
        @RequestBody request: TagRequest
    ): Tag = tagService.save(request.name)
}
