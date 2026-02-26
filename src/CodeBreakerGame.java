import java.util.*;

public class CodeBreakerGame {

    static Scanner sc = new Scanner(System.in);
    static Random rand = new Random();

    // ANSI COLORS
    public static final String RESET = "\u001B[0m";
    public static final String GREEN = "\u001B[32m";
    public static final String RED = "\u001B[31m";
    public static final String CYAN = "\u001B[36m";
    public static final String YELLOW = "\u001B[33m";

    public static void main(String[] args) {

        while (true) {

            printBanner();
            printMainMenu();

            int choice = safeIntInput();

            if (choice == 4) {
                System.out.println(RED + "Game Exit" + RESET);
                break;
            }

            printDifficultyMenu();
            int diff = safeIntInput();

            if (diff == 0)
                continue;

            int length = (diff == 1) ? 3 : 4;

            if (choice == 1) {
                singlePlayer(length);
            } 
            else if (choice == 2) {
                twoPlayer(length);
            } 
            else if (choice == 3) {
                playerVsComputer(length);
            }

            System.out.print("\nPlay Again? (y/n): ");
            char ch = sc.next().charAt(0);

            if (ch == 'n' || ch == 'N') {
                System.out.println("Thanks for playing!");
                break;
            } else {
                clearScreen();
            }
        }
    }

    // ---------------- SAFE INTEGER INPUT ----------------

    static int safeIntInput() {
        while (true) {
            try {
                return sc.nextInt();
            } catch (InputMismatchException e) {
                sc.nextLine();
                System.out.print(RED + "Enter a valid number: " + RESET);
            }
        }
    }

    // ---------------- UI METHODS ----------------

    static void printBanner() {
        System.out.println("\n====================================");
        System.out.println("        🔐 CODE BREAKER GAME 🔐");
        System.out.println("====================================");
    }

    static void printMainMenu() {
        System.out.println("\n╔══════════════════════════════╗");
        System.out.println("║          MAIN MENU           ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ 1. Single Player             ║");
        System.out.println("║ 2. Two Player                ║");
        System.out.println("║ 3. Player vs Computer        ║");
        System.out.println("║ 4. Exit                      ║");
        System.out.println("╚══════════════════════════════╝");
        System.out.print("👉 Enter choice: ");
    }

    static void printDifficultyMenu() {
        System.out.println("\nSelect Difficulty:");
        System.out.println("1. 3 Digit");
        System.out.println("2. 4 Digit");
        System.out.println("0. Back");
        System.out.print("👉 Enter choice: ");
    }

    static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    // ---------------- PROGRESS BAR ----------------

    static void showProgressBar() {
        System.out.print(YELLOW + "Checking ");
        for (int i = 0; i <= 20; i++) {
            System.out.print("█");
            pause(40);
        }
        System.out.println(" Done!" + RESET);
    }

    static void pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ---------------- SINGLE PLAYER ----------------

    static void singlePlayer(int length) {

        System.out.println(CYAN + "\nSingle Player Mode" + RESET);

        String secret = generateUniqueNumber(length);
        int attempts = 0;

        while (true) {

            String guess = inputGuess(length);
            attempts++;

            showProgressBar();
            int[] result = checkHint(secret, guess);
            printResult(result);

            if (result[0] == length) {
                System.out.println(GREEN + "\n🎉 YOU WIN! 🎉" + RESET);
                System.out.println("Total Attempts: " + attempts);
                System.out.println("🔓 Secret Number was: " + secret);
                break;
            }
        }
    }

    // ---------------- TWO PLAYER ----------------

    static void twoPlayer(int length) {

        System.out.println(CYAN + "\nTwo Player Mode" + RESET);

        System.out.println("Player 1 - Enter your secret:");
        String secretP1 = inputGuess(length);
        clearScreen();

        System.out.println("Player 2 - Enter your secret:");
        String secretP2 = inputGuess(length);
        clearScreen();

        while (true) {

            System.out.println("\n===== PLAYER 1 TURN =====");
            String guess1 = inputGuess(length);

            showProgressBar();
            int[] res1 = checkHint(secretP2, guess1);
            printResult(res1);

            if (res1[0] == length) {
                System.out.println(GREEN + "Player 1 Wins!" + RESET);
                System.out.println("🔓 Player 2 Secret was: " + secretP2);
                break;
            }

            System.out.println("\n===== PLAYER 2 TURN =====");
            String guess2 = inputGuess(length);

            showProgressBar();
            int[] res2 = checkHint(secretP1, guess2);
            printResult(res2);

            if (res2[0] == length) {
                System.out.println(GREEN + "Player 2 Wins!" + RESET);
                System.out.println("🔓 Player 1 Secret was: " + secretP1);
                break;
            }
        }
    }

    // ---------------- PLAYER VS COMPUTER ----------------

    static void playerVsComputer(int length) {

        System.out.println(CYAN + "\nPlayer vs Computer Mode" + RESET);

        String computerSecret = generateUniqueNumber(length);

        System.out.println("Enter your secret number for computer:");
        String userSecret = inputGuess(length);

        List<String> possibleNumbers = generateAllPossibleNumbers(length);
        Set<String> usedGuesses = new HashSet<>();

        while (true) {

            // PLAYER TURN
            System.out.println("\n===== PLAYER TURN =====");
            String playerGuess = inputGuess(length);

            showProgressBar();
            int[] playerResult = checkHint(computerSecret, playerGuess);
            printResult(playerResult);

            if (playerResult[0] == length) {
                System.out.println(GREEN + "Player Wins!" + RESET);
                System.out.println("🤖 Computer Secret was: " + computerSecret);
                break;
            }

            // COMPUTER TURN
            System.out.println("\n===== COMPUTER TURN =====");

            String computerGuess = null;

            for (String num : possibleNumbers) {
                if (!usedGuesses.contains(num)) {
                    computerGuess = num;
                    break;
                }
            }

            usedGuesses.add(computerGuess);

            System.out.println("Computer guessed: " + computerGuess);

            showProgressBar();
            int[] compResult = checkHint(userSecret, computerGuess);
            printResult(compResult);

            if (compResult[0] == length) {
                System.out.println(RED + "Computer Wins!" + RESET);
                System.out.println("👤 Your Secret was: " + userSecret);
                break;
            }
        }
    }

    // ---------------- GENERATE ALL POSSIBLE NUMBERS ----------------

    static List<String> generateAllPossibleNumbers(int length) {

        List<String> list = new ArrayList<>();

        for (int i = 0; i < Math.pow(10, length); i++) {

            String num = String.format("%0" + length + "d", i);

            if (isValidGuess(num, length)) {
                list.add(num);
            }
        }

        Collections.shuffle(list);
        return list;
    }

    // ---------------- RESULT DISPLAY ----------------

    static void printResult(int[] result) {
        System.out.println("✔ Exact Match : " + result[0]);
        System.out.println("🔄 Partial Match : " + result[1]);
        System.out.println("--------------------------------");
    }

    // ---------------- INPUT VALIDATION ----------------

    static String inputGuess(int length) {

        while (true) {

            System.out.print("Enter guess: ");
            String guess = sc.next();

            if (isValidGuess(guess, length))
                return guess;

            System.out.println(RED + "Invalid! Enter unique " + length + "-digit number." + RESET);
        }
    }

    static boolean isValidGuess(String guess, int length) {

        if (guess.length() != length)
            return false;

        for (int i = 0; i < guess.length(); i++) {
            if (!Character.isDigit(guess.charAt(i)))
                return false;
        }

        for (int i = 0; i < guess.length(); i++) {
            for (int j = i + 1; j < guess.length(); j++) {
                if (guess.charAt(i) == guess.charAt(j))
                    return false;
            }
        }

        return true;
    }

    // ---------------- RANDOM UNIQUE NUMBER ----------------

    static String generateUniqueNumber(int length) {

        StringBuilder num = new StringBuilder();

        while (num.length() < length) {

            int digit = rand.nextInt(10);
            char ch = (char)(digit + '0');

            if (num.indexOf(String.valueOf(ch)) == -1)
                num.append(ch);
        }

        return num.toString();
    }

    // ---------------- HINT LOGIC ----------------

    static int[] checkHint(String secret, String guess) {

        int correctPos = 0;
        int correctDigit = 0;

        for (int i = 0; i < guess.length(); i++) {

            if (guess.charAt(i) == secret.charAt(i)) {
                correctPos++;
            } 
            else if (secret.indexOf(guess.charAt(i)) != -1) {
                correctDigit++;
            }
        }

        return new int[]{correctPos, correctDigit};
    }
}