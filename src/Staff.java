public class Staff{
    private int id;
    private String nom;
    private String prenom;
    private String numeroTelephone;
    private String salaire;
    private String role;

    public Staff(String nom, String prenom, String numeroTelephone, 
                String salaire, String role){
                    
                    this.nom = nom;
                    this.prenom = prenom;
                    this.numeroTelephone = numeroTelephone;
                    this.salaire = salaire;
                    this.role = role;
    }
    public Staff(int id, String nom, String prenom, String numeroTelephone, 
                String salaire, String role){
                    this.id = id;
                    this.nom = nom;
                    this.prenom = prenom;
                    this.numeroTelephone = numeroTelephone;
                    this.salaire = salaire;
                    this.role = role;
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

    public String getNumeroTelephone() {
        return numeroTelephone;
    }

    public String getSalaire() {
        return salaire;
    }

    public String getRole() {
        return role;
    }

    // Setters
    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setNumeroTelephone(String numeroTelephone) {
        this.numeroTelephone = numeroTelephone;
    }

    public void setSalaire(String salaire) {
        this.salaire = salaire;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // toString
    @Override
    public String toString() {
        return "Staff{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", numeroTelephone='" + numeroTelephone + '\'' +
                ", salaire='" + salaire + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
