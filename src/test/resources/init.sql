CREATE TABLE citest (
 ci_id INT IDENTITY(1,1) PRIMARY KEY,
 key VARCHAR(64) NOT NULL,
 value VARCHAR(64) NOT NULL,
 UNIQUE(ci_id)
);
INSERT INTO citest (key, value) VALUES('plugin', 'dameng-jdbc');
INSERT INTO citest (key, value) VALUES('init', 'updateKey');
INSERT INTO citest (key, value) VALUES('ciUpdateValueCall', 'init');
INSERT INTO citest (key, value) VALUES('4del', '4del');

SELECT * from citest;
/

CREATE OR REPLACE PROCEDURE ciUpdateKey (prc_id INT, prc_key VARCHAR(64))
AS
BEGIN
  UPDATE citest SET key = prc_key WHERE ci_id = prc_id;
END;
/

CREATE OR REPLACE PROCEDURE ciUpdateValue (prc_id INT, prc_value VARCHAR(64))
AS
BEGIN
  UPDATE citest SET value = prc_value WHERE ci_id = prc_id;
END;
/
