import java.io.BufferedReader;
import java.io.InputStreamReader;

public class SystemMonitor {

    // ANSI escape codes for coloring the output
    public static class ConsoleColors {
        public static final String RESET = "\033[0m";  // Text Reset
        public static final String RED = "\033[0;31m";     // RED
        public static final String GREEN = "\033[0;32m";   // GREEN
        public static final String YELLOW = "\033[0;33m";  // YELLOW
        public static final String BLUE = "\033[0;34m";    // BLUE
        public static final String CYAN = "\033[0;36m";    // CYAN
        public static final String WHITE_BOLD = "\033[1;37m"; // BOLD WHITE
    }

    // Clear the console screen
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // Method to get CPU usage by executing the mpstat command 
    public static void getCpuUsage() {
        try {
            Process process = Runtime.getRuntime().exec("mpstat 1 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("all")) {
                    String[] cpuData = line.trim().split("\\s+");
                    double idle = Double.parseDouble(cpuData[cpuData.length - 1]);
                    double usage = 100 - idle;
                    System.out.println(ConsoleColors.CYAN + "CPU Usage: " + ConsoleColors.YELLOW + usage + "%" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get memory usage by executing the free command
    public static void getMemoryUsage() {
        try {
            Process process = Runtime.getRuntime().exec("free -m");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Mem:")) {
                    String[] memData = line.trim().split("\\s+");
                    long totalMem = Long.parseLong(memData[1]);
                    long usedMem = Long.parseLong(memData[2]);
                    double memUsage = (usedMem * 100.0) / totalMem;
                    System.out.println(ConsoleColors.CYAN + "Memory Usage: " + ConsoleColors.YELLOW + memUsage + "%" + ConsoleColors.RESET);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get disk usage by executing the df command
    public static void getDiskUsage() {
        try {
            Process process = Runtime.getRuntime().exec("df -h");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            System.out.println(ConsoleColors.CYAN + "Disk Usage:" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.YELLOW + "Filesystem      Size  Used Avail Use%" + ConsoleColors.RESET);
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Method to get network usage by executing the ifstat command
    public static void getNetworkUsage() {
        try {
            Process process = Runtime.getRuntime().exec("ifstat 1 1");
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            System.out.println(ConsoleColors.CYAN + "Network Usage:" + ConsoleColors.RESET);
            while ((line = reader.readLine()) != null) {
                System.out.println(ConsoleColors.GREEN + line + ConsoleColors.RESET);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Simple spinner for a loading effect between refreshes
    public static void displaySpinner() {
        String spinner = "|/-\\";
        for (int i = 0; i < 10; i++) {
            System.out.print("\rFetching data " + spinner.charAt(i % spinner.length()));
            try {
                Thread.sleep(100);  // Spin every 100 milliseconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.print("\r");
    }

    public static void main(String[] args) {
        while (true) {
            clearScreen(); // Clear screen before each update
            System.out.println(ConsoleColors.WHITE_BOLD + "\n--- System Monitoring ---" + ConsoleColors.RESET);
            
            // Divider with ASCII
            System.out.println(ConsoleColors.CYAN + "===================================" + ConsoleColors.RESET);
            
            // Display stats
            getCpuUsage();
            getMemoryUsage();
            getDiskUsage();
            getNetworkUsage();
            
            // Add a progress spinner for effect
            displaySpinner();

            // Sleep for 5 seconds before refreshing the stats
            try {
                Thread.sleep(5000);  // Refresh every 5 seconds
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
