----- Postgres 数据库初始化语句

-- 以下语句以管理员身份执行

create user api_service with password 'api_service_password';

create database iot;

grant all privileges on database iot to api_service;

alter schema iot.public owner to api_service;

-- 以下语句以 api_service 身份执行
\c iot

create table public.firmware
(
    object_name varchar(255) not null
        constraint firmware_pk
            primary key,
    product     text         not null,
    version     text         not null,
    name        text         not null
);

insert into public.firmware (object_name, product, version, name)
values ('oss://aipi-v1-bw20-ota/vb6824/axk_factory_v1.7.bin', 'vb6824', '1.7', '你好小安 axk AiToy ChatRobot cn 1.7');

insert into public.firmware (object_name, product, version, name)
values ('oss:///axk_vb6824_v1.9_ota.bin', 'vb6824', '1.8', '你好小安 axk AiToy ChatRobot cn 1.9');

insert into public.firmware (object_name, product, version, name)
values ('oss://aipi-v1-bw20-ota/vb6824/axk_vb6824_v1.9_ota.bin', 'vb6824', '1.9', '你好小安 axk AiToy ChatRobot cn 1.9');

insert into public.firmware (object_name, product, version, name)
values ('oss://aipi-v1-bw20-ota/vb6824-xinban/v1.9_2026040201.bin', 'vb6824-xinban', '1.9', '你好小安 axk AiToy ChatRobot cn 1.9');
