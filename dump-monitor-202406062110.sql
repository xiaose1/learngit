-- MySQL dump 10.13  Distrib 8.0.35, for Win64 (x86_64)
--
-- Host: localhost    Database: monitor
-- ------------------------------------------------------
-- Server version	8.0.35

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `equipment`
--

DROP TABLE IF EXISTS `equipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `equipment` (
  `equipment_id` int NOT NULL AUTO_INCREMENT COMMENT '设备ID',
  `equipment_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备名',
  `equipment_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '设备状态 0为关闭 1为开启',
  `equipment_plant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '设备监听的植物名',
  `equipment_ip` varchar(15) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci DEFAULT NULL COMMENT '设备IP地址',
  `equipment_plant_id` int NOT NULL DEFAULT '0' COMMENT '设备监听的植物ID',
  `equipment_user_id` int NOT NULL DEFAULT '0' COMMENT '设备所属用户ID',
  `equipment_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '设备创建时间',
  `equipment_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '设备更新时间',
  PRIMARY KEY (`equipment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `equipment`
--

LOCK TABLES `equipment` WRITE;
/*!40000 ALTER TABLE `equipment` DISABLE KEYS */;
INSERT INTO `equipment` VALUES (3,'监听青草',0,'青草','127.0.0.1',1,1,'2024-06-05 05:57:22','2024-06-05 06:18:38'),(5,'未命名',1,'花','127.0.0.1',2,1,'2024-06-05 06:17:18','2024-06-05 15:51:41'),(7,'未命名',1,'测试','127.0.0.1',7,1,'2024-06-05 15:48:30','2024-06-06 12:46:35'),(8,'未命名',0,'花','127.0.0.1',2,3,'2024-06-05 16:31:01','2024-06-05 16:31:01'),(9,'未命名',0,'草','127.0.0.1',1,8,'2024-06-06 12:26:57','2024-06-06 12:27:15');
/*!40000 ALTER TABLE `equipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `plants`
--

DROP TABLE IF EXISTS `plants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `plants` (
  `plant_id` int NOT NULL AUTO_INCREMENT COMMENT '植物ID',
  `plant_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '植物名',
  `plant_image_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '植物图片uri',
  `plant_information` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci COMMENT '植物信息',
  `plant_temperature` decimal(5,2) NOT NULL COMMENT '植物适应温度',
  `plant_humidity` decimal(5,2) NOT NULL COMMENT '植物适应湿度',
  `plant_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '植物创建时间',
  `plant_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '植物更新时间',
  PRIMARY KEY (`plant_id`),
  KEY `idx_plant_information` (`plant_information`(255))
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `plants`
--

LOCK TABLES `plants` WRITE;
/*!40000 ALTER TABLE `plants` DISABLE KEYS */;
INSERT INTO `plants` VALUES (1,'草','/images/e9984fbabb154effb9dc85810026fc78.jpeg','为食草动物提供食物的绿色草本植物群，常多由具窄叶的禾本科，莎草科和灯心草科等单子叶植物组成',28.00,88.00,'2024-06-03 08:16:22','2024-06-06 12:24:40'),(2,'花','/images/646a3f015c7b4db6a79f6411b099684f.jpg','一种好看的植物',27.00,80.00,'2024-06-05 10:21:49','2024-06-05 10:21:49'),(7,'测试','/images/f463f2bb3b764851821600565a6381b0.jpg','测试',60.00,50.00,'2024-06-06 12:46:23','2024-06-06 12:46:23');
/*!40000 ALTER TABLE `plants` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `user_id` int NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `user_nickname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs DEFAULT NULL COMMENT '用户昵称',
  `user_username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户名',
  `user_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_as_cs NOT NULL COMMENT '用户密码',
  `user_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户手机号',
  `user_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户状态（0为正常，1为封禁）',
  `user_create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '用户创建时间',
  `user_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '用户更新时间',
  `user_role` tinyint(1) NOT NULL DEFAULT '0' COMMENT '用户角色（-1为封禁用户，0为普通用户，1为管理员）',
  `is_delete` tinyint NOT NULL DEFAULT '0' COMMENT '逻辑删除（0为存在，1为删除）',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_as_cs;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'林叁柒','lin037','cc6c1c15d92e1b86bd3d1ad5c2b15442','18476095076',0,'2024-05-27 13:22:56','2024-06-05 13:42:40',1,0),(3,'林叁柒','Lin037','cc6c1c15d92e1b86bd3d1ad5c2b15442','18476095075',1,'2024-06-01 05:56:43','2024-06-05 02:32:48',0,0),(4,'叁柒','123456','cc6c1c15d92e1b86bd3d1ad5c2b15442','18476095073',1,'2024-06-01 06:51:19','2024-06-05 02:07:10',0,1),(5,'lin037','lin0370','cc6c1c15d92e1b86bd3d1ad5c2b15442','18476095077',1,'2024-06-03 13:10:27','2024-06-04 16:46:54',0,1),(6,'12','12345','e7dfa810c2ad73d39c98d9e530d6a42c','13512345678',0,'2024-06-05 02:00:35','2024-06-05 02:01:31',0,1),(7,'llll','哇啊啊啊1','849478c3ce2525ca7e04404eadcd0f10','13380788370',1,'2024-06-06 12:19:55','2024-06-06 12:20:18',0,1),(8,'666','666','185a34983e3e578442d8c30475a157fe','13380788370',0,'2024-06-06 12:26:12','2024-06-06 12:26:27',0,0);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'monitor'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-06-06 21:10:45
