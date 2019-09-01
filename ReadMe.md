### Sql Query

Run this query after running Spring Boot application to add users

```sql
INSERT IGNORE INTO roles(name) VALUES('ROLE_CLIENT');
INSERT IGNORE INTO roles(name) VALUES('ROLE_ADMIN');
```
insert into user_roles values (1,2);
insert into users values('1', now(),now(), 'desg', 'abc@gmail.com', 'test name', 'org', 'pwd', '1234567899', 'admin');


CREATE TABLE `rank_care`.`site_data` (
  `site_id` VARCHAR(20) NOT NULL,
  `site_name` VARCHAR(40) NULL,
  `site_location` VARCHAR(40) NULL,
  `site_state` VARCHAR(15) NULL,
  `site_org` VARCHAR(40) NULL,
  PRIMARY KEY (`site_id`));


CREATE TABLE `rank_care`.`consumption_data` (
  `id` INT NOT NULL,
  `age_grp` VARCHAR(20) NULL,
  `body_wt_avg` VARCHAR(20) NULL,
  `ci_data_1` VARCHAR(10) NULL,
  `soil_inv_avg` VARCHAR(20) NULL,
  `water_cons_avg` VARCHAR(20) NULL,
  `ci_data_2` VARCHAR(10) NULL,
  PRIMARY KEY (`id`));

CREATE TABLE `rank_care`.`toxicity_data` (
  `id` INT NOT NULL,
  `chemical_name` VARCHAR(45) NULL,
  `chemical_formula` VARCHAR(45) NULL,
  `soil_guideline` VARCHAR(20) NULL,
  `soil_ref` VARCHAR(20) NULL,
  `water_guideline` VARCHAR(20) NULL,
  `water_ref` VARCHAR(20) NULL,
  `dosage_ref` VARCHAR(20) NULL,
  `reference` VARCHAR(20) NULL,
  `cancer_slope_factor` VARCHAR(20) NULL,
  PRIMARY KEY (`id`));
