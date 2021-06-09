create table gift
(
    id               int auto_increment
        primary key,
    name             varchar(45)               not null,
    description      varchar(45)               not null,
    price            int                       not null,
    duration         int                       not null,
    create_date      timestamp default (now()) not null,
    last_update_date timestamp default (now()) not null
);

create table tag
(
    id   int auto_increment
        primary key,
    name varchar(45) not null
);

create table gift_tag
(
    id      int auto_increment
        primary key,
    gift_id int not null,
    tag_id  int not null,
    constraint gift
        foreign key (gift_id) references gift (id)
            on update cascade on delete cascade,
    constraint tag
        foreign key (tag_id) references tag (id)
            on update cascade on delete cascade
);

create index gift_idx
    on gift_tag (gift_id);

create index tag_idx
    on gift_tag (tag_id);

