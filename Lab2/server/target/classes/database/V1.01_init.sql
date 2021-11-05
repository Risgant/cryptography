CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE app_user (
    id uuid NOT NULL DEFAULT uuid_generate_v1(),
    name varchar NOT NULL,
    password varchar NOT NULL,
    CONSTRAINT app_user_pkey PRIMARY KEY (id),
    CONSTRAINT app_user_uniq UNIQUE ("name")
);

CREATE TABLE note (
    id uuid NOT NULL DEFAULT uuid_generate_v1(),
    user_id uuid NOT NULL,
    text varchar NOT NULL,
    CONSTRAINT note_pkey PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES app_user (id)
);