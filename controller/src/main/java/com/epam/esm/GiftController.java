package com.epam.esm;

import com.epam.esm.dto.GiftDTO;
import com.epam.esm.dto.TagDTO;
import com.epam.esm.service.GiftService;
import com.epam.esm.service.TagService;
import com.epam.esm.util.GiftQueryParameters;
import com.epam.esm.util.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/gifts")
public class GiftController {
    private final GiftService giftService;
    private final TagService tagService;

    @Autowired
    public GiftController(GiftService giftService, TagService tagService) {
        this.giftService = giftService;
        this.tagService = tagService;
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public GiftDTO createGift(@RequestBody @Valid GiftDTO giftDTO) {
        return giftService.createGift(giftDTO);
    }

    @GetMapping("/{id}")
    public GiftDTO getGiftById(@PathVariable int id) {
        return giftService.getGiftById(id);
    }

    @GetMapping
    public List<GiftDTO> getGiftsByParams(@Valid GiftQueryParameters params) {
        return giftService.getGiftsByParams(params);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public GiftDTO updateGift(@RequestBody GiftDTO giftDTO, @PathVariable int id) {
        return giftService.updateGiftById(giftDTO, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGift(@PathVariable int id) {
        giftService.deleteGiftById(id);
    }

    @GetMapping("/{id}/tags")
    public List<TagDTO> getTagsByGiftId(@PathVariable int id) {
        return tagService.getTagListByGiftId(id);
    }


}
