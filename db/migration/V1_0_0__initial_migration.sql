CREATE TABLE "country" (
    id serial8 primary key unique ,
    name varchar(50) not null,
    code varchar(50) not null unique,
    country_code varchar(10) not null unique
);

CREATE TABLE "state" (
    id serial8 primary key unique,
    name varchar(50) not null,
    code varchar(50) not null unique,
    country varchar(50) references country(code)
);

CREATE TABLE "city" (
    id serial8 primary key unique ,
    name varchar(50) not null,
    code varchar(50) not null unique,
    country varchar(50) references country (code),
    state varchar(50) references state (code)
);

CREATE TABLE "organisation" (
    id serial8 primary key unique ,
    organisation_code varchar(50) not null unique ,
    name varchar(50) not null,
    address varchar(200) not null,
    poc_name varchar(50) not null,
    poc_number varchar(15) not null,
    latitude float8,
    longitude float8,
    city varchar(50) references city (code),
    created_date timestamp not null default now(),
    modified_date timestamp not null default now()
);

CREATE TABLE "role" (
   id serial8 primary key unique ,
   name varchar(50) not null,
   created_date timestamp not null default now(),
   modified_date timestamp not null default now()
);

CREATE TABLE "permission" (
    id serial8 primary key unique ,
    name varchar(50) not null,
    created_date timestamp not null default now(),
    modified_date timestamp not null default now()
);

CREATE TABLE "role_permission" (
    id serial8 primary key unique ,
    role_id bigint references "role" (id),
    permission_id bigint references "permission" (id)
);


CREATE TABLE "user" (
    id serial8 primary key,
    name varchar(50) not null,
    email varchar(50) not null,
    password varchar(100) not null,
    organisation varchar(50) references "organisation" (organisation_code),
    created_date timestamp not null default now(),
    modified_date timestamp not null default now()
);

CREATE TABLE "user_role" (
    id serial8 primary key unique ,
    role_id bigint references "role" (id),
    user_id bigint references "user" (id)
);

CREATE TABLE "device" (
    id serial8 primary key unique ,
    device_id varchar(20) not null unique ,
    manufacturing_date date,
    country varchar(50) references country (code),
    state varchar(50) references state (code),
    city varchar(50) references city (code),
    created_date timestamp not null default now(),
    modified_date timestamp not null default now()
);

CREATE TABLE "vehicle" (
    id serial8 primary key unique ,
    registration_number varchar(20) not null,
    user_id bigint references "user" (id),
    device_id varchar(20) references "device" (device_id),
    organisation_code varchar(50) references "organisation" (organisation_code)
)