/*
SQLyog Enterprise Trial - MySQL GUI v7.11 
MySQL - 5.5.27 : Database - robotmanage
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

CREATE DATABASE /*!32312 IF NOT EXISTS*/`robotmanage` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `robotmanage`;

/*Table structure for table `robot` */

DROP TABLE IF EXISTS `robot`;

CREATE TABLE `robot` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) DEFAULT NULL,
  `address` varchar(200) NOT NULL,
  `model` varchar(200) DEFAULT NULL,
  `state` int(11) DEFAULT NULL,
  `uniqueId` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
