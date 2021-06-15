package by.zhigarev.service.impl;

import by.zhigarev.dao.TagDAO;
import by.zhigarev.dto.TagDTO;
import by.zhigarev.exeption.impl.DuplicateTagException;
import by.zhigarev.exeption.impl.InvalidDataException;
import by.zhigarev.exeption.impl.NoSuchTagException;
import by.zhigarev.model.Tag;
import by.zhigarev.service.TagService;
import by.zhigarev.util.TagEntityDTOMapper;
import by.zhigarev.util.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class TagServiceImpl implements TagService {
    private static final String MESSAGE_INVALID_DATA_EXCEPTION = "Invalid data";
    private static final String MESSAGE_DUPLICATE_TAG_EXCEPTION = "Duplicate tag";
    private static final String MESSAGE_NO_SUCH_TAG_EXCEPTION = "No such tag exception";
    private final TagDAO tagDAO;

    @Autowired
    public TagServiceImpl(TagDAO tagDAO) {
        this.tagDAO = tagDAO;
    }
    @Transactional
    @Override
    public TagDTO createTag(String name) {
        if (tagDAO.getTagByName(name).isPresent()) {
            throw new DuplicateTagException(MESSAGE_DUPLICATE_TAG_EXCEPTION);
        }
        return TagEntityDTOMapper.toDTO(tagDAO.createTag(name));
    }
    @Transactional
    @Override
    public void deleteTagById(int id) {
        if (tagDAO.getTagById(id).isEmpty() || !Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        tagDAO.deleteTagById(id);
    }

    @Override
    public TagDTO getTagById(int id) {
        if (!Validator.isValidNumber(id)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        Optional<Tag> tagById = tagDAO.getTagById(id);
        if (tagById.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_EXCEPTION);
        }
        return TagEntityDTOMapper.toDTO(tagById.get());
    }

    @Override
    public TagDTO getTagByName(String name) {
        if (!Validator.isValidString(name)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        Optional<Tag> tagByName = tagDAO.getTagByName(name);
        if (tagByName.isEmpty()) {
            throw new NoSuchTagException(MESSAGE_NO_SUCH_TAG_EXCEPTION);
        }
        return TagEntityDTOMapper.toDTO(tagByName.get());
    }

    @Override
    public List<TagDTO> getTagListByGiftId(int giftId) {
        if (!Validator.isValidNumber(giftId)) {
            throw new InvalidDataException(MESSAGE_INVALID_DATA_EXCEPTION);
        }
        return TagEntityDTOMapper.toDTO(tagDAO.getTagListByGiftId(giftId));
    }

    @Override
    public List<TagDTO> getAllTags() {
        return TagEntityDTOMapper.toDTO(tagDAO.getAllTags());
    }
}
