/*
 Navicat Premium Data Transfer

 Source Server         : test环境
 Source Server Type    : MySQL
 Source Server Version : 80019
 Source Host           : 159.138.36.84:3306
 Source Schema         : enterprise

 Target Server Type    : MySQL
 Target Server Version : 80019
 File Encoding         : 65001

 Date: 23/09/2021 19:47:07
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for channel_pts_updates  频道消息相关pts
-- ----------------------------
DROP TABLE IF EXISTS `channel_pts_updates`;
CREATE TABLE `channel_pts_updates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `channel_id` int NOT NULL,
  `pts` int NOT NULL,
  `pts_count` int NOT NULL,
  `update_type` tinyint NOT NULL DEFAULT '0',
  `new_message_id` int NOT NULL DEFAULT '0',
  `update_data` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date2` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=29090 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--sqlite
CREATE TABLE channel_pts_updates (
  id BIGINT NOT NULL PRIMARY KEY,
  channel_id INTEGER NOT NULL,
  pts INTEGER NOT NULL,
  pts_count INTEGER NOT NULL,
  update_type TINYINT NOT NULL DEFAULT '0',
  new_message_id INTEGER NOT NULL DEFAULT '0',
  update_data mediumtext CHARACTER  NOT NULL,
  date2 INTEGER NOT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- Table structure for secret_chat_qts_updates  加密消息
-- ----------------------------
DROP TABLE IF EXISTS `secret_chat_qts_updates`;
CREATE TABLE `secret_chat_qts_updates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `auth_key_id` bigint NOT NULL,
  `chat_id` int NOT NULL,
  `qts` int NOT NULL,
  `chat_message_id` bigint NOT NULL,
  `date2` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1123 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- sqlite
CREATE TABLE secret_chat_qts_updates (
    id              BIGINT    NOT NULL
                              PRIMARY KEY,
    user_id         INTEGER   NOT NULL,
    auth_key_id     BIGINT    NOT NULL,
    chat_id         INTEGER   NOT NULL,
    qts             INTEGER   NOT NULL,
    chat_message_id BIGINT    NOT NULL,
    date2           INTEGER   NOT NULL,
    created_at      TIMESTAMP NOT NULL
                              DEFAULT CURRENT_TIMESTAMP
);

-- ----------------------------
-- Table structure for user_pts_updates  私人或者chat消息
-- ----------------------------
DROP TABLE IF EXISTS `user_pts_updates`;
CREATE TABLE `user_pts_updates` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` int NOT NULL,
  `pts` int NOT NULL,
  `pts_count` int NOT NULL,
  `update_type` tinyint NOT NULL DEFAULT '0',
  `update_data` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `date2` int NOT NULL,
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`,`pts`)
) ENGINE=InnoDB AUTO_INCREMENT=2981562 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

--sqlite
CREATE TABLE user_pts_updates (
  id BIGINT NOT NULL  PRIMARY KEY,
  user_id INTEGER NOT NULL,
  pts INTEGER  KEY NOT NULL,
  pts_count INTEGER NOT NULL,
  update_type tinyint NOT NULL DEFAULT '0',
  update_data mediumtext CHARACTER  NOT NULL,
  date2 INTEGER NOT NULL,
  created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP
) ;


