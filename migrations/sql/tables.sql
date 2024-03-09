create table link
(
    id integer primary key generated by default as identity,
    url varchar not null unique,
    updated_at timestamp not null
);
create table chat
(
    id integer primary key generated by default as identity,
    chat_id integer not null unique
);
create table connections
(
    link_id integer references link(id),
    chat_id integer references chat(id),
    primary key (link_id, chat_id)
)
