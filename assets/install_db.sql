INSERT INTO PAYS VALUES(-1, '');
INSERT INTO PAYS VALUES(1, 'France');
INSERT INTO PAYS VALUES(2, 'Allemagne');
INSERT INTO PAYS VALUES(3, 'Italie');
INSERT INTO PAYS VALUES(4, 'Espagne');
INSERT INTO PAYS VALUES(5, 'USA');
INSERT INTO PAYS VALUES(6, 'Australie');
INSERT INTO PAYS VALUES(7, 'Argentine');
INSERT INTO PAYS VALUES(8, 'Portugal');
INSERT INTO PAYS VALUES(9, 'Chili');
INSERT INTO PAYS VALUES(10, 'Afrique du Sud');
INSERT INTO PAYS VALUES(11, 'Suisse');
INSERT INTO PAYS VALUES(12, 'Luxembourg');
INSERT INTO PAYS VALUES (13,'Roumanie');
INSERT INTO PAYS VALUES (14,'Russie');
INSERT INTO PAYS VALUES (15,'Grèce');
INSERT INTO PAYS VALUES (16,'Hongrie');
INSERT INTO PAYS VALUES (17,'Brésil');
INSERT INTO PAYS VALUES (18,'Autriche');
INSERT INTO PAYS VALUES (19,'Ukraine');
INSERT INTO PAYS VALUES (20,'Moldavie');
INSERT INTO PAYS VALUES (21,'Bulgarie');

INSERT INTO REGIONS VALUES(-1,1,'',0);
INSERT INTO REGIONS VALUES(1,1,'Alsace',0);
INSERT INTO REGIONS VALUES(2,1,'Beaujolais et Lyonnais',0);
INSERT INTO REGIONS VALUES(3,1,'Bordelais',0);
INSERT INTO REGIONS VALUES(4,1,'Appellations régionales de Bordeaux',3);
INSERT INTO REGIONS VALUES(5,1,'Blayais et Bourgeais',3);
INSERT INTO REGIONS VALUES(6,1,'Libournais',3);
INSERT INTO REGIONS VALUES(7,1,'Entre Garonne et Dordogne',3);
INSERT INTO REGIONS VALUES(8,1,'Région des Graves',3);
INSERT INTO REGIONS VALUES(9,1,'Médoc',3);
INSERT INTO REGIONS VALUES(10,1,'Vins blancs liquoreux',3);
INSERT INTO REGIONS VALUES(11,1,'Bourgogne',0);
INSERT INTO REGIONS VALUES(12,1,'Appellations régionales de Bourgogne',11);
INSERT INTO REGIONS VALUES(13,1,'Chablisien',11);
INSERT INTO REGIONS VALUES(14,1,'Côte de Nuits',11);
INSERT INTO REGIONS VALUES(15,1,'Côte de Beaune',11);
INSERT INTO REGIONS VALUES(16,1,'Côte chalonnaise',11);
INSERT INTO REGIONS VALUES(17,1,'Mâconnais',11);
INSERT INTO REGIONS VALUES(18,1,'Champagne',0);
INSERT INTO REGIONS VALUES(19,1,'Corse',0);
INSERT INTO REGIONS VALUES(20,1,'Jura',0);
INSERT INTO REGIONS VALUES(21,1,'Languedoc',0);
INSERT INTO REGIONS VALUES(22,1,'Lorraine',0);
INSERT INTO REGIONS VALUES(23,1,'Poitou-Charentes',0);
INSERT INTO REGIONS VALUES(24,1,'Provence',0);
INSERT INTO REGIONS VALUES(25,1,'Rhône Nord',0);
INSERT INTO REGIONS VALUES(26,1,'Rhône Sud',0);
INSERT INTO REGIONS VALUES(27,1,'Roussillon',0);
INSERT INTO REGIONS VALUES(28,1,'Savoie et Bugey',0);
INSERT INTO REGIONS VALUES(29,1,'Sud-Ouest',0);
INSERT INTO REGIONS VALUES(30,1,'Piémont du Massif central',29);
INSERT INTO REGIONS VALUES(31,1,'Moyenne Garonne',29);
INSERT INTO REGIONS VALUES(32,1,'Bergeracois et Duras',29);
INSERT INTO REGIONS VALUES(33,1,'Piémont pyrénéen',29);
INSERT INTO REGIONS VALUES(34,1,'Vallée de la Loire et Centre',0);
INSERT INTO REGIONS VALUES(35,1,'Appellations régionales de la Loire',34);
INSERT INTO REGIONS VALUES(36,1,'La région nantaise',34);
INSERT INTO REGIONS VALUES(37,1,'Anjou-Saumur',34);
INSERT INTO REGIONS VALUES(38,1,'La Touraine',34);
INSERT INTO REGIONS VALUES(39,1,'Les vignobles du Centre',34);

INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(-1,'',-1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(1,'Alsace chasselas ou gutedel',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(2,'Alsace edelzwicker',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(3,'Alsace gewurztraminer',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(4,'Alsace grand cru',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(5,'Alsace klevener-de-heiligenstein',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(6,'Alsace muscat',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(7,'Alsace pinot gris',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(8,'Alsace pinot noir',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(9,'Alsace pinot ou klevner',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(10,'Alsace riesling',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(11,'Alsace sylvaner',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(12,'Alsace sélection de grains nobles',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(13,'Alsace vendanges tardives',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(14,'Crémant-d''alsace',1);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(15,'Beaujolais',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(16,'Beaujolais-supérieur',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(17,'Beaujolais-villages',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(18,'Brouilly',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(19,'Chiroubles',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(20,'Chénas',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(21,'Coteaux-du-lyonnais',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(22,'Côte-de-brouilly',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(23,'Fleurie',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(24,'Juliénas',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(25,'Morgon',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(26,'Moulin-à-vent',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(27,'Régnié',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(28,'Saint-amour',2);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(29,'Bordeaux',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(30,'Bordeaux clairet',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(31,'Bordeaux rosé',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(32,'Bordeaux sec',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(33,'Bordeaux supérieur',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(34,'Crémant-de-bordeaux',4);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(35,'Blaye',5);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(36,'Côtes-de-blaye',5);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(37,'Côtes-de-bourg',5);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(38,'Premières-côtes-de-blaye',5);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(39,'Bordeaux-côtes-de-francs',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(40,'Canon-fronsac',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(41,'Côtes-de-castillon',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(42,'Fronsac',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(43,'Lalande-de-pomerol',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(44,'Lussac-saint-émilion',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(45,'Montagne-saint-émilion',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(46,'Pomerol',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(47,'Puisseguin-saint-émilion',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(48,'Saint-georges-saint-émilion',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(49,'Saint-émilion',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(50,'Saint-émilion grand cru',6);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(51,'Bordeaux-haut-benauge',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(52,'Côtes-de-bordeaux-saint-macaire',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(53,'Entre-deux-mers',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(54,'Entre-deux-mers-haut-benauge',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(55,'Graves-de-vayres',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(56,'Premières-côtes-de-bordeaux',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(57,'Sainte-foy-bordeaux',7);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(58,'Graves',8);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(59,'Graves supérieures',8);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(60,'Pessac-léognan',8);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(61,'Haut-médoc',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(62,'Listrac-médoc',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(63,'Margaux',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(64,'Moulis-en-médoc',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(65,'Médoc',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(66,'Pauillac',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(67,'Saint-estèphe',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(68,'Saint-julien',9);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(69,'Barsac',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(70,'Cadillac',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(71,'Cérons',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(72,'Loupiac',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(73,'Sainte-croix-du-mont',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(74,'Sauternes',10);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(75,'Bourgogne',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(76,'Bourgogne-aligoté',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(77,'Bourgogne-grand-ordinaire',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(78,'Bourgogne-hautes-côtes-de-beaune',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(79,'Bourgogne-hautes-côtes-de-nuits',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(80,'Bourgogne-passetoutgrain',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(81,'Crémant-de-bourgogne',12);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(82,'Chablis',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(83,'Chablis grand cru',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(84,'Chablis premier cru',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(85,'Irancy',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(86,'Petit-chablis',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(87,'Saint-bris',13);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(88,'Bonnes-mares',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(89,'Chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(90,'Chambertin-clos-de-bèze',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(91,'Chambolle-musigny',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(92,'Chapelle-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(93,'Charmes-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(94,'Clos-de-la-roche',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(95,'Clos-de-tart',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(96,'Clos-de-vougeot',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(97,'Clos-des-lambrays',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(98,'Clos-saint-denis',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(99,'Côte-de-nuits-villages',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(100,'Échézeaux',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(101,'Fixin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(102,'Gevrey-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(103,'Grande-rue (La)',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(104,'Grands-échézeaux',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(105,'Griotte-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(106,'Latricières-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(107,'Marsannay',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(108,'Mazis-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(109,'Mazoyères-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(110,'Morey-saint-denis',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(111,'Musigny',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(112,'Nuits-saint-georges',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(113,'Richebourg',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(114,'Romanée (La)',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(115,'Romanée-conti',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(116,'Romanée-saint-vivant',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(117,'Ruchottes-chambertin',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(118,'Tâche (La)',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(119,'Vosne-romanée',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(120,'Vougeot',14);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(121,'Aloxe-corton',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(122,'Auxey-duresses',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(123,'Beaune',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(124,'Bienvenues-bâtard-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(125,'Blagny',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(126,'Bâtard-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(127,'Chassagne-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(128,'Chevalier-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(129,'Chorey-lès-beaune',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(130,'Corton',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(131,'Corton-charlemagne',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(132,'Criots-bâtard-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(133,'Côte-de-beaune',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(134,'Côte-de-beaune-villages',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(135,'Ladoix',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(136,'Maranges',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(137,'Meursault',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(138,'Monthélie',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(139,'Montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(140,'Pernand-vergelesses',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(141,'Pommard',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(142,'Puligny-montrachet',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(143,'Saint-aubin',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(144,'Saint-romain',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(145,'Santenay',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(146,'Savigny-lès-beaune',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(147,'Volnay',15);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(148,'Bourgogne-côte-chalonnaise',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(149,'Bouzeron',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(150,'Givry',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(151,'Mercurey',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(152,'Montagny',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(153,'Rully',16);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(154,'Mâcon',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(155,'Mâcon-villages',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(156,'Pouilly-fuissé',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(157,'Pouilly-loché',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(158,'Pouilly-vinzelles',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(159,'Saint-véran',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(160,'Viré-clessé',17);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(161,'Champagne',18);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(162,'Coteaux-champenois',18);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(163,'Rosé-des-riceys',18);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(164,'Ajaccio',19);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(165,'Corse ou vin-de-corse',19);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(166,'Muscat-du-cap-corse',19);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(167,'Patrimonio',19);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(168,'Arbois',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(169,'Château-chalon',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(170,'Crémant-du-jura',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(171,'Côtes-du-jura',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(172,'Etoile (L'')',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(173,'Macvin-du-jura',20);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(174,'Blanquette méthode ancestrale',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(175,'Blanquette-de-limoux',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(176,'Cabardès',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(177,'Clairette-du-languedoc',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(178,'Corbières',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(179,'Corbières-boutenac',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(180,'Crémant-de-limoux',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(181,'Faugères',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(182,'Fitou',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(183,'Languedoc',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(184,'Limoux',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(185,'Malepère',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(186,'Minervois ',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(187,'Minervois-la-livinière',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(188,'Muscat-de-frontignan',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(189,'Muscat-de-lunel',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(190,'Muscat-de-mireval',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(191,'Muscat-de-saint-jean-de-minervois',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(192,'Saint-chinian',21);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(193,'Côtes-de-toul',22);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(194,'Moselle',22);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(195,'Haut-poitou',23);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(196,'Pineau-des-charentes',23);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(197,'Bandol',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(198,'Baux-de-provence (les)',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(199,'Bellet',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(200,'Cassis',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(201,'Coteaux-d''aix-en-provence',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(202,'Coteaux-varois-en-provence',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(203,'Côtes-de-provence',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(204,'Palette',24);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(205,'Château-grillet',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(206,'Châtillons-en-diois',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(207,'Clairette-de-die ',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(208,'Condrieu',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(209,'Cornas',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(210,'Crozes-hermitage',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(211,'Crémant-de-die',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(212,'Côte-rôtie',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(213,'Hermitage',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(214,'Saint-joseph',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(215,'Saint-péray',25);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(216,'Beaumes-de-venise',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(217,'Châteauneuf-du-pape',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(218,'Clairette-de-bellegarde',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(219,'Costières-de-nîmes',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(220,'Coteaux-de-pierrevert',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(221,'Coteaux-du-tricastin',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(222,'Côtes-du-luberon',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(223,'Côtes-du-rhône',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(224,'Côtes-du-rhône-villages',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(225,'Côtes-du-ventoux',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(226,'Côtes-du-vivarais',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(227,'Gigondas',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(228,'Lirac',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(229,'Muscat-de-beaumes-de-venise',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(230,'Rasteau',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(231,'Tavel',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(232,'Vacqueyras',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(233,'Vinsobres',26);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(234,'Banyuls',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(235,'Banyuls grand cru',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(236,'Collioure',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(237,'Côtes-du-roussillon',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(238,'Côtes-du-roussillon-villages',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(239,'Maury',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(240,'Muscat-de-rivesaltes',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(241,'Rivesaltes',27);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(242,'Bugey',28);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(243,'Crépy',28);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(244,'Roussette-de-savoie',28);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(245,'Seyssel',28);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(246,'Vin-de-savoie',28);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(247,'Cahors',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(248,'Coteaux-du-quercy',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(249,'Côtes-de-millau',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(250,'Gaillac',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(251,'Marcillac',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(252,'Vins-d''entraygues-le-fel',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(253,'Vins-d''estaing',30);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(254,'Buzet',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(255,'Côtes-du-brulhois',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(256,'Côtes-du-marmandais',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(257,'Fronton',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(258,'Lavilledieu',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(259,'Saint-sardos',31);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(260,'Bergerac',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(261,'Bergerac rosé',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(262,'Bergerac sec',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(263,'Côtes-de-bergerac',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(264,'Côtes-de-bergerac blanc',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(265,'Côtes-de-duras',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(266,'Côtes-de-montravel',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(267,'Haut-montravel',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(268,'Monbazillac',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(269,'Montravel',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(270,'Pécharmant',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(271,'Rosette',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(272,'Saussignac',32);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(273,'Béarn',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(274,'Floc-de-gascogne',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(275,'Irouléguy',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(276,'Jurançon ',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(277,'Jurançon sec',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(278,'Madiran',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(279,'Pacherenc-du-vic-bilh',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(280,'Saint-mont',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(281,'Tursan',33);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(282,'Rosé-de-loire',35);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(283,'Crémant-de-loire',35);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(284,'Coteaux-d''ancenis',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(285,'Fiefs-vendéens',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(286,'Gros-plant-du-pays-nantais',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(287,'Muscadet',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(288,'Muscadet-coteaux-de-la-loire',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(289,'Muscadet-côtes-de-grand-lieu',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(290,'Muscadet-sèvre-et-maine',36);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(291,'Anjou',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(292,'Anjou-coteaux-de-la-loire',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(293,'Anjou-gamay',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(294,'Anjou-villages',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(295,'Anjou-villages-brissac',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(296,'Bonnezeaux',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(297,'Cabernet-d''anjou',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(298,'Cabernet-de-saumur',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(299,'Chaume',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(300,'Coteaux-de-l''aubance',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(301,'Coteaux-de-saumur',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(302,'Coteaux-du-layon',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(303,'Quarts-de-chaume',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(304,'Rosé-d''anjou',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(305,'Saumur',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(306,'Saumur-champigny',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(307,'Savennières',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(308,'Savennières-coulée-de-serrant',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(309,'Savennières-roche-aux-moines',37);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(310,'Bourgueil',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(311,'Cheverny',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(312,'Chinon',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(313,'Coteaux-du-loir',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(314,'Coteaux-du-vendômois',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(315,'Cour-cheverny',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(316,'Jasnières',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(317,'Montlouis-sur-loire',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(318,'Orléans ',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(319,'Orléans-cléry',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(320,'Saint-nicolas-de-bourgueil',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(321,'Touraine',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(322,'Touraine-amboise',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(323,'Touraine-azay-le-rideau',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(324,'Touraine-mesland',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(325,'Touraine-noble-joué',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(326,'Valençay',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(327,'Vouvray',38);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(328,'Châteaumeillant',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(329,'Coteaux-du-giennois',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(330,'Côte-roannaise',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(331,'Côtes-d''auvergne',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(332,'Côtes-du-forez',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(333,'Menetou-salon',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(334,'Pouilly-fumé',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(335,'Pouilly-sur-loire',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(336,'Quincy',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(337,'Reuilly',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(338,'Saint-pourçain',39);
INSERT INTO APPELLATIONS (_id, nom, id_region)  VALUES(339,'Sancerre',39);
