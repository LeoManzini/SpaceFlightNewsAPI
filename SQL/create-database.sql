CREATE TABLE IF NOT EXISTS article (
   id int8 NOT NULL,
   featured BOOLEAN NOT NULL,
   image_url VARCHAR(10485760) NOT NULL,
   inserted_by_human BOOLEAN,
   news_site VARCHAR(10485760) NOT NULL,
   published_at VARCHAR(255) NOT NULL,
   summary VARCHAR(10485760) NOT NULL,
   title VARCHAR(10485760) NOT NULL,
   url VARCHAR(10485760) NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS article_events (
   article_id int8 NOT NULL,
   events_id int8 NOT NULL
);

CREATE TABLE IF NOT EXISTS article_launches (
   article_id int8 NOT NULL,
   launches_id VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS article_control (
   id  bigserial NOT NULL,
   article_count int8 NOT NULL,
   last_article_id int8 NOT NULL,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS article_delete_control (
   id  bigserial NOT NULL,
   article_excluded_id int8,
   exclusion_date timestamp,
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS events (
   id int8 NOT NULL,
   provider VARCHAR(255),
   PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS launches (
   id VARCHAR(255) NOT NULL,
   provider VARCHAR(255),
   PRIMARY KEY (id)
);

ALTER TABLE article_events 
ADD CONSTRAINT FKcyhbd89t9d5q3l8cekejg27hm 
FOREIGN KEY (events_id) 
REFERENCES events;

ALTER TABLE article_events 
ADD CONSTRAINT FKe19vasqhpsl7i93ebhcxo2f4n 
FOREIGN KEY (article_id) 
REFERENCES article;

ALTER TABLE article_launches 
ADD CONSTRAINT FKd9e6lifl3bv99664a436arkhy 
FOREIGN KEY (launches_id) 
REFERENCES launches;

ALTER TABLE article_launches 
ADD CONSTRAINT FKgwaym0kyy0fv79asc222l6jgj 
FOREIGN KEY (article_id) 
REFERENCES article;
