create table user(
user_id bigint not null auto_increment primary key comment '����',
name varchar(32) not null comment '����',
gender varchar(2) comment '�Ա�',
connect_type varchar(32) comment '��ϵ��ʽ',
connect_value varchar(32) comment '��ϵ��ʽֵ',
address varchar(64) comment '��ַ',
birthday date comment '��������',
create_time timestamp comment '����ʱ��',
update_time timestamp comment '����ʱ��'
);

create table product(
product_id bigint not null auto_increment primary key comment '����',
product_name varchar(64) not null comment '��Ʒ����',
product_other_name varchar(64) comment '��Ʒ����',
product_type varchar(64) comment '��Ʒ����',
product_unit varchar(64) comment '��Ʒ��λ',
product_states varchar(64) comment '��Ʒ״̬',
create_time timestamp comment '����ʱ��',
update_time timestamp comment '����ʱ��'
);

create table product_price_hist (
product_price_hist_id bigint not null auto_increment primary key comment '����',
current_price double not null comment '��ǰ�۸�',
is_available boolean comment '��ǰ�۸��Ƿ���Ч',
effective_begin_time timestamp comment '��Ч��ʼʱ��',
effective_end_time timestamp comment '��Ч����ʱ��',
create_time timestamp comment '����ʱ��',
update_time timestamp comment '����ʱ��'
);

create table order_request(
order_request_id bigint not null auto_increment primary key comment '����',
order_request_states varchar(64) comment '����״̬',
total_balance double comment '�����ܼ۸�',
create_time timestamp comment '����ʱ��',
update_time timestamp comment '����ʱ��'
);

create table order_request_item(
order_request_item_id bigint not null auto_increment primary key comment '����',
order_request_id bigint not null comment '����id',
quantity double comment '����',
quantity_unit varchar(64) comment '������λ����Ʒ��λ��',
current_price double comment '��ǰ�۸�'
);

create table order_request_hist(
order_request_hist_id bigint not null auto_increment primary key comment '����',
order_request_id bigint not null comment '����id',
order_request_states varchar(64) comment '����״̬',
total_balance double comment '�����ܼ۸�',
create_time timestamp comment '����ʱ��',
update_time timestamp comment '����ʱ��'
);