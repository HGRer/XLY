create table user(
user_id bigint not null auto_increment primary key comment '主键',
name varchar(32) not null comment '姓名',
gender varchar(2) comment '性别',
connect_type varchar(32) comment '联系方式',
connect_value varchar(32) comment '联系方式值',
address varchar(64) comment '地址',
birthday date comment '出生日期',
create_time timestamp comment '创建时间',
update_time timestamp comment '更新时间'
);

create table product(
product_id bigint not null auto_increment primary key comment '主键',
product_name varchar(64) not null comment '商品名字',
product_other_name varchar(64) comment '商品别名',
product_type varchar(64) comment '商品类型',
product_unit varchar(64) comment '商品单位',
product_states varchar(64) comment '商品状态',
create_time timestamp comment '创建时间',
update_time timestamp comment '更新时间'
);

create table product_price_hist (
product_price_hist_id bigint not null auto_increment primary key comment '主键',
current_price double not null comment '当前价格',
is_available boolean comment '当前价格是否生效',
effective_begin_time timestamp comment '生效开始时间',
effective_end_time timestamp comment '生效结束时间',
create_time timestamp comment '创建时间',
update_time timestamp comment '更新时间'
);

create table order_request(
order_request_id bigint not null auto_increment primary key comment '主键',
order_request_states varchar(64) comment '订单状态',
total_balance double comment '订单总价格',
create_time timestamp comment '创建时间',
update_time timestamp comment '更新时间'
);

create table order_request_item(
order_request_item_id bigint not null auto_increment primary key comment '主键',
order_request_id bigint not null comment '订单id',
quantity double comment '数量',
quantity_unit varchar(64) comment '数量单位（商品单位）',
current_price double comment '当前价格'
);

create table order_request_hist(
order_request_hist_id bigint not null auto_increment primary key comment '主键',
order_request_id bigint not null comment '订单id',
order_request_states varchar(64) comment '订单状态',
total_balance double comment '订单总价格',
create_time timestamp comment '创建时间',
update_time timestamp comment '更新时间'
);