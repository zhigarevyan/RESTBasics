package com.epam.esm;

import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/tags")
public class TagController {
    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TagDTO createTag(@RequestBody TagDTO tagDTO){
        return tagService.createTag(tagDTO.getName());
    }
    @GetMapping("/{id}")
    public TagDTO getTagById(@PathVariable int id){
        return tagService.getTagById(id);
    }
    @GetMapping
    public List<TagDTO> getTags(){
        return tagService.getAllTags();
    }
    @DeleteMapping("/{id}")
    public void deleteTag(@PathVariable int id){
        tagService.deleteTagById(id);
    }
    @GetMapping("/byName/{name}")
    public TagDTO getTagByName(@PathVariable String name){
        return tagService.getTagByName(name);
    }

}
