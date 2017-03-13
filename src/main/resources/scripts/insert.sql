--deletes everything from all tables
--restarts all ids 
BEGIN;
DELETE FROM rel_exam_question;
DELETE FROM rel_subject_topic;
DELETE FROM rel_question_topic;
DELETE FROM rel_resource_question;
DELETE FROM entity_exam;
DELETE FROM entity_subject;
DELETE FROM entity_topic;
DELETE FROM entity_answer;
DELETE FROM entity_question;
DELETE FROM entity_resource;
ALTER TABLE entity_subject ALTER COLUMN subjectid RESTART WITH 1;
ALTER TABLE entity_exercise_exam ALTER COLUMN examid RESTART WITH 1;
ALTER TABLE entity_topic ALTER COLUMN topicid RESTART WITH 1;
ALTER TABLE entity_question ALTER COLUMN questionid RESTART WITH 1;
ALTER TABLE entity_answer ALTER COLUMN answerid RESTART WITH 1;
ALTER TABLE entity_resource ALTER COLUMN resourceid RESTART WITH 1;
ALTER TABLE entity_exam ALTER COLUMN examid RESTART WITH 1;
COMMIT;

--Inserts for subject
INSERT INTO entity_subject VALUES
(DEFAULT,'SEPM',6.0,'SS16',70000,'Max'),
(DEFAULT,'TGI',5.9,'WS14/15',3000,'Jakob'),
(DEFAULT,'OOM',3.0,'SS14',1234,'Phil'),
(DEFAULT,'Statistik',3.0,'WS15/16',5000,'Max'),
(DEFAULT,'Analysis',4.0,'SS18',12500,'Franz'),
(DEFAULT,'AlgoDat 1',3.0,'SS14',0,'Jackson');
--Inserts for exam
INSERT INTO entity_exam VALUES
(DEFAULT, 1,  CURRENT_TIMESTAMP, '2016-11-11 17:00:00.0', 'SEPM Vorlesungsprüfung'), 
(DEFAULT, 5, '2016-06-09 11:56:25.764', '2016-06-30 18:30:00.0', 'Analysis Vorlesungsprüfung'),
(DEFAULT, 3, '2016-03-09 13:36:45.454', '2016-03-21 19:00:00.0', 'OOM Test 1'),
(DEFAULT, 3, '2016-04-22 15:06:24.55', '2016-06-29 19:00:00.0', 'OOM Test 2'),
(DEFAULT, 3, '2016-04-22 15:08:41.71', '2016-06-03 19:00:00.0', 'OOM Test 3'),
(DEFAULT, 4, '2016-06-02 09:11:44.12', '2016-07-30 12:00:00.0', 'Statistik Vorlesungsprüfung'),
(DEFAULT, 2, '2016-05-02 21:35:31.12', '2016-06-17 16:00:00.0', 'TGI Test 3');
--Inserts for exercise_exam
INSERT INTO entity_exercise_exam VALUES
(DEFAULT, 1,  CURRENT_TIMESTAMP, FALSE, 'Max', 1, 90),
(DEFAULT, 3, '2016-03-13 14:55:31.011', TRUE, 'Phil', 3, 30),
(DEFAULT, 4, '2016-05-13 15:15:31.09', FALSE, 'Phil', 3, 30),
(DEFAULT, 5, '2016-06-03 01:55:34.123', TRUE, 'Phil', 3, 30),
(DEFAULT, 2, '2016-06-10 12:10:10', TRUE, 'Franz', 5, 100),
(DEFAULT, 3, '2016-02-28 04:12:33.982', TRUE, 'Phil', 3, 30),
(DEFAULT, 1, '2016-08-22 06:57:11.02', FALSE, 'Max', 1, 90),
(DEFAULT, 6, CURRENT_TIMESTAMP, FALSE, 'Max', 4, 100),
(DEFAULT, 7, '2016-05-29 16:54:11.34', FALSE, 'Jakob', 2, 90);
--Insert for topic
INSERT INTO entity_topic VALUES
(DEFAULT,'Klassendiagramm'),
(DEFAULT,'Aktivitätsdiagramm'),
(DEFAULT,'Objektdiagramm'),
(DEFAULT,'Anwendungsfalldiagramm'),
(DEFAULT,'Zustandsdiagramm'),
(DEFAULT,'Sequenzdiagramm'),
(DEFAULT, 'Pipelining'),
(DEFAULT, 'Netzwerke'),
(DEFAULT, 'Speichermanagement'),
(DEFAULT, 'Design Patterns'),
(DEFAULT, 'Persistenz'),
(DEFAULT, 'Software Engineering Phasen'),
(DEFAULT, 'Technik und Werkzeuge'),
(DEFAULT, 'Software Engineering Prozesse'),
(DEFAULT, 'Folgen, Reihen, Funktionen'),
(DEFAULT, 'Lineare Algebra'),
(DEFAULT, 'Differential- und Integralrechnung in einer Variable'),
(DEFAULT, 'Differential- und Integralrechnung in mehreren Variablen'),
(DEFAULT, 'Differenzengleichungen'),
(DEFAULT, 'Stochastische Größen und Verteilungen'),
(DEFAULT, 'Spezielle Verteilungen'),
(DEFAULT, 'Folgen von Stochastischen Größen'),
(DEFAULT, 'Multivariate Verteilungen'),
(DEFAULT, 'Wahrscheinlichkeit'),
(DEFAULT, 'Deskriptive und explorative Statistik');
--Insert for subject_topic
INSERT INTO rel_subject_topic VALUES
(1,10),
(1,11),
(1,12),
(1,13),
(2,7),
(2,8),
(2,9),
(3,1),
(3,2),
(3,3),
(3,4),
(3,5),
(3,6),
(4,20),
(4,21),
(4,22),
(4,23),
(4,24),
(4,25),
(5,15),
(5,16),
(5,17),
(5,18),
(5,19);
--Insert for question
INSERT INTO entity_question VALUES
(DEFAULT,'src/main/resources/images/software_lifecycle.png',4,3, 1), -- software entwicklungs prozesse
(DEFAULT,'Bei welchen der unten angeführten Personen handelt es sich um Stakeholder?',1,1,1), -- sw entw. phasen
(DEFAULT,'src/main/resources/images/wasserfall_modell.png',4,1,2), -- sw. etw. prozesse
(DEFAULT,'Was ist die Philosophie des V-Modell XT?',2,3,2), -- sw. etw. prozesse
(DEFAULT,'Wie ist ein SCRUM Prozess aufgebaut?',2,1,3), -- sw. etw. prozesse
(DEFAULT,'Was sind die Vorteile einer File Persistence?',1,2,3), -- persistenz
(DEFAULT,'Für was steht die Abkürzung CRUD?',2,1, 2), -- persistenz
(DEFAULT,'Was ist die besondere Eigenschaft des Design Patterns SINGLETON?',2,2, 2), -- design patterns
(DEFAULT,'Welche Arten von Anforderungen kann es in der Software Entwicklungsphase geben? Geben Sie zu jeder Art eine kurze Erklärung! (Lösung: Auto, rot, hallo, Welt)',3,7, 2), -- sw. etw. phasen
(DEFAULT,'Wer definierte software design folgendermaßen: the process of defining the architecture, components, interfaces, and other characteristics of a system or component and the result of it?',2,2, 2), -- sw. etw. phasen
(DEFAULT,'Nennen Sie die wesentlichen 2 Komponenten von Entwurf und Design in der Software Entwicklungsphase! (Lösung: Flugzeug, Fahrrad, Geld, waschen)',3,3, 2), -- sw. entw. phasen
(DEFAULT,'Welche der unten angeführten Patterns gehören in die Kategorie BEHAVIORAL PATTERNS?',1,2,4), -- design patterns
(DEFAULT,'Welche Schicht der 3-Schichten-Architektur hat direkten Zugriff auf die Datenbank?',2,1, 2), -- persistenz
(DEFAULT,'src/main/resources/images/decorator_pattern.png',4,3, 2), -- design patterns
(DEFAULT,'Für was steht die Abkürzung SCM im Sinne des Software Engineerings?',2,2, 2), -- techn. u. werkz.
(DEFAULT,'Was ist Maven?',2,1,1), -- techn. u. werkz.
-- end SEPM
(DEFAULT,'src/main/resources/images/mikroprozessor.png',4,1, 2), -- speicherman. 1
(DEFAULT,'Sie haben die Tasks WASCHEN, TROCKNEN, FALTEN, EINRÄUMEN in dieser Reihenfolge zu erledigen, wenn Sie Ihre Wäsche waschen wollen. Wie sieht die entsprechende Pipeline aus, wenn Sie pro Arbeitsschritt keine 2 gleichen Tasks gleichzeitig durchführen können? Skizzieren Sie die Pipeline in einem Koordinatensystem und beschriften Sie die Achsen entsprechend! (Lösung: ich, weiß, es, nicht)',3,10, 2), -- pipe
(DEFAULT,'Welche Typen von Data-Hazards gibt es?',1,1,2), -- pipe
(DEFAULT,'Welche Aufgabe erfüllt die Arbitration-Einheit bei einem Bus?',2,2,1), -- speicherman.1
(DEFAULT,'Wie ist die richtige Reihenfolge des OSI Modells, wenn man mit der physikalischen Verbindung anfängt?',2,3, 2), -- netz
(DEFAULT,'Die Begriffe FTP, Telnet, SMTP und NFS gehören in welche Schicht des OSI Modells?',2,1,4), -- netz
(DEFAULT,'Wer hat den Algorithmus des Shortest Path Routing erfunden?',2,1,3), -- netz
(DEFAULT,'Welche von den unten angeführten Aktivitäten geschieht im Transport Layer des OSI Modells?',2,3, 2), -- netz
-- end TGI
(DEFAULT,'Welche der folgenden Aussagen sind korrekt?',1,4, 2), -- klassen
(DEFAULT,'src/main/resources/images/klassendiagramm_1.png',4,4,1), -- klassen
(DEFAULT,'src/main/resource/images/mietvertrag_ternaer.png',4,3,3), -- klassen
(DEFAULT,'Assoziationen ...',1,3,3), -- klassen
(DEFAULT,'Welche Sichtbarkeit können Attribute und Methoden in Klassen NICHT haben?',2,1, 2), -- klassen
(DEFAULT,'src/main/resources/images/mietvertrag_binaer.png',4,2, 2), -- klassen
(DEFAULT,'src/main/resources/images/starke_aggregation.png',4,4, 2), -- klassen
(DEFAULT,'Ein Klassendiagramm beschreibt ...',2,3,3), -- klassen
(DEFAULT,'Aufzählungstypen ...',1,3, 2), -- klassen
(DEFAULT,'Nennen Sie alle Sichtbarkeiten, die ein Attribut oder eine Methode in einem Klassendiagramm haben kann! (Lösung: public, private, protected)',3,1, 2), -- klassen
(DEFAULT,'src/main/resources/images/sequenzdiagramm_traces.png',4,5,1), -- seq
(DEFAULT,'src/main/resources/images/sequenzdiagrmm_traces_1.png',4,5,1), -- seq
(DEFAULT,'Aktive Objekte in einem Sequenzdiagramm ...',1,4,1), -- seq
(DEFAULT,'Welche speziellen Arten der Nachrichtenübermittlung gibt es im Sequenzdiagramm?',1,2, 2), -- seq
(DEFAULT,'Ein Sequenzdiagramm besteht aus zwei Darstellungsdimensionen. Diese sind ...',2,1, 2), -- seq
(DEFAULT,'Welche Arten der Ausführung einer Aktivität gibt es im Sequenzdiagramm?',1,2, 2), -- seq
(DEFAULT,'In Sequenzdiagrammen ...',2,2, 2), -- seq
(DEFAULT,'Bei der ODER-Verfeinerung von Subzuständen, ...',2,2,4), -- zustand
(DEFAULT,'src/main/resources/images/zustandsdiagramm_1.png',4,2,2), -- zustand
(DEFAULT,'In einem UML-Zustandsdiagramm können folgende Elemente modelliert werden:',1,1, 2), --zustand
(DEFAULT,'Es soll folgender Sachverhalt modelliert werden: Ein Lieferservice kann die zwei Zustände Warten und Auslieferung annehmen. Zunächst befindet es sich im Zustand Warten. Sobald ein Kunde eine Ware bestellt hat, erfolgt eine Transition in den Zustand Auslieferung. Im Zuge dieser Transition wird die Bestellung bearbeitet. Der Lieferservice befindet sich so lange im Zustand Auslieferung, bis das Produkt geliefert wurde, und geht anschließend wieder in den Zustand Warten, bis erneut eine Bestellung eintrifft. Wie muss die Transition vom Zustand Warten in den Zustand Auslieferung beschriftet sein?',2,6, 2), -- zustand
(DEFAULT,'src/main/resources/images/aktivitaetsdiagramm_1.png',4,4,1), -- aktiv
(DEFAULT,'src/main/resources/images/aktivitaetsdiagramm_2.png',4,4,1), -- aktiv
(DEFAULT,'Welche der folgenden Aussagen über das UML2-Aktivitätsdiagramm sind korrekt?',1, 3,3), -- aktiv
(DEFAULT,'Ein Entscheidungsknoten in einem UML2-Aktivitätsdiagramm ...',1,2, 2), -- aktiv
(DEFAULT,'Welche der folgenden Aussagen treffen auf das Konzept der Partitionen in einem UML2-Aktivitätsdiagramm zu?',1,4,3), -- aktiv
(DEFAULT,'Welches der unten angeführten Begriffe ist KEINE Art von Knoten eines UML2-Aktivitätsdiagramms?',2,1, 2), -- aktiv
(DEFAULT,'Ein Synchronisierungsknoten in einem UML2-Aktivitätsdiagramm ...',1,2, 2), -- aktiv
(DEFAULT,'Welches der folgenden ist KEIN Kontrollknoten in einem UML2-Aktivitätsdiagramm?',2, 2, 2), -- aktiv
(DEFAULT,'Welche der folgenden Aussagen über Erweiterungsstellen (extension points) sind korrekt?',1,3,1), -- anwendung
(DEFAULT,'src/main/resources/images/use_case_generalisierung.png',4,3, 2), -- anwendung
(DEFAULT,'Welche der folgenden Anwendungsfälle sind korrekte Anwendungsfälle beim Entwurf eines Anwendungsfalldiagramms für einen Online-Buchhandel?',1,1, 2), -- anwendung
(DEFAULT,'Welche der folgenden Aussagen charakterisieren Anwendungsfälle?',1,2,1), -- anwendung
(DEFAULT,'Akteure in einem Anwendungsfalldiagramm ...',1,4, 2), -- anwendung
-- end OOM
(DEFAULT,'src/main/resources/images/mosaikplot.png',4,1,3), -- desk. exp. stat.
(DEFAULT,'src/main/resources/images/tortendiagramm.png',4,1,4), -- desk. exp. stat.
(DEFAULT,'Bestimmen und zeichnen Sie für die folgenden Daten 40,52,55,60,70,75,85,85,90,90,92,94,94,95,98,100,115,125,125 (a) den Median und die Hinges, (b) auf Basis der Hinges die Fences, (c) den Boxplot. Gibt es Ausreißer? (Lösung: 42, lol, yolo, 12)',3,10,3), -- desk. exp. stat.
(DEFAULT,'Die Dichte einer sG X lautet wie folgt: f(x) = 2(1-x) für 0 < x < 1 sonst 0. (a) Bestimmen Sie den Erwartungswert von X, (b) Bestimmen Sie die Varianz von X, (c) Bestimmen Sie die Verteilungsfunktion von X (plus Skizze), (d) Wie kann man auf Basis von U~u(0,1) Beobachtungen von X generieren? (Lösung: sonne, mond, sterne, himmel)',3,15, 2), -- stoch. gr.
(DEFAULT,'Die Lebensdauer eines Gerätes sei exponentialverteilt mit Mittelwert 10.000 Stunden. Wenn das Gerät 8 Stunden pro Tag in Betrieb ist, mit welcher Wahrscheinlichkeit gibt es innerhalb von 5 Jahren keinen Ausfall? (1 Jahr = 365 Tage) (Lösung: bla, blub)',3,7,4), -- wahrsch.
-- end Statistik
(DEFAULT,'Man finde ein Bildungsgesetz für die unendlichen Folgen. Wie groß ist dann jeweils das zwölfte Folgenglied? 0,3;  0,09;  0,027; ... (Lösung: ich, bin, so, cool)',3,4,2), -- folg. reih. funk.
(DEFAULT,'Seien P1 und P2 beliebige Punkte der Zahlengeraden. Man halbiere fortgesetzt die Strecke P1P2 in P3, die Strecke P2P3 in P4, P3P4 in P5, usw. und bestimme die Lage von Pn für n gegen Unendlich!. (Lösung: unendlich, minus, eins, lol)',3,7,1), -- fol. reih. funk.
(DEFAULT,'Man berechne die Ableitungen von arcsin(x) und arccos(x) mit Hilfe der Regel für die Ableitung der Umkehrfunktion. (Lösung: eins, zwei, drei)',3,6,1), -- diff. u. diff. in. einer. var.
(DEFAULT,'Um was für eine Art von lineraren Differentialgleichungen erster Ordnung handelt es sich hierbei? y'' + a(x)y = 0',2,2,2); -- diff. u. diff. in einer var.
 -- end Analysis
