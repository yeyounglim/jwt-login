create database testdata;
use testdata;

drop table users;

CREATE TABLE users (
                         `user_id` int NOT NULL AUTO_INCREMENT,
                         `name` varchar(100) NOT NULL,
                         `email` varchar(100) NOT NULL,
                         `mobile` varchar(20) NOT NULL,
                         `pwd` varchar(500) NOT NULL,
                         `role` varchar(100) NOT NULL,
                         `create_dt` date DEFAULT NULL,
                         `refresh_token` varchar(1024) DEFAULT NULL,
                         PRIMARY KEY (`user_id`)
);

INSERT INTO users (`name`,`email`,`mobile`, `pwd`, `role`,`create_dt`)
VALUES ('yeyoung','yeyoung@test.com','01000000000', '$2y$12$oRRbkNfwuR8ug4MlzH5FOeui.//1mkd.RsOAJMbykTSupVy.x/vb2', 'admin',CURDATE());

{
    "name":"myname",
    "email":"hi@example.com",
    "mobile":"01020304050",
    "pwd":"12345",
    "role":"user"
}