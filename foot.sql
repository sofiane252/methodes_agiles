CREATE DATABASE IF NOT EXISTS foot;
USE foot;

--
-- Table structure for table staff
--

DROP TABLE IF EXISTS staff;
CREATE TABLE staff (
  id INT(10) NOT NULL AUTO_INCREMENT,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  numeroTelephone VARCHAR(100) NOT NULL,
  salaire VARCHAR(100) NOT NULL,
  role VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table joueur
--

DROP TABLE IF EXISTS joueur;
CREATE TABLE joueur (
  id INT(10) NOT NULL AUTO_INCREMENT,
  nom VARCHAR(100) NOT NULL,
  prenom VARCHAR(100) NOT NULL,
  poste VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Table structure for table match
--

DROP TABLE IF EXISTS matchs;
CREATE TABLE matchs (
  id INT(10) NOT NULL AUTO_INCREMENT,
  date DATE NOT NULL,
  adversaire VARCHAR(100) NOT NULL,
  competition VARCHAR(100) NOT NULL,
  stade VARCHAR(100) NOT NULL,
  PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


--
-- Datas for table staff
--

INSERT INTO staff (nom, prenom, numeroTelephone, salaire, role) VALUES
("Ancelotti", "Carlo", "0303030303", "30000", "entraineur"),
("Ancelotti", "Davide", "0303030303", "20000", "entraineur"),
("Llopis", "Luis", "0303030303", "12000", "entraineur"),
("Mallo", "Javier", "0303030303", "5000", "preparateur"),
("Antonio", "Pintus", "0303030303", "5000", "preparateur"),
("Sebastien", "Devillaz", "0303030303", "5000", "preparateur"),
("Mihic", "Niko", "0303030303", "15000", "medecin");


--
-- Datas for table joueur
--

INSERT INTO joueur (nom, prenom, poste) VALUES
("Courtois", "Thibaut", "gardien"),
("Carvajal", "Dani", "defenseur"),
("Militao", "Eder", "defenseur"),
("Bellingham", "Jude", "milieu"),
("Guler", "Arda", "mileu"),
("Mbappe", "Kylian", "attaquant"),
("Vinicius", "Junio", "attaquant");


--
-- Datas for table match
--

INSERT INTO matchs (date, adversaire, competition, stade) VALUES
("2025-02-18", "Barcelone", "LDC", "Santiago Bernabeu"),
("2025-02-24", "Seville", "Liga", "Santiago Bernabeu"),
("2025-02-28", "Osasuna", "Liga", "Santiago Bernabeu");







