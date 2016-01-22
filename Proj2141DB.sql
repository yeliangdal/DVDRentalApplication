-- MySQL dump 10.13  Distrib 5.6.21, for osx10.8 (x86_64)
--
-- Host: localhost    Database: project
-- ------------------------------------------------------
-- Server version	5.6.21

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL DEFAULT '0',
  `first_name` char(50) DEFAULT NULL,
  `last_name` char(50) DEFAULT NULL,
  `street_number` int(11) DEFAULT NULL,
  `street_name` char(50) DEFAULT NULL,
  `city` char(50) DEFAULT NULL,
  `area_code` int(11) DEFAULT NULL,
  `telephone_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`customer_id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES (1,'Randy','Herritt',64,'Burgess','Windsor',902,5551213),(2,'Ye','Liang',541,'Long Street','Timberlea',902,5558877),(3,'Hannah','Smith',123,'Portland','Dartmouth',902,5551234),(5,'Ted','Roosevelt',66,'Washington','Halifax',902,5555253),(6,'Frank','Hall',99,'Main','Halifax',902,5556272),(7,'Bob','Jones',55,'Wellington','Windsor',902,5559876),(8,'Candace','Herritt',64,'Burgess','Windsor',902,5551212);
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `inventory`
--

DROP TABLE IF EXISTS `inventory`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `inventory` (
  `dvdID` int(11) NOT NULL DEFAULT '0',
  `movieID` int(11) DEFAULT NULL,
  `inStock` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`dvdID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `inventory`
--

LOCK TABLES `inventory` WRITE;
/*!40000 ALTER TABLE `inventory` DISABLE KEYS */;
INSERT INTO `inventory` VALUES (1,58,1),(2,58,1),(3,58,1),(4,12,0),(5,12,1),(6,47,1),(7,47,1),(8,47,1),(9,47,1),(10,57,1),(11,2,1),(12,32,1),(13,6,1),(14,14,1),(15,14,1),(16,61,1),(17,61,1),(18,61,1),(19,48,1),(20,62,1),(21,65,1),(22,65,1),(23,9,1),(24,17,1),(25,17,1),(26,60,1),(27,60,1),(28,40,1),(29,36,1),(30,31,1),(31,55,1),(32,55,1),(33,33,1),(34,5,1),(35,4,1),(36,11,1),(37,11,1),(38,34,1),(39,34,1),(40,38,1),(41,38,1),(42,38,1),(43,35,1),(44,35,1),(45,26,1),(46,26,1),(47,18,1),(48,18,1),(49,18,1),(50,45,1),(51,46,1),(52,46,1),(53,44,1),(54,3,1),(55,3,1),(56,23,1),(57,56,1),(58,56,1),(59,53,1),(60,53,1),(61,54,1),(62,54,1),(63,43,1),(64,41,1),(65,41,1),(66,7,1),(67,30,1),(68,59,1),(69,16,1),(70,8,1),(71,21,1),(72,52,1),(73,52,1),(74,29,1),(75,22,1),(76,58,1),(78,24,1),(79,51,1),(80,27,1),(81,39,1),(82,50,1),(83,13,1),(84,66,1),(85,25,1),(86,19,1),(87,42,1),(88,63,1),(89,1,1),(90,1,1),(91,28,1),(92,20,1),(93,49,1),(94,49,1),(95,64,1),(96,15,1),(97,37,1),(99,10,1),(100,10,1);
/*!40000 ALTER TABLE `inventory` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `movies`
--

DROP TABLE IF EXISTS `movies`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `movies` (
  `movieID` int(11) DEFAULT NULL,
  `title` char(128) DEFAULT NULL,
  `year` int(11) DEFAULT NULL,
  `genre` char(128) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `movies`
--

LOCK TABLES `movies` WRITE;
/*!40000 ALTER TABLE `movies` DISABLE KEYS */;
INSERT INTO `movies` VALUES (1,'2001: A Space Odyssey',1968,'Science Fiction'),(2,'A Hard Day\'s Night',1964,'Musical'),(3,'A Nightmare on Elm Street',1984,'Horror'),(4,'Airplane!',1980,'Comedy'),(5,'Aliens',1986,'Action'),(6,'All About Eve',1950,'Comedy'),(7,'Anchorman: The Legend of Ron Burgandy',2004,'Comedy'),(8,'Annie',2014,'Musical'),(9,'Apocalypse Now',1979,'Action'),(10,'Back to the Future',1985,'Science Fiction'),(11,'Casablanca',1942,'Classic'),(12,'Citizen Kane',1941,'Classic'),(13,'Dawn of the Planet of the Apes',2014,'Suspense'),(14,'E.T. The Extra-Terrestrial',1982,'Science Fiction'),(15,'Elysium',2013,'Science Fiction'),(16,'Ferris Bueller\'s Day Off',1986,'Comedy'),(17,'Finding Nemo',2003,'Animated'),(18,'Frozen',2013,'Animated'),(19,'Godzilla',2014,'Science Fiction'),(20,'Gravity',2013,'Science Fiction'),(21,'Grease',1978,'Musical'),(22,'Grease 2',1982,'Musical'),(23,'Halloween',1978,'Horror'),(24,'How the Grinch Stole Christmas',2000,'Musical'),(25,'Inception',2010,'Suspense'),(26,'Indiana Jones and the Temple of Doom',1984,'Action'),(27,'Interstellar',2014,'Suspense'),(28,'Jurassic Park',1993,'Science Fiction'),(29,'Les Miserables',2012,'Musical'),(30,'Let\'s be Cops',2014,'Comedy'),(31,'Marry Poppins',1964,'Musical'),(32,'Modern Times',1936,'Comedy'),(33,'Mud',2013,'Drama'),(34,'Psycho',1960,'Horror'),(35,'Raiders of the Lost Ark',1981,'Action'),(36,'Rosemary\'s Baby',1976,'Horror'),(37,'Star Trek',2009,'Science Fiction'),(38,'Star Wars: Episode IV - A New Hope',1977,'Science Fiction'),(39,'Supernatural',2005,'Suspense'),(40,'Taxi Driver',1976,'Suspense'),(41,'The 40-Year-Old Virgin',2005,'Comedy'),(42,'The Avengers',2012,'Science Fiction'),(43,'The Big Lebowski',1998,'Comedy'),(44,'The Blair Witch Project',1999,'Horror'),(45,'The Dark Knight',2008,'Action'),(46,'The Exorcist',1973,'Horror'),(47,'The Godfather',1972,'Drama'),(48,'The Godfather 2',1974,'Drama'),(49,'The Matrix',1999,'Science Fiction'),(50,'The Maze Runner',2014,'Suspense'),(51,'The Phantom of the Opera',2004,'Musical'),(52,'The Rocky Horror Picture Show',1975,'Musical'),(53,'The Shining',1980,'Horror'),(54,'The Silence of the Lambs',1991,'Horror'),(55,'The Terminator',1984,'Action'),(56,'The Texas Chain Saw Massacre',1974,'Horror'),(57,'The Third Man',1949,'Mystery'),(58,'The Wizard of Oz',1939,'Classic'),(59,'There\'s Something About Mary',1998,'Comedy'),(60,'Toy Story',1995,'Animated'),(61,'Toy Story 2',1999,'Animated'),(62,'Toy Story 3',2010,'Animated'),(63,'Transcendence',2014,'Science Fiction'),(64,'Twelve Monkeys',1995,'Science Fiction'),(65,'Up',2009,'Animated'),(66,'X-Men: Days of Future Past',2014,'Suspense');
/*!40000 ALTER TABLE `movies` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rent_record`
--

DROP TABLE IF EXISTS `rent_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `rent_record` (
  `recordID` int(11) NOT NULL DEFAULT '0',
  `dvdID` int(11) DEFAULT NULL,
  `customer_id` int(11) DEFAULT NULL,
  `rentDate` date DEFAULT NULL,
  `returnDate` date DEFAULT NULL,
  PRIMARY KEY (`recordID`),
  KEY `dvdID` (`dvdID`),
  KEY `customer_id` (`customer_id`),
  CONSTRAINT `rent_record_ibfk_2` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  CONSTRAINT `rent_record_ibfk_3` FOREIGN KEY (`dvdID`) REFERENCES `inventory` (`dvdID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rent_record`
--

LOCK TABLES `rent_record` WRITE;
/*!40000 ALTER TABLE `rent_record` DISABLE KEYS */;
INSERT INTO `rent_record` VALUES (1,1,1,'2014-11-11','2014-11-23'),(2,3,3,'2014-11-20','2014-11-21'),(3,5,5,'2014-11-20','2014-11-22'),(4,7,2,'2014-11-20','2014-11-23'),(5,1,2,'2014-11-21','2014-11-23'),(6,4,2,'2014-11-21',NULL);
/*!40000 ALTER TABLE `rent_record` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2014-11-24 16:55:01
