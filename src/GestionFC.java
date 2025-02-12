import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GestionFC extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    
    public GestionFC() {
        setTitle("ES Soso Fatou");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel principal avec BorderLayout
        mainPanel = new JPanel(new BorderLayout());
        
        // Création de la barre latérale
        JPanel sideBar = createSideBar();
        mainPanel.add(sideBar, BorderLayout.WEST);
        
        // Panel de contenu avec CardLayout pour switcher entre les pages
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE);
        
        // Ajout des différentes pages
        contentPanel.add(new JoueursPanel(), "JOUEURS");
        contentPanel.add(new MatchsPanel(), "MATCHS");
        contentPanel.add(new StaffPanel(), "STAFF");
        contentPanel.add(new CalendrierPanel(), "CALENDRIER");
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
        setLocationRelativeTo(null);
    }
    
    private JPanel createSideBar() {
        JPanel sideBar = new JPanel();
        sideBar.setLayout(new BoxLayout(sideBar, BoxLayout.Y_AXIS));
        sideBar.setBackground(new Color(63, 81, 181)); // Bleu foncé
        sideBar.setPreferredSize(new Dimension(200, getHeight()));
        
        // Logo et nom du club
        JLabel logoLabel = new JLabel("2024/2025");
        logoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JLabel clubLabel = new JLabel("ES Soso Fatou");
        clubLabel.setFont(new Font("Arial", Font.BOLD, 18));
        clubLabel.setForeground(Color.WHITE);
        clubLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Boutons de navigation
        JButton[] navButtons = {
            createNavButton("Joueurs", "JOUEURS"),
            createNavButton("Staff", "STAFF"),
            createNavButton("Matchs", "MATCHS"),
            createNavButton("Calendrier", "CALENDRIER")
        };
        
        // Ajout des composants
        sideBar.add(Box.createRigidArea(new Dimension(0, 20)));
        sideBar.add(logoLabel);
        sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        sideBar.add(clubLabel);
        sideBar.add(Box.createRigidArea(new Dimension(0, 30)));
        
        for (JButton button : navButtons) {
            sideBar.add(button);
            sideBar.add(Box.createRigidArea(new Dimension(0, 10)));
        }
        
        return sideBar;
    }
    
    private JButton createNavButton(String text, String command) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(180, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setBackground(new Color(63, 81, 181));
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(92, 107, 192));
            }
            
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(63, 81, 181));
            }
        });
        
        button.addActionListener(e -> cardLayout.show(contentPanel, command));
        
        return button;
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GestionFC frame = new GestionFC();
            frame.setVisible(true);
        });
    }
}