package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "gift")
@Data
@Audited
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
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private Integer price;
    @Column(nullable = false)
    private Integer duration;
    @Column(name = "create_date", nullable = false)
    private Instant createDate;
    @Column(name = "last_update_date", nullable = false)
    private Instant lastUpdateDate;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "gift_tag",
            joinColumns = {@JoinColumn(name = "gift_id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "tag_id", nullable = false)})
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Tag> tagList;


}
