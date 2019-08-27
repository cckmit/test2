/*
SQLyog Enterprise Trial - MySQL GUI v7.11 
MySQL - 5.6.41 : Database - robotmanage
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`robotmanage` /*!40100 DEFAULT CHARACTER SET utf8 COLLATE utf8_bin */;

USE `robotmanage`;

/*Table structure for table `armscreen` */

DROP TABLE IF EXISTS `armscreen`;

CREATE TABLE `armscreen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `armId` int(11) DEFAULT NULL,
  `robotId` int(11) DEFAULT NULL,
  `times` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `timesHistory` int(11) DEFAULT NULL,
  `totalHistory` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `armscreen` */

insert  into `armscreen`(`id`,`armId`,`robotId`,`times`,`total`,`timesHistory`,`totalHistory`) values (1,5,1,5,5,0,0),(2,5,2,0,0,0,0),(3,5,3,0,0,0,0),(4,5,4,0,0,0,0),(5,6,1,0,0,0,0),(6,6,2,0,0,0,0),(7,6,3,0,0,0,0),(8,6,4,0,0,0,0),(9,7,1,0,0,0,0),(10,7,2,0,0,0,0),(11,7,3,0,0,0,0),(12,7,4,0,0,0,0);

/*Table structure for table `attributedefinition` */

DROP TABLE IF EXISTS `attributedefinition`;

CREATE TABLE `attributedefinition` (
  `attdef_id` int(11) NOT NULL AUTO_INCREMENT,
  `attribute` varchar(200) COLLATE utf8_bin DEFAULT '',
  `attribute_type` int(11) DEFAULT '0',
  `string_value` varchar(200) COLLATE utf8_bin DEFAULT '',
  `int_value` int(11) DEFAULT '0',
  `float_value` float DEFAULT '0',
  `double_value` double DEFAULT '0',
  `boolean_value` tinyint(1) DEFAULT '0',
  `robot_uniqueId` varchar(200) COLLATE utf8_bin DEFAULT '',
  PRIMARY KEY (`attdef_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `attributedefinition` */

insert  into `attributedefinition`(`attdef_id`,`attribute`,`attribute_type`,`string_value`,`int_value`,`float_value`,`double_value`,`boolean_value`,`robot_uniqueId`) values (8,'ware',1,'hehe',0,0,0,0,'123'),(9,'ware',1,'enen',0,0,0,0,'321');

/*Table structure for table `drink` */

DROP TABLE IF EXISTS `drink`;

CREATE TABLE `drink` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `type` int(11) DEFAULT NULL,
  `storage_type` int(11) DEFAULT NULL,
  `amount` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Data for the table `drink` */

insert  into `drink`(`id`,`type`,`storage_type`,`amount`) values (1,1,1,20),(2,2,1,30),(3,3,1,1),(4,4,1,1),(5,1,2,1),(6,2,2,3),(7,3,2,0),(8,4,2,3);

/*Table structure for table `location` */

DROP TABLE IF EXISTS `location`;

CREATE TABLE `location` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `floor` varchar(100) NOT NULL DEFAULT '',
  `name` varchar(200) NOT NULL DEFAULT '',
  `w` double NOT NULL DEFAULT '0',
  `x` double NOT NULL DEFAULT '0',
  `y` double NOT NULL DEFAULT '0',
  `z` double NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`floor`,`name`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

/*Data for the table `location` */

insert  into `location`(`id`,`floor`,`name`,`w`,`x`,`y`,`z`) values (1,'1','A1_START',0.654,-1.127,8.194,0.756),(2,'1','A1_TERMINAL',0.778,1.56,1.267,-0.629),(3,'1','A1_IDLE_1',-0.174,-2.093,5.028,0.985),(4,'3.4','A1_IDLE_2',-0.174,-2.093,5.028,0.985),(5,'1','A2-START',0.761,2.58,1.459,-0.649),(6,'38','A2-IDLE-1',0.769,3.667,4.541,0.639),(7,'23','A2-IDLE-2',0.769,3.667,4.541,0.639),(8,'3.4','A2-TABLE-1',-0.041,4.952,3.294,0.999),(9,'3.4','A2-TABLE-2',0.731,4.151,1.357,-0.683),(10,'MOVE_TO','idelPoint',-0.041,4.952,3.294,0.999),(11,'calling_clean_table','Table001',0.761,2.58,1.459,-0.649),(12,'dump_dish','recyclePoi',0.654,-1.127,8.194,0.756),(13,'0','new marker',1,-1.374411,0.417879,0);

/*Table structure for table `modelactiongoal` */

DROP TABLE IF EXISTS `modelactiongoal`;

CREATE TABLE `modelactiongoal` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `modelactiongoal` */

/*Table structure for table `orders` */

DROP TABLE IF EXISTS `orders`;

CREATE TABLE `orders` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `table_number` int(11) DEFAULT NULL,
  `has_pending_order` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `orders` */

