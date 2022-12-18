DROP TABLE IF EXISTS USERS CASCADE;
DROP TABLE IF EXISTS ITEMS CASCADE;
DROP TABLE IF EXISTS BOOKINGS CASCADE;
DROP TABLE IF EXISTS COMMENTS CASCADE;
DROP TABLE IF EXISTS REQUESTS CASCADE;

CREATE TABLE IF NOT EXISTS USERS
(
    USER_ID BIGINT auto_increment
        primary key
        unique,
    NAME    CHARACTER VARYING,
    EMAIL   CHARACTER VARYING not null
        unique
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ITEM_ID      BIGINT auto_increment
        primary key
        unique,
    NAME         CHARACTER VARYING,
    DESCRIPTION  CHARACTER VARYING,
    IS_AVALIABLE BOOLEAN,
    OWNER_ID     BIGINT not null,
    REQUEST_ID   BIGINT not null,
    constraint ITEMS_USERS_USER_ID_FK
        foreign key (OWNER_ID) references USERS
            on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS COMMENTS
(
    COMMENT_ID BIGINT auto_increment
        primary key
        unique,
    TEXT       CHARACTER VARYING,
    ITEM_ID    BIGINT,
    AUTHOR_ID  BIGINT,
    CREATED TIMESTAMP WITHOUT TIME ZONE not null ,
    constraint COMMENTS_ITEMS_ITEM_ID_FK
        foreign key (ITEM_ID) references ITEMS
            on update cascade on delete cascade,
    constraint COMMENTS_USERS_USER_ID_FK
        foreign key (AUTHOR_ID) references USERS
            on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    BOOKING_ID BIGINT auto_increment
        primary key
        unique,
    START_DATE TIMESTAMP WITHOUT TIME ZONE,
    END_DATE   TIMESTAMP WITHOUT TIME ZONE,
    ITEM_ID    BIGINT,
    BOOKER_ID  BIGINT,
    STATUS     VARCHAR,
    constraint BOOKINGS_ITEMS_ITEM_ID_FK
        foreign key (ITEM_ID) references ITEMS
            on update cascade on delete cascade,
    constraint BOOKINGS_USERS_USER_ID_FK
        foreign key (BOOKER_ID) references USERS
            on update cascade on delete cascade
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    REQUEST_ID     BIGINT auto_increment
        primary key
        unique,
    " description" CHARACTER VARYING,
    REQUESTOR_ID   BIGINT,
    constraint REQUESTS_USERS_USER_ID_FK
        foreign key (REQUESTOR_ID) references USERS
            on update cascade on delete cascade
);