INSERT INTO gift (name, description, price, duration)
values ('First Gift', 'First certificate', 5, 10);
INSERT INTO gift (name, description, price, duration)
values ('Second Gift', 'Second certificate', 10, 20);

INSERT INTO tag (name)
values ('First Tag');
INSERT INTO tag (name)
values ('Second Tag');
INSERT INTO tag (name)
values ('Third Tag');

INSERT INTO gift_tag(gift_id, tag_id)
values (1, 1);
INSERT INTO gift_tag(gift_id, tag_id)
values (1, 2);
INSERT INTO gift_tag(gift_id, tag_id)
values (2, 2);