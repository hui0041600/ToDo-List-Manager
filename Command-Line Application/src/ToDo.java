import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ToDo {
    static final String FILE_NAME = "data.txt";
    private static final List<Task> tasks = new ArrayList<>();

    public static void main(String[] args) {

        createDataFileIfNotExists(); // Add this line to ensure data.txt exists
        loadDataFromFile();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to ToDo. Enter help for instructions.");
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine().trim();

            if (input.equals("exit")) {
                saveDataToFile();
                System.out.println("Exiting the application.");
                break;
            }

            try {
                processCommand(input);
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    private static void createDataFileIfNotExists() {
        File file = new File(FILE_NAME);
        try {
            if (file.createNewFile()) {
                System.out.println("data.txt created successfully.");
            } else {
                System.out.println("data.txt already exists.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void loadDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                boolean complete = Boolean.parseBoolean(parts[0]);
                String description = parts[1];
                Task task = new Task(description, complete);
                tasks.add(task);
            }
        } catch (IOException e) {
        }
    }

    private static void saveDataToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.isComplete() + "," + task.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void processCommand(String input) {
        String[] parts = input.split("\\s+", 2);
        String command = parts[0].toLowerCase();

        switch (command) {
            case "add":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid command. Please provide a task description.");
                }
                addTask(parts[1]);
                break;

            case "view":
                viewTasks();
                break;

            case "delete":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid command. Please provide a task position to delete.");
                }
                deleteTask(Integer.parseInt(parts[1]));
                break;

            case "complete":
            case "incomplete":
                if (parts.length < 2) {
                    throw new IllegalArgumentException("Invalid command. Please provide a task position to mark as complete/incomplete.");
                }
                markTask(Integer.parseInt(parts[1]), command.equals("complete"));
                break;

            case "help":
                displayHelp();
                break;

            default:
                throw new IllegalArgumentException("Invalid command. Type 'help' for instructions.");
        }
    }

    private static void addTask(String description) {
        Task task = new Task(description);
        tasks.add(task);
        System.out.println("Task \"" + description + "\" has been added to the list.");
        saveDataToFile();
    }

    private static void viewTasks() {
        if (tasks.isEmpty()) {
            System.out.println("There are no tasks to display.");
        } else {
            System.out.printf("%-10s%-20s%s%n", "       ", "Status", "Description");
            for (int i = 0; i < tasks.size(); i++) {
                Task task = tasks.get(i);
                System.out.printf("%-10d%-20s%s%n",
                        i + 1,
                        task.isComplete() ? "complete" : "incomplete",
                        task.getDescription());
            }
        }
    }


    private static void deleteTask(int position) {
        if (position < 1 || position > tasks.size()) {
            throw new IllegalArgumentException("Invalid task position.");
        }
        Task task = tasks.remove(position - 1);
        task.getDescription();
        System.out.println("Task " + position + " has been deleted.");
        saveDataToFile();
    }

    private static void markTask(int position, boolean complete) {
        if (position < 1 || position > tasks.size()) {
            throw new IllegalArgumentException("Invalid task position.");
        }
        Task task = tasks.get(position - 1);
        task.setComplete(complete);
        System.out.println("Task " + position + " has been marked as " + (complete ? "complete" : "incomplete") + ".");
        saveDataToFile();
    }

    private static void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("  add <task_description> - Adds a task with the specified description.");
        System.out.println("  view - Displays tasks in a neat table format with position, description, and status.");
        System.out.println("  delete <task_position> - Deletes a task at the specified position.");
        System.out.println("  complete <task_position> - Marks a task as complete at the specified position.");
        System.out.println("  incomplete <task_position> - Marks a task as incomplete at the specified position.");
        System.out.println("  help - Displays clear usage instructions.");
        System.out.println("  exit - Exits the application.");
    }
}