insert  into `orders`(`id`,`name`,`table_number`,`has_pending_order`) values (1,'name',3,0);

/*Table structure for table `orderstatus` */

DROP TABLE IF EXISTS `orderstatus`;

CREATE TABLE `orderstatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `table_number` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `orders_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `orders_id` (`orders_id`),
  CONSTRAINT `orderstatus_ibfk_1` FOREIGN KEY (`orders_id`) REFERENCES `orders` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `orderstatus` */

insert  into `orderstatus`(`id`,`table_number`,`status`,`orders_id`) values (1,3,1,1);

/*Table structure for table `robot` */

DROP TABLE IF EXISTS `robot`;

CREATE TABLE `robot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(200) NOT NULL,
  `model` varchar(200) DEFAULT NULL,
  `firmware` varchar(200) DEFAULT NULL,
  `version` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `uniqueId` varchar(100) DEFAULT NULL,
  `robotType` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*Data for the table `robot` */

insert  into `robot`(`id`,`name`,`address`,`model`,`firmware`,`version`,`status`,`uniqueId`,`robotType`) values (1,'A1','192.168.100.100:9090','A1','firmware','1.0',10,'1',0),(2,'A1','192.168.100.100:9090','A1','firmware','1',10,'2',0),(3,'A2','192.168.100.91:9090','A2','firmware','1.0',10,'3',0),(4,'A2','192.168.100.92:9090','A2','firmware','1.0',10,'4',0);

/*Table structure for table `robotarm` */

DROP TABLE IF EXISTS `robotarm`;

CREATE TABLE `robotarm` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `loadCounts` int(11) DEFAULT NULL,
  `loadTotal` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `robotarm` */

insert  into `robotarm`(`id`,`loadCounts`,`loadTotal`) values (1,30,30);

/*Table structure for table `robotcount` */

DROP TABLE IF EXISTS `robotcount`;

CREATE TABLE `robotcount` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `runDistanceTotal` int(11) DEFAULT NULL,
  `transportTotal` int(11) DEFAULT NULL,
  `robot_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `robot_id` (`robot_id`),
  CONSTRAINT `robotcount_ibfk_1` FOREIGN KEY (`robot_id`) REFERENCES `robot` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `robotcount` */

insert  into `robotcount`(`id`,`runDistanceTotal`,`transportTotal`,`robot_id`) values (1,20,20,1);

/*Table structure for table `robotscreen` */

DROP TABLE IF EXISTS `robotscreen`;

CREATE TABLE `robotscreen` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `robotId` int(11) DEFAULT NULL,
  `total` int(11) DEFAULT NULL,
  `totalHistory` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `robotscreen` */

insert  into `robotscreen`(`id`,`robotId`,`total`,`totalHistory`) values (1,1,5,0),(2,2,0,0),(3,3,0,0),(4,4,0,0);

/*Table structure for table `screenip` */

DROP TABLE IF EXISTS `screenip`;

CREATE TABLE `screenip` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `screenIP` varchar(100) COLLATE utf8_bin DEFAULT NULL,
  `port` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `screenip` */

insert  into `screenip`(`id`,`screenIP`,`port`) values (1,'192.168.100.119',11999);

/*Table structure for table `servicestatus` */

DROP TABLE IF EXISTS `servicestatus`;

CREATE TABLE `servicestatus` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `out_of_service` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `servicestatus` */

insert  into `servicestatus`(`id`,`out_of_service`) values (1,1);

/*Table structure for table `systemconfiguration` */

DROP TABLE IF EXISTS `systemconfiguration`;

CREATE TABLE `systemconfiguration` (
  `conf_id` int(11) NOT NULL AUTO_INCREMENT,
  `conf_name` varchar(200) COLLATE utf8_bin DEFAULT '',
  `conf_type` int(11) DEFAULT '0',
  `string_value` varchar(200) COLLATE utf8_bin DEFAULT '',
  `int_value` int(11) DEFAULT '0',
  `float_value` float DEFAULT '0',
  `double_value` double DEFAULT '0',
  `boolean_value` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`conf_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

/*Data for the table `systemconfiguration` */

insert  into `systemconfiguration`(`conf_id`,`conf_name`,`conf_type`,`string_value`,`int_value`,`float_value`,`double_value`,`boolean_value`) values (1,'store_id',2,'',0,0,0,0),(2,'T1',2,'',7,0,0,0),(3,'T2',2,'',10,0,0,0),(4,'T3',2,'',5,0,0,0),(5,'T4',2,'',5,0,0,0),(6,'T5',2,'',30,0,0,0);

/*Table structure for table `user` */

DROP TABLE IF EXISTS `user`;

CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(100) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `token` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `user` */

insert  into `user`(`id`,`name`,`password`,`status`,`token`) values (1,'jerry','123456',1,'admintoken');

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
