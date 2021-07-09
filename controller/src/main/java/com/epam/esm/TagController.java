package com.epam.esm;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import com.epam.esm.util.Page;
import com.epam.esm.util.assembler.TagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
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
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return tagModelAssembler.toModel(tagService.createTag(tagDTO.getName()));
    }

    @GetMapping("/{id}")
    public EntityModel<TagDTO> getTagById(@PathVariable int id) {
        return tagModelAssembler.toModel(tagService.getTagById(id));
    }

    @GetMapping
    public List<EntityModel<TagDTO>> getTags(@RequestBody @Valid Page page) {
        return tagModelAssembler.toModel(tagService.getAllTags(page));
    }

    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable int id) {
        tagService.deleteTagById(id);
    }

    @GetMapping("/byName/{name}")
    public EntityModel<TagDTO> getTagByName(@PathVariable String name) {
        return tagModelAssembler.toModel(tagService.getTagByName(name));
    }

}
