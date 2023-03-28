create table products
(
    id          int8 generated by default as identity,
    created_at  date,
    color       varchar(255),
    description varchar(255),
    icon_path   varchar(255),
    name        varchar(255),
    number      int4,
    primary key (id)
);
create table users
(
    id         int8 generated by default as identity,
    created_at date,
    full_name  varchar(255),
    login      varchar(255),
    password   varchar(255),
    primary key (id)
);
create table users_cards
(
    id         int8 generated by default as identity,
    created_at date,
    barcode    varchar(255),
    product_id int8,
    user_id    int8,
    primary key (id)
);
alter table if exists products drop constraint if exists UK_o61fmio5yukmmiqgnxf8pnavn;
alter table if exists products add constraint UK_o61fmio5yukmmiqgnxf8pnavn unique (name);
alter table if exists users_cards add constraint FKrurccvp1pt4n9uq2x3pmrtvwg foreign key (product_id) references products;
alter table if exists users_cards add constraint FKr2696cs9gb8al9cj5da42wfkv foreign key (user_id) references users;