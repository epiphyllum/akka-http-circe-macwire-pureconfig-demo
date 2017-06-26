CREATE TABLE `certificate` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `idType` int(2) NOT NULL COMMENT  '证件类型 1:身份证',
  `idName` varchar(20) NOT NULL  COMMENT '证件名称(例如:身份证)',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;