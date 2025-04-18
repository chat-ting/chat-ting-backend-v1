-- 확장자 설치
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- 테이블 생성
CREATE TABLE chat_message (
                              id uuid DEFAULT gen_random_uuid() NOT NULL,
                              room_id uuid NOT NULL,
                              sender_id int8 NOT NULL,
                              content text NOT NULL,
                              sent_at timestamptz DEFAULT CURRENT_TIMESTAMP,
                              CONSTRAINT chat_message_pkey PRIMARY KEY (id)
);

CREATE TABLE chat_room (
                           id uuid DEFAULT gen_random_uuid() NOT NULL,
                           name varchar(255) NOT NULL,
                           created_at timestamptz DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT chat_room_pkey PRIMARY KEY (id)
);

CREATE TABLE chat_room_member (
                                  id uuid DEFAULT gen_random_uuid() NOT NULL,
                                  room_id uuid NOT NULL,
                                  user_id int8 NOT NULL,
                                  joined_at timestamptz DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT chat_room_member_pkey PRIMARY KEY (id)
);

-- 컬럼 존재 여부 확인 후 추가
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1
        FROM information_schema.columns
        WHERE table_name='chat_message'
          AND column_name='member_name'
    ) THEN
ALTER TABLE chat_message ADD COLUMN member_name TEXT;
END IF;
END $$;

-- null값 채우기
UPDATE chat_message SET member_name = '알 수 없음' WHERE member_name IS NULL;

-- NOT NULL 제약 추가
ALTER TABLE chat_message ALTER COLUMN member_name SET NOT NULL;
