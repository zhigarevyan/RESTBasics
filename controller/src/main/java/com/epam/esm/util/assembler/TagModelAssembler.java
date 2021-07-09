package com.epam.esm.util.assembler;

import com.epam.esm.TagController;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.util.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class TagModelAssembler implements RepresentationModelAssembler<TagDTO,
        EntityModel<TagDTO>> {

    @Override
    public EntityModel<TagDTO> toModel(TagDTO tagDTO) {
        return EntityModel.of(tagDTO,
                linkTo(methodOn(TagController.class).getTagById(tagDTO.getId())).withSelfRel(),
                linkTo(methodOn(TagController.class).getTags(Page.getDefaultPage())).withRel("Tags"));
    }

    public List<EntityModel<TagDTO>> toModel(List<TagDTO> tagDTO) {
        return tagDTO.stream().map(this::toModel).collect(Collectors.toList());
    }
}