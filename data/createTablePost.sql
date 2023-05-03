create table if not exists Post (
    id serial primary key,
    name varchar(255) unique,
    text varchar(255),
    link varchar(255),
    created timestamp
);