package by.zhigarev.controller;

import by.zhigarev.dto.GiftDTO;
import by.zhigarev.dto.TagDTO;
import by.zhigarev.service.GiftService;
import by.zhigarev.service.TagService;
import by.zhigarev.util.GiftSQLQueryParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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
    public GiftDTO createGift(@RequestBody GiftDTO giftDTO) {
        return giftService.createGift(giftDTO);
    }

    @GetMapping("/{id}")
    public GiftDTO getGiftById(@PathVariable int id) {
        return giftService.getGiftById(id);
    }

    @GetMapping("/")
    public List<GiftDTO> getGifts() {
        return giftService.getGifts();
    }

    @GetMapping("/byParams")
    public List<GiftDTO> getGiftsByParams(@RequestBody GiftSQLQueryParameters params) {
        return giftService.getGiftsByParams(params);
    }

    @PutMapping("/{id}")
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
