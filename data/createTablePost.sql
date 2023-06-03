create table if not exists Post (
    id serial primary key,
    title varchar(255),
    link varchar(255) unique,
    description text,
    created timestamp
);