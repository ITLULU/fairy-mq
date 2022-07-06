CREATE TABLE `op_consumer_record` (
`topic` varchar(300) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '主题',
`partition` varchar(500) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '分区',
`offset` varchar(1000) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '偏移量\r\n偏移量',
`consumer_date` datetime DEFAULT NULL COMMENT '消费数据时间',
KEY `index_topic_partition_offset` (`topic`,`partition`(40),`offset`(100))
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;

CREATE  INDEX index_topic_partition_offset ON op_consumer_record(topic(300),`partition`(40),`offset`(100)) ;