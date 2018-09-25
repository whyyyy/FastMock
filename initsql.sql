CREATE DATABASE /*!32312 IF NOT EXISTS*/`mock` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `mock`;

/*Table structure for table `mockproject` */

DROP TABLE IF EXISTS `mockproject`;

CREATE TABLE `mockproject` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `mockProjectName` varchar(256) NOT NULL COMMENT '项目名称',
  `idex` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增序列',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index` (`idex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mockrequeststrategy` */

DROP TABLE IF EXISTS `mockrequeststrategy`;

CREATE TABLE `mockrequeststrategy` (
  `id` varchar(64) NOT NULL COMMENT '请求策略ID',
  `mockUriId` varchar(64) NOT NULL COMMENT 'mock uri ID',
  `mockRequestName` varchar(64) NOT NULL COMMENT '请求策标题',
  `requestWait` int(11) NOT NULL DEFAULT '0' COMMENT '请求等待时间',
  `verifyExpect` text COMMENT '预期验证，自定义约束json 或 验证脚本',
  `responseExpect` text COMMENT '预期返回，自定义json 格式，或脚本',
  `isRun` varchar(256) NOT NULL DEFAULT 'RUN' COMMENT '是否启用',
  `remark` varchar(256) DEFAULT NULL COMMENT '备注',
  `orderNum` bigint(20) unsigned DEFAULT '0',
  `idex` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增序列',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index` (`idex`),
  KEY `uriid` (`mockUriId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `mockuri` */

DROP TABLE IF EXISTS `mockuri`;

CREATE TABLE `mockuri` (
  `id` varchar(64) NOT NULL COMMENT 'id',
  `mockProjectId` varchar(64) NOT NULL COMMENT 'mock_project id',
  `mockUriName` varchar(256) NOT NULL COMMENT '请求URI名称',
  `mockUri` varchar(256) NOT NULL COMMENT '请求URI名称',
  `mockMethod` varchar(256) NOT NULL,
  `isRun` varchar(256) NOT NULL COMMENT '是否启用',
  `idex` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增序列',
  `createTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updateTime` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `index` (`idex`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Table structure for table `sysconf` */

DROP TABLE IF EXISTS `sysconf`;

CREATE TABLE `sysconf` (
  `code` varchar(12) NOT NULL,
  `value` varchar(12) NOT NULL,
  `desc` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;