import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe d'accès aux données contenues dans la table article
 * @version 1.1
 * */
public class ArticleDAO {

	/**
	 * Paramètres de connexion à la base de données oracle
	 * URL, LOGIN et PASS sont des constantes
	 */
	final static String URL = "jdbc:mysql://127.0.0.1:3306/stocks?serverTimezone=UTC";
	final static String LOGIN = "root";
	final static String PASS = "root";


	/**
	 * Constructeur de la classe
	 * 
	 */
	public ArticleDAO() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver"); // Chargement du pilote de bases de données
		} catch (ClassNotFoundException e2) {
			System.err.println("Impossible de charger le pilote de BDD, ne pas oublier d'importer le fichier .jar dans le projet");
		}
	}
	

	/**
	 * Permet d'ajouter un article dans la table article
	 * la référence de l'article est produite automatiquement par la base de données en utilisant une séquence
	 * Le mode est auto-commit par défaut : chaque insertion est validée
	 * @param nouvArticle l'article à ajouter
	 * @return le nombre de ligne ajoutées dans la table
	 */
	public int ajouter(Article nouvArticle) {
		Connection con = null;
		PreparedStatement ps = null;
		int retour=0;

		//connexion à la base de données
		try {
			con = DriverManager.getConnection(URL, LOGIN, PASS); // tentative de connexion
			
			//préparation de l'instruction SQL, chaque ? représente une valeur à communiquer dans l'insertion
			//les getters permettent de récupérer les valeurs des attributs souhaités de nouvArticle
			ps = con.prepareStatement("INSERT INTO article (designation, pu_ht, qtestock) VALUES (?, ?, ?)");
			
			ps.setString(1, nouvArticle.getDesignation());
			ps.setDouble(2, nouvArticle.getPuHt());
			ps.setInt(3, nouvArticle.getQteStock());

			//Ex�cution de la requ�te
			retour = ps.executeUpdate();


		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			//fermeture du preparedStatement et de la connexion
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;

	}

	/**
	 * Permet de récupérer un article à partir de sa référence
	 * @param reference la r�f�rence de l'article � r�cup�rer
	 * @return l'article
	 * @return null si aucun article ne correspond � cette r�f�rence
	 */
	public Article getArticle(int reference) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Article retour = null;

		//connexion � la base de donn�es
		try {

			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM article WHERE reference = ?");
			ps.setInt(1,reference);

			//on ex�cute la requ�te
			//rs contient un pointeur situ� jusute avant la premi�re ligne retourn�e
			rs=ps.executeQuery();
			//passe � la premi�re (et unique) ligne retourn�e 
			if(rs.next())
				retour=new Article(rs.getInt("reference"),rs.getString("designation"),rs.getDouble("pu_ht"),rs.getInt("qtestock"));


		} catch (Exception ee) {
			ee.printStackTrace();
		} finally {
			//fermeture du ResultSet, du PreparedStatement et de la Connection
			try {if (rs != null)rs.close();} catch (Exception t) {}
			try {if (ps != null)ps.close();} catch (Exception t) {}
			try {if (con != null)con.close();} catch (Exception t) {}
		}
		return retour;

	}

	/**
	 * Permet de r�cup�rer tous les articles stock�s dans la table article
	 * @return une ArrayList d'Articles
	 */
	public List<Article> getListeArticles()
	{

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs=null;
		List<Article> retour=new ArrayList<Article>();

		//connexion � la base de donn�es
		try {

			con = DriverManager.getConnection(URL, LOGIN, PASS);
			ps = con.prepareStatement("SELECT * FROM article");

			//on ex�cute la requ�te
			rs=ps.executeQuery();
			//on parcourt les lignes du r�sultat
			while(rs.next())
				retour.add(new Article(rs.getInt("reference"),rs.getString("designation"),rs.getDouble("pu_ht"),rs.getInt("qtestock")));


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

	//main permettant de tester la classe
	public static void main(String[] args)  throws SQLException {

		 ArticleDAO articleDAO=new ArticleDAO();
		//test de la méthode ajouter
		Article a = new Article("Set de 2 raquettes de ping-pong", 149.9, 10);
		int retour=articleDAO.ajouter(a);

		System.out.println(retour+ " lignes ajoutées");

		//test de la méthode getArticle
		Article a2 = articleDAO.getArticle(1);
		System.out.println(a2);

		 //test de la m�thode getListeArticles
		List<Article> liste=articleDAO.getListeArticles();
		//System.out.println(liste);
		for(Article art : liste)
		{
			System.out.println(art.toString());
		}

	}
}
