create table users (
    id bigint not null auto_increment,
    email varchar(255) not null,
    password varchar(255) not null,
    name varchar(100) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    primary key (id),
    constraint uk_users_email unique (email)
) engine=InnoDB default charset=utf8mb4;

create table user_roles (
    user_id bigint not null,
    role varchar(30) not null,
    primary key (user_id, role),
    constraint fk_user_roles_user foreign key (user_id) references users (id)
) engine=InnoDB default charset=utf8mb4;
