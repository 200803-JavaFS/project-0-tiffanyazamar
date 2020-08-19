
CREATE TABLE User_Information (
id  SERIAL,
username VARCHAR(30),
user_password VARCHAR(30),
firstname VARCHAR(30),
lastname VARCHAR(30),
user_role VARCHAR(10),
PRIMARY KEY (id)
);
-- DDL commands
CREATE TABLE Bank_Account (
account_id SERIAL,
available_amount NUMERIC(20, 2),
account_status VARCHAR(15),
account_type VARCHAR(10),
user_id integer NOT NULL ,

PRIMARY KEY(account_id),
   CONSTRAINT fk_user_id
      FOREIGN KEY(user_id) 
	  REFERENCES user_information(id)
);
-- add first employee user 
INSERT INTO user_information (id, username, user_password, firstname, lastname , user_role )
	VALUES (0, 'tiffany', 'welcome', 'Tiffany', 'Azamar', 'employee');
