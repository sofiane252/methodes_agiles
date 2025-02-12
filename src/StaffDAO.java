import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données contenues dans la table staff
 * @version 1.0
 */
public class StaffDAO {

    /**
     * Paramètres de connexion à la base de données MySQL
     */
    final static String URL = "jdbc:mysql://127.0.0.1:3306/foot?serverTimezone=UTC";
    final static String LOGIN = "root";
    final static String PASS = "root";

    /**
     * Constructeur : Charge le driver JDBC
     */
    public StaffDAO() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Erreur de chargement du driver JDBC.");
            e.printStackTrace();
        }
    }

    /**
     * Ajoute un membre du staff dans la base de données
     * @param staff Le membre du staff à ajouter
     * @return Nombre de lignes ajoutées
     */
    public int ajouter(Staff staff) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("INSERT INTO staff (nom, prenom, numeroTelephone, salaire, role) VALUES (?, ?, ?, ?, ?)");
            ps.setString(1, staff.getNom());
            ps.setString(2, staff.getPrenom());
            ps.setString(3, staff.getNumeroTelephone());
            ps.setString(4, staff.getSalaire());
            ps.setString(5, staff.getRole());

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
     * Modifie les informations d’un membre du staff
     * @param staff L'objet Staff contenant les nouvelles valeurs
     * @return Nombre de lignes modifiées
     */
    public int modifier(Staff staff) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("UPDATE staff SET nom = ?, prenom = ?, numeroTelephone = ?, salaire = ?, role = ? WHERE id = ?");
            ps.setString(1, staff.getNom());
            ps.setString(2, staff.getPrenom());
            ps.setString(3, staff.getNumeroTelephone());
            ps.setString(4, staff.getSalaire());
            ps.setString(5, staff.getRole());
            ps.setInt(6, staff.getId());

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
     * Supprime un membre du staff en fonction de son ID
     * @param id L'ID du membre à supprimer
     * @return Nombre de lignes supprimées
     */
    public int supprimer(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        int retour = 0;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("DELETE FROM staff WHERE id = ?");
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
     * Récupère un membre du staff en fonction de son ID
     * @param id L'ID du membre du staff
     * @return Un objet Staff ou null si non trouvé
     */
    public Staff getStaff(int id) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Staff retour = null;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM staff WHERE id = ?");
            ps.setInt(1, id);

            rs = ps.executeQuery();
            if (rs.next()) {
                retour = new Staff(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                		rs.getString("numeroTelephone"),
                		rs.getString("salaire"),
                		rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return retour;
    }
        /**
     * Récupère un membre du staff en fonction de son nom
     * @param nom Nom du membre du staff
     * @return Un objet Staff ou null si non trouvé
     */
    public Staff getStaffByName(String nom) {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Staff retour = null;

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM staff WHERE nom = ?");
            ps.setString(1, nom);

            rs = ps.executeQuery();
            if (rs.next()) {
                retour = new Staff(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                		rs.getString("numeroTelephone"),
                		rs.getString("salaire"),
                		rs.getString("role"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }
        return retour;
    }

    /**
     * Récupère la liste complète du staff
     * @return Liste de tous les membres du staff
     */
    public List<Staff> getListeStaff() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Staff> retour = new ArrayList<>();

        try {
            con = DriverManager.getConnection(URL, LOGIN, PASS);
            ps = con.prepareStatement("SELECT * FROM staff");

            rs = ps.executeQuery();
            while (rs.next()) {
                retour.add(new Staff(rs.getInt("id"), rs.getString("nom"), rs.getString("prenom"),
                		rs.getString("numeroTelephone"),
                		rs.getString("salaire"),
                		rs.getString("role")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
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
			ps = con.prepareStatement("INSERT INTO staff (nom, prenom, numeroTelephone, salaire, role) VALUES\r\n"
					+ "(\"Ancelotti\", \"Carlo\", \"0303030303\", \"30000\", \"entraineur\"),\r\n"
					+ "(\"Ancelotti\", \"Davide\", \"0303030303\", \"20000\", \"entraineur\"),\r\n"
					+ "(\"Llopis\", \"Luis\", \"0303030303\", \"12000\", \"entraineur\"),\r\n"
					+ "(\"Mallo\", \"Javier\", \"0303030303\", \"5000\", \"preparateur\"),\r\n"
					+ "(\"Antonio\", \"Pintus\", \"0303030303\", \"5000\", \"preparateur\"),\r\n"
					+ "(\"Sebastien\", \"Devillaz\", \"0303030303\", \"5000\", \"preparateur\"),\r\n"
					+ "(\"Mihic\", \"Niko\", \"0303030303\", \"15000\", \"medecin\");");
			
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
    

    /**
     * Test des méthodes de StaffDAO
     */
    public static void main(String[] args) {
        StaffDAO staffDAO = new StaffDAO();
        
        /* Insertion des données */
//        int retour = staffDAO.initSQL();
//        System.out.println(retour + " lignes ajoutées");

        // Ajouter un membre du staff
       /* Staff s1 = new Staff("Zidane", "Zinedine", "0404040404", "2500", "Entraîneur");
        int ajout = staffDAO.ajouter(s1);
        System.out.println(ajout + " ligne(s) ajoutée(s)");

        // Modifier un membre du staff
        Staff s2 = new Staff(1, "Henry", "Thierry", "0303030303", "15000", "Adjoint");
        int modif = staffDAO.modifier(s2);
        System.out.println(modif + " ligne(s) modifiée(s)");

        // Récupérer un membre du staff
        Staff s3 = staffDAO.getStaff(1);
        System.out.println(s3);

        // Supprimer un membre du staff
        int suppression = staffDAO.supprimer(2);
        System.out.println(suppression + " ligne(s) supprimée(s)");

        // Récupérer tous les membres du staff
        List<Staff> liste = staffDAO.getListeStaff();
        for (Staff staff : liste) {
            System.out.println(staff);
        }*/
    }
}
