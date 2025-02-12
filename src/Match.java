import java.time.LocalDate;

public class Match {
	private int id; // Facultatif, utile si stocké en BDD
	private LocalDate date; // Correction du nom pour éviter la confusion
	private String adversaire;
	private String competition;
	private String stade;

	/**
	 * @param date
	 * @param adversaire
	 * @param competition
	 * @param stade
	 */
	public Match(LocalDate date, String adversaire, String competition, String stade) {
		this.date = date;
		this.adversaire = adversaire;
		this.competition = competition;
		this.stade = stade;
	}


	/**
	 * @param id
	 * @param date
	 * @param adversaire
	 * @param competition
	 * @param stade
	 */
	public Match(int id, LocalDate date, String adversaire, String competition, String stade) {
		this.id = id;
		this.date = date;
		this.adversaire = adversaire;
		this.competition = competition;
		this.stade = stade;
	}

	// Getters
	public int getId() {
		return id;
	}

	public LocalDate getDate() {
		return date;
	}

	public String getAdversaire() {
		return adversaire;
	}

	public String getCompetition() {
		return competition;
	}

	public String getStade() {
		return stade;
	}

	// Setters (pas de setId, car géré par la BDD si auto-incrémenté)
	public void setdate(LocalDate date) {
		this.date = date;
	}

	public void setAdversaire(String adversaire) {
		this.adversaire = adversaire;
	}

	public void setCompetition(String competition) {
		this.competition = competition;
	}

	public void setStade(String stade) {
		this.stade = stade;
	}

	// toString
	@Override
	public String toString() {
		return "Match{" + "date=" + date + ", adversaire='" + adversaire + '\'' + ", competition='" + competition + '\''
				+ ", stade='" + stade + '\'' + '}';
	}
}
