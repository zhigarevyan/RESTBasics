package com.epam.esm;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.util.assembler.TagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public TagController(TagService tagService, TagModelAssembler tagModelAssembler) {
        this.tagService = tagService;
        this.tagModelAssembler = tagModelAssembler;
    }


    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return tagModelAssembler.toModel(tagService.createTag(tagDTO.getName()));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EntityModel<TagDTO> getTagById(@PathVariable int id) {
        return tagModelAssembler.toModel(tagService.getTagById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public List<EntityModel<TagDTO>> getTags(@Valid Pageable page) {
        return tagModelAssembler.toModel(tagService.getAllTags(page));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteTag(@PathVariable int id) {
        tagService.deleteTagById(id);
    }

    @GetMapping("/byName/{name}")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public EntityModel<TagDTO> getTagByName(@PathVariable String name) {
        return tagModelAssembler.toModel(tagService.getTagByName(name));
    }

}
