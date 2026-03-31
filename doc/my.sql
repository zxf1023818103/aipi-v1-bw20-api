### MySQL 数据库初始化语句

# 以下语句以管理员身份执行

create database iot;
create user 'api_service'@'%' identified by 'api_service_password';
grant all privileges on iot.* to 'api_service'@'%';
flush privileges;

# 以下语句以 api_service 身份执行

create table iot.firmware (
    object_name varchar(255) not null primary key,
    product    text         not null,
    version    text         not null,
    name       text         not null
)
collate = utf8mb4_unicode_ci;

INSERT INTO iot.firmware (object_name, product, version, name) VALUES ('axk_vb6824_v1.9_ota.bin', 'vb6824', '1.9', '你好小安 axk AiToy ChatRobot cn 1.9');
