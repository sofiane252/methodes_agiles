import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class JoueursPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	private JTable tableJoueurs;
	private DefaultTableModel modelJoueurs;
	private JTextField searchField;
	private JButton addButton, modifyButton, deleteButton;
	private JoueurDAO joueurDAO;

	public JoueursPanel() {
		joueurDAO = new JoueurDAO();

		// Set layout for the main panel
		setLayout(new BorderLayout(10, 10)); // BorderLayout for the main panel
		setBackground(Color.WHITE);
		
		// Create top panel for title and search bar
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);

		JLabel titleLabel = new JLabel("Gestion des Joueurs");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		topPanel.add(titleLabel, BorderLayout.WEST);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);

		searchField = new JTextField(15);
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterJoueursList(searchField.getText());
			}
		});

		addButton = new JButton("Ajouter Joueur");
		modifyButton = new JButton("Modifier Joueur");
		deleteButton = new JButton("Supprimer Joueur");

		styleButton(addButton);
		styleButton(modifyButton);
		styleButton(deleteButton);

		addButton.addActionListener(e -> ajouterJoueur());
		modifyButton.addActionListener(e -> modifierJoueurSelectionne());
		deleteButton.addActionListener(e -> supprimerJoueurSelectionne());

		buttonPanel.add(new JLabel("Rechercher :"));
		buttonPanel.add(searchField);
		buttonPanel.add(addButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(deleteButton);

		topPanel.add(buttonPanel, BorderLayout.EAST);

		// Définition des colonnes
		String[] columns = { "Sélection", "ID", "Prénom", "Nom", "Poste" };
		modelJoueurs = new DefaultTableModel(columns, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return column == 0; // Seule la case à cocher est éditable
			}

			@Override
			public Class<?> getColumnClass(int columnIndex) {
				if (columnIndex == 0)
					return Boolean.class; // Case à cocher
				return super.getColumnClass(columnIndex);
			}
		};
		
		// Create table with the model
		tableJoueurs = new JTable(modelJoueurs);
		tableJoueurs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(tableJoueurs);

		add(topPanel, BorderLayout.NORTH);
		add(scrollPane, BorderLayout.CENTER);

		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		refreshJoueursList(); // Load initial data
	}
	
	// Method to refresh the list of players in the table
	private void refreshJoueursList() {
		modelJoueurs.setRowCount(0); // Clear the table model
		List<Joueur> joueurList = joueurDAO.getListeJoueurs();

		for (Joueur j : joueurList) {
			modelJoueurs.addRow(new Object[] {
					false,
					j.getId(),
					j.getPrenom(),
					j.getNom(),
					j.getPoste()
				});
		}
	}
	
	// Method to filter staff based on the search query
	private void filterJoueursList(String query) {
		modelJoueurs.setRowCount(0); // Clear the table model
		List<Joueur> joueurList = joueurDAO.getListeJoueurs();
		for (Joueur j : joueurList) {
			if (j.getPrenom().toLowerCase().contains(query.toLowerCase()) ||
				j.getNom().toLowerCase().contains(query.toLowerCase()) ||
				j.getPoste().toLowerCase().contains(query.toLowerCase())) {
				modelJoueurs.addRow(new Object[] { 
						false, 
						j.getId(), 
						j.getPrenom(), 
						j.getNom(), 
						j.getPoste() 
					});
			}
		}
	}

	private void ajouterJoueur() {
		String prenom = JOptionPane.showInputDialog(this, "Entrez le prénom du joueur:");
		String nom = JOptionPane.showInputDialog(this, "Entrez le nom du joueur:");
		String poste = JOptionPane.showInputDialog(this, "Entrez le poste du joueur:");

		if (prenom != null && nom != null && poste != null && !prenom.trim().isEmpty() && !nom.trim().isEmpty()
				&& !poste.trim().isEmpty()) {
			Joueur nouveauJoueur = new Joueur(nom, prenom, poste);
			joueurDAO.ajouter(nouveauJoueur);
			refreshJoueursList();
		}
	}

	private void modifierJoueurSelectionne() {
		int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un joueur à modifier.", "Erreur",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int id = (int) modelJoueurs.getValueAt(selectedRow, 1);
		Joueur j = joueurDAO.getJoueurById(id);
		if (j == null) {
			JOptionPane.showMessageDialog(this, "Le joueur sélectionné n'existe plus.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		String newPrenom = JOptionPane.showInputDialog(this, "Modifier le prénom:", j.getPrenom());
		String newNom = JOptionPane.showInputDialog(this, "Modifier le nom:", j.getNom());
		String newPoste = JOptionPane.showInputDialog(this, "Modifier le poste:", j.getPoste());

		if (newPrenom != null && newNom != null && newPoste != null) {
			joueurDAO.modifier(new Joueur(id, newNom, newPrenom, newPoste));
			JOptionPane.showMessageDialog(this, "Joueur modifié avec succès !");
			refreshJoueursList();
		}
	}

	private void supprimerJoueurSelectionne() {
		int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un joueur à supprimer.", "Erreur",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		int id = (int) modelJoueurs.getValueAt(selectedRow, 1);
		int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce joueur ?", "Confirmation",
				JOptionPane.YES_NO_OPTION);

		if (confirm == JOptionPane.YES_OPTION) {
			joueurDAO.supprimer(id);
			JOptionPane.showMessageDialog(this, "Joueur supprimé avec succès !");
			refreshJoueursList();
		}
	}

	private int getSelectedRow() {
		for (int i = 0; i < modelJoueurs.getRowCount(); i++) {
			Boolean isChecked = (Boolean) modelJoueurs.getValueAt(i, 0);
			if (Boolean.TRUE.equals(isChecked)) {
				return i; // Retourne l'index de la première case cochée
			}
		}
		return -1; // Aucun joueur sélectionné
	}

	private void styleButton(JButton button) {
		button.setBackground(new Color(63, 81, 181));
		button.setForeground(Color.WHITE);
		button.setFocusPainted(false);
		button.setBorderPainted(false);
	}
}
