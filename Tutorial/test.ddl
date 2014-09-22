/*
 * DDL for testing data types
 */
CREATE KEYSPACE orm WITH replication = {
  'class': 'SimpleStrategy',
  'replication_factor': '1'
};


CREATE TABLE testsimple (
  id int,
  description TEXT,
  event_time timestamp,
  PRIMARY KEY (id)
);

CREATE TABLE testadvanced (
 id int,
 description text,
 event_time timestamp,
 maps map<text, text>,
 tags set<text>,
 PRIMARY KEY (id)
);
/*
CREATE TABLE testblob (
  id int,
  description text,
  blobs blob,
  event_time timestamp,
  PRIMARY KEY (id)
)
CREATE TABLE testmap (
  id int,
  description text,
  event_time timestamp,
  maps map<text, text>,
  tags set<text>,
  PRIMARY KEY (id)
)

CREATE TABLE testmap2 (
  id int,
  description text,
  event_time timestamp,
  maps map<text, text>,
  tags set<text>,
  PRIMARY KEY (id)
);
*/