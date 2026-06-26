/**
 * Helper class for ANSI escape sequences. Provides methods for screen control
 * and color formatting
 */
public class Ansihelper {

    private static final String ESC = "\u001B";

    public static String clearScreen() {
        return ESC + "[2J" + ESC + "[H";
    }

    /**
     * Move cursor to position (row, col).. Top-left is (1, 1)
     */
    public static String moveCursor(int row, int col) {
        return ESC + "[" + row + ";" + col + "H";
    }

    /**
     * Clear from cursor to end of line
     */
    public static String clearLine() {
        return ESC + "[K";
    }

    // === Text Formatting ===

    /** Reset all formatting */
    public static String reset() {
        return ESC + "[0m";
    }

    // === Foreground Colors ===

    public static String fgBlack() {
        return ESC + "[30m";
    }

    public static String fgRed() {
        return ESC + "[31m";
    }

    public static String fgGreen() {
        return ESC + "[32m";
    }

    public static String fgYellow() {
        return ESC + "[33m";
    }

    public static String fgBlue() {
        return ESC + "[34m";
    }

    public static String fgMagenta() {
        return ESC + "[35m";
    }

    public static String fgCyan() {
        return ESC + "[36m";
    }

    public static String fgWhite() {
        return ESC + "[37m";
    }

    public static String fgBrightBlack() {
        return ESC + "[90m";
    }

    public static String fgBrightRed() {
        return ESC + "[91m";
    }

    public static String fgBrightGreen() {
        return ESC + "[92m";
    }

    public static String fgBrightYellow() {
        return ESC + "[93m";
    }

    public static String fgBrightBlue() {
        return ESC + "[94m";
    }

    public static String fgBrightMagenta() {
        return ESC + "[95m";
    }

    public static String fgBrightCyan() {
        return ESC + "[96m";
    }

    public static String fgBrightWhite() {
        return ESC + "[97m";
    }

    // === Background Colors ===

    public static String bgBlack() {
        return ESC + "[40m";
    }

    public static String bgRed() {
        return ESC + "[41m";
    }

    public static String bgGreen() {
        return ESC + "[42m";
    }

    public static String bgYellow() {
        return ESC + "[43m";
    }

    public static String bgBlue() {
        return ESC + "[44m";
    }

    public static String bgMagenta() {
        return ESC + "[45m";
    }

    public static String bgCyan() {
        return ESC + "[46m";
    }

    public static String bgWhite() {
        return ESC + "[47m";
    }

    public static String colorize(String text, String colorCode) {
        return colorCode + text + reset();
    }

    public static String colored(String text, String fgColor) {
        return fgColor + text + reset();
    }

    public static String colored(String text, String fgColor, String bgColor) {
        return fgColor + bgColor + text + reset();
    }

    public static String horizontalLine(int length, char character) {
        return String.valueOf(character).repeat(length);
    }

    public static String boxTop(int width) {
        return "╔" + "═".repeat(width - 2) + "╗";
    }

    public static String boxBottom(int width) {
        return "╚" + "═".repeat(width - 2) + "╝";
    }

    public static String boxMiddle(String text, int width) {
        int padding = width - text.length() - 2;
        int leftPad = padding / 2;
        int rightPad = padding - leftPad;
        return "║" + " ".repeat(leftPad) + text + " ".repeat(rightPad) + "║";
    }
}