CREATE EXTENSION IF NOT EXISTS pgcrypto;

CREATE TABLE chat_message (
                              id uuid DEFAULT gen_random_uuid() NOT NULL,
                              room_id uuid NOT NULL,
                              sender_id int8 NOT NULL,
                              "content" text NOT NULL,
                              sent_at timestamptz DEFAULT CURRENT_TIMESTAMP NULL,
                              CONSTRAINT chat_message_pkey PRIMARY KEY (id)
);

CREATE TABLE chat_room (
                           id uuid DEFAULT gen_random_uuid() NOT NULL,
                           "name" varchar(255) NOT NULL,
                           created_at timestamptz DEFAULT CURRENT_TIMESTAMP NULL,
                           CONSTRAINT chat_room_pkey PRIMARY KEY (id)
);

CREATE TABLE chat_room_member (
                                  id uuid DEFAULT gen_random_uuid() NOT NULL,
                                  room_id uuid NOT NULL,
                                  user_id int8 NOT NULL,
                                  joined_at timestamptz DEFAULT CURRENT_TIMESTAMP NULL,
                                  CONSTRAINT chat_room_member_pkey PRIMARY KEY (id)
);