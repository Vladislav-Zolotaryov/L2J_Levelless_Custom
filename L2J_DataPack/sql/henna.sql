DROP TABLE IF EXISTS `henna`;
CREATE TABLE `henna` (
  `symbol_id` int(11) NOT NULL default '0',
  `symbol_name` varchar(45) default NULL,
  `dye_id` int(11) default NULL,
  `dye_amount` int(11) default NULL,
  `price` int(11) default NULL,
  `stat_INT` decimal(11,0) default NULL,
  `stat_STR` decimal(11,0) default NULL,
  `stat_CON` decimal(11,0) default NULL,
  `stat_MEM` decimal(11,0) default NULL,
  `stat_DEX` decimal(11,0) default NULL,
  `stat_WIT` decimal(11,0) default NULL,
  PRIMARY KEY (`symbol_id`)
);

INSERT INTO `henna` VALUES
(1,'symbol_s+1c-3_d',4445,10,5100,0,1,-3,0,0,0),
(2,'symbol_s+1d-3_d',4446,10,5100,0,1,0,0,-3,0),
(3,'symbol_c+1s-3_d',4447,10,5100,0,-3,1,0,0,0),
(4,'symbol_c+1d-3_d',4448,10,5100,0,0,1,0,-3,0),
(5,'symbol_d+1s-3_d',4449,10,5100,0,-3,0,0,1,0),
(6,'symbol_d+1c-3_d',4450,10,5100,0,0,-3,0,1,0),
(7,'symbol_i+1m-3_d',4451,10,5100,1,0,0,-3,0,0),
(8,'symbol_i+1w-3_d',4452,10,5100,1,0,0,0,0,-3),
(9,'symbol_m+1i-3_d',4453,10,5100,-3,0,0,1,0,0),
(10,'symbol_m+1w-3_d',4454,10,5100,0,0,0,1,0,-3),
(11,'symbol_w+1i-3_d',4455,10,5100,-3,0,0,0,0,1),
(12,'symbol_w+1m-3_d',4456,10,5100,0,0,0,-3,0,1),
(13,'symbol_s+1c-2_d',4457,10,12000,0,1,-2,0,0,0),
(14,'symbol_s+1d-2_d',4458,10,12000,0,1,0,0,-2,0),
(15,'symbol_c+1s-2_d',4459,10,12000,0,-2,1,0,0,0),
(16,'symbol_c+1d-2_d',4460,10,12000,0,0,1,0,-2,0),
(17,'symbol_d+1s-2_d',4461,10,12000,0,-2,0,0,1,0),
(18,'symbol_d+1c-2_d',4462,10,12000,0,0,-2,0,1,0),
(19,'symbol_i+1m-2_d',4463,10,12000,1,0,0,-2,0,0),
(20,'symbol_i+1w-2_d',4464,10,12000,1,0,0,0,0,-2),
(21,'symbol_m+1i-2_d',4465,10,12000,-2,0,0,1,0,0),
(22,'symbol_m+1w-2_d',4466,10,12000,0,0,0,1,0,-2),
(23,'symbol_w+1i-2_d',4467,10,12000,-2,0,0,0,0,1),
(24,'symbol_w+1m-2_d',4468,10,12000,0,0,0,-2,0,1),
(25,'symbol_s+1c-1_d',4469,10,35000,0,1,-1,0,0,0),
(26,'symbol_s+1d-1_d',4470,10,35000,0,1,0,0,-1,0),
(27,'symbol_c+1s-1_d',4471,10,35000,0,-1,1,0,0,0),
(28,'symbol_c+1d-1_d',4472,10,35000,0,0,1,0,-1,0),
(29,'symbol_d+1s-1_d',4473,10,35000,0,-1,0,0,1,0),
(30,'symbol_d+1c-1_d',4474,10,35000,0,0,-1,0,1,0),
(31,'symbol_i+1m-1_d',4475,10,35000,1,0,0,-1,0,0),
(32,'symbol_i+1w-1_d',4476,10,35000,1,0,0,0,0,-1),
(33,'symbol_m+1i-1_d',4477,10,35000,-1,0,0,1,0,0),
(34,'symbol_m+1w-1_d',4478,10,35000,0,0,0,1,0,-1),
(35,'symbol_w+1i-1_d',4479,10,35000,-1,0,0,0,0,1),
(36,'symbol_w+1m-1_d',4480,10,35000,0,0,0,-1,0,1),
(37,'symbol_s+1c-3_c',4481,10,12000,0,1,-3,0,0,0),
(38,'symbol_s+1d-3_c',4482,10,24600,0,1,0,0,-3,0),
(39,'symbol_c+1s-3_c',4483,10,24600,0,-3,1,0,0,0),
(40,'symbol_c+1d-3_c',4484,10,24600,0,0,1,0,-3,0),
(41,'symbol_d+1s-3_c',4485,10,30000,0,-3,0,0,1,0),
(42,'symbol_d+1c-3_c',4486,10,30000,0,0,-3,0,1,0),
(43,'symbol_i+1m-3_c',4487,10,30000,1,0,0,-3,0,0),
(44,'symbol_i+1w-3_c',4488,10,30000,1,0,0,0,0,-3),
(45,'symbol_m+1i-3_c',4489,10,30000,-3,0,0,1,0,0),
(46,'symbol_m+1w-3_c',4490,10,12000,0,0,0,1,0,-3),
(47,'symbol_w+1i-3_c',4491,10,30000,-3,0,0,0,0,1),
(48,'symbol_w+1m-3_c',4492,10,12000,0,0,0,-3,0,1),
(49,'symbol_s+1c-2_c',4493,10,24600,0,1,-2,0,0,0),
(50,'symbol_s+1d-2_c',4494,10,30000,0,1,0,0,-2,0),
(51,'symbol_c+1s-2_c',4495,10,35000,0,-2,1,0,0,0),
(52,'symbol_c+1d-2_c',4496,10,35000,0,0,1,0,-2,0),
(53,'symbol_d+1s-2_c',4497,10,36000,0,-2,0,0,1,0),
(54,'symbol_d+1c-2_c',4498,10,36000,0,0,-2,0,1,0),
(55,'symbol_i+1m-2_c',4499,10,50000,1,0,0,-2,0,0),
(56,'symbol_i+1w-2_c',4500,10,36000,1,0,0,0,0,-2),
(57,'symbol_m+1i-2_c',4501,10,36000,-2,0,0,1,0,0),
(58,'symbol_m+1w-2_c',4502,10,21000,0,0,0,1,0,-2),
(59,'symbol_w+1i-2_c',4503,10,30000,-2,0,0,0,0,1),
(60,'symbol_w+1m-2_c',4504,10,36000,0,0,0,-2,0,1),
(61,'symbol_s+2c-4_c',4505,10,24600,0,2,-4,0,0,0),
(62,'symbol_s+2d-4_c',4506,10,24600,0,2,0,0,-4,0),
(63,'symbol_c+2s-4_c',4507,10,24600,0,-4,2,0,0,0),
(64,'symbol_c+2d-4_c',4508,10,24600,0,0,2,0,-4,0),
(65,'symbol_d+2s-4_c',4509,10,24600,0,-4,0,0,2,0),
(66,'symbol_d+2c-4_c',4510,10,24600,0,0,-4,0,2,0),
(67,'symbol_i+2m-4_c',4511,10,24600,2,0,0,-4,0,0),
(68,'symbol_i+2w-4_c',4512,10,24600,2,0,0,0,0,-4),
(69,'symbol_m+2i-4_c',4513,10,24600,-4,0,0,2,0,0),
(70,'symbol_m+2w-4_c',4514,10,30000,0,0,0,2,0,-4),
(71,'symbol_w+2i-4_c',4515,10,30000,-4,0,0,0,0,2),
(72,'symbol_w+2m-4_c',4516,10,30000,0,0,0,-4,0,2),
(73,'symbol_s+2c-3_c',4517,10,30000,0,2,-3,0,0,0),
(74,'symbol_s+2d-3_c',4518,10,35000,0,2,0,0,-3,0),
(75,'symbol_c+2s-3_c',4519,10,35000,0,-3,2,0,0,0),
(76,'symbol_c+2d-3_c',4520,10,35000,0,0,2,0,-3,0),
(77,'symbol_d+2s-3_c',4521,10,27000,0,-3,0,0,2,0),
(78,'symbol_d+2c-3_c',4522,10,27000,0,0,-3,0,2,0),
(79,'symbol_i+2m-3_c',4523,10,27000,2,0,0,-3,0,0),
(80,'symbol_i+2w-3_c',4524,10,30000,2,0,0,0,0,-3),
(81,'symbol_m+2i-3_c',4525,10,30000,-3,0,0,2,0,0),
(82,'symbol_m+2w-3_c',4526,10,30000,0,0,0,2,0,-3),
(83,'symbol_w+2i-3_c',4527,10,30000,-3,0,0,0,0,2),
(84,'symbol_w+2m-3_c',4528,10,30000,0,0,0,-3,0,2),
(85,'symbol_s+3c-5_c',4529,10,30000,0,3,-5,0,0,0),
(86,'symbol_s+3d-5_c',4530,10,30000,0,3,0,0,-5,0),
(87,'symbol_c+3s-5_c',4531,10,30000,0,-5,3,0,0,0),
(88,'symbol_c+3d-5_c',4532,10,30000,0,0,3,0,-5,0),
(89,'symbol_d+3s-5_c',4533,10,30000,0,-5,0,0,3,0),
(90,'symbol_d+3c-5_c',4534,10,30000,0,0,-5,0,3,0),
(91,'symbol_i+3m-5_c',4535,10,30000,3,0,0,-5,0,0),
(92,'symbol_i+3w-5_c',4536,10,30000,3,0,0,0,0,-5),
(93,'symbol_m+3i-5_c',4537,10,30000,-5,0,0,3,0,0),
(94,'symbol_m+3w-5_c',4538,10,30000,0,0,0,3,0,-5),
(95,'symbol_w+3i-5_c',4539,10,30000,-5,0,0,0,0,3),
(96,'symbol_w+3m-5_c',4540,10,30000,0,0,0,-5,0,3),
(97,'symbol_s+3c-4_c',4541,10,30000,0,3,-4,0,0,0),
(98,'symbol_s+3d-4_c',4542,10,30000,0,3,0,0,-4,0),
(99,'symbol_c+3s-4_c',4543,10,50000,0,-4,3,0,0,0),
(100,'symbol_c+3d-4_c',4544,10,50000,0,0,3,0,-4,0),
(101,'symbol_d+3s-4_c',4545,10,50000,0,-4,0,0,3,0),
(102,'symbol_d+3c-4_c',4546,10,50000,0,0,-4,0,3,0),
(103,'symbol_i+3m-4_c',4547,10,50000,3,0,0,-4,0,0),
(104,'symbol_i+3w-4_c',4548,10,50000,3,0,0,0,0,-4),
(105,'symbol_m+3i-4_c',4549,10,50000,-4,0,0,3,0,0),
(106,'symbol_m+3w-4_c',4550,10,50000,0,0,0,3,0,-4),
(107,'symbol_w+3i-4_c',4551,10,50000,-4,0,0,0,0,3),
(108,'symbol_w+3m-4_c',4552,10,50000,0,0,0,-4,0,3),
(109,'symbol_s+1c-1_c',4553,10,50000,0,1,-1,0,0,0),
(110,'symbol_s+1d-1_c',4554,10,50000,0,1,0,0,-1,0),
(111,'symbol_c+1s-1_c',4555,10,50000,0,-1,1,0,0,0),
(112,'symbol_c+1d-1_c',4556,10,50000,0,0,1,0,-1,0),
(113,'symbol_d+1s-1_c',4557,10,50000,0,-1,0,0,1,0),
(114,'symbol_d+1c-1_c',4558,10,50000,0,0,-1,0,1,0),
(115,'symbol_i+1m-1_c',4559,10,90000,1,0,0,-1,0,0),
(116,'symbol_i+1w-1_c',4560,10,50000,1,0,0,0,0,-1),
(117,'symbol_m+1i-1_c',4561,10,50000,-1,0,0,1,0,0),
(118,'symbol_m+1w-1_c',4562,10,50000,0,0,0,1,0,-1),
(119,'symbol_w+1i-1_c',4563,10,50000,-1,0,0,0,0,1),
(120,'symbol_w+1m-1_c',4564,10,50000,0,0,0,-1,0,1),
(121,'symbol_s+4c-6_c',4565,10,36000,0,4,-6,0,0,0),
(122,'symbol_s+4d-6_c',4566,10,36000,0,4,0,0,-6,0),
(123,'symbol_c+4s-6_c',4567,10,50000,0,-6,4,0,0,0),
(124,'symbol_c+4d-6_c',4568,10,50000,0,0,4,0,-6,0),
(125,'symbol_d+4s-6_c',4569,10,30000,0,-6,0,0,4,0),
(126,'symbol_d+4c-6_c',4570,10,36000,0,0,-6,0,4,0),
(127,'symbol_i+4m-6_c',4571,10,36000,4,0,0,-6,0,0),
(128,'symbol_i+4w-6_c',4572,10,30000,4,0,0,0,0,-6),
(129,'symbol_m+4i-6_c',4573,10,36000,-6,0,0,4,0,0),
(130,'symbol_m+4w-6_c',4574,10,36000,0,0,0,4,0,-6),
(131,'symbol_w+4i-6_c',4575,10,36000,-6,0,0,0,0,4),
(132,'symbol_w+4m-6_c',4576,10,30000,0,0,0,-6,0,4),
(133,'symbol_s+4c-5_c',4577,10,36000,0,4,-5,0,0,0),
(134,'symbol_s+4d-5_c',4578,10,90000,0,4,0,0,-5,0),
(135,'symbol_c+4s-5_c',4579,10,90000,0,-5,4,0,0,0),
(136,'symbol_c+4d-5_c',4580,10,90000,0,0,4,0,-5,0),
(137,'symbol_d+4s-5_c',4581,10,36000,0,-5,0,0,4,0),
(138,'symbol_d+4c-5_c',4582,10,36000,0,0,-5,0,4,0),
(139,'symbol_i+4m-5_c',4583,10,90000,4,0,0,-5,0,0),
(140,'symbol_i+4w-5_c',4584,10,36000,4,0,0,0,0,-5),
(141,'symbol_m+4i-5_c',4585,10,90000,-5,0,0,4,0,0),
(142,'symbol_m+4w-5_c',4586,10,90000,0,0,0,4,0,-5),
(143,'symbol_w+4i-5_c',4587,10,36000,-5,0,0,0,0,4),
(144,'symbol_w+4m-5_c',4588,10,36000,0,0,0,-5,0,4),
(145,'symbol_s+2c-2_c',4589,10,60000,0,2,-2,0,0,0),
(146,'symbol_s+2d-2_c',4590,10,60000,0,2,0,0,-2,0),
(147,'symbol_c+2s-2_c',4591,10,60000,0,-2,2,0,0,0),
(148,'symbol_c+2d-2_c',4592,10,60000,0,0,2,0,-2,0),
(149,'symbol_d+2s-2_c',4593,10,60000,0,-2,0,0,2,0),
(150,'symbol_d+2c-2_c',4594,10,60000,0,0,-2,0,2,0),
(151,'symbol_i+2m-2_c',4595,10,60000,2,0,0,-2,0,0),
(152,'symbol_i+2w-2_c',4596,10,90000,2,0,0,0,0,-2),
(153,'symbol_m+2i-2_c',4597,10,60000,-2,0,0,2,0,0),
(154,'symbol_m+2w-2_c',4598,10,60000,0,0,0,2,0,-2),
(155,'symbol_w+2i-2_c',4599,10,60000,-2,0,0,0,0,2),
(156,'symbol_w+2m-2_c',4600,10,60000,0,0,0,-2,0,2),
(157,'symbol_s+3c-3_c',4601,10,90000,0,3,-3,0,0,0),
(158,'symbol_s+3d-3_c',4602,10,90000,0,3,0,0,-3,0),
(159,'symbol_c+3s-3_c',4603,10,90000,0,-3,3,0,0,0),
(160,'symbol_c+3d-3_c',4604,10,90000,0,0,3,0,-3,0),
(161,'symbol_d+3s-3_c',4605,10,90000,0,-3,0,0,3,0),
(162,'symbol_d+3c-3_c',4606,10,90000,0,0,-3,0,3,0),
(163,'symbol_i+3m-3_c',4607,10,90000,3,0,0,-3,0,0),
(164,'symbol_i+3w-3_c',4608,10,90000,3,0,0,0,0,-3),
(165,'symbol_m+3i-3_c',4609,10,90000,-3,0,0,3,0,0),
(166,'symbol_m+3w-3_c',4610,10,90000,0,0,0,3,0,-3),
(167,'symbol_w+3i-3_c',4611,10,90000,-3,0,0,0,0,3),
(168,'symbol_w+3m-3_c',4612,10,90000,0,0,0,-3,0,3),
(169,'symbol_s+4c-4_c',4613,10,145000,0,4,-4,0,0,0),
(170,'symbol_s+4d-4_c',4614,10,145000,0,4,0,0,-4,0),
(171,'symbol_c+4s-4_c',4615,10,145000,0,-4,4,0,0,0),
(172,'symbol_c+4d-4_c',4616,10,145000,0,0,4,0,-4,0),
(173,'symbol_d+4s-4_c',4617,10,145000,0,-4,0,0,4,0),
(174,'symbol_d+4c-4_c',4618,10,145000,0,0,-4,0,4,0),
(175,'symbol_i+4m-4_c',4619,10,145000,4,0,0,-4,0,0),
(176,'symbol_i+4w-4_c',4620,10,145000,4,0,0,0,0,-4),
(177,'symbol_m+4i-4_c',4621,10,145000,-4,0,0,4,0,0),
(178,'symbol_m+4w-4_c',4622,10,145000,0,0,0,4,0,-4),
(179,'symbol_w+4i-4_c',4623,10,145000,-4,0,0,0,0,4),
(180,'symbol_w+4m-4_c',4624,10,145000,0,0,0,-4,0,4);