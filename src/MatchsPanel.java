import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.List;

public class MatchsPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private JTable tableMatchs;
    private DefaultTableModel modelMatchs;
    private JTextField searchField;
    private JButton addButton, modifyButton, deleteButton;
    private MatchDAO matchDAO;

    public MatchsPanel() {
        matchDAO = new MatchDAO();
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Gestion des Matchs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        topPanel.add(titleLabel, BorderLayout.WEST);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(Color.WHITE);

        searchField = new JTextField(15);
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterMatchList(searchField.getText());
            }
        });

        addButton = new JButton("Nouveau Match");
        modifyButton = new JButton("Modifier Match");
        deleteButton = new JButton("Supprimer Match");

        styleButton(addButton);
        styleButton(modifyButton);
        styleButton(deleteButton);

        addButton.addActionListener(e -> ajouterMatch());
        modifyButton.addActionListener(e -> modifierMatchSelectionne());
        deleteButton.addActionListener(e -> supprimerMatchSelectionne());

        buttonPanel.add(new JLabel("Rechercher :"));
        buttonPanel.add(searchField);
        buttonPanel.add(addButton);
        buttonPanel.add(modifyButton);
        buttonPanel.add(deleteButton);

        topPanel.add(buttonPanel, BorderLayout.EAST);

        String[] columns = {"Sélection", "Id", "Date", "Adversaire", "Compétition", "Stade"};
        modelMatchs = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 0;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
        };

        tableMatchs = new JTable(modelMatchs);
        JScrollPane scrollPane = new JScrollPane(tableMatchs);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        refreshMatchList();
    }

    private void refreshMatchList() {
        modelMatchs.setRowCount(0);
        List<Match> matches = matchDAO.getListeMatchs();
        for (Match match : matches) {
            modelMatchs.addRow(new Object[]{false, match.getId(), match.getDate(), match.getAdversaire(), match.getCompetition(), match.getStade()});
        }
    }

    private void filterMatchList(String query) {
        modelMatchs.setRowCount(0);
        List<Match> matches = matchDAO.getListeMatchs();
        for (Match match : matches) {
            if (match.getAdversaire().toLowerCase().contains(query.toLowerCase()) ||
                match.getCompetition().toLowerCase().contains(query.toLowerCase()) ||
                match.getStade().toLowerCase().contains(query.toLowerCase()) ||
                match.getDate().toString().contains(query.toLowerCase())) {
                modelMatchs.addRow(new Object[]{false, match.getId(), match.getDate(), match.getAdversaire(), match.getCompetition(), match.getStade()});
            }
        }
    }

    private void ajouterMatch() {
        String date = JOptionPane.showInputDialog(this, "Entrez la date du match:");
        String adversaire = JOptionPane.showInputDialog(this, "Entrez l'adversaire:");
        String competition = JOptionPane.showInputDialog(this, "Entrez la compétition:");
        String stade = JOptionPane.showInputDialog(this, "Entrez le stade:");

        if (date != null && adversaire != null && competition != null && stade != null &&
            !date.trim().isEmpty() && !adversaire.trim().isEmpty() && !competition.trim().isEmpty() && !stade.trim().isEmpty()) {
            Match nouveauMatch = new Match(LocalDate.parse(date), adversaire, competition, stade);
            matchDAO.ajouter(nouveauMatch);
            refreshMatchList();
        }
    }

    private void modifierMatchSelectionne() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un match à modifier.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modelMatchs.getValueAt(selectedRow, 1);
        LocalDate date = (LocalDate) modelMatchs.getValueAt(selectedRow, 2);
        String adversaire = (String) modelMatchs.getValueAt(selectedRow, 3);
        String competition = (String) modelMatchs.getValueAt(selectedRow, 4);
        String stade = (String) modelMatchs.getValueAt(selectedRow, 5);

        String newDate = JOptionPane.showInputDialog(this, "Modifier la date:", date);
        String newAdversaire = JOptionPane.showInputDialog(this, "Modifier l'adversaire:", adversaire);
        String newCompetition = JOptionPane.showInputDialog(this, "Modifier la compétition:", competition);
        String newStade = JOptionPane.showInputDialog(this, "Modifier le stade:", stade);

        if (newDate != null && newAdversaire != null && newCompetition != null && newStade != null) {
            matchDAO.modifier(new Match(id, date, newAdversaire, newCompetition, newStade));
            JOptionPane.showMessageDialog(this, "Match modifié avec succès !");
            refreshMatchList();
        }
    }

    private void supprimerMatchSelectionne() {
        int selectedRow = getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Veuillez sélectionner un match à supprimer.", "Erreur", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) modelMatchs.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this, "Voulez-vous vraiment supprimer ce match ?", "Confirmation", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            matchDAO.supprimer(id);
            JOptionPane.showMessageDialog(this, "Match supprimé avec succès !");
            refreshMatchList();
        }
    }

    private int getSelectedRow() {
        for (int i = 0; i < modelMatchs.getRowCount(); i++) {
            Boolean isChecked = (Boolean) modelMatchs.getValueAt(i, 0);
            if (Boolean.TRUE.equals(isChecked)) {
                return i;
            }
        }
        return -1;
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
    }
}
