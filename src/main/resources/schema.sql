create table transaction_user(
    id int auto_increment primary key,
    first_name varchar (65) not null,
    last_name varchar (65) not null,
    phone_number varchar(65) not null,
    email varchar(65) not null,
    password varchar(128) not null,
    created_at DATE not null,
    is_active TINYINT
);