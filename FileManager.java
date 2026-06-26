import java.io.*;
import java.util.List;
//import java.util.ArrayList;

public class FileManager {
    private static final String FILENAME = "appointments.dat";

    public static void saveAppointments(Appointmentdatabase database) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILENAME))) {
            List<Appointment> appointments = database.getAllAppointments();
            oos.writeObject(appointments);
            System.out.println(appointments.size() + " appointments saved to file.");
        } catch (IOException e) {
            System.err.println("Error saving appointments: " + e.getMessage());
        }
    }

    /* Load appointments from the file */
    @SuppressWarnings("unchecked")
    public static void loadAppointments(Appointmentdatabase database) {
        File file = new File(FILENAME);
        if (!file.exists()) {
            System.out.println("No existing appointments file found. Starting with an empty database.");
            return;
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILENAME))) {
            List<Appointment> appointments = (List<Appointment>) ois.readObject();
            database.setAppointments(appointments);

            System.out.println(appointments.size() + " appointments loaded from file.");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading appointments " + e.getMessage());
        } catch (ClassCastException e) {
            System.err.println("Error! Appointment not found " + e.getMessage());
        }
    }

    public static void exportToCSV(Appointmentdatabase database, String filename) {
        try (PrintWriter pw = new PrintWriter(new File(filename))) {
            pw.println("Date,Time,Person,Description");
            for (Appointment appt : database.getAllAppointments()) {
                pw.printf("%s,%s,%s,%s%n", 
                appt.getDate(), 
                appt.getTime(), 
                appt.getPerson(), 
                appt.getDescription());
            }
            System.out.println("Appointments exported to " + filename);
        } catch (FileNotFoundException e) {
            System.err.println("Error exporting to CSV: " + e.getMessage());
        }
    }

    public static int importFromCSV(Appointmentdatabase database, String filename) {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = br.readLine();

            //boolean firstLine = true;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if(parts.length >= 4){
                    String date = parts[0].replaceAll("\"", "");
                    String time = parts[1].replaceAll("\"", "");
                    String person = parts[2].replaceAll("\"", "");
                    String description = parts[3].replaceAll("\"", "");

                    Appointment appt = new Appointment(date, time, person, description);
                    database.addAppointment(appt);
                    count++;
                }
            }
            System.out.println(count + " appointments imported from " + filename);
        } catch (IOException e) {
            System.err.println("Error importing from CSV: " + e.getMessage());
        }
        return count;
    }
}
