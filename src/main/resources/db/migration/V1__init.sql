create table orders (
    id uuid not null primary key,
    client_id uuid not null,
    description varchar(255) not null,
    total_value float
);
create table outbox_messages (
    id uuid not null primary key,
    type varchar(50) not null,
    aggregate_type varchar(50) not null,
    aggregate_id uuid not null,
    payload jsonb not null
);