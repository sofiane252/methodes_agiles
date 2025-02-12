import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données contenues dans la table match
 * @version 1.1
 * */
public class MatchDAO {

	/**
	 * Paramètres de connexion à la base de données mysql
	 * URL, LOGIN et PASS sont des constantes
	 */
	final static String URL = "jdbc:mysql://127.0.0.1:3306/foot?serverTimezone=UTC";
	final static String LOGIN = "root";
	final static String PASS = "root";


	/**
	 * Constructeur de la classe
	 * 
	 */
	public MatchDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Chargement du pilote de bases de données
		} catch (ClassNotFoundException e2) {
			System.err.println("Impossible de charger le pilote de BDD, ne pas oublier d'importer le fichier .jar dans le projet");
		}
	}
	

	/**
	 * Permet d'ajouter un match dans la table match
	 * la référence de l'article est produite automatiquement par la base de données en utilisant une séquence
	 * Le mode est auto-commit par défaut : chaque insertion est validée
	 * @param newMatch le match à ajouter
	 * @return le nombre de ligne ajoutées dans la table
	 */
	public int ajouter(Match newMatch) {
		Connection con = null;
		PreparedStatement ps = null;
		int retour = 0;

		//connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS); // tentative de connexion
			
			// préparation de l'instruction SQL, chaque ? représente une valeur à communiquer dans l'insertion
			// les getters permettent de récupérer les valeurs des attributs souhaités de newMatch
			ps = con.prepareStatement("INSERT INTO matchs (date, adversaire, competition, stade) VALUES (?, ?, ?, ?)");
			
			ps.setDate(1, java.sql.Date.valueOf(newMatch.getDate()));
			ps.setString(2, newMatch.getAdversaire());
			ps.setString(3, newMatch.getCompetition());
			ps.setString(4, newMatch.getStade());

			// Exécution de la requête
			retour = ps.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// fermeture du preparedStatement et de la connexion
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;
	}

	/**
	 * Permet de récupérer un match à partir de son id
	 * @param id l'id du match à récupérer
	 * @return le match
	 * @return null si aucun match ne correspond à ce match
	 */
	public Match getMatch(int id) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Match retour = null;

		// connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM matchs WHERE id = ?");
			ps.setInt(1, id);

			// on exécute la requête
			// rs contient un pointeur situé jusute avant la première ligne retournée
			rs = ps.executeQuery();
			// passe à la première (et unique) ligne retournée 
			if (rs.next()) {
				Date date = rs.getDate("date");
				retour = new Match(date.toLocalDate(), rs.getString("adversaire"), rs.getString("competition"), rs.getString("stade"));
			}
				
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//fermeture du ResultSet, du PreparedStatement et de la Connection
			try {if (rs != null)rs.close();} catch (Exception t) {}
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;
	}

	/**
	 * Permet de récupérer tous les matchs stockés dans la table match
	 * @return une ArrayList de Match
	 */
	public List<Match> getListeMatchs() {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Match> retour = new ArrayList<Match>();

		// connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM matchs");
			rs = ps.executeQuery(); // on exécute la requête
			//on parcourt les lignes du résultat
			while (rs.next())
				retour.add(new Match(rs.getInt("id"), rs.getDate("date").toLocalDate(), rs.getString("adversaire"), rs.getString("competition"), rs.getString("stade")));

		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			//fermeture du rs, du preparedStatement et de la connexion
			try {if (rs != null)rs.close();} catch (Exception t) {}
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;
	}
	
    /**
     * Modifie un match dans la base
     */
    public int modifier(Match match) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("UPDATE matchs SET adversaire = ?, competition = ?, stade = ?, date = ? WHERE id = ?");
            ps.setString(1, match.getAdversaire());
            ps.setString(2, match.getCompetition());
            ps.setString(3, match.getStade());
            ps.setDate(4, java.sql.Date.valueOf(match.getDate()));
            ps.setInt(5, match.getId());
            retour = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return retour;
    }
	
    /**
     * Supprime un match par ID
     */
    public int supprimer(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("DELETE FROM matchs WHERE id = ?");
            ps.setInt(1, id);
            retour = ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return retour;
    }
	
	public int initSQL() {
		Connection con = null;
		PreparedStatement ps = null;
		int retour = 0;

		//connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS); // tentative de connexion
			
			ps = con.prepareStatement("INSERT INTO matchs (date, adversaire, competition, stade) VALUES\r\n"
					+ "(\"2025-02-18\", \"Barcelone\", \"LDC\", \"Santiago Bernabeu\"),\r\n"
					+ "(\"2025-02-24\", \"Seville\", \"Liga\", \"Santiago Bernabeu\"),\r\n"
					+ "(\"2025-02-28\", \"Osasuna\", \"Liga\", \"Santiago Bernabeu\");");
			
			retour = ps.executeUpdate(); // Exécution de la requête
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// fermeture du preparedStatement et de la connexion
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;
	}

	// main permettant de tester la classe
	public static void main(String[] args) throws SQLException {
		MatchDAO matchDAO = new MatchDAO();
		
		/* Insertion des données */
//		int retour = matchDAO.initSQL();
//		System.out.println(retour + " lignes ajoutées");

		// test de la méthode ajouter
		//LocalDate date = LocalDate.of(2025, 02, 15);
		//Match m = new Match(date, "Villareal", "SuperCoupe", "Santiago Bernabeu");
		//int retour = matchDAO.ajouter(m);
		

		// test de la méthode getMatch
		//Match m2 = matchDAO.getMatch(1);
		//System.out.println(m2);

		// test de la méthode getListeMatchs
		/*List<Match> liste = matchDAO.getListeMatchs();
//		System.out.println(liste);
		for(Match ma : liste) {
			System.out.println(ma.toString());
		}*/

	}
}
