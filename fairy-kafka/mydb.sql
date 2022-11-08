CREATE TABLE `op_consumer_record` (
  `topic` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主题',
  `topic_partition` int DEFAULT NULL COMMENT '分区',
  `topic_offset` bigint DEFAULT NULL COMMENT '偏移量\r\n偏移量',
  `consumer_date` datetime DEFAULT NULL COMMENT '消费数据时间',
  `send_date` datetime DEFAULT NULL,
  KEY `index_topic_partition_offset` (`topic`,`topic_partition`,`topic_offset`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE  INDEX index_topic_partition_offset ON op_consumer_record(topic(300),`topic_partition`(40),`topic_offset`(100)) ;