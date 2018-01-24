CREATE TABLE IF NOT EXISTS `custom_npc`(
  `id` decimal(11,0) NOT NULL default '0',
  `idTemplate` int(11) NOT NULL default '0',
  `name` varchar(200) default NULL,
  `serverSideName` int(1) default '0',
  `title` varchar(45) default '',
  `serverSideTitle` int(1) default '0',
  `class` varchar(200) default NULL,
  `collision_radius` decimal(5,2) default NULL,
  `collision_height` decimal(5,2) default NULL,
  `level` decimal(2,0) default NULL,
  `sex` varchar(6) default NULL,
  `type` varchar(20) default NULL,
  `attackrange` int(11) default NULL,
  `hp` decimal(8,0) default NULL,
  `mp` decimal(6,0) default NULL,
  `hpreg` decimal(8,2) default NULL,
  `mpreg` decimal(5,2) default NULL,
  `str` decimal(7,0) default NULL,
  `con` decimal(7,0) default NULL,
  `dex` decimal(7,0) default NULL,
  `int` decimal(7,0) default NULL,
  `wit` decimal(7,0) default NULL,
  `men` decimal(7,0) default NULL,
  `exp` decimal(9,0) default NULL,
  `sp` decimal(8,0) default NULL,
  `patk` decimal(5,0) default NULL,
  `pdef` decimal(5,0) default NULL,
  `matk` decimal(5,0) default NULL,
  `mdef` decimal(5,0) default NULL,
  `atkspd` decimal(3,0) default NULL,
  `aggro` decimal(6,0) default NULL,
  `matkspd` decimal(4,0) default NULL,
  `rhand` decimal(5,0) default NULL,
  `lhand` decimal(5,0) default NULL,
  `armor` decimal(1,0) default NULL,
  `enchant` INT NOT NULL default 0, 
  `walkspd` decimal(3,0) default NULL,
  `runspd` decimal(3,0) default NULL,
  `isUndead` int(11) default 0,
  `absorb_level` decimal(2,0) default 0,
  `absorb_type` enum('FULL_PARTY','LAST_HIT','PARTY_ONE_RANDOM') DEFAULT 'LAST_HIT' NOT NULL,
  `drop_herbs` enum('true','false') DEFAULT 'false' NOT NULL,
  PRIMARY KEY (`id`)
);

INSERT IGNORE INTO `custom_npc` VALUES
(50007,31324,'Andromeda',1,'L2J Wedding Manager',1,'NPC.a_casino_FDarkElf',8,23,70,'female','L2WeddingManager',40,2444,2444,0,0,10,10,10,10,10,10,0,0,500,500,500,500,278,0,333,0,0,NULL,0,28,120,0,0,'LAST_HIT','false'),
(70010,31606,'Catrina',1,'L2J TvT Event Manager',1,'Monster2.queen_of_cat',8,15,70,'female','L2TvTEventNpc',40,2444,2444,0,0,10,10,10,10,10,10,0,0,500,500,500,500,278,0,333,0,0,NULL,0,28,120,0,0,'LAST_HIT','false'),
(1000003,32226,'Shiela',1,'L2J NPC Buffer',1,'LineageNPC2.K_F1_grand',11,22.25,70,'male','L2NpcBuffer',40,2444,2444,0,0,10,10,10,10,10,10,0,0,500,500,500,500,278,0,333,0,0,NULL,0,28,120,0,0,'LAST_HIT','false');