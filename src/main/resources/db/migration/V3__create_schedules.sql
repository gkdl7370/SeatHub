create table schedules (
    id bigint not null auto_increment,
    product_id bigint not null,
    start_at datetime(6) not null,
    end_at datetime(6) not null,
    status varchar(30) not null,
    created_at datetime(6) not null,
    updated_at datetime(6) not null,
    primary key (id),
    constraint fk_schedules_product foreign key (product_id) references products (id)
) engine=InnoDB default charset=utf8mb4;

create index idx_schedules_product_start_at on schedules (product_id, start_at);
