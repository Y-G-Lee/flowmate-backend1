-- 샘플 데이터 (teams → team_members → projects → tasks 구조)
-- 가입된 users가 있을 때 실행

INSERT INTO teams (name, description, created_by)
SELECT 'Product Team', 'Flowmate product squad', u.id
FROM users u
WHERE NOT EXISTS (SELECT 1 FROM teams WHERE name = 'Product Team')
LIMIT 1;

INSERT INTO team_members (team_id, user_id, team_role, joined_at)
SELECT te.id, u.id, 'MEMBER', NOW()
FROM teams te
CROSS JOIN users u
WHERE te.name = 'Product Team'
  AND NOT EXISTS (
      SELECT 1 FROM team_members tm
      WHERE tm.team_id = te.id AND tm.user_id = u.id
  );

INSERT INTO projects (team_id, name, description, status, created_by, created_at)
SELECT te.id, 'Flowmate MVP', '협업 프로젝트 관리 MVP', 'IN_PROGRESS', u.id, NOW()
FROM teams te, users u
WHERE te.name = 'Product Team'
  AND NOT EXISTS (SELECT 1 FROM projects WHERE name = 'Flowmate MVP')
LIMIT 1;

INSERT INTO tasks (project_id, title, status, assignee_id, priority, due_date, created_by, created_at)
SELECT p.id, '로그인 API 연동', 'IN_PROGRESS', u.id, 'HIGH', CURRENT_DATE, u.id, NOW()
FROM projects p, users u
WHERE p.name = 'Flowmate MVP'
  AND NOT EXISTS (SELECT 1 FROM tasks WHERE title = '로그인 API 연동')
LIMIT 1;

INSERT INTO tasks (project_id, title, status, assignee_id, priority, due_date, created_by, created_at)
SELECT p.id, '대시보드 API 구현', 'TODO', u.id, 'MEDIUM', CURRENT_DATE + 1, u.id, NOW()
FROM projects p, users u
WHERE p.name = 'Flowmate MVP'
  AND NOT EXISTS (SELECT 1 FROM tasks WHERE title = '대시보드 API 구현')
LIMIT 1;

INSERT INTO tasks (project_id, title, status, assignee_id, priority, due_date, created_by, created_at)
SELECT p.id, '이메일 중복 확인 API', 'DONE', u.id, 'MEDIUM', CURRENT_DATE - 1, u.id, NOW()
FROM projects p, users u
WHERE p.name = 'Flowmate MVP'
  AND NOT EXISTS (SELECT 1 FROM tasks WHERE title = '이메일 중복 확인 API')
LIMIT 1;
