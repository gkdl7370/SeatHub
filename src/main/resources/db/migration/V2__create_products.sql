create table products (
    id bigint not null auto_increment,
    name varchar(100) not null,
    description longtext not null,
    status varchar(30) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    primary key (id)
) engine=InnoDB default charset=utf8mb4;

