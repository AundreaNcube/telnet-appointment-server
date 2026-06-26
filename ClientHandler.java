import java.io.*;
import java.net.*;
import java.util.List;

/**
 * ClientHandler class: handles communication with a single Telnet client,
 * processes commands and interacts with the Appointmentdatabase
 */

public class ClientHandler implements Runnable {
    private Socket socket;
    private Appointmentdatabase database;
    private BufferedReader in;
    private OutputStream out;
    private boolean running;

    public ClientHandler(Socket socket, Appointmentdatabase database) {
        this.socket = socket;
        this.database = database;
        this.running = true;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = socket.getOutputStream();

            displayWelcomeMessage();

            while (running) {
                displayMenu();
                String choice = readLine();

                if (choice == null || choice.trim().isEmpty()) {
                    println(Ansihelper.fgYellow() + "Please enter a choice." + Ansihelper.reset());
                    pause();
                    continue;
                }

                if (choice.trim().equals("5")) {
                    running = false;
                    break;
                }

                handleMenuChoice(choice.trim());
            }

            clearScreen();

            println("Thank you for using the Appointment Database. Goodbye!");
        } catch (IOException e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            cleanup();
        }
    }

    private void displayWelcomeMessage() throws IOException {
        clearScreen();

        println(Ansihelper.colored("╔═══════════════════════════════════════╗",
                Ansihelper.fgBrightWhite(), Ansihelper.bgBlue()));
        println(Ansihelper.colored("║  APPOINTMENT DATABASE SERVER          ║",
                Ansihelper.fgBrightWhite(), Ansihelper.bgBlue()));
        println(Ansihelper.colored("║  Professional Scheduling System       ║",
                Ansihelper.fgBrightWhite(), Ansihelper.bgBlue()));
        println(Ansihelper.colored("╚═══════════════════════════════════════╝",
                Ansihelper.fgBrightWhite(), Ansihelper.bgBlue()));

        println("");
        println(Ansihelper.fgBrightGreen() + "Welcome! Loading..." + Ansihelper.reset());
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void displayMenu() throws IOException {
        clearScreen();
        moveCursor(1, 1);
        println(Ansihelper.fgBrightCyan() + "+++++++++++++++++++++++++++" + Ansihelper.reset());
        println(Ansihelper.fgBrightCyan() + "APPOINTMENT DATABASE MENU" + Ansihelper.reset());
        println(Ansihelper.fgBrightCyan() + "+++++++++++++++++++++++++++" + Ansihelper.reset());
        println("");
        println(Ansihelper.fgWhite() + "Please select an option:" + Ansihelper.reset());
        println(Ansihelper.fgBrightGreen() + "1. Add a new appointments" + Ansihelper.reset());
        println(Ansihelper.fgBrightBlue() + "2. View all appointments" + Ansihelper.reset());
        println(Ansihelper.fgBrightYellow() + "3. Search for an appointment" + Ansihelper.reset());
        println(Ansihelper.fgBrightMagenta() + "4. Delete an appointment" + Ansihelper.reset());
        println(Ansihelper.fgBrightRed() + "5. Exit" + Ansihelper.reset());
        println("");
        println(Ansihelper.fgCyan() + "-".repeat(50) + Ansihelper.reset());
        print(Ansihelper.fgYellow() + "➤ Enter your choice: (1-5) " + Ansihelper.reset());
    }

    private void handleMenuChoice(String choice) throws IOException {
        switch (choice) {
            case "1":
                addAppointment();
                break;
            case "2":
                viewAllAppointments();
                break;
            case "3":
                searchAppointments();
                break;
            case "4":
                deleteAppointment();
                break;
            default:
                println(Ansihelper.fgRed() + "Invalid choice. Please enter a number between 1 and 5."
                        + Ansihelper.reset());
                pause();
        }
    }

    private void addAppointment() throws IOException {
        clearScreen();
        println(Ansihelper.fgCyan() + "++++ ADD NEW APPOINTMENT ++++" + Ansihelper.reset());

        print("Enter a date (YYYY-MM-DD)\n");
        String date = readLine();
        while (!isValidDate(date)) {
            println(Ansihelper.fgRed() + "Error: Invalid date format. Please use YYYY-MM-DD." + Ansihelper.reset());
            print("Enter a date (YYYY-MM-DD)\n");
            date = readLine();
        }

        print("Enter a time (HH:MM)\n");
        String time = readLine();
        while (!isValidTime(time)) {
            println(Ansihelper.fgRed() + "Error: Invalid time format. Please use HH:MM." + Ansihelper.reset());
            print("Enter a time (HH:MM)\n");
            time = readLine();
        }

        print("Enter person/title\n");
        String person = readLine();

        print("Enter a description\n");
        String description = readLine();

        if (!isValidDate(date)) {
            println(Ansihelper.fgRed() + "Error: Invalid date format. Please use YYYY-MM-DD." + Ansihelper.reset());
            pause();
            return;
        }

        if (!isValidTime(time)) {
            println(Ansihelper.fgRed() + "Error: Invalid time format. Please use HH:MM." + Ansihelper.reset());
            pause();
            return;
        }

        if (!isValidInput(person)) {
            println(Ansihelper.fgRed() + "Error: Person cannot be empty." + Ansihelper.reset());
            pause();
            return;
        }

        if (!isValidInput(description)) {
            println(Ansihelper.fgRed() + "Error: Description cannot be empty." + Ansihelper.reset());
            pause();
            return;
        }

        Appointment appointment = new Appointment(date.trim(),
                time.trim(),
                person.trim(),
                description.trim());

        database.addAppointment(appointment);
        Server.saveDatabase();
        println(Ansihelper.fgGreen() + "Appointment added successfully!" + Ansihelper.reset());

        pause();
    }

    private boolean isValidDate(String date) {
        if (date == null || date.trim().isEmpty()) {
            return false;
        }
        return date.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private boolean isValidTime(String time) {
        if (time == null || time.trim().isEmpty()) {
            return false;
        }
        return time.matches("\\d{2}:\\d{2}");
    }

    private boolean isValidInput(String input) {
        return input != null && !input.trim().isEmpty();
    }

    private void viewAllAppointments() throws IOException {
        clearScreen();
        println(Ansihelper.fgCyan() + "++++VIEW ALL APPOINTMENTS ++++" + Ansihelper.reset());
        List<Appointment> appointments = database.getAllAppointments();

        if (appointments.isEmpty()) {
            println(Ansihelper.fgYellow() + "No appointments found." + Ansihelper.reset());
        } else {
            for (int i = 0; i < appointments.size(); i++) {
                Appointment appointment = appointments.get(i);

                String color = getTimeColor(appointment.getTime());
                println(color + String.format("[%d] %s", i + 1, appointment.toString()) + Ansihelper.reset());
                println("    " + appointment.getDescription());
                println("");

            }
        }

        pause();
    }

    private String getTimeColor(String time) {
        try {
            int hour = Integer.parseInt(time.split(":")[0]);

            if (hour >= 6 && hour < 12) {
                return Ansihelper.fgBrightYellow();
            } else if (hour >= 12 && hour < 17) {
                return Ansihelper.fgBrightCyan();
            } else if (hour >= 17 && hour < 21) {
                return Ansihelper.fgBrightMagenta();
            } else {
                return Ansihelper.fgBrightBlue();
            }
        } catch (Exception e) {
            return Ansihelper.fgWhite();
        }
    }

    private void searchAppointments() throws IOException {
        clearScreen();
        println(Ansihelper.fgCyan() + "++++ SEARCH APPOINTMENTS ++++" + Ansihelper.reset());
        print("Enter a search query (date, time, person, or description): ");
        String query = readLine();

        if (query == null || query.trim().isEmpty()) {

            println(Ansihelper.fgRed() + "Error: Search query cannot be empty." + Ansihelper.reset());
            pause();
            return;
        }

        List<Appointment> results = database.searchAppointments(query);

        println("\nSearch results for: " + query);
        println("---------------------------------");
        if (results.isEmpty()) {
            println("No appointments found matching the query.");
        } else {
            println(results.size() + " appointment(s) found:");
            for (Appointment appointment : results) {
                println(appointment.toString());
                println("    " + appointment.getDescription());
                println("");
            }
        }

        pause();
    }

    private void deleteAppointment() throws IOException {
        clearScreen();
        println(Ansihelper.fgCyan() + "++++ DELETE AN APPOINTMENT ++++" + Ansihelper.reset());
        List<Appointment> appointments = database.getAllAppointments();

        if (appointments.isEmpty()) {
            println(Ansihelper.fgYellow() + "No appointments to delete." + Ansihelper.reset());
            pause();
            return;
        }

        for (int i = 0; i < appointments.size(); i++) {
            Appointment appointment = appointments.get(i);
            println(String.format("[%d] %s", i + 1, appointment.toString()));
            println("    " + appointment.getDescription());
            println("");
        }

        print("Enter the number of the appointment to delete (or 0 to cancel): ");
        String input = readLine();

        if (input == null || input.trim().isEmpty()) {
            println(Ansihelper.fgYellow() + "Error: Input cannot be empty." + Ansihelper.reset());
            pause();
            return;
        }

        try {
            int choice = Integer.parseInt(input);
            if (choice == 0) {
                println(Ansihelper.fgYellow() + "Deletion cancelled." + Ansihelper.reset());
                return;
            } else if (choice > 0 && choice <= appointments.size()) {
                database.deleteAppointment(choice - 1);
                Server.saveDatabase();
                println(Ansihelper.fgGreen() + "Appointment deleted successfully!" + Ansihelper.reset());
            } else {
                println(Ansihelper.fgRed() + "Invalid choice. Please enter a valid number." + Ansihelper.reset());
            }
        } catch (NumberFormatException e) {
            println(Ansihelper.fgRed() + "Invalid input. Please enter a number." + Ansihelper.reset());
        }

        pause();
    }

    private String readLine() throws IOException {
        StringBuilder sb = new StringBuilder();
        int ch;

        while ((ch = in.read()) != -1) {

            if (ch == '\r' || ch == '\n') {
                out.write('\r');
                out.write('\n');
                out.flush();

                if (ch == '\r') {
                    in.mark(1);
                    int nextCh = in.read();
                    if (nextCh != '\n' && nextCh != -1) {
                        in.reset();
                    }
                }
                break;
            } else if (ch == 127 || ch == 8) {
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                    out.write(8);
                    out.write(' ');
                    out.write(8);
                    out.flush();
                }
            } else if (ch >= 32 && ch <= 127) {
                sb.append((char) ch);
                out.write(ch);
                out.flush();
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    private void print(String message) throws IOException {
        out.write(message.getBytes());
        out.flush();
    }

    private void println(String message) throws IOException {
        print(message + "\r\n");
    }

    private void clearScreen() throws IOException {
        out.write(Ansihelper.clearScreen().getBytes());
        out.flush();

    }

    private void moveCursor(int row, int col) throws IOException {
        out.write(Ansihelper.moveCursor(row, col).getBytes());
        out.flush();
    }

    private void pause() throws IOException {
        println("\n Please Press Enter to continue :))");
        readLine();
    }

    private void cleanup() {
        try {
            if (in != null)
                in.close();
            if (out != null)
                out.close();
            if (socket != null && !socket.isClosed())
                socket.close();
        } catch (IOException e) {
            System.err.println("Error during cleanup: " + e.getMessage());
        }
    }
}
