package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "gift")
@Data
@DynamicUpdate
@NoArgsConstructor
@NamedNativeQueries(
        {
                @NamedNativeQuery(
                        name = "deleteGiftTagByGiftId",
                        query = "DELETE FROM gift_tag WHERE (gift_id = :giftId)"),
                @NamedNativeQuery(
                        name = "createGiftTag",
                        query = "INSERT INTO gift_tag(gift_id, tag_id) VALUES (:giftId,:tagId)")

        }
)
public class Gift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;
    private Integer price;
    private Integer duration;
    @Column(name = "create_date")
    private Instant createDate;
    @Column(name = "last_update_date")
    private Instant lastUpdateDate;


}
