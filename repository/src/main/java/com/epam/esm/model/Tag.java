package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "gift")
@Data
@DynamicUpdate
@NoArgsConstructor
@NamedNativeQuery(
        name = "getTagListByGiftId",
        query = "select * from tag join gift_tag gt on tag.id = gt.tag_id where gt.gift_id = :giftId",
        resultClass = Tag.class)
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;

}
