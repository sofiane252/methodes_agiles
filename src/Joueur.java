public class Joueur {
    private int id;  // L'ID est géré par la BDD, mais on peut le récupérer
    private String nom;
    private String prenom;
    private String poste;

    // Constructeur sans ID (pour l'insertion en BDD)
    public Joueur(String nom, String prenom, String poste) {
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
    }

    // Constructeur avec ID (pour récupérer un employé depuis la BDD)
    public Joueur(int id, String nom, String prenom, String poste) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
        this.poste = poste;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getPoste() {
        return poste;
    }

    // Setters (pas de setId, car l'ID est auto-incrémenté)
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    // toString
    @Override
    public String toString() {
        return "Joueur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", poste='" + poste + '\'' +
                '}';
    }
}
