import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JoueurDAO {

    // Paramètres de connexion
    final static String URL = "jdbc:mysql://127.0.0.1:3306/foot?serverTimezone=UTC";
    final static String LOGIN = "root";
    final static String PASS = "root";

    // Constructeur
    public JoueurDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver MySQL.");
        }
    }

    /**
     * Ajoute un joueur dans la base
     */
    public int ajouter(Joueur joueur) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("INSERT INTO joueur (nom, prenom, poste) VALUES (?, ?, ?)");
            ps.setString(1, joueur.getNom());
            ps.setString(2, joueur.getPrenom());
            ps.setString(3, joueur.getPoste());
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
     * Modifie un joueur dans la base
     */
    public int modifier(Joueur joueur) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("UPDATE joueur SET nom = ?, prenom = ?, poste = ? WHERE id = ?");
            ps.setString(1, joueur.getNom());
            ps.setString(2, joueur.getPrenom());
            ps.setString(3, joueur.getPoste());
            ps.setInt(4, joueur.getId());
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
     * Supprime un joueur par ID
     */
    public int supprimer(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("DELETE FROM joueur WHERE id = ?");
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

    /**
     * Récupère un joueur par son ID
     */
    public Joueur getJoueurById(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Joueur joueur = null;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM joueur WHERE id = ?");
            ps.setInt(1, id);
            rs = ps.executeQuery();
            if (rs.next()) {
                joueur = new Joueur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("poste"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return joueur;
    }

    /**
     * Récupère un joueur par son nom
     */
    public Joueur getJoueurByName(String nom) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Joueur joueur = null;
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM joueur WHERE nom = ?");
            ps.setString(1, nom);
            rs = ps.executeQuery();
            if (rs.next()) {
                joueur = new Joueur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("poste"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return joueur;
    }

    /**
     * Récupère la liste de tous les joueurs
     */
    public List<Joueur> getListeJoueurs() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Joueur> listeJoueurs = new ArrayList<>();
        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM joueur");
            rs = ps.executeQuery();
            while (rs.next()) {
                listeJoueurs.add(new Joueur(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"), rs.getString("poste")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return listeJoueurs;
    }
    
    public int initSQL() {
		Connection con = null;
		PreparedStatement ps = null;
		int retour = 0;

		//connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS); // tentative de connexion
			ps = con.prepareStatement("INSERT INTO joueur (nom, prenom, poste) VALUES\r\n"
					+ "(\"Courtois\", \"Thibaut\", \"gardien\"),\r\n"
					+ "(\"Carvajal\", \"Dani\", \"defenseur\"),\r\n"
					+ "(\"Militao\", \"Eder\", \"defenseur\"),\r\n"
					+ "(\"Bellingham\", \"Jude\", \"milieu\"),\r\n"
					+ "(\"Guler\", \"Arda\", \"mileu\"),\r\n"
					+ "(\"Mbappe\", \"Kylian\", \"attaquant\"),\r\n"
					+ "(\"Vinicius\", \"Junio\", \"attaquant\");");

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

    // TESTS
    public static void main(String[] args) {
        JoueurDAO joueurDAO = new JoueurDAO();
        
        /* Insertion des données */
//        int retour = joueurDAO.initSQL();
//        System.out.println(retour + " lignes ajoutées");

        // Test ajout
        /*Joueur joueur1 = new Joueur("Mbappé", "Kylian", "Attaquant");
        int ajout = joueurDAO.ajouter(joueur1);
        System.out.println(ajout + " joueur ajouté.");

        // Test récupération
        Joueur joueur2 = joueurDAO.getJoueurByName("Mbappé");
        System.out.println("Joueur trouvé : " + joueur2);

        // Test modification
        if (joueur2 != null) {
            joueur2.setPoste("Milieu");
            joueurDAO.modifier(joueur2);
            System.out.println("Joueur modifié : " + joueurDAO.getJoueurById(joueur2.getId()));
        }

        // Test suppression
        if (joueur2 != null) {
            joueurDAO.supprimer(joueur2.getId());
            System.out.println("Joueur supprimé.");
        }

        // Test liste des joueurs
        List<Joueur> joueurs = joueurDAO.getListeJoueurs();
        System.out.println("Liste des joueurs : " + joueurs);*/
    }
}
