CREATE TABLE LOCATION (
    ID serial primary key,
    NAME text not null,
    X integer not null,
    Y integer not null
);

CREATE TABLE PERSON (
    ID serial primary key,
    NAME text not null,
    BIRTHDAY date not null,
    PASSPORT text, -- nullable
    LOCATION_ID integer references LOCATION -- nullable
);

CREATE TYPE formOfEducation AS ENUM ('DISTANCE_EDUCATION', 'FULL_TIME_EDUCATION', 'EVENING_CLASSES');
CREATE TYPE semester AS ENUM ('SECOND', 'FOURTH', 'FIFTH', 'SEVENTH', 'EIGHTH');

CREATE TABLE STUDY_GROUP (
    ID bigserial primary key,
    NAME text not null,
    COORDINATE_X real not null,
    COORDINATE_Y integer not null,
    CREATION_DATE timestamptz not null default current_timestamp,
    STUDENT_COUNT int not null,
    FORM_OF_EDUCATION formOfEducation not null,
    SEMESTER semester not null,
    GROUP_ADMIN_ID integer references PERSON -- nullable
);

-- creating initial data

INSERT INTO LOCATION(
NAME, X, Y
) values
('Moscow', 38, 56),
('Saint-Petersburg', 30, 60),
('Riga', 24, 57);

INSERT INTO PERSON (
NAME, BIRTHDAY, PASSPORT, LOCATION_ID
) values
('Sonya', '2002-02-15', '4008158929', 1),
('Maxim', '2002-04-10', '4002500172', 2),
('Phillip', '2002-04-10', '4000629759', 2),
('Roman', '2002-04-10', '4112123984', 3);

INSERT INTO STUDY_GROUP (
NAME, COORDINATE_X, COORDINATE_Y, CREATION_DATE, STUDENT_COUNT, FORM_OF_EDUCATION, SEMESTER, GROUP_ADMIN_ID
) values
('P3111', 5.5, 60, '2020-08-25 13:33:50'::timestamptz, 30, 'FULL_TIME_EDUCATION', 'SECOND', 1),
('P3112', 8.7, 90, '2017-08-25 12:20:10'::timestamptz, 24, 'EVENING_CLASSES', 'SEVENTH', 2),
('P3114', 5.2, 19, '2018-08-26 10:50:18'::timestamptz, 27, 'DISTANCE_EDUCATION', 'FIFTH', 3),
('P3130', 5.0, 5, '2020-08-25 14:05:43'::timestamptz, 15, 'FULL_TIME_EDUCATION', 'SECOND', 4);