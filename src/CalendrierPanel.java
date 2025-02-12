import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class CalendrierPanel extends JPanel {
	/**
	 * numero de version pour classe serialisable
	 * Permet d'eviter le warning "The serializable class ArticleFenetre does not declare a static final serialVersionUID field of type long"
	 */
	private static final long serialVersionUID = 1L; 
    private MatchDAO matchDAO;
    private JPanel calendarPanel;
    private LocalDate currentMonth;
    
    public CalendrierPanel() {
        matchDAO = new MatchDAO();
        currentMonth = LocalDate.now();
        
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        
        // Panel du haut avec titre et navigation
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("Calendrier des Matchs");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        
        JPanel navigationPanel = new JPanel(new FlowLayout());
        navigationPanel.setBackground(Color.WHITE);
        
        JButton prevMonth = new JButton("<<");
        JButton nextMonth = new JButton(">>");
        JLabel monthLabel = new JLabel(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        
        navigationPanel.add(prevMonth);
        navigationPanel.add(monthLabel);
        navigationPanel.add(nextMonth);
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(navigationPanel, BorderLayout.EAST);
        
        // Calendrier
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        refreshCalendar();
        
        // Ajout des composants
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(calendarPanel), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Actions des boutons
        prevMonth.addActionListener(e -> {
            currentMonth = currentMonth.minusMonths(1);
            monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
            refreshCalendar();
        });
        
        nextMonth.addActionListener(e -> {
            currentMonth = currentMonth.plusMonths(1);
            monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
            refreshCalendar();
        });
    }
    
    private void refreshCalendar() {
        calendarPanel.removeAll();
        
        // En-têtes des jours
        String[] days = {"Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim"};
        for (String day : days) {
            JLabel label = new JLabel(day, SwingConstants.CENTER);
            label.setFont(new Font("Arial", Font.BOLD, 12));
            calendarPanel.add(label);
        }
        
        // Récupération des matchs du mois
        List<Match> monthMatches = matchDAO.getListeMatchs(); // Idéalement, filtrer pour le mois courant
        
        // Création des cellules du calendrier
        LocalDate firstOfMonth = currentMonth.withDayOfMonth(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();
        
        // Jours du mois précédent
        for (int i = 1; i < dayOfWeek; i++) {
            calendarPanel.add(new JLabel(""));
        }
        
        // Jours du mois courant
        for (int i = 1; i <= currentMonth.lengthOfMonth(); i++) {
            JPanel dayPanel = new JPanel(new BorderLayout());
            dayPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
            
            JLabel dayLabel = new JLabel(String.valueOf(i), SwingConstants.RIGHT);
            dayPanel.add(dayLabel, BorderLayout.NORTH);
            
            // Vérifier si il y a des matchs ce jour
            LocalDate currentDate = currentMonth.withDayOfMonth(i);
            for (Match match : monthMatches) {
                if (match.getDate().equals(currentDate)) {
                    JLabel matchLabel = new JLabel(match.getAdversaire());
                    matchLabel.setFont(new Font("Arial", Font.PLAIN, 10));
                    dayPanel.add(matchLabel, BorderLayout.CENTER);
                    dayPanel.setBackground(new Color(230, 230, 255));
                }
            }
            
            calendarPanel.add(dayPanel);
        }
        
        calendarPanel.revalidate();
        calendarPanel.repaint();
    }
}