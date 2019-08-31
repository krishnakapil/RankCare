### Sql Query

Run this query after running Spring Boot application to add users

```sql
INSERT IGNORE INTO roles(name) VALUES('ROLE_CLIENT');
INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
```