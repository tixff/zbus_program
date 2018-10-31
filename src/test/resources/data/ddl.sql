DROP TABLE IF EXISTS `topic`;
CREATE TABLE `topic` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL,
  `ip` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `delete_time` datetime DEFAULT NULL,
  `status` int(11) DEFAULT '1',
  `desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

CREATE TABLE `topic_analyze` (
  `id` int(11) NOT NULL,
  `topic_name` varchar(255) DEFAULT NULL,
  `connect_time` datetime DEFAULT NULL,
  `connect_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

