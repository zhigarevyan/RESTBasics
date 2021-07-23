package com.epam.esm.util.assembler;

import com.epam.esm.GiftController;
import com.epam.esm.dto.GiftDTO;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.Page;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
@Component
public class GiftModelAssembler implements RepresentationModelAssembler<GiftDTO, EntityModel<GiftDTO>> {

    @Override
    public EntityModel<GiftDTO> toModel(GiftDTO giftDTO) {
        return EntityModel.of(giftDTO,
                linkTo(methodOn(GiftController.class).getGiftById(giftDTO.getId())).withSelfRel(),
                linkTo(methodOn(GiftController.class).getGiftsByParams(new GiftQueryParameters())).withRel("Gifts"),
                linkTo(methodOn(GiftController.class).getTagsByGiftId(giftDTO.getId(), Page.getDefaultPage())).withRel("Tags"));
    }
    public List<EntityModel<GiftDTO>> toModel(List<GiftDTO> giftCertificateDto) {
        return giftCertificateDto.stream().map(this::toModel).collect(Collectors.toList());
    }

}
