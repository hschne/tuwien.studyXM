CREATE TABLE entity_subject(subjectid INTEGER AUTO_INCREMENT PRIMARY KEY, name VARCHAR(80) NOT NULL, ects NUMERIC(5,1) CHECK(ects >= 0), semester VARCHAR(50) NOT NULL, time_spent BIGINT NOT NULL, author VARCHAR(80) NOT NULL);

CREATE TABLE entity_topic(topicid INTEGER AUTO_INCREMENT PRIMARY KEY, topic VARCHAR(200) NOT NULL);

CREATE TABLE rel_subject_topic(subjectid INTEGER REFERENCES entity_subject(subjectid),topicid INTEGER REFERENCES entity_topic(topicid), PRIMARY KEY(subjectid,topicid));

CREATE TABLE entity_exam(examid INTEGER AUTO_INCREMENT PRIMARY KEY, created TIMESTAMP NOT NULL, passed BOOLEAN NOT NULL, author VARCHAR(80) NOT NULL, subject INTEGER REFERENCES entity_subject(subjectid));

CREATE TABLE entity_question(questionid INTEGER AUTO_INCREMENT PRIMARY KEY, question VARCHAR(2000) NOT NULL, type INTEGER NOT NULL,question_time BIGINT NOT NULL);

CREATE TABLE rel_exam_question(examid INTEGER REFERENCES entity_exam(examid),questionid INTEGER REFERENCES entity_question(questionid), question_passed BOOLEAN DEFAULT FALSE NOT NULL, already_answered BOOLEAN DEFAULT FALSE NOT NULL,PRIMARY KEY(examid,questionid));

CREATE TABLE rel_question_topic(questionid INTEGER REFERENCES entity_question(questionid),topicid INTEGER REFERENCES entity_topic(topicid),PRIMARY KEY(questionid,topicid));

CREATE TABLE entity_answer(answerid INTEGER AUTO_INCREMENT PRIMARY KEY, type INTEGER NOT NULL, answer VARCHAR(2000) NOT NULL, is_correct BOOLEAN, question INTEGER REFERENCES entity_question(questionid));

CREATE TABLE entity_resource(resourceid INTEGER AUTO_INCREMENT PRIMARY KEY, type INTEGER NOT NULL, name VARCHAR(100), reference VARCHAR(500));

CREATE TABLE rel_resource_topic(resourceid INTEGER REFERENCES entity_resource(resourceid), topicid INTEGER REFERENCES entity_topic(topicid),PRIMARY KEY(resourceid,topicid));

CREATE TABLE rel_resource_question(resourceid INTEGER REFERENCES entity_resource(resourceid),questionid INTEGER REFERENCES entity_question(questionid),PRIMARY KEY(resourceid,questionid));

CREATE TABLE entity_note(noteid INTEGER AUTO_INCREMENT PRIMARY KEY, note VARCHAR(2000) NOT NULL,author VARCHAR(80) NOT NULL, created TIMESTAMP, resource INTEGER REFERENCES entity_resource(resourceid), reference VARCHAR(500));
