# Sequel Pro dump
# Version 2492
# http://code.google.com/p/sequel-pro
#
# Host: localhost (MySQL 5.1.47)
# Database: collabrootest
# Generation Time: 2011-03-17 23:26:41 -0400
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table composite_step_mapping
# ------------------------------------------------------------

CREATE TABLE `composite_step_mapping` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  `from_step` bigint(20) DEFAULT NULL,
  `parentsim` bigint(20) DEFAULT NULL,
  `to_step` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5340C513FC544E75` (`to_step`),
  KEY `FK5340C5137641ED22` (`parentsim`),
  KEY `FK5340C51345F5AAA6` (`from_step`),
  CONSTRAINT `FK5340C51345F5AAA6` FOREIGN KEY (`from_step`) REFERENCES `step` (`id`),
  CONSTRAINT `FK5340C5137641ED22` FOREIGN KEY (`parentsim`) REFERENCES `default_simulation` (`id`),
  CONSTRAINT `FK5340C513FC544E75` FOREIGN KEY (`to_step`) REFERENCES `step` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table default_scenario
# ------------------------------------------------------------

CREATE TABLE `default_scenario` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `last_step` int(11) DEFAULT NULL,
  `simulation` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKCBAD47CE70444036` (`simulation`),
  CONSTRAINT `FKCBAD47CE70444036` FOREIGN KEY (`simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;

LOCK TABLES `default_scenario` WRITE;
/*!40000 ALTER TABLE `default_scenario` DISABLE KEYS */;
INSERT INTO `default_scenario` (`dtype`,`id`,`created`,`version`,`last_step`,`simulation`)
VALUES
	('DefaultScenario',6,NULL,0,NULL,1);

/*!40000 ALTER TABLE `default_scenario` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table default_scenario_values_
# ------------------------------------------------------------

CREATE TABLE `default_scenario_values_` (
  `default_scenario` bigint(20) NOT NULL,
  `values_` bigint(20) NOT NULL,
  PRIMARY KEY (`default_scenario`,`values_`),
  KEY `FKB2E5480C64739986` (`default_scenario`),
  KEY `FKB2E5480CF18F00CC` (`values_`),
  CONSTRAINT `FKB2E5480CF18F00CC` FOREIGN KEY (`values_`) REFERENCES `tuple` (`id`),
  CONSTRAINT `FKB2E5480C64739986` FOREIGN KEY (`default_scenario`) REFERENCES `default_scenario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `default_scenario_values_` WRITE;
/*!40000 ALTER TABLE `default_scenario_values_` DISABLE KEYS */;
INSERT INTO `default_scenario_values_` (`default_scenario`,`values_`)
VALUES
	(6,13),
	(6,14),
	(6,15),
	(6,16),
	(6,17),
	(6,18),
	(6,19),
	(6,20),
	(6,21),
	(6,22),
	(6,23),
	(6,24),
	(6,25),
	(6,26),
	(6,27),
	(6,28),
	(6,29),
	(6,30),
	(6,31),
	(6,32),
	(6,33),
	(6,34),
	(6,35),
	(6,36),
	(6,37),
	(6,38);

/*!40000 ALTER TABLE `default_scenario_values_` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table default_simulation
# ------------------------------------------------------------

CREATE TABLE `default_simulation` (
  `dtype` varchar(31) NOT NULL,
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `created` datetime DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `simulation_version` bigint(20) NOT NULL,
  `url` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `many_to_one` int(11) DEFAULT NULL,
  `replication` int(11) DEFAULT NULL,
  `sampling_frequency` int(11) DEFAULT NULL,
  `executor_simulation` bigint(20) DEFAULT NULL,
  `indexing_variable` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKD244E2C5D32BC7A0` (`indexing_variable`),
  KEY `FKD244E2C549DCEB22` (`executor_simulation`),
  CONSTRAINT `FKD244E2C549DCEB22` FOREIGN KEY (`executor_simulation`) REFERENCES `default_simulation` (`id`),
  CONSTRAINT `FKD244E2C5D32BC7A0` FOREIGN KEY (`indexing_variable`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;

LOCK TABLES `default_simulation` WRITE;
/*!40000 ALTER TABLE `default_simulation` DISABLE KEYS */;
INSERT INTO `default_simulation` (`dtype`,`id`,`created`,`description`,`name`,`simulation_version`,`url`,`version`,`many_to_one`,`replication`,`sampling_frequency`,`executor_simulation`,`indexing_variable`)
VALUES
	('DefaultSimulation',1,'2011-03-15 00:00:00','Pangaea','Pangaea',1,'http://cognosis.mit.edu:8887/pangaea-servlet-0.1-SNAPSHOT/rest/',1,NULL,NULL,NULL,NULL,NULL);

/*!40000 ALTER TABLE `default_simulation` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table default_simulation_inputs
# ------------------------------------------------------------

CREATE TABLE `default_simulation_inputs` (
  `default_simulation` bigint(20) NOT NULL,
  `inputs` bigint(20) NOT NULL,
  PRIMARY KEY (`default_simulation`,`inputs`),
  KEY `FKEE0004A38F87FCF4` (`default_simulation`),
  KEY `FKEE0004A36008C17E` (`inputs`),
  CONSTRAINT `FKEE0004A36008C17E` FOREIGN KEY (`inputs`) REFERENCES `variable` (`id`),
  CONSTRAINT `FKEE0004A38F87FCF4` FOREIGN KEY (`default_simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `default_simulation_inputs` WRITE;
/*!40000 ALTER TABLE `default_simulation_inputs` DISABLE KEYS */;
INSERT INTO `default_simulation_inputs` (`default_simulation`,`inputs`)
VALUES
	(1,1),
	(1,2),
	(1,3),
	(1,4),
	(1,5),
	(1,6),
	(1,7),
	(1,8),
	(1,9),
	(1,10),
	(1,11),
	(1,12);

/*!40000 ALTER TABLE `default_simulation_inputs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table default_simulation_outputs
# ------------------------------------------------------------

CREATE TABLE `default_simulation_outputs` (
  `default_simulation` bigint(20) NOT NULL,
  `outputs` bigint(20) NOT NULL,
  PRIMARY KEY (`default_simulation`,`outputs`),
  KEY `FK1B8DFC188F87FCF4` (`default_simulation`),
  KEY `FK1B8DFC1864AB2727` (`outputs`),
  CONSTRAINT `FK1B8DFC1864AB2727` FOREIGN KEY (`outputs`) REFERENCES `variable` (`id`),
  CONSTRAINT `FK1B8DFC188F87FCF4` FOREIGN KEY (`default_simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

LOCK TABLES `default_simulation_outputs` WRITE;
/*!40000 ALTER TABLE `default_simulation_outputs` DISABLE KEYS */;
INSERT INTO `default_simulation_outputs` (`default_simulation`,`outputs`)
VALUES
	(1,13),
	(1,14),
	(1,15),
	(1,16),
	(1,17),
	(1,18),
	(1,19),
	(1,20),
	(1,21),
	(1,22),
	(1,23),
	(1,24),
	(1,25),
	(1,26);

/*!40000 ALTER TABLE `default_simulation_outputs` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table default_simulation_step_mapping
# ------------------------------------------------------------

CREATE TABLE `default_simulation_step_mapping` (
  `default_simulation` bigint(20) NOT NULL,
  `step_mapping` bigint(20) NOT NULL,
  PRIMARY KEY (`default_simulation`,`step_mapping`),
  KEY `FK45A2995CD1FD3FA` (`default_simulation`),
  KEY `FK45A2995B7DF814F` (`step_mapping`),
  CONSTRAINT `FK45A2995B7DF814F` FOREIGN KEY (`step_mapping`) REFERENCES `composite_step_mapping` (`id`),
  CONSTRAINT `FK45A2995CD1FD3FA` FOREIGN KEY (`default_simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table default_simulation_steps
# ------------------------------------------------------------

CREATE TABLE `default_simulation_steps` (
  `default_simulation` bigint(20) NOT NULL,
  `steps` bigint(20) NOT NULL,
  KEY `FKAD66312DCD1FD3FA` (`default_simulation`),
  KEY `FKAD66312D47B19B8C` (`steps`),
  CONSTRAINT `FKAD66312D47B19B8C` FOREIGN KEY (`steps`) REFERENCES `step` (`id`),
  CONSTRAINT `FKAD66312DCD1FD3FA` FOREIGN KEY (`default_simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table excel_simulation
# ------------------------------------------------------------

CREATE TABLE `excel_simulation` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `creation` datetime DEFAULT NULL,
  `file` blob,
  `version` int(11) DEFAULT NULL,
  `simulation` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKC2AE208F70444036` (`simulation`),
  CONSTRAINT `FKC2AE208F70444036` FOREIGN KEY (`simulation`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=latin1;



# Dump of table excel_simulation_inputs
# ------------------------------------------------------------

CREATE TABLE `excel_simulation_inputs` (
  `excel_simulation` bigint(20) NOT NULL,
  `inputs` bigint(20) NOT NULL,
  PRIMARY KEY (`excel_simulation`,`inputs`),
  KEY `FK4D58F994EDE5CD` (`excel_simulation`),
  KEY `FK4D58F9990EF755C` (`inputs`),
  CONSTRAINT `FK4D58F9990EF755C` FOREIGN KEY (`inputs`) REFERENCES `excel_variable` (`id`),
  CONSTRAINT `FK4D58F994EDE5CD` FOREIGN KEY (`excel_simulation`) REFERENCES `excel_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table excel_simulation_outputs
# ------------------------------------------------------------

CREATE TABLE `excel_simulation_outputs` (
  `excel_simulation` bigint(20) NOT NULL,
  `outputs` bigint(20) NOT NULL,
  PRIMARY KEY (`excel_simulation`,`outputs`),
  KEY `FKDF69CFE24EDE5CD` (`excel_simulation`),
  KEY `FKDF69CFE29591DB05` (`outputs`),
  CONSTRAINT `FKDF69CFE29591DB05` FOREIGN KEY (`outputs`) REFERENCES `excel_variable` (`id`),
  CONSTRAINT `FKDF69CFE24EDE5CD` FOREIGN KEY (`excel_simulation`) REFERENCES `excel_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table excel_variable
# ------------------------------------------------------------

CREATE TABLE `excel_variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `cell_range` varchar(255) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `worksheet_name` varchar(255) DEFAULT NULL,
  `excel_simulation` bigint(20) DEFAULT NULL,
  `simulation_variable` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6BBECF04146ED549` (`simulation_variable`),
  KEY `FK6BBECF044EDE5CD` (`excel_simulation`),
  CONSTRAINT `FK6BBECF044EDE5CD` FOREIGN KEY (`excel_simulation`) REFERENCES `excel_simulation` (`id`),
  CONSTRAINT `FK6BBECF04146ED549` FOREIGN KEY (`simulation_variable`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;



# Dump of table scenario_list
# ------------------------------------------------------------

CREATE TABLE `scenario_list` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;



# Dump of table scenario_list_scenarios
# ------------------------------------------------------------

CREATE TABLE `scenario_list_scenarios` (
  `scenario_list` bigint(20) NOT NULL,
  `scenarios` bigint(20) NOT NULL,
  PRIMARY KEY (`scenario_list`,`scenarios`),
  KEY `FKA4A66511FFAF509B` (`scenarios`),
  KEY `FKA4A6651133D18D4` (`scenario_list`),
  CONSTRAINT `FKA4A6651133D18D4` FOREIGN KEY (`scenario_list`) REFERENCES `scenario_list` (`id`),
  CONSTRAINT `FKA4A66511FFAF509B` FOREIGN KEY (`scenarios`) REFERENCES `default_scenario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table scenario_list_test_field
# ------------------------------------------------------------

CREATE TABLE `scenario_list_test_field` (
  `scenario_list` bigint(20) NOT NULL,
  `test_field` int(11) DEFAULT NULL,
  KEY `FKF49B729F33D18D4` (`scenario_list`),
  CONSTRAINT `FKF49B729F33D18D4` FOREIGN KEY (`scenario_list`) REFERENCES `scenario_list` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table step
# ------------------------------------------------------------

CREATE TABLE `step` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table step_scenario
# ------------------------------------------------------------

CREATE TABLE `step_scenario` (
  `default_scenario` bigint(20) NOT NULL,
  `child_scenarios` bigint(20) NOT NULL,
  `child_scenarios_key` bigint(20) NOT NULL,
  PRIMARY KEY (`default_scenario`,`child_scenarios_key`),
  KEY `FK16E183A38A6D1185` (`child_scenarios_key`),
  KEY `FK16E183A3CE017A0C` (`default_scenario`),
  KEY `FK16E183A3158EA467` (`child_scenarios`),
  CONSTRAINT `FK16E183A3158EA467` FOREIGN KEY (`child_scenarios`) REFERENCES `scenario_list` (`id`),
  CONSTRAINT `FK16E183A38A6D1185` FOREIGN KEY (`child_scenarios_key`) REFERENCES `step` (`id`),
  CONSTRAINT `FK16E183A3CE017A0C` FOREIGN KEY (`default_scenario`) REFERENCES `default_scenario` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table step_simulations
# ------------------------------------------------------------

CREATE TABLE `step_simulations` (
  `step` bigint(20) NOT NULL,
  `simulations` bigint(20) NOT NULL,
  PRIMARY KEY (`step`,`simulations`),
  KEY `FK9D62E596A66B57B` (`simulations`),
  KEY `FK9D62E59415CCF31` (`step`),
  CONSTRAINT `FK9D62E59415CCF31` FOREIGN KEY (`step`) REFERENCES `step` (`id`),
  CONSTRAINT `FK9D62E596A66B57B` FOREIGN KEY (`simulations`) REFERENCES `default_simulation` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table step_var_to_var
# ------------------------------------------------------------

CREATE TABLE `step_var_to_var` (
  `composite_step_mapping` bigint(20) NOT NULL,
  `mapping` bigint(20) NOT NULL,
  `mapping_key` bigint(20) NOT NULL,
  PRIMARY KEY (`composite_step_mapping`,`mapping_key`),
  KEY `FKE611D2CE214C0C07` (`composite_step_mapping`),
  KEY `FKE611D2CE603CEF03` (`mapping_key`),
  KEY `FKE611D2CED8853863` (`mapping`),
  CONSTRAINT `FKE611D2CED8853863` FOREIGN KEY (`mapping`) REFERENCES `variable` (`id`),
  CONSTRAINT `FKE611D2CE214C0C07` FOREIGN KEY (`composite_step_mapping`) REFERENCES `composite_step_mapping` (`id`),
  CONSTRAINT `FKE611D2CE603CEF03` FOREIGN KEY (`mapping_key`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table tuple
# ------------------------------------------------------------

CREATE TABLE `tuple` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `value_` longtext,
  `version` int(11) DEFAULT NULL,
  `var` bigint(20) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK69987C8A69AE6BC` (`var`),
  CONSTRAINT `FK69987C8A69AE6BC` FOREIGN KEY (`var`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=39 DEFAULT CHARSET=latin1;

LOCK TABLES `tuple` WRITE;
/*!40000 ALTER TABLE `tuple` DISABLE KEYS */;
INSERT INTO `tuple` (`id`,`value_`,`version`,`var`)
VALUES
	(13,'0.50',0,4),
	(14,'3.83;7.73;11.69;15.73;19.83;23.99;28.22;32.51;36.86;41.28;45.75;50.29;54.89;59.56;64.28;69.07;73.92;78.83;83.81;88.84;93.93;99.08;104.29;109.55;114.86;120.23;125.66;131.13;136.65;142.22;147.85;153.52;159.24;165.01;170.84;176.73;182.66;188.66;194.71;200.81;206.98;213.20;219.48;225.82;232.23;238.71;245.25;251.86;258.54;265.29;272.12;279.02;285.99;293.04;300.16;307.36;314.64;322.00;329.44;336.96;344.57;352.25;360.02;367.86;375.78;383.78;391.86;400.01;408.24;416.55;424.93;433.38;441.91;450.50;459.16;467.89;476.68;485.54;494.46;503.43;512.47;521.57;530.72;539.93;549.19;558.49;567.84;577.23;586.67;596.15;605.66;615.22;624.81;634.45;644.12;653.82;663.57;673.35;683.17;693.02;702.91',0,19),
	(15,'0.80;0.82;0.84;0.86;0.88;0.90;0.92;0.94;0.96;0.97;0.99;1.01;1.03;1.06;1.08;1.10;1.12;1.15;1.17;1.20;1.22;1.25;1.28;1.30;1.33;1.36;1.39;1.42;1.45;1.49;1.52;1.55;1.59;1.62;1.66;1.70;1.74;1.78;1.82;1.86;1.90;1.95;1.99;2.04;2.09;2.13;2.18;2.23;2.29;2.34;2.39;2.45;2.50;2.56;2.61;2.67;2.73;2.78;2.84;2.90;2.95;3.01;3.07;3.13;3.18;3.24;3.29;3.35;3.40;3.45;3.51;3.56;3.61;3.67;3.72;3.77;3.82;3.87;3.91;3.96;4.01;4.06;4.10;4.15;4.19;4.23;4.28;4.32;4.36;4.40;4.44;4.48;4.52;4.55;4.59;4.63;4.67;4.70;4.74;4.78;4.81',0,21),
	(16,'3.89;3.90;3.92;3.98;4.03;4.16;4.17;4.17;4.17;4.16;4.16;4.16;4.17;4.17;4.17;4.17;4.17;4.17;4.17;4.16;4.16;4.16;4.16;4.16;4.16;4.16;4.16;4.15;4.15;4.14;4.14;4.14;4.15;4.16;4.17;4.18;4.19;4.20;4.21;4.21;4.22;4.23;4.25;4.26;4.27;4.28;4.29;4.30;4.31;4.32;4.33;4.33;4.33;4.34;4.34;4.34;4.35;4.35;4.36;4.36;4.36;4.37;4.37;4.38;4.39;4.39;4.40;4.40;4.41;4.41;4.42;4.42;4.43;4.43;4.44;4.45;4.45;4.46;4.46;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.47;4.48;4.48;4.48;4.48;4.48;4.48',0,14),
	(17,'0.50',0,1),
	(18,'3.28;3.32;3.37;3.42;3.47;3.51;3.49;3.47;3.45;3.43;3.41;3.38;3.35;3.32;3.29;3.27;3.25;3.23;3.21;3.19;3.17;3.14;3.12;3.10;3.07;3.05;3.04;3.02;3.00;2.99;2.98;2.96;2.94;2.92;2.90;2.88;2.87;2.85;2.83;2.82;2.81;2.78;2.76;2.75;2.73;2.71;2.69;2.68;2.66;2.65;2.63;2.62;2.61;2.60;2.59;2.58;2.57;2.56;2.55;2.54;2.53;2.52;2.51;2.50;2.49;2.48;2.47;2.46;2.45;2.44;2.43;2.42;2.41;2.41;2.40;2.39;2.38;2.37;2.36;2.35;2.34;2.35;2.35;2.35;2.35;2.35;2.35;2.35;2.35;2.35;2.35;2.36;2.36;2.36;2.36;2.36;2.37;2.37;2.37;2.37;2.37',0,25),
	(19,'0.50',0,2),
	(20,'2050',0,7),
	(21,'373;374;376;377;379;381;383;385;388;390;392;394;397;399;402;404;407;410;412;415;418;421;425;428;431;435;438;442;446;449;453;458;462;466;471;475;480;485;490;495;500;505;511;516;522;528;534;540;547;553;560;566;573;580;587;594;601;608;615;622;629;636;644;651;658;666;673;681;689;697;704;712;720;728;737;745;753;762;770;778;787;796;804;813;822;831;839;848;857;866;875;884;893;902;911;920;929;938;947;957;966',0,20),
	(22,'<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>;<ERR_OOB/>',0,26),
	(23,'2005',0,8),
	(24,'0.50',0,5),
	(25,'300',0,12),
	(26,'2050.00',0,9),
	(27,'0.75;0.76;0.79;0.82;0.85;0.93;0.93;0.94;0.94;0.95;0.95;0.96;0.97;0.97;0.98;0.99;0.99;1.00;1.00;1.00;1.01;1.01;1.02;1.02;1.03;1.03;1.04;1.04;1.04;1.05;1.05;1.05;1.06;1.06;1.06;1.07;1.07;1.07;1.07;1.08;1.08;1.08;1.09;1.09;1.10;1.10;1.11;1.11;1.11;1.12;1.12;1.12;1.12;1.13;1.13;1.13;1.13;1.13;1.13;1.14;1.14;1.14;1.14;1.14;1.15;1.15;1.15;1.15;1.15;1.16;1.16;1.16;1.16;1.16;1.17;1.17;1.17;1.17;1.17;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18;1.18',0,16),
	(28,'2000;2001;2002;2003;2004;2005;2006;2007;2008;2009;2010;2011;2012;2013;2014;2015;2016;2017;2018;2019;2020;2021;2022;2023;2024;2025;2026;2027;2028;2029;2030;2031;2032;2033;2034;2035;2036;2037;2038;2039;2040;2041;2042;2043;2044;2045;2046;2047;2048;2049;2050;2051;2052;2053;2054;2055;2056;2057;2058;2059;2060;2061;2062;2063;2064;2065;2066;2067;2068;2069;2070;2071;2072;2073;2074;2075;2076;2077;2078;2079;2080;2081;2082;2083;2084;2085;2086;2087;2088;2089;2090;2091;2092;2093;2094;2095;2096;2097;2098;2099;2100',0,13),
	(29,'2050',0,11),
	(30,'0.80;0.82;0.84;0.86;0.88;0.90;0.92;0.94;0.96;0.97;0.99;1.01;1.03;1.05;1.07;1.08;1.10;1.12;1.14;1.16;1.17;1.19;1.21;1.22;1.24;1.25;1.27;1.28;1.30;1.31;1.33;1.34;1.36;1.37;1.39;1.41;1.42;1.44;1.45;1.47;1.49;1.50;1.52;1.54;1.56;1.58;1.60;1.62;1.64;1.66;1.68;1.70;1.73;1.75;1.77;1.79;1.82;1.84;1.86;1.89;1.91;1.94;1.96;1.98;2.01;2.03;2.05;2.07;2.10;2.12;2.14;2.16;2.18;2.20;2.22;2.24;2.26;2.28;2.30;2.32;2.33;2.35;2.37;2.38;2.40;2.41;2.42;2.44;2.45;2.46;2.47;2.49;2.50;2.51;2.52;2.53;2.54;2.55;2.56;2.57;2.58',0,18),
	(31,'2005',0,10),
	(32,'8.07;8.16;8.25;8.35;8.43;8.53;8.45;8.38;8.31;8.24;8.18;8.11;8.04;7.98;7.92;7.86;7.81;7.76;7.71;7.67;7.63;7.57;7.51;7.46;7.41;7.37;7.33;7.29;7.26;7.22;7.20;7.15;7.09;7.04;6.98;6.93;6.88;6.84;6.79;6.75;6.71;6.65;6.59;6.53;6.48;6.42;6.37;6.32;6.28;6.23;6.19;6.17;6.14;6.12;6.10;6.08;6.05;6.03;6.01;5.99;5.97;5.95;5.93;5.92;5.90;5.88;5.86;5.85;5.83;5.81;5.80;5.78;5.76;5.75;5.73;5.71;5.70;5.68;5.66;5.65;5.63;5.64;5.64;5.64;5.65;5.65;5.66;5.66;5.67;5.67;5.67;5.68;5.68;5.68;5.69;5.69;5.69;5.69;5.70;5.70;5.70',0,24),
	(33,'1.67;1.69;1.72;1.75;1.78;1.81;1.84;1.87;1.90;1.93;1.96;1.99;2.02;2.06;2.09;2.12;2.15;2.17;2.20;2.23;2.25;2.28;2.30;2.33;2.35;2.37;2.39;2.42;2.44;2.46;2.48;2.51;2.54;2.57;2.60;2.63;2.66;2.69;2.72;2.75;2.78;2.82;2.86;2.89;2.93;2.97;3.01;3.05;3.09;3.13;3.17;3.21;3.25;3.29;3.33;3.37;3.42;3.46;3.50;3.55;3.59;3.63;3.66;3.70;3.73;3.77;3.80;3.84;3.88;3.91;3.95;3.97;4.00;4.03;4.05;4.08;4.10;4.13;4.16;4.18;4.21;4.23;4.24;4.26;4.27;4.28;4.30;4.31;4.33;4.34;4.36;4.37;4.39;4.40;4.42;4.43;4.45;4.46;4.48;4.50;4.51',0,22),
	(34,'2005',0,6),
	(35,'0.50',0,3),
	(36,'1.82;1.87;1.94;2.15;2.42;2.70;2.71;2.72;2.74;2.76;2.77;2.79;2.81;2.82;2.84;2.85;2.87;2.88;2.89;2.91;2.92;2.93;2.95;2.96;2.97;2.98;3.00;3.01;3.02;3.03;3.04;3.05;3.05;3.06;3.06;3.07;3.07;3.07;3.08;3.08;3.08;3.09;3.10;3.10;3.11;3.11;3.12;3.12;3.13;3.13;3.13;3.14;3.14;3.14;3.14;3.14;3.14;3.15;3.15;3.15;3.15;3.15;3.15;3.16;3.16;3.16;3.16;3.17;3.17;3.17;3.17;3.17;3.18;3.18;3.18;3.18;3.18;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19;3.19',0,15),
	(37,'373;374;376;377;379;381;383;385;387;389;391;393;395;397;399;401;402;404;406;407;409;410;412;413;415;416;418;419;421;422;424;425;427;428;430;431;433;434;436;437;439;441;442;444;445;447;449;450;452;454;455;457;459;460;462;464;465;467;469;470;472;474;476;477;479;481;483;484;486;488;490;491;493;495;497;499;500;502;504;506;508;509;511;513;515;517;519;520;522;524;526;528;530;532;533;535;537;539;541;543;545',0,17),
	(38,'1.59;1.61;1.63;1.65;1.68;1.71;1.74;1.76;1.79;1.82;1.85;1.87;1.90;1.92;1.95;1.97;2.00;2.02;2.04;2.06;2.08;2.10;2.12;2.14;2.16;2.18;2.20;2.22;2.24;2.26;2.27;2.29;2.31;2.33;2.35;2.37;2.39;2.41;2.42;2.44;2.46;2.48;2.50;2.52;2.54;2.56;2.58;2.60;2.62;2.64;2.66;2.68;2.70;2.72;2.74;2.76;2.78;2.79;2.81;2.83;2.85;2.87;2.89;2.91;2.93;2.95;2.97;2.99;3.01;3.03;3.05;3.07;3.09;3.11;3.12;3.14;3.16;3.18;3.20;3.22;3.24;3.26;3.28;3.30;3.32;3.34;3.36;3.37;3.39;3.41;3.43;3.45;3.47;3.49;3.51;3.52;3.54;3.56;3.58;3.60;3.62',0,23);

/*!40000 ALTER TABLE `tuple` ENABLE KEYS */;
UNLOCK TABLES;


# Dump of table var_mapping
# ------------------------------------------------------------

CREATE TABLE `var_mapping` (
  `default_simulation` bigint(20) NOT NULL,
  `variable_map` bigint(20) NOT NULL,
  `variable_map_key` bigint(20) NOT NULL,
  PRIMARY KEY (`default_simulation`,`variable_map_key`),
  KEY `FKF6336FB61D43E378` (`default_simulation`),
  KEY `FKF6336FB6906528AE` (`variable_map`),
  KEY `FKF6336FB64AC5C1CE` (`variable_map_key`),
  CONSTRAINT `FKF6336FB64AC5C1CE` FOREIGN KEY (`variable_map_key`) REFERENCES `variable` (`id`),
  CONSTRAINT `FKF6336FB61D43E378` FOREIGN KEY (`default_simulation`) REFERENCES `default_simulation` (`id`),
  CONSTRAINT `FKF6336FB6906528AE` FOREIGN KEY (`variable_map`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;



# Dump of table variable
# ------------------------------------------------------------

CREATE TABLE `variable` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `_options_raw` varchar(255) DEFAULT NULL,
  `arity` int(11) NOT NULL,
  `data_type` int(11) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `external_name` varchar(255) DEFAULT NULL,
  `max_` double DEFAULT NULL,
  `min_` double DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `precision_` int(11) DEFAULT NULL,
  `version` int(11) DEFAULT NULL,
  `indexing_variable` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKB584D27CD32BC7A0` (`indexing_variable`),
  CONSTRAINT `FKB584D27CD32BC7A0` FOREIGN KEY (`indexing_variable`) REFERENCES `variable` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=latin1;

LOCK TABLES `variable` WRITE;
/*!40000 ALTER TABLE `variable` DISABLE KEYS */;
INSERT INTO `variable` (`id`,`_options_raw`,`arity`,`data_type`,`description`,`external_name`,`max_`,`min_`,`name`,`precision_`,`version`,`indexing_variable`)
VALUES
	(1,'',1,0,'Pct change in Developed FF emissions','Pct change in Developed FF emissions',3,-0.99,'Pct change in Developed FF emissions',2,0,NULL),
	(2,'',1,0,'Pct change in Developing A FF emissions','Pct change in Developing A FF emissions',3,-1,'Pct change in Developing A FF emissions',2,0,1),
	(3,'',1,0,'Pct change in Developing B FF emissions','Pct change in Developing B FF emissions',3,-1,'Pct change in Developing B FF emissions',2,0,1),
	(4,'',1,0,'Global land use emissions change','Global land use emissions change',1,0,'Global land use emissions change',2,0,1),
	(5,'',1,0,'Target Sequestration','Target Sequestration',1,0,'Target Sequestration',2,0,1),
	(6,'',1,0,'Developed start year','Developed start year',2100,2005,'Developed start year',0,0,1),
	(7,'',1,0,'Developed target year','Developed target year',2100,2005,'Developed target year',0,0,1),
	(8,'',1,0,'Developing A start year','Developing A start year',2100,2005,'Developing A start year',0,0,1),
	(9,'',1,0,'Developing A target year','Developing A target year',2100,2005,'Developing A target year',2,0,1),
	(10,'',1,0,'Developing B start year','Developing B start year',2100,2005,'Developing B start year',0,0,1),
	(11,'',1,0,'Developing B target year','Developing B target year',2100,2005,'Developing B target year',0,0,1),
	(12,'',1,0,'Goal for CO2 in the atmosphere','Goal for CO2 in the atmosphere',1500,0,'Goal for CO2 in the atmosphere',0,0,1),
	(13,'',101,0,'Year','Year',2100,2000,'Year',0,0,1),
	(14,'',101,0,'DevelopedFossilFuelEmissions','DevelopedFossilFuelEmissions',10,0,'DevelopedFossilFuelEmissions',2,1,13),
	(15,'',101,0,'DevelopingAFossilFuelEmissions','DevelopingAFossilFuelEmissions',10,0,'DevelopingAFossilFuelEmissions',2,0,13),
	(16,'',101,0,'DevelopingBFossilFuelEmissions','DevelopingBFossilFuelEmissions',10,0,'DevelopingBFossilFuelEmissions',2,1,13),
	(17,'',101,0,'AtmosphericCO2Concentration','AtmosphericCO2Concentration',2000,0,'AtmosphericCO2Concentration',0,0,13),
	(18,'',101,0,'GlobalTempChange','GlobalTempChange',10,0,'GlobalTempChange',2,0,13),
	(19,'',101,0,'Sea_Level_Rise_output','Sea_Level_Rise_output',2000,0,'Sea_Level_Rise_output',2,1,13),
	(20,'',101,0,'BAUCO2Concentration','BAUCO2Concentration',2000,0,'BAUCO2Concentration',0,1,13),
	(21,'',101,0,'ExpectedBAUTempChange','ExpectedBAUTempChange',10,0,'ExpectedBAUTempChange',2,0,13),
	(22,'',101,0,'RadiativeForcing','RadiativeForcing',10,0,'RadiativeForcing',2,0,13),
	(23,'',101,0,'CO2RadiativeForcing','CO2RadiativeForcing',10,0,'CO2RadiativeForcing',2,0,13),
	(24,'',101,0,'GlobalCH4EmissionsCO2e','GlobalCH4EmissionsCO2e',20,0,'GlobalCH4EmissionsCO2e',2,0,13),
	(25,'',101,0,'GlobalN2OEmissionsCO2e','GlobalN2OEmissionsCO2e',20,0,'GlobalN2OEmissionsCO2e',2,1,13),
	(26,'',101,0,'GlobalCO2FFEmissions','GlobalCO2FFEmissions',20,0,'GlobalCO2FFEmissions',2,0,13),
	(27,NULL,1,0,'Test',NULL,NULL,NULL,'Test',1,0,NULL),
	(49,NULL,1,0,'Test',NULL,NULL,NULL,'Test',1,0,NULL);

/*!40000 ALTER TABLE `variable` ENABLE KEYS */;
UNLOCK TABLES;





/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
