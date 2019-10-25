CREATE DATABASE  IF NOT EXISTS `rankcare` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `rankcare`;
-- MySQL dump 10.13  Distrib 8.0.17, for macos10.14 (x86_64)
--
-- Host: localhost    Database: rankcare
-- ------------------------------------------------------
-- Server version	8.0.17

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `consumption_data`
--

DROP TABLE IF EXISTS `consumption_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `consumption_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `age_grp` varchar(20) DEFAULT NULL,
  `body_wt_avg` varchar(20) DEFAULT NULL,
  `ci_data_1` varchar(10) DEFAULT NULL,
  `soil_inv_avg` varchar(20) DEFAULT NULL,
  `water_cons_avg` varchar(20) DEFAULT NULL,
  `ci_data_2` varchar(10) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `age_from` int(11) DEFAULT NULL,
  `age_to` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `consumption_data`
--

LOCK TABLES `consumption_data` WRITE;
/*!40000 ALTER TABLE `consumption_data` DISABLE KEYS */;
INSERT INTO `consumption_data` VALUES (1,'0-3','11','13','100','0.3','0.9','2019-09-20 10:33:49','2019-09-20 17:42:28',NULL,NULL),(2,'3-6','24','33','100','0.4','1.1','2019-09-20 10:33:49','2019-09-20 17:42:28',NULL,NULL),(3,'6-10','24','33','100','0.5','1.3','2019-09-20 10:33:49','2019-09-20 17:42:28',NULL,NULL),(4,'10-18','60','83','50','0.7','1.7','2019-09-20 10:33:49','2019-10-16 10:32:40',NULL,NULL),(5,'18+','85.2','114','50','0.85','2.8','2019-10-16 10:40:37','2019-10-16 10:40:37',NULL,NULL);
/*!40000 ALTER TABLE `consumption_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(60) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_nb4h0p6txrmfc0xbrd1kglp9t` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (2,'ROLE_ADMIN'),(1,'ROLE_CLIENT');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_calculation`
--

DROP TABLE IF EXISTS `site_calculation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_calculation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chemical_id` int(11) DEFAULT NULL,
  `contamination_type` varchar(45) DEFAULT NULL,
  `contamination_value` varchar(8) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `active_yn` varchar(1) DEFAULT 'Y',
  `site_id` bigint(20) NOT NULL,
  `measuring_unit` varchar(10) DEFAULT NULL,
  `chemical_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_calculation`
--

LOCK TABLES `site_calculation` WRITE;
/*!40000 ALTER TABLE `site_calculation` DISABLE KEYS */;
INSERT INTO `site_calculation` VALUES (11,2,'soil','2','2019-10-20 11:31:26','2019-10-20 11:31:26','Y',1,'mg',NULL),(12,3,'water','4.5','2019-10-20 11:31:26','2019-10-20 11:31:26','Y',1,'mg',NULL),(13,2,'soil','2','2019-10-20 11:32:49','2019-10-20 11:32:49','Y',2,'mg',NULL),(14,3,'water','4.5','2019-10-20 11:32:49','2019-10-20 11:32:49','Y',2,'mg',NULL),(15,2,'soil','2','2019-10-20 11:45:48','2019-10-20 11:45:48','Y',3,'mg',NULL),(16,4,'water','1','2019-10-20 11:45:48','2019-10-20 11:45:48','Y',3,'mg',NULL),(17,5,'soil','2','2019-10-22 06:11:03','2019-10-22 06:11:03','Y',4,'mg',NULL),(18,4,'water','1','2019-10-22 06:11:03','2019-10-22 06:11:03','Y',4,'mg',NULL),(19,3,'soil','2','2019-10-22 06:11:03','2019-10-22 06:11:03','Y',4,'mg',NULL),(20,3,'water','6','2019-10-22 06:11:03','2019-10-22 06:11:03','Y',4,'mg',NULL),(21,5,'soil','0.6','2019-10-23 04:52:50','2019-10-23 04:52:50','Y',5,'mg',NULL),(22,5,'water','0.2','2019-10-23 04:52:50','2019-10-23 04:52:50','Y',5,'mg',NULL),(23,2,'soil','0.8','2019-10-23 04:52:50','2019-10-23 04:52:50','Y',5,'mg',NULL),(24,2,'water','0.4','2019-10-23 04:52:50','2019-10-23 04:52:50','Y',5,'mg',NULL),(25,4,'soil','2','2019-10-23 04:52:50','2019-10-23 04:52:50','Y',5,'mg',NULL),(26,3,'soil','2','2019-10-25 05:45:12','2019-10-25 05:45:12','Y',6,'mg',NULL),(27,2,'water','1.3','2019-10-25 05:45:12','2019-10-25 05:45:12','Y',6,'μg',NULL),(28,2,'soil','0.002','2019-10-25 08:00:16','2019-10-25 08:00:16','Y',7,'mg',NULL),(29,2,'water','0.05','2019-10-25 08:00:16','2019-10-25 08:00:16','Y',7,'mg',NULL);
/*!40000 ALTER TABLE `site_calculation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `site_data`
--

DROP TABLE IF EXISTS `site_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `site_data` (
  `site_name` varchar(40) DEFAULT NULL,
  `site_location` varchar(40) DEFAULT NULL,
  `site_state` varchar(15) DEFAULT NULL,
  `site_org` varchar(40) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `site_id` varchar(40) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `site_data`
--

LOCK TABLES `site_data` WRITE;
/*!40000 ALTER TABLE `site_data` DISABLE KEYS */;
INSERT INTO `site_data` VALUES ('Test Site1','Test Location','Test State','Test Org','2019-10-20 11:31:26','2019-10-20 11:31:26',1,NULL),('Test 2','Test Location 2','Test State 2','Test Org 2','2019-10-20 11:45:48','2019-10-20 11:45:48',3,NULL),('New Site','New Location','New State','New Org','2019-10-22 06:11:03','2019-10-22 06:11:03',4,NULL),('Working Site','working location','working state','workign or','2019-10-23 04:52:50','2019-10-23 04:52:50',5,NULL),('Another Site','Sydney','ML','Aus','2019-10-25 05:45:12','2019-10-25 05:45:12',6,NULL),('test4','t4','sa','crc','2019-10-25 08:00:16','2019-10-25 08:00:16',7,NULL);
/*!40000 ALTER TABLE `site_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `toxicity_data`
--

DROP TABLE IF EXISTS `toxicity_data`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `toxicity_data` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `chemical_name` varchar(45) DEFAULT NULL,
  `chemical_formula` varchar(45) DEFAULT NULL,
  `soil_guideline` varchar(20) DEFAULT NULL,
  `soil_ref` varchar(20) DEFAULT NULL,
  `water_guideline` varchar(20) DEFAULT NULL,
  `water_ref` varchar(20) DEFAULT NULL,
  `dosage_ref` varchar(20) DEFAULT NULL,
  `reference` varchar(20) DEFAULT NULL,
  `cancer_slope_factor` varchar(20) DEFAULT NULL,
  `cancer_slope_ref` varchar(20) DEFAULT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `toxicity_data`
--

LOCK TABLES `toxicity_data` WRITE;
/*!40000 ALTER TABLE `toxicity_data` DISABLE KEYS */;
INSERT INTO `toxicity_data` VALUES (2,'Arsenic','Ar','10','NEPM','20','NEPM','30','IRIS','40','IRIS','2019-09-22 20:14:32','2019-09-22 20:14:32'),(3,'Cadmium','Cd','15','NEPM','24','NEPM','20','IRIS','20','IRIS','2019-09-22 20:14:32','2019-09-22 20:14:32'),(4,'DDT','Mg','8','NEPMM','12','NEPM','16','IRIS','12','IRIS','2019-09-22 20:14:32','2019-10-16 08:27:31'),(5,'Pd','Mg','1','RMM','2','RSS','3','DSD','123','ASDAS','2019-10-06 01:43:01','2019-10-16 08:26:40');
/*!40000 ALTER TABLE `toxicity_data` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_roles`
--

DROP TABLE IF EXISTS `user_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user_roles` (
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`user_id`,`role_id`),
  KEY `FKh8ciramu9cc9q3qcqiv4ue8a6` (`role_id`),
  CONSTRAINT `FKh8ciramu9cc9q3qcqiv4ue8a6` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`),
  CONSTRAINT `FKhfh9dx7w3ubf1co1vdev94g3f` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_roles`
--

LOCK TABLES `user_roles` WRITE;
/*!40000 ALTER TABLE `user_roles` DISABLE KEYS */;
INSERT INTO `user_roles` VALUES (10,1),(11,1),(13,1),(1,2);
/*!40000 ALTER TABLE `user_roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `designation` varchar(100) DEFAULT NULL,
  `email` varchar(40) DEFAULT NULL,
  `name` varchar(40) DEFAULT NULL,
  `organization` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `phone_number` varchar(15) DEFAULT NULL,
  `username` varchar(15) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr43af9ap4edm43mmtq01oddj6` (`username`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'2019-09-01 00:45:02','2019-09-01 00:45:02','desg','abc@gmail.com','test name','org','$2a$10$fRVE16MV8chlVYMxJ/MK0u/sILQT0FZahF5mpthbQ0ov7haXXlbcm','1234567899','admin'),(10,'2019-10-05 10:23:44','2019-10-24 08:21:45','asdasd','client1@admin.com','Client One','asd','$2a$10$3rhzYc4K8KqaYInLNQHaee3d5nrnLYg/GpciHqmnbbNWI8GEj1YSi','32423423423','client1'),(11,'2019-10-05 10:24:08','2019-10-05 19:55:59','adsadasd','client2@admin.com','Client Two','Orga','$2a$10$s/mULbRUFez17r8wN/UuHu.fycYa9EVj/OxnpRoWdI/vmqWq.SeEC','3423423423','client2'),(13,'2019-10-23 09:16:49','2019-10-23 09:35:41','Lead','krishnakapil@gmail.com','Satya krishna kapil Tadikonda','Home','$2a$10$lK1p.5PmDv60BITP5L7SKu8R07DtFZA065y43EtX1AeMMqBjFVFx6','4044349478','krishna');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-10-25  1:36:54
