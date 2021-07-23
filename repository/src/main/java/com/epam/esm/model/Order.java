package com.epam.esm.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.envers.Audited;

import javax.persistence.*;
import java.time.Instant;
import java.util.List;

@Entity
@Table(name = "order")
@Data
@Audited
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private Integer price;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user", nullable = false)
    private User user;
    @Column(nullable = false)
    private Instant date;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {
            CascadeType.DETACH,
            CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.PERSIST
    })
    @JoinTable(
            name = "order_gift",
            joinColumns = @JoinColumn(name = "order_id", referencedColumnName = "id", nullable = false),
            inverseJoinColumns = @JoinColumn(name = "gift_id", referencedColumnName = "id" ,nullable = false))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private List<Gift> giftList;
}
