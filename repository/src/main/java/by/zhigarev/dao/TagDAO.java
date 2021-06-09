package by.zhigarev.dao;

import by.zhigarev.dao.exception.DAOException;
import by.zhigarev.model.Tag;

import java.util.List;
import java.util.Optional;

public interface TagDAO {
    Tag createTag(String name) throws DAOException;

    void deleteTagById(int id);

    Tag updateTagById(String name, int id);

    Optional<Tag> getTagById(int id);

    Optional<Tag> getTagByName(String name);

    List<Tag> getTagListByGiftId(int giftId);

    List<Tag> getAllTags();
}
