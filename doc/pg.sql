----- Postgres 数据库初始化语句

-- 以下语句以管理员身份执行

create user api_service with password 'api_service_password';

create database iot;

grant all privileges on database iot to api_service;

alter schema iot.public owner to api_service;

-- 以下语句以 api_service 身份执行

create table iot.public.firmware
(
    object_name varchar(255) not null
        constraint firmware_pk
            primary key,
    product     text         not null,
    version     text         not null,
    name        text         not null
);

INSERT INTO iot.public.firmware (object_name, product, version, name) VALUES ('axk_vb6824_v1.9_ota.bin', 'vb6824', '1.9', '你好小安 axk AiToy ChatRobot cn 1.9');
