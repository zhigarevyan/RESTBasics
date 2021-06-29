package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftDAO;
import com.epam.esm.model.Gift;
import com.epam.esm.util.GiftMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link Gift}. Provides methods to interact with Tags data from database.
 * Methods connect to database using {@link DataSource} and manipulate with data(save, edit, etc.).
 */
@Repository
public class GiftDAOImpl implements GiftDAO {
    @PersistenceContext
    EntityManager entityManager;


    @Override
    public Gift createGift(Gift gift) {
        return null;
    }

    @Override
    public void deleteGiftById(int id) {

    }

    @Override
    public Gift updateGiftById(String updateSQL, int id) {
        return null;
    }

    @Override
    public Optional<Gift> getGiftById(int id) {
        return Optional.empty();
    }

    @Override
    public List<Gift> getGifts() {
        return null;
    }

    @Override
    public List<Gift> getGiftsByParams(String getSql) {
        return null;
    }

    @Override
    public void createGiftTag(int giftId, int tagId) {

    }

    @Override
    public void deleteGiftTagByGiftId(int id) {

    }
}

