CREATE TABLE foodJournal (
    id bigserial NOT NULL PRIMARY KEY,
    date DATE NOT NULL,
    time character(5),
    meal character(10),
    food character(200)
);

INSERT INTO foodJournal( date, time, meal, food) VALUES ('2017-02-19', '09:45', 'breakfast', 'papanasi cu branza de vaca');
