-- ----------------------------
-- Table structure for demo_user
-- ----------------------------
DROP TABLE IF EXISTS `demo_user`;
CREATE TABLE `demo_user`
(
  `user_id`       int(11)                       NOT NULL AUTO_INCREMENT,
  `user_name`     varchar(255) COLLATE utf8_bin NOT NULL,
  `user_mail`     varchar(255) COLLATE utf8_bin NOT NULL,
  `user_password` varchar(255) COLLATE utf8_bin NOT NULL,
  `user_address`  varchar(255) COLLATE utf8_bin NOT NULL,
  `user_tel`      varchar(255) COLLATE utf8_bin NOT NULL,
  `user_birthday` datetime                      NOT NULL,
  `user_created`  datetime                      NOT NULL,
  `user_updated`  datetime                      NOT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- ----------------------------
-- Records of demo_user
-- ----------------------------
INSERT INTO `demo_user`
VALUES ('1', 'Yahyaoo', '123456@qq.com', '$2a$10$xsAHUzgZv.ZFXzJB2pTn8uHpz7ecMhleaBGwlAi0ZdCTwo/SD76cq',
        'QingYangQu,Chengdu,China', '12345678910', '2004-06-08 17:17:48', '2019-03-24 17:17:56', '2019-03-24 17:17:59');

-- ----------------------------
-- Table structure for oauth_client_details
-- ----------------------------
DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
  `client_id`               varchar(256) COLLATE utf8_bin NOT NULL,
  `resource_ids`            varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `client_secret`           varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `scope`                   varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `authorized_grant_types`  varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `web_server_redirect_uri` varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `authorities`             varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  `access_token_validity`   int(11)                        DEFAULT NULL,
  `refresh_token_validity`  int(11)                        DEFAULT NULL,
  `additional_information`  varchar(4096) COLLATE utf8_bin DEFAULT NULL,
  `autoapprove`             varchar(256) COLLATE utf8_bin  DEFAULT NULL,
  PRIMARY KEY (`client_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

-- ----------------------------
-- Records of oauth_client_details
-- ----------------------------
INSERT INTO `oauth_client_details`
VALUES ('SampleClientId', null, '$2a$10$bDYM4qbYKsm2gQkACg5IEewphce1azSdeZE/7VGUnzKIoXoSzYV.K', 'user_info',
        'authorization_code', 'http://localhost:8082/ui/login', null, '3600', null, null, 'true');
INSERT INTO `oauth_client_details`
VALUES ('SampleClientId2', null, '$2a$10$bDYM4qbYKsm2gQkACg5IEewphce1azSdeZE/7VGUnzKIoXoSzYV.K', 'user_info2',
        'authorization_code', 'http://localhost:8083/ui2/login', null, '3600', null, null, 'true');