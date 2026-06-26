import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * in memory database to manage appointments, its thread safe and can be
 * accessed by multiple clients at the same time
 */
public class Appointmentdatabase {
    private List<Appointment> appointments;

    public Appointmentdatabase() {
        this.appointments = new ArrayList<>();
    }

    /**
     * adding a new appointment to the database
     */
    public synchronized void addAppointment(Appointment appointment) {
        appointments.add(appointment);
    }

    /**
     * get all appointments from the database
     */
    public synchronized List<Appointment> getAllAppointments() {
        return new ArrayList<>(appointments);
    }

    /**
     * search for appointments that match a query
     */
    public synchronized List<Appointment> searchAppointments(String query) {
        return appointments.stream()
                .filter(appointment -> appointment.matches(query))
                .collect(Collectors.toList());
    }

    /**
     * delete an appointment from the database using an index
     */
    public synchronized boolean deleteAppointment(int index) {
        if (index >= 0 && index < appointments.size()) {
            appointments.remove(index);
            return true;
        }
        return false;
    }

    /**
     * delete a specific appointment object
     */
    public synchronized boolean deleteAppointment(Appointment apt) {
        return appointments.remove(apt);
    }

    /**
     * get an appointment at a specific index
     */
    public synchronized Appointment getAppointment(int index) {
        if (index >= 0 && index < appointments.size()) {
            return appointments.get(index);
        }
        return null;
    }

    /**
     * get total number of appointments in the database
     */
    public synchronized int size() {
        return appointments.size();
    }

    /**
     * clear all appointments from the database
     */
    public synchronized void clear() {
        appointments.clear();
    }

    /**
     * replace all appointments
     */
    public synchronized void setAppointments(List<Appointment> newAppointments) {
        this.appointments = new ArrayList<>(newAppointments);
    }
}