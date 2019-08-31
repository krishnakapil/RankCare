### Sql Query

Run this query after running Spring Boot application to add users

```sql
INSERT IGNORE INTO roles(name) VALUES('ROLE_CLIENT');
INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
```
insert into user_roles values (1,2);
insert into users values('1', now(),now(), 'desg', 'abc@gmail.com', 'test name', 'org', 'pwd', '1234567899', 'admin');
