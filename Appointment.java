import java.io.Serializable;

/**
 * Class to show a single appointment
 */
public class Appointment implements Serializable{
    private static final long serialVersionUID = 1L;

    private String date;
    private String time;
    private String person;
    private String description;

    public Appointment(String date, String time, String person, String description) {
        this.date = date;
        this.time = time;
        this.person = person;
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getPerson() {
        return person;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public void setPerson(String person)
    {
        this.person = person;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * comparing if appointments match a serch query
     */
    public boolean matches(String query) {
        String term = query.toLowerCase();
        return date.toLowerCase().contains(term) ||
               time.toLowerCase().contains(term) ||
               person.toLowerCase().contains(term) ||
               description.toLowerCase().contains(term);
    }

    @Override
    public String toString() {
        return String.format("%s at %s - %s", date, time, person);
    }

    public String toDetailedString() {
        return String.format("Date: %s\nTime: %s\nPerson: %s\nDescription:%s", date, time, person, description);
    }
}
