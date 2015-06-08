create table good
(
  id int primary key auto_increment,
  trader_name varchar(32),
  user_name varchar(32),
  name varchar(32),
  comment varchar(256),
  count int,
  price int,
  status int,
  vip bool,
  time bigint
);

create table history
(
  id int primary key auto_increment,
  gid int,
  trader_name varchar(32),
  user_name varchar(32),
  count int,
  price int,
  time bigint,
  foreign key (gid) references good(id)
);

create table trader
(
  name varchar(32) primary key,
  host varchar(32),
  port int,
  app_name varchar(32)
);