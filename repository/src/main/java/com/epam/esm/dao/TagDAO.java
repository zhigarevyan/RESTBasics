package com.epam.esm.dao;


import com.epam.esm.model.Tag;

import java.util.List;
import java.util.Optional;

/**
 * Interface provides methods to interact with Tag data from database.
 * Methods should connect to database and manipulate with data(save, edit, etc.).
 */
public interface TagDAO {
    /**
     * Connects to database and add an new Tag.
     *
     * @param name is Tag name value
     * @return Created {@link Tag} entity from database.
     */
    Tag createTag(String name);

    /**
     * Connects to database and deletes Tag with provided ID.
     *
     * @param id is Tag ID value.
     */
    void deleteTagById(int id);

    /**
     * Connects to database and returns Tag by ID.
     *
     * @param id is Tag ID value.
     * @return Optional of {@link Tag} entity from database.
     */
    Optional<Tag> getTagById(int id);

    /**
     * Connects to database and returns Tag by name.
     *
     * @param name is Tag name value.
     * @return Optional of {@link Tag} entity from database.
     */
    Optional<Tag> getTagByName(String name);

    /**
     * Connects to database and returns list of Tags linked to GiftCertificate in gift_tag table.
     *
     * @param giftId is GiftCertificate
     * @return List of matched {@link Tag} entities from database.
     */
    List<Tag> getTagListByGiftId(int giftId);

    /**
     * Connects to database and returns all Tags.
     *
     * @return List of all {@link Tag} entities from database.
     */
    List<Tag> getAllTags();
}
