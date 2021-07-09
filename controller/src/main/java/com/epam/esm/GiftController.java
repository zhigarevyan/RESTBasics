package com.epam.esm;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.assembler.GiftModelAssembler;
import com.epam.esm.util.assembler.TagModelAssembler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/gifts")
public class GiftController {
    private final GiftService giftService;
    private final TagService tagService;
    private final GiftModelAssembler giftModelAssembler;
    private final TagModelAssembler tagModelAssembler;

    @Autowired
    public GiftController(GiftService giftService, TagService tagService, GiftModelAssembler giftModelAssembler, TagModelAssembler tagModelAssembler) {
        this.giftService = giftService;
        this.tagService = tagService;
        this.giftModelAssembler = giftModelAssembler;
        this.tagModelAssembler = tagModelAssembler;
    }


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public EntityModel<GiftDTO> createGift(@RequestBody @Valid GiftDTO giftDTO) {
        return giftModelAssembler.toModel(giftService.createGift(giftDTO));
    }

    @GetMapping("/{id}")
    public EntityModel<GiftDTO> getGiftById(@PathVariable int id) {
        return giftModelAssembler.toModel(giftService.getGiftById(id));
    }

    @GetMapping
    public List<EntityModel<GiftDTO>> getGiftsByParams(@Valid GiftQueryParameters params) {
        return giftModelAssembler.toModel(giftService.getGiftsByParams(params));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public EntityModel<GiftDTO> updateGift(@RequestBody GiftDTO giftDTO, @PathVariable int id) {
        return giftModelAssembler.toModel(giftService.updateGiftById(giftDTO, id));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGift(@PathVariable int id) {
        giftService.deleteGiftById(id);
    }

    @GetMapping("/{id}/tags")
    public List<EntityModel<TagDTO>> getTagsByGiftId(@PathVariable int id) {
        return tagModelAssembler.toModel(tagService.getTagListByGiftId(id));
    }


}
