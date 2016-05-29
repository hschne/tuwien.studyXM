--deletes everything from all tables
--restarts all ids 
BEGIN;
DELETE FROM rel_exam_question;
DELETE FROM rel_subject_topic;
DELETE FROM rel_question_topic;
DELETE FROM rel_resource_topic;
DELETE FROM rel_resource_question;
DELETE FROM entity_exam;
DELETE FROM entity_subject;
DELETE FROM entity_topic;
DELETE FROM entity_answer;
DELETE FROM entity_question;
DELETE FROM entity_note;
DELETE FROM entity_resource;
ALTER TABLE entity_subject ALTER COLUMN subjectid RESTART WITH 1;
ALTER TABLE entity_exam ALTER COLUMN examid RESTART WITH 1;
ALTER TABLE entity_topic ALTER COLUMN topicid RESTART WITH 1;
ALTER TABLE entity_question ALTER COLUMN questionid RESTART WITH 1;
ALTER TABLE entity_answer ALTER COLUMN answerid RESTART WITH 1;
ALTER TABLE entity_resource ALTER COLUMN resourceid RESTART WITH 1;
ALTER TABLE entity_note ALTER COLUMN noteid RESTART WITH 1;
COMMIT;

--Inserts for subject
INSERT INTO entity_subject VALUES(DEFAULT,'SEPM',6.0,'ss16',25000,'Max'),(DEFAULT,'OOP',3.0,'ws15/16',4000,'Franz'),(DEFAULT,'PP',5.9,'ws14/15',3000,'Jakob'),(DEFAULT,'OOM',3.0,'ss14',3000000,'Phil'),(DEFAULT,'Statistik',3.0,'ws15/16',5000,'Max'),(DEFAULT,'Analysis',4.0,'ss18',5222000,'Franz');
--Inserts for exam
INSERT INTO entity_exam VALUES(DEFAULT, CURRENT_TIMESTAMP, TRUE, 'Max', 1),(DEFAULT, CURRENT_TIMESTAMP, FALSE, 'Phil', 1),(DEFAULT, CURRENT_TIMESTAMP, TRUE, 'Josef', 4),(DEFAULT, CURRENT_TIMESTAMP, FALSE, 'Moritz', 3),(DEFAULT, CURRENT_TIMESTAMP, TRUE, 'Johann', 6),(DEFAULT, CURRENT_TIMESTAMP, TRUE, 'Sherlock', 2);
--Insert for topic
INSERT INTO entity_topic VALUES(DEFAULT,'topic1'),(DEFAULT,'topic2'),(DEFAULT,'topic3'),(DEFAULT,'topic4'),(DEFAULT,'topic5'),(DEFAULT,'topic6');
--Insert for subject_topic
INSERT INTO rel_subject_topic VALUES(1,2),(3,4),(2,6),(2,5),(2,2),(3,5);
--Insert for question
INSERT INTO entity_question VALUES(DEFAULT,'Was ist die Antwort auf alle Fragen?',1,1),(DEFAULT,'Wieviel ergibt 1+1?',1,1),(DEFAULT,'Wer war der erste US Präsident?',1,1),(DEFAULT,'Wie heißt die Hauptstadt von Österreich',1,1),(DEFAULT,'Erklaeren Sie dieses Bild!',1,3),(DEFAULT,'Definieren Sie den Begriff Schokolade!',1,2);
--Insert for exam_question
INSERT INTO rel_exam_question VALUES(1,1,TRUE,TRUE),(1,2,FALSE,TRUE),(2,2,FALSE,FALSE),(3,2,TRUE,TRUE),(4,1,FALSE,FALSE),(6,1,TRUE,TRUE);
--Insert for question_topic
INSERT INTO rel_question_topic VALUES(1,1),(1,2),(1,3),(2,3),(3,3),(5,6);
--Insert for answer
INSERT INTO entity_answer VALUES(DEFAULT,1,'42',TRUE,1),(DEFAULT,1,'69',FALSE,1),(DEFAULT,1,'42',FALSE,2),(DEFAULT,1,'2',TRUE,2),(DEFAULT,1,'George Washington',TRUE,3),(DEFAULT,1,'Barack Obama',FALSE,3),(DEFAULT,1,'Wien',TRUE,4),(DEFAULT,1,'Graz',FALSE,4),(DEFAULT,3,'bild.png',TRUE,5),(DEFAULT,2,'lange Definiton',TRUE,6);
--Insert for resource
INSERT INTO entity_resource VALUES(DEFAULT,1,'name1','reference1'),(DEFAULT,2,'name2','reference2'),(DEFAULT,1,'name3','reference3'),(DEFAULT,2,'name4','reference4'),(DEFAULT,4,'name5','reference5'),(DEFAULT,4,'name6','reference6');
--Insert for resource_topic
INSERT INTO rel_resource_topic VALUES(1,1),(1,2),(2,4),(5,4),(6,6),(2,2);
--Insert for resource_question
INSERT INTO rel_resource_question VALUES(1,1),(1,2),(2,2),(3,2),(4,5),(5,6);
--Insert for note
INSERT INTO entity_note VALUES(DEFAULT, 'Das ist Notiz 1','Author1',CURRENT_TIMESTAMP,1,'reference1'),(DEFAULT, 'Das ist Notiz 2','Author2',CURRENT_TIMESTAMP,2,'reference2'),(DEFAULT, 'Das ist Notiz 3','Author3',CURRENT_TIMESTAMP,3,'reference3'),(DEFAULT, 'Das ist Notiz 4','Author4',CURRENT_TIMESTAMP,4,'reference4'),(DEFAULT, 'Das ist Notiz 5','Author5',CURRENT_TIMESTAMP,5,'reference5'),(DEFAULT, 'Das ist Notiz 6','Author6',CURRENT_TIMESTAMP,1,'reference6');

