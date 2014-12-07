#Layla用
create database layla default character set utf8;

CREATE TABLE account (
 id INTEGER(10) NOT NULL AUTO_INCREMENT,
 email VARBINARY(128) NOT NULL,
 name VARCHAR(256) NOT NULL,
 password VARCHAR(128) NOT NULL,
 sex TINYINT(2) NOT NULL default 0,
 age INTEGER(3) NOT NULL default 0,
 status TINYINT(2) NOT NULL default 0,
 provisioning TINYINT(2) NOT NULL default 0,
 entry_date TIMESTAMP NOT NULL,
 update_date TIMESTAMP NOT NULL,
 PRIMARY KEY (ID)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE account_provision (
 uuid VARBINARY(36) NOT NULL,
 account_id INTEGER(10) NOT NULL,
 entry_date TIMESTAMP NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
