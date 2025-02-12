import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import javax.swing.Box;

import java.util.List;

public class MatchFenetre extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    
    private JPanel containerPanel;
    private JTextField textFieldDate;
    private JTextField textFieldAdversaire;
    private JTextField textFieldCompetition;
    private JTextField textFieldStade;
    
    private JLabel labelId;
    private JLabel labelDate;
    private JLabel labelAdversaire;
    private JLabel labelCompetition;
    private JLabel labelStade;
    
    private JButton boutonEnvoi;
    private JButton boutonAffichageTousLesMatchs;
    
    private JTextArea zoneTextListMatch;
    private JScrollPane zoneDefilement;
    
    private MatchDAO monMatchDAO;
    
    public MatchFenetre() {
        this.monMatchDAO = new MatchDAO();
        
        // Configuration de la fenêtre
        this.setTitle("Gestion des Matchs");
        this.setSize(500, 500);
        
        // Création et configuration du panneau principal
        containerPanel = new JPanel();
        containerPanel.setLayout(new BoxLayout(containerPanel, BoxLayout.PAGE_AXIS));
        containerPanel.setBackground(new Color(240, 240, 240));
        
        // Initialisation des composants
        textFieldDate = new JTextField();
        textFieldAdversaire = new JTextField();
        textFieldCompetition = new JTextField();
        textFieldStade = new JTextField();
        
        boutonEnvoi = new JButton("Ajouter le match");
        boutonAffichageTousLesMatchs = new JButton("Afficher tous les matchs");
        
        labelId = new JLabel("L'ID sera généré automatiquement par la base de données");
        labelDate = new JLabel("Date du match (AAAA-MM-JJ) :");
        labelAdversaire = new JLabel("Équipe adverse :");
        labelCompetition = new JLabel("Compétition :");
        labelStade = new JLabel("Stade :");
        
        zoneTextListMatch = new JTextArea(10, 40);
        zoneTextListMatch.setEditable(false);
        zoneDefilement = new JScrollPane(zoneTextListMatch);
        
        // Ajout des composants au panneau avec espacement
        containerPanel.add(labelDate);
        containerPanel.add(Box.createRigidArea(new Dimension(0,5)));
        containerPanel.add(textFieldDate);
        containerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        
        containerPanel.add(labelAdversaire);
        containerPanel.add(Box.createRigidArea(new Dimension(0,5)));
        containerPanel.add(textFieldAdversaire);
        containerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        
        containerPanel.add(labelCompetition);
        containerPanel.add(Box.createRigidArea(new Dimension(0,5)));
        containerPanel.add(textFieldCompetition);
        containerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        
        containerPanel.add(labelStade);
        containerPanel.add(Box.createRigidArea(new Dimension(0,5)));
        containerPanel.add(textFieldStade);
        containerPanel.add(Box.createRigidArea(new Dimension(0,15)));
        
        containerPanel.add(boutonEnvoi);
        containerPanel.add(Box.createRigidArea(new Dimension(0,5)));
        containerPanel.add(labelId);
        containerPanel.add(Box.createRigidArea(new Dimension(0,20)));
        
        containerPanel.add(boutonAffichageTousLesMatchs);
        containerPanel.add(Box.createRigidArea(new Dimension(0,10)));
        containerPanel.add(zoneDefilement);
        
        // Ajout d'une bordure au panneau
        containerPanel.setBorder(BorderFactory.createEmptyBorder(15,15,15,15));
        
        // Ajout des écouteurs d'événements
        boutonEnvoi.addActionListener(this);
        boutonAffichageTousLesMatchs.addActionListener(this);
        
        // Configuration finale de la fenêtre
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(containerPanel);
        this.setLocationRelativeTo(null); // Centre la fenêtre
        this.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent ae) {
        try {
            if(ae.getSource() == boutonEnvoi) {
                // Vérification que tous les champs sont remplis
                if(textFieldDate.getText().trim().isEmpty() || 
                   textFieldAdversaire.getText().trim().isEmpty() ||
                   textFieldCompetition.getText().trim().isEmpty() ||
                   textFieldStade.getText().trim().isEmpty()) {
                    zoneTextListMatch.setText("Erreur : Tous les champs doivent être remplis");
                    return;
                }
                
                // Création d'un nouveau match
                LocalDate date = LocalDate.parse(textFieldDate.getText());
                Match nouveauMatch = new Match(
                    date,
                    textFieldAdversaire.getText().trim(),
                    textFieldCompetition.getText().trim(),
                    textFieldStade.getText().trim()
                );
                
                // Ajout du match dans la base de données
                int retour = monMatchDAO.ajouter(nouveauMatch);
                
                if(retour > 0) {
                    // Nettoyage des champs
                    textFieldDate.setText("");
                    textFieldAdversaire.setText("");
                    textFieldCompetition.setText("");
                    textFieldStade.setText("");
                    
                    zoneTextListMatch.setText("Match ajouté avec succès !");
                    // Rafraîchissement automatique de la liste
                    afficherTousLesMatchs();
                }
            }
            else if(ae.getSource() == boutonAffichageTousLesMatchs) {
                afficherTousLesMatchs();
            }
        }
        catch (DateTimeParseException e) {
            zoneTextListMatch.setText("Erreur : Format de date incorrect. Utilisez le format AAAA-MM-JJ");
        }
        catch (Exception e) {
            zoneTextListMatch.setText("Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void afficherTousLesMatchs() {
        List<Match> listeMatchs = monMatchDAO.getListeMatchs();
        StringBuilder sb = new StringBuilder();
        sb.append("Liste des matchs :\n\n");
        
        for(Match match : listeMatchs) {
            sb.append(match.toString()).append("\n");
        }
        
        zoneTextListMatch.setText(sb.toString());
    }
    
    public static void main(String[] args) {
        // Lancement de l'application
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MatchFenetre();
            }
        });
    }
}