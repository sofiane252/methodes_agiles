import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.stream.Collectors;

public class StaffPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	private JTable tableStaffs;
	private DefaultTableModel modelStaff;
	private JTextField searchField;
	private JButton addButton, modifyButton, deleteButton;
	private StaffDAO staffDAO;

	public StaffPanel() {
		staffDAO = new StaffDAO();

		// Set layout for the main panel
		setLayout(new BorderLayout(10, 10)); // BorderLayout for the main panel
		setBackground(Color.WHITE);

		// Create top panel for title and search bar
		JPanel topPanel = new JPanel(new BorderLayout());
		topPanel.setBackground(Color.WHITE);
		
		JLabel titleLabel = new JLabel("Gestion du personnel");
		titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
		topPanel.add(titleLabel, BorderLayout.WEST);
		
		JPanel buttonPanel = new JPanel();
		buttonPanel.setBackground(Color.WHITE);

		searchField = new JTextField(15);
		searchField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				filterStaffsList(searchField.getText());
			}
		});

		// Add "Add Staff" button
		addButton = new JButton("Ajouter Membre");
		modifyButton = new JButton("Modifier Membre");
		deleteButton = new JButton("Supprimer Membre");
		
		styleButton(addButton);
		styleButton(modifyButton);
		styleButton(deleteButton);		
		
		addButton.addActionListener(e -> ajouterStaff());
		modifyButton.addActionListener(e -> modifierStaff());
		deleteButton.addActionListener(e -> supprimerStaff());

		buttonPanel.add(new JLabel("Rechercher :"));
		buttonPanel.add(searchField);
		buttonPanel.add(addButton);
		buttonPanel.add(modifyButton);
		buttonPanel.add(deleteButton);
		
		topPanel.add(buttonPanel, BorderLayout.EAST);


		// Définition des colonnes
		String[] columns = { "Sélection", "ID", "Prénom", "Nom", "Téléphone", "Rôle" };
		modelStaff = new DefaultTableModel(columns, 0) { // Corrected variable name
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
		tableStaffs = new JTable(modelStaff);
		tableStaffs.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JScrollPane scrollPane = new JScrollPane(tableStaffs);
		
		add(topPanel, BorderLayout.NORTH); // Add top panel to main panel
		add(scrollPane, BorderLayout.CENTER); // Add scrollPane to the center of the main panel
		
		setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

		refreshStaffsList(); // Load initial data
	}

	// Method to refresh the list of players in the table
	private void refreshStaffsList() {
		modelStaff.setRowCount(0); // Clear the table model
		List<Staff> staffList = staffDAO.getListeStaff();
		
		for (Staff s : staffList) {
			// Create a row object for the staff
			modelStaff.addRow(new Object[] {
					false,
					s.getId(),
					s.getPrenom(),
					s.getNom(),
					s.getNumeroTelephone(),
					s.getRole()});
		}
	}
	
	// Method to filter staff based on the search query
	private void filterStaffsList(String query) {
		modelStaff.setRowCount(0); // Clear the table model
		List<Staff> staffList = staffDAO.getListeStaff();
		for (Staff s : staffList) {
			if (s.getPrenom().toLowerCase().contains(query.toLowerCase()) ||
				s.getNom().toLowerCase().contains(query.toLowerCase()) ||
				s.getNumeroTelephone().toLowerCase().contains(query.toLowerCase()) ||
				s.getRole().toLowerCase().contains(query.toLowerCase())) {
				modelStaff.addRow(new Object[] { 
						false, 
						s.getId(),
						s.getPrenom(), 
						s.getNom(), 
						s.getNumeroTelephone(), 
						s.getRole() 
					});
			}
		}
	}
	
	// Method to add a new staff
	private void ajouterStaff() {
		String prenom = JOptionPane.showInputDialog(this, "Entrez le prenom ");
		String nom = JOptionPane.showInputDialog(this, "Entrez le nom");
		String numero = JOptionPane.showInputDialog(this, "Entrez le numero ");
		String salaire = JOptionPane.showInputDialog(this, "Entrez le salaire ");
		String role = JOptionPane.showInputDialog(this, "Entrez le role ");

		if (prenom != null && nom != null && numero != null && salaire != null && role != null
				&& !prenom.trim().isEmpty() && !nom.trim().isEmpty() && !numero.trim().isEmpty()
				&& !salaire.trim().isEmpty() && !role.trim().isEmpty()) {
			Staff nouveauStaff = new Staff(prenom, nom, numero, salaire, role);
			staffDAO.ajouter(nouveauStaff); // Add the new staff to the database
			refreshStaffsList(); // Refresh the staff table
		}
	}

	// Method to modify a staff
	private void modifierStaff() {
		int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un mmebre à modifier.", "Erreur",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int id = (int) modelStaff.getValueAt(selectedRow, 1);
		Staff s = staffDAO.getStaff(id);
		if (s == null) {
			JOptionPane.showMessageDialog(this, "Le membre sélectionné n'existe plus.", "Erreur",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		
		String prenom = JOptionPane.showInputDialog(this, "Entrez le nouveau prenom du membre:", s.getPrenom());
		String nom = JOptionPane.showInputDialog(this, "Entrez le nouveau nom du membre:", s.getNom());
		String numero = JOptionPane.showInputDialog(this, "Entrez le nouveau numero du membre", s.getNumeroTelephone());
		String salaire = JOptionPane.showInputDialog(this, "Entrez le nouveau salaire du membre", s.getSalaire());
		String role = JOptionPane.showInputDialog(this, "Entrez le nouveau role du membre", s.getRole());

		if (prenom != null && nom != null && numero != null && salaire != null && role != null) {
			staffDAO.modifier(new Staff(id, nom, prenom, numero, salaire, role)); // Update the staff in the database
			JOptionPane.showMessageDialog(this, "Membre modifié avec succès !");
			refreshStaffsList(); // Refresh the staff table
		}
	}

	// Method to delete a staff
	private void supprimerStaff() {
		int selectedRow = getSelectedRow();
		if (selectedRow == -1) {
			JOptionPane.showMessageDialog(this, "Veuillez sélectionner un membre à supprimer.", "Erreur",
					JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		int id = (int) modelStaff.getValueAt(selectedRow, 1);
		int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce membre ?",
				"Confirmation", JOptionPane.YES_NO_OPTION);
		
		if (confirm == JOptionPane.YES_OPTION) {
			staffDAO.supprimer(id); // Delete the staff from the database
			JOptionPane.showMessageDialog(this, "Joueur supprimé avec succès !");
			refreshStaffsList(); // Refresh the staff table
		}
	}
	
	private int getSelectedRow() {
		for (int i = 0; i < modelStaff.getRowCount(); i++) {
			Boolean isChecked = (Boolean) modelStaff.getValueAt(i, 0);
			if (Boolean.TRUE.equals(isChecked)) {
				return i; // Retourne l'index de la première case cochée
			}
		}
		return -1; // Aucun joueur sélectionné
	}

	// Method to style buttons
	private void styleButton(JButton bouton) {
		bouton.setBackground(new Color(63, 81, 181));
		bouton.setForeground(Color.WHITE);
		bouton.setFocusPainted(false);
		bouton.setBorderPainted(false);
	}
}