--Insert for exam_question
INSERT INTO rel_exam_question VALUES
(1,1,FALSE,TRUE), -- sepm vo
(1,2,FALSE,TRUE),
(1,3,TRUE,TRUE),
(1,4,TRUE,TRUE),
(1,5,FALSE,FALSE),
(1,6,TRUE,TRUE),
(1,7,TRUE,TRUE),
(1,8,FALSE,FALSE),
(1,9,TRUE,TRUE),
(1,10,FALSE,FALSE),
(1,11,FALSE,TRUE),
(1,12,TRUE,TRUE),
(1,13,TRUE,TRUE),
(1,14,FALSE,TRUE),
(1,15,TRUE,TRUE),
(1,16,TRUE,TRUE),
(2,25,TRUE,TRUE), -- oom test 1
(2,26,TRUE,TRUE),
(2,27,TRUE,TRUE),
(2,28,TRUE,TRUE),
(2,29,TRUE,TRUE),
(2,30,TRUE,TRUE),
(2,31,TRUE,TRUE),
(2,32,TRUE,TRUE),
(2,33,TRUE,TRUE),
(2,34,TRUE,TRUE),
(3,35,FALSE,FALSE), -- oom test 2
(3,36,FALSE,FALSE),
(3,37,TRUE,FALSE),
(3,38,TRUE,FALSE),
(3,39,FALSE,FALSE),
(3,40,TRUE,TRUE),
(3,41,TRUE,TRUE),
(3,42,FALSE,FALSE),
(3,43,FALSE,FALSE),
(3,44,FALSE,FALSE),
(3,45,FALSE,FALSE),
(4,46,TRUE,TRUE), -- oom test 3
(4,47,TRUE,TRUE),
(4,48,TRUE,TRUE),
(4,49,TRUE,TRUE),
(4,50,TRUE,TRUE),
(4,51,TRUE,TRUE),
(4,52,TRUE,TRUE),
(4,53,TRUE,TRUE),
(4,54,TRUE,TRUE),
(4,55,TRUE,TRUE),
(4,56,TRUE,TRUE),
(4,57,TRUE,TRUE),
(4,58,TRUE,TRUE),
(5,64,TRUE,TRUE), -- analysis
(5,65,TRUE,TRUE),
(5,66,TRUE,TRUE),
(5,67,TRUE,TRUE),
(6,25,TRUE,TRUE), -- oom test 1
(6,26,TRUE,TRUE),
(6,27,TRUE,TRUE),
(6,28,TRUE,TRUE),
(6,29,TRUE,TRUE),
(6,30,TRUE,TRUE),
(6,31,TRUE,TRUE),
(6,32,TRUE,TRUE),
(6,33,TRUE,TRUE),
(6,34,TRUE,TRUE),
(7,1,FALSE,TRUE), -- sepm vo
(7,2,FALSE,TRUE),
(7,3,TRUE,TRUE),
(7,4,TRUE,TRUE),
(7,5,FALSE,FALSE),
(7,6,TRUE,TRUE),
(7,7,TRUE,TRUE),
(7,8,FALSE,FALSE),
(7,9,TRUE,TRUE),
(7,10,FALSE,FALSE),
(7,11,FALSE,TRUE),
(7,12,TRUE,TRUE),
(7,13,TRUE,TRUE),
(7,14,FALSE,TRUE),
(7,15,TRUE,TRUE),
(7,16,TRUE,TRUE),
(8,59,TRUE,TRUE), -- statistik
(8,60,FALSE,TRUE),
(8,61,FALSE,FALSE),
(8,62,TRUE,TRUE),
(8,63,TRUE,TRUE),
(9,17,TRUE,TRUE), -- tgi
(9,18,TRUE,TRUE),
(9,19,FALSE,TRUE),
(9,20,FALSE,FALSE),
(9,21,TRUE,TRUE),
(9,22,TRUE,TRUE),
(9,23,FALSE,FALSE),
(9,24,TRUE,TRUE);
--Insert for question_topic
INSERT INTO rel_question_topic VALUES
(1,14),
(2,12),
(3,14),
(4,14),
(5,14),
(6,11),
(7,11),
(8,10),
(9,12),
(10,12),
(11,12),
(12,10),
(13,11),
(14,10),
(15,13),
(16,13),
(17,9),
(18,7),
(19,7),
(20,9),
(21,8),
(22,8),
(23,8),
(24,8),
(25,1),
(26,1),
(27,1),
(28,1),
(29,1),
(30,1),
(31,1),
(32,1),
(33,1),
(34,1),
(35,6),
(36,6),
(37,6),
(38,6),
(39,6),
(40,6),
(41,6),
(42,5),
(43,5),
(44,5),
(45,5),
(46,2),
(47,2),
(48,2),
(49,2),
(50,2),
(51,2),
(52,2),
(53,2),
(54,4),
(55,4),
(56,4),
(57,4),
(58,4),
(59,25),
(60,25),
(61,25),
(62,20),
(63,24),
(64,15),
(65,15),
(66,17),
(67,17);
--Insert for answer
INSERT INTO entity_answer VALUES
(DEFAULT,4,'Software Spezifikation',TRUE,1),
(DEFAULT,4,'Designimplementierung',TRUE,1),
(DEFAULT,4,'Osterhase',FALSE,1),
(DEFAULT,4,'Software Evolution',TRUE,1),
(DEFAULT,1,'Kunden',TRUE,2),
(DEFAULT,1,'Anwender',TRUE,2),
(DEFAULT,1,'Entwickler',TRUE,2),
(DEFAULT,4,'Es handelt sich hierbei um ein Kochrezept.',FALSE,3),
(DEFAULT,4,'Die Abbildung demonstriert das Wasserfall-Modell',TRUE,3),
(DEFAULT,2,'Produkte stehen im Mittelpunkt (= Projektergebnisse), für jedes Produkt gibt es definierte Rollen mit definierten Verantwortlichkeiten.',TRUE,4),
(DEFAULT,2,'Ein Mensch hat 3 Augen.',FALSE,4),
(DEFAULT,2,'Ein Mensch hat 3 Augen.',FALSE,4),
(DEFAULT,2,'70 Minuten sind eine Stunde',FALSE,4),
(DEFAULT,2,'Pregame - Spring - Postgame',TRUE,5),
(DEFAULT,2,'Aufstehen - Zähneputzen - Duschen',FALSE,5),
(DEFAULT,2,'Essen - Trinken - Schlafen',FALSE,5),
(DEFAULT,2,'Spielen - Schlafen - Essen',FALSE,5),
(DEFAULT,1,'einfach zu implementieren',TRUE,6),
(DEFAULT,1,'weniger externe Abhängigkeiten',TRUE,6),
(DEFAULT,1,'erlaubt spezifische Optimierungen',TRUE,6),
(DEFAULT,1,'einfache Wartung',TRUE,6),
(DEFAULT,2,'create, read, update, delete',TRUE,7),
(DEFAULT,2,'cosmos, reden, unten, dumm',FALSE,7),
(DEFAULT,2,'cola, redbull, ungesund, dunkel',FALSE,7),
(DEFAULT,2,'Sorgt dafür, dass es nur eine Instanz einer Klasse gibt',TRUE,8),
(DEFAULT,2,'Ein Mensch hat 3 Augen',FALSE,8),
(DEFAULT,3,'Auto',TRUE,9),
(DEFAULT,3,'rot',TRUE,9),
(DEFAULT,3,'hallo',TRUE,9),
(DEFAULT,3,'Welt',TRUE,9),
(DEFAULT,2,'IEEE',TRUE,10),
(DEFAULT,2,'Herbert',FALSE,10),
(DEFAULT,2,'Anna',FALSE,10),
(DEFAULT,2,'Theodor',FALSE,10),
(DEFAULT,3,'Flugzeug',TRUE,11),
(DEFAULT,3,'Fahrrad',TRUE,11),
(DEFAULT,3,'Geld',TRUE,11),
(DEFAULT,3,'waschen',TRUE,11),
(DEFAULT,1,'Diese nicht',FALSE,12),
(DEFAULT,1,'Diese nicht',FALSE,12),
(DEFAULT,1,'Diese hier',TRUE,12),
(DEFAULT,2,'DAO',TRUE,13),
(DEFAULT,2,'SERVICE',FALSE,13),
(DEFAULT,2,'GUI',FALSE,13),
(DEFAULT,2,'MEIN HUND REX',FALSE,13),
(DEFAULT,4,'Richtig',TRUE,14),
(DEFAULT,4,'Falsch',FALSE,14),
(DEFAULT,2,'Sourcecode-Management System',TRUE,15),
(DEFAULT,2,'Schlafen Cornflakes Mantel',FALSE,15),
(DEFAULT,2,'Super Cool Mega',FALSE,15),
(DEFAULT,2,'XYZ',FALSE,15),
(DEFAULT,2,'Sockenmarke',FALSE,16),
(DEFAULT,2,'Build-Management System',TRUE,16),
(DEFAULT,2,'Automarke',FALSE,16),
(DEFAULT,2,'Jahreszeit',FALSE,16),
(DEFAULT,1,'Die Abbildung zeigt eine Skizze eines Mikroprozessors',TRUE,17),
(DEFAULT,1,'Diese Antwort ist auch richtig',TRUE,17),
(DEFAULT,1,'Diese Antwort ist falsch',FALSE,17),
(DEFAULT,1,'Falsche Antwort',FALSE,17),
(DEFAULT,3,'ich',TRUE,18),
(DEFAULT,3,'weiß',TRUE,18),
(DEFAULT,3,'es',TRUE,18),
(DEFAULT,3,'nicht',TRUE,18),
(DEFAULT,1,'Richtig',TRUE,19),
(DEFAULT,1,'Falsch',FALSE,19),
(DEFAULT,2,'Sie sorgt dafür, dass die Sonne täglich scheint. Diese Antwort ist richtig.',TRUE,20),
(DEFAULT,2,'Der Weihnachtsmann hat einen Sixpack',FALSE,20),
(DEFAULT,2,'1 - 2 - 3 - 4 - 5 - 6 - 7',TRUE,21),
(DEFAULT,2,'2 - 1 - 3 - 4 - 5 - 7 - 6',FALSE,21),
(DEFAULT,2,'In diese',TRUE,22),
(DEFAULT,2,'NICHT in diese',FALSE,22),
(DEFAULT,2,'NICHT in dieser',FALSE,22),
(DEFAULT,2,'NICHT in dieser',FALSE,22),
(DEFAULT,2,'Ich. Ich schwöre',TRUE,23),
(DEFAULT,2,'Niemand',FALSE,23),
(DEFAULT,2,'Keiner',FALSE,23),
(DEFAULT,2,'Ich bin eine richtige Antwort',TRUE,24),
(DEFAULT,2,'Das ist die falsche Antwort',FALSE,24),
(DEFAULT,2,'Ich bin falsch',FALSE,24),
(DEFAULT,1,'Diese',TRUE,25),
(DEFAULT,1,'Diese',TRUE,25),
(DEFAULT,4,'Das ist ein Klassendiagramm.',TRUE,26),
(DEFAULT,4,'Das ist ein Kochrezept',FALSE,26),
(DEFAULT,4,'Falsch',FALSE,26),
(DEFAULT,4,'Ein Klassendiagramm.',TRUE,26),
(DEFAULT,4,'Ternär',TRUE,27),
(DEFAULT,4,'Ternär ist richtig',TRUE,27),
(DEFAULT,4,'Ternär',TRUE,27),
(DEFAULT,4,'Ternär ist richtig',TRUE,27),
(DEFAULT,1,'Richtig',TRUE,28),
(DEFAULT,1,'Richtig',TRUE,28),
(DEFAULT,1,'Das stimmt nicht',FALSE,28),
(DEFAULT,2,'transparent',TRUE,29),
(DEFAULT,2,'protected',FALSE,29),
(DEFAULT,4,'Richtig',TRUE,30),
(DEFAULT,4,'Richtig',TRUE,30),
(DEFAULT,4,'Richtig',TRUE,30),
(DEFAULT,4,'Richtig',TRUE,30),
(DEFAULT,4,'Wähle mich NICHT aus',FALSE,31),
(DEFAULT,4,'Wähle mich aus',TRUE,31),
(DEFAULT,4,'Ich bin eine richtige Antwort',TRUE,31),
(DEFAULT,4,'Richtig',TRUE,31),
(DEFAULT,2,'Vieles. Diese Antwort ist richtig',TRUE,32),
(DEFAULT,2,'Falsch',FALSE,32),
(DEFAULT,2,'Falsch',FALSE,32),
(DEFAULT,1,'Zählen Typen auf (Richtig)',TRUE,33),
(DEFAULT,1,'Richtig',TRUE,33),
(DEFAULT,1,'Falsch',FALSE,33),
(DEFAULT,1,'Falsch',FALSE,33),
(DEFAULT,3,'public',TRUE,34),
(DEFAULT,3,'private',TRUE,34),
(DEFAULT,3,'protected',TRUE,34),
(DEFAULT,4,'Ich bin richtig',TRUE,35),
(DEFAULT,4,'Ich bin falsch',FALSE,35),
(DEFAULT,4,'Cooles Bild (richtig)',TRUE,36),
(DEFAULT,4,'Schlecht (falsch)',FALSE,36),
(DEFAULT,4,'Guuuuut! (richtig)',TRUE,36),
(DEFAULT,1,'Alle Antworten sind richtig',TRUE,37),
(DEFAULT,1,'Hallo',TRUE,37),
(DEFAULT,1,'Hohoho',TRUE,37),
(DEFAULT,1,'Nice',TRUE,37),
(DEFAULT,1,'Nur diese Antwort ist richtig',TRUE,38),
(DEFAULT,1,'Fischer Fritz fischt frische Fische',FALSE,38),
(DEFAULT,2,'Ich bin (nicht) richtig.',FALSE,39),
(DEFAULT,2,'Ich bin die Antwort auf ALLES',TRUE,39),
(DEFAULT,2,'Hmm?!',FALSE,39),
(DEFAULT,2,'Nein',FALSE,39),
(DEFAULT,1,'SEPM (richtig)',TRUE,40),
(DEFAULT,1,'IST (richtig)',TRUE,40),
(DEFAULT,1,'SOO (richtig)',TRUE,40),
(DEFAULT,1,'COOL (richtig)',TRUE,40),
(DEFAULT,2,'I am the best, f*** the rest',TRUE,41),
(DEFAULT,2,'…',FALSE,41),
(DEFAULT,2,'-.-',FALSE,41),
(DEFAULT,2,',,,',FALSE,41),
(DEFAULT,2,'Richtig',TRUE,42),
(DEFAULT,2,'False',FALSE,42),
(DEFAULT,2,'False',FALSE,42),
(DEFAULT,4,'Was soll das? (richtig)',TRUE,43),
(DEFAULT,4,'Nicht schlecht!',FALSE,43),
(DEFAULT,4,'Schlecht!',FALSE,43),
(DEFAULT,4,'Hallo! (richtig)',TRUE,43),
(DEFAULT,1,'Ich bin eine sehr lange Antwort, die nur dazu dient, um zu schauen, wie das Programm mit sehr langen Antworten umgeht! By the way: Ich bin eine richtige Antwort',TRUE,44),
(DEFAULT,1,'Ich bin kurz und richtig',TRUE,44),
(DEFAULT,2,'Hallo (richtig)',TRUE,45),
(DEFAULT,2,'Tschüss',FALSE,45),
(DEFAULT,2,'Bye',FALSE,45),
(DEFAULT,2,'Bye Bye',FALSE,45),
(DEFAULT,4,'Richtig',TRUE,46),
(DEFAULT,4,'RICHTIG',TRUE,46),
(DEFAULT,4,'RICHTIG',TRUE,46),
(DEFAULT,4,'RICHTIG',TRUE,46),
(DEFAULT,4,'Ein Mensch hat 3 Augen',FALSE,47),
(DEFAULT,4,'Ein Mensch hat 2 Augen (richtig)',TRUE,47),
(DEFAULT,4,'Ich bin falsch',FALSE,47),
(DEFAULT,4,'Ich bin falsch',FALSE,47),
(DEFAULT,1,'Nein.',FALSE,48),
(DEFAULT,1,'Nein.',FALSE,48),
(DEFAULT,1,'Ja.',TRUE,48),
(DEFAULT,1,'Ja.',TRUE,49),
(DEFAULT,1,'Ja.',TRUE,49),
(DEFAULT,1,'Ja.',TRUE,49),
(DEFAULT,1,'Ja.',TRUE,49),
(DEFAULT,1,'test123 ist nicht die Lösung (richtig)',TRUE,50),
(DEFAULT,1,'Ich bin die Lösung (richtig)',TRUE,50),
(DEFAULT,1,'Ich bin eine falsche Antwort.',FALSE,50),
(DEFAULT,2,'Markiere mich als richtig, denn ich bin richtig!',TRUE,51),
(DEFAULT,2,'False',FALSE,51),
(DEFAULT,2,'FAAAALSCH',FALSE,51),
(DEFAULT,1,'Ich bin die Antwort auf alle Fragen!',TRUE,52),
(DEFAULT,1,'Ich bin immer die richtige Antwort!',TRUE,52),
(DEFAULT,1,'Ich bin immer FALSCH',FALSE,52),
(DEFAULT,2,'Ich bin die Wahrheit!',TRUE,53),
(DEFAULT,2,'Falsch',FALSE,53),
(DEFAULT,2,'Falsch',FALSE,53),
(DEFAULT,1,'Wenn du mich ausgewählt hast, dann wähle mich sofort ab, denn ich bin falsch!',FALSE,54),
(DEFAULT,1,'Ich bin richtig :)',TRUE,54),
(DEFAULT,1,'TRUE BABY!',TRUE,54),
(DEFAULT,1,'Never gonna give you up! (Das ist eine Lüge, also FALSCH)',FALSE,54),
(DEFAULT,4,'Richtiiiisch!',TRUE,55),
(DEFAULT,4,'Richtig',TRUE,55),
(DEFAULT,4,'Falsch',FALSE,55),
(DEFAULT,4,'Komplett falsch',FALSE,55),
(DEFAULT,1,'Vielleicht richtig, vielleicht auch nicht (richtig)',TRUE,56),
(DEFAULT,1,'Right',TRUE,56),
(DEFAULT,1,'Richtig',TRUE,56),
(DEFAULT,1,'TRUE..',TRUE,56),
(DEFAULT,1,'Ich bin eine richtige Antwort.',TRUE,57),
(DEFAULT,1,'Ich bin falsch',FALSE,57),
(DEFAULT,1,'Richtig..',TRUE,57),
(DEFAULT,1,'NICHT richtig!',FALSE,57),
(DEFAULT,1,'Falsche Antwort',FALSE,58),
(DEFAULT,1,'Richtige Antwort',TRUE,58),
(DEFAULT,1,'Richtige Antwort',TRUE,58),
(DEFAULT,1,'Falsch.',FALSE,58),
(DEFAULT,4,'Faaalsch falsch!',FALSE,59),
(DEFAULT,4,'Richtig!',TRUE,59),
(DEFAULT,4,'Richtig',TRUE,59),
(DEFAULT,4,':) (richtig)',TRUE,59),
(DEFAULT,4,'Ja. Richtig',TRUE,60),
(DEFAULT,4,'Richtig…',TRUE,60),
(DEFAULT,3,'42',TRUE,61),
(DEFAULT,3,'lol',TRUE,61),
(DEFAULT,3,'yolo',TRUE,61),
(DEFAULT,3,'12',TRUE,61),
(DEFAULT,3,'sonne',TRUE,62),
(DEFAULT,3,'mond',TRUE,62),
(DEFAULT,3,'sterne',TRUE,62),
(DEFAULT,3,'himmel',TRUE,62),
(DEFAULT,3,'bla',TRUE,63),
(DEFAULT,3,'blub',TRUE,63),
(DEFAULT,3,'ich',TRUE,64),
(DEFAULT,3,'bin',TRUE,64),
(DEFAULT,3,'so',TRUE,64),
(DEFAULT,3,'cool',TRUE,64),
(DEFAULT,3,'unendlich',TRUE,65),
(DEFAULT,3,'minus',TRUE,65),
(DEFAULT,3,'eins',TRUE,65),
(DEFAULT,3,'lol',TRUE,65),
(DEFAULT,3,'eins',TRUE,66),
(DEFAULT,3,'zwei',TRUE,66),
(DEFAULT,3,'drei',TRUE,66),
(DEFAULT,2,'homogen (richtig)',TRUE,67),
(DEFAULT,2,'inhomogen (falsch)',FALSE,67),
(DEFAULT,2,'symetrisch (falsch)',FALSE,67);
--Insert for resource
INSERT INTO entity_resource VALUES
(DEFAULT,1,'OOM: Klassendiagramm','src/main/resources/resources/02_Klassendiagram_FolienMitText.pdf'),
(DEFAULT,1,'OOM: Sequenzdiagramm','src/main/resources/resources/03_Sequenzdiagramm_FolienMitText.pdf'),
(DEFAULT,1,'OOM: Zustandsdiagramm','src/main/resources/resources/04_FolienZustandsdiagramm_mitText.pdf'),
(DEFAULT,1,'OOM: Aktivitätsdiagramm','src/main/resources/resources/05_FolienAktivitaetsdiagramm_mitText.pdf'),
(DEFAULT,1,'OOM: Anwendungsfalldiagramm','src/main/resources/resources/06_FolienAnwendungsfalldiagramm_mitText.pdf'),
(DEFAULT,1,'TGI: Pipelining','src/main/resources/resources/12_Pipelining.pdf'),
(DEFAULT,1,'TGI: Speichermanagement 1','src/main/resources/resources/13_Speichermanagement1.pdf'),
(DEFAULT,1,'TGI: Speichermanagement 2','src/main/resources/resources/14_Speichermanagement2.pdf'),
(DEFAULT,1,'TGI: Netzwerke','src/main/resources/resources/16_Netzwerke.pdf'),
(DEFAULT,1,'SEPM: SE Phasen','src/main/resources/resources/Block 03 - SE Phasen.pdf'),
(DEFAULT,1,'SEPM: SE Prozesse','src/main/resources/resources/Block 05 - SE Prozesse.pdf'),
(DEFAULT,1,'SEPM: Persistenz','src/main/resources/resources/Block 09 - Persistenz.pdf'),
(DEFAULT,1,'SEPM: Design Patterns','src/main/resources/resources/Block 10 - Design Patterns.pdf'),
(DEFAULT,1,'SEPM: Technik und Werkzeuge','src/main/resources/resources/Block_3_Technik und Werkzeuge.pdf'),
(DEFAULT,4,'TGI Test Beispiel (arbitration)','src/main/resources/resources/arbitration_einheit.gif');
--Insert for resource_question
INSERT INTO rel_resource_question VALUES
(1,25),
(1,26),
(1,27),
(1,28),
(1,29),
(1,30),
(1,31),
(1,32),
(1,33),
(1,34),
(2,35),
(2,36),
(2,37),
(2,38),
(2,39),
(2,40),
(2,41),
(3,42),
(3,43),
(3,44),
(3,45),
(4,46),
(4,47),
(4,48),
(4,49),
(4,50),
(4,51),
(4,52),
(4,53),
(5,54),
(5,55),
(5,56),
(5,57),
(5,58),
(6,18),
(6,19),
(7,17),
(9,21),
(9,22),
(9,23),
(9,24),
(10,2),
(10,9),
(10,10),
(10,11),
(11,1),
(11,3),
(11,4),
(11,5),
(15,20);
