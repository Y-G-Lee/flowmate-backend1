-- 기존 테이블에 name 컬럼이 없다면 추가 (회원가입 화면에서 이름을 받기 때문)
ALTER TABLE users ADD COLUMN IF NOT EXISTS name VARCHAR(50) NOT NULL DEFAULT '';

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);
