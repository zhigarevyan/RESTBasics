package com.epam.esm.repository;

import com.epam.esm.model.Gift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GiftRepository extends JpaRepository<Gift, Integer>,
        JpaSpecificationExecutor<Gift> {

    /**
     * Connects to database and returns Gift by name.
     *
     * @param name is Gift name value.
     * @return Optional of {@link Gift} entity from database.
     */
    Optional<Gift> findByName(String name);

    /**
     * Connects to database and returns Tag by ID.
     *
     * @param id is Gift  ID value.
     * @return Optional of {@link Gift} entity from database.
     */
    Optional<Gift> findById(int id);

    /**
     * Connects to database and deletes Gift  with provided ID
     *
     * @param id is Tag ID value.
     */
    void deleteById(int id);

}