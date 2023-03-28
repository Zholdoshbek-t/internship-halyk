create table "api_service"
(
    id   int8 generated by default as identity,
    name varchar(255) not null,
    primary key (id)
);
create table "chat"
(
    id        int8 generated by default as identity,
    chat_id   int8,
    is_active boolean,
    primary key (id)
);
create table chats_api_services
(
    chat_id        int8 not null,
    api_service_id int8 not null
);
create table "error_message"
(
    id         int8 generated by default as identity,
    code       int4 not null,
    created_at timestamp,
    url_id     int8,
    primary key (id)
);
create table "http_method"
(
    id   int8 generated by default as identity,
    name varchar(50) not null,
    primary key (id)
);
create table "telegram_message"
(
    id   int8 generated by default as identity,
    name varchar(50) not null,
    text varchar(1000) not null,
    primary key (id)
);
create table "url"
(
    id             int8 generated by default as identity,
    address        varchar(255) not null,
    api_service_id int8,
    http_method_id int8,
    primary key (id)
);
alter table if exists "api_service" drop constraint if exists UK_6draeadq4ei0ck07k8tld1ngt;
alter table if exists "api_service" add constraint UK_6draeadq4ei0ck07k8tld1ngt unique (name);
alter table if exists "http_method" drop constraint if exists UK_q5641sl2pds0rkovvvcvsn5ce;
alter table if exists "http_method" add constraint UK_q5641sl2pds0rkovvvcvsn5ce unique (name);
alter table if exists "telegram_message" drop constraint if exists UK_9r3kiww9uowsb5o362eps3kr8;
alter table if exists "telegram_message" add constraint UK_9r3kiww9uowsb5o362eps3kr8 unique (name);
alter table if exists "telegram_message" drop constraint if exists UK_d0emjk4q63ce39e7ie19ggoia;
alter table if exists "telegram_message" add constraint UK_d0emjk4q63ce39e7ie19ggoia unique (text);
alter table if exists "url" drop constraint if exists UK_70uu0ql508oa7efc65tbmttvc;
alter table if exists "url" add constraint UK_70uu0ql508oa7efc65tbmttvc unique (address);
alter table if exists chats_api_services add constraint FKgq1grwqv7fhw359yrub4gor5d foreign key (api_service_id) references "api_service";
alter table if exists chats_api_services add constraint FKint2gjxb1rtsav2nwo4suy1bu foreign key (chat_id) references "chat";
alter table if exists "error_message" add constraint FK9m0xixjdneil9tw7qnjvaf0m5 foreign key (url_id) references "url";
alter table if exists "url" add constraint FKi9be7o273gd3rawteimgqya0g foreign key (api_service_id) references "api_service";
alter table if exists "url" add constraint FKlm6tq1mpi3rqsk498r8fqbwhc foreign key (http_method_id) references "http_method";