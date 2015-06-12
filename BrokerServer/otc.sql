create table broker
(
  name varchar(32) primary key
);

create table order_t
(
  id int primary key auto_increment,
  name varchar(32),
  trader varchar(32),
  broker varchar(32),
  count int,
  price int,
  status int,
  time bigint,
  type boolean
);

create table history
(
  id int primary key auto_increment,
  seller int,
  buyer int,
  count int,
  time bigint,
  foreign key (seller) references order_t(id),
  foreign key (buyer) references order_t(id)
);