-- Flowmate 실제 DB 구조 (참고용)
-- users → team_members → teams → projects → tasks

ALTER TABLE users ADD COLUMN IF NOT EXISTS name VARCHAR(50) NOT NULL DEFAULT '';

CREATE INDEX IF NOT EXISTS idx_users_email ON users (email);

-- teams, team_members, projects, tasks 테이블은 IDE/DB에서 이미 생성된 상태를 사용합니다.
-- projects.deadline 컬럼은 DB에 이미 존재합니다.

CREATE TABLE IF NOT EXISTS project_members (
    id SERIAL PRIMARY KEY,
    project_id INTEGER NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    user_id INTEGER NOT NULL REFERENCES users(id),
    role VARCHAR(20) NOT NULL DEFAULT 'MEMBER',
    joined_at TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_project_members_project_user UNIQUE (project_id, user_id)
);

CREATE INDEX IF NOT EXISTS idx_project_members_project_id ON project_members (project_id);
CREATE INDEX IF NOT EXISTS idx_project_members_user_id ON project_members (user_id);

-- 기존 프로젝트 생성자를 OWNER로 백필
INSERT INTO project_members (project_id, user_id, role, joined_at)
SELECT p.id, p.created_by, 'OWNER', p.created_at
FROM projects p
WHERE p.created_by IS NOT NULL
  AND NOT EXISTS (
      SELECT 1
      FROM project_members pm
      WHERE pm.project_id = p.id
        AND pm.user_id = p.created_by
  );

-- 같은 팀 멤버를 MEMBER로 백필
INSERT INTO project_members (project_id, user_id, role, joined_at)
SELECT p.id, tm.user_id, 'MEMBER', tm.joined_at
FROM projects p
INNER JOIN team_members tm ON tm.team_id = p.team_id
WHERE NOT EXISTS (
    SELECT 1
    FROM project_members pm
    WHERE pm.project_id = p.id
      AND pm.user_id = tm.user_id
);
