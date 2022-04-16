CREATE DATABASE gogoboard;

CREATE TABLE "Users"
(
  id       uuid      PRIMARY KEY NOT NULL,
  email    VARCHAR   NOT NULL,
  name     VARCHAR   NOT NULL,
  password VARCHAR   NOT NULL,
  created  TIMESTAMP NOT NULL DEFAULT now(),
  updated  TIMESTAMP NOT NULL DEFAULT now()
);


CREATE TABLE "Posts"
(
  id      SERIAL    PRIMARY KEY NOT NULL,
  title   VARCHAR   NOT NULL,
  content TEXT      NOT NULL,
  user_id  uuid      REFERENCES "Users" (id)  NOT NULL,
  created TIMESTAMP NOT NULL DEFAULT now(),
  updated TIMESTAMP NOT NULL DEFAULT now()
);

create extension if not exists "uuid-ossp";
     
insert into "Users" (id, email, name, password) values (uuid_generate_v4(),'norang@gmail.com', 'norang', '1234');
