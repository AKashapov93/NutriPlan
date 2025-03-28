--liquibase formatted sql

--changeset kashapov-as:20250327-1 failOnError:true
--comment: create users table, dishes table, meal table, meal_dish table.
--preconditions onFail:mark_ran onError:halt
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'users';
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'dishes';
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'meal';
--precondition-sql-check expectedResult:0 select count(*) from information_schema.tables where table_name = 'meal_dish';
create table if not exists dishes
(
    id                   bigserial primary key,
    calories_per_serving double precision not null,
    carbohydrates        double precision not null,
    fats                 double precision not null,
    name                 varchar(255)     not null,
    proteins             double precision not null
);

create table if not exists users
(
    id                   bigserial primary key,
    age                  integer,
    daily_calorie_intake double precision,
    email                varchar(255),
    goal                 integer,
    height               double precision,
    username             varchar(255),
    weight               double precision
);

create table if not exists meal
(
    id                bigserial primary key,
    calories_per_meal double precision not null,
    create_meal_date  timestamp,
    meal_type         varchar(255)     not null,
    user_id           bigint,
    constraint fk_meal_users foreign key (user_id) references users (id)
);

create index if not exists idx_meal_user_date
    on meal (user_id, create_meal_date);

create table if not exists meal_dish
(
    meal_id bigint not null,
    constraint fk_meal_dish_meal foreign key (meal_id) references meal (id),
    dish_id bigint not null,
    constraint fk_meal_dish_dishes foreign key (dish_id) references dishes (id)
);



