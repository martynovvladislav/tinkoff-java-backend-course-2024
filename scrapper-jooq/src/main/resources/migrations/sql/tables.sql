create table link
(
    id serial primary key,
    url varchar not null unique,
    updated_at timestamp with time zone not null,
    last_checked_at timestamp with time zone not null,
    last_commit_sha varchar,
    answers_count bigint
);
create table chat
(
    chat_id bigint primary key
);
create table chat_link
(
    chat_id bigint references chat(chat_id),
    link_id bigint references link(id),
    primary key (chat_id, link_id)
)

