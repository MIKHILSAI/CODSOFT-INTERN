import java.util.*;
import java.util.concurrent.*;

class QuizItem {
    private String question;
    private List<String> options;
    private int correctOptionIndex;

    public QuizItem(String question, List<String> options, int correctOptionIndex) {
        this.question = question;
        this.options = options;
        this.correctOptionIndex = correctOptionIndex;
    }

    public String getQuestion() {
        return question;
    }

    public List<String> getOptions() {
        return options;
    }

    public boolean isCorrectAnswer(int index) {
        return index == correctOptionIndex;
    }

    public int getCorrectOptionIndex() {
        return correctOptionIndex;
    }
}

class QuizGame {
    private List<QuizItem> quizItems;
    private int score;
    private List<String> feedback;
    private Scanner scanner;

    public QuizGame(List<QuizItem> quizItems) {
        this.quizItems = quizItems;
        this.score = 0;
        this.feedback = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }

    private void displayQuestion(QuizItem item) {
        System.out.println(item.getQuestion());
        List<String> options = item.getOptions();
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ": " + options.get(i));
        }
    }

    private boolean askQuestion(QuizItem item) {
        displayQuestion(item);

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> future = executor.submit(() -> {
            int answer = -1;
            try {
                answer = scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Enter a number.");
                scanner.next(); // Clear the invalid input
            }
            return answer;
        });

        try {
            int answer = future.get(30, TimeUnit.SECONDS); // 30 seconds to answer
            if (item.isCorrectAnswer(answer - 1)) {
                feedback.add("Question: " + item.getQuestion() + "\nYour answer: " + item.getOptions().get(answer - 1) + " (Correct)");
                return true;
            } else {
                feedback.add("Question: " + item.getQuestion() + "\nYour answer: " + item.getOptions().get(answer - 1) + " (Incorrect)\nCorrect answer: " + item.getOptions().get(item.getCorrectOptionIndex()));
                return false;
            }
        } catch (TimeoutException e) {
            System.out.println("Time's up!");
            feedback.add("Question: " + item.getQuestion() + "\nYour answer: None (Timed out)\nCorrect answer: " + item.getOptions().get(item.getCorrectOptionIndex()));
            return false;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        } finally {
            executor.shutdown();
        }
    }

    public void start() {
        for (QuizItem item : quizItems) {
            boolean isCorrect = askQuestion(item);
            if (isCorrect) {
                System.out.println("Correct!");
                score++;
            } else {
                System.out.println("Incorrect.");
            }
        }
        showResults();
    }

    private void showResults() {
        System.out.println("Quiz Over!");
        System.out.println("Your final score is: " + score + "/" + quizItems.size());
        System.out.println("\nReview your answers:");
        for (String feedbackItem : feedback) {
            System.out.println(feedbackItem);
        }
        System.out.println("Thank you for participating!");
    }
}

public class Main {
    public static void main(String[] args) {
        List<QuizItem> quizItems = Arrays.asList(
            new QuizItem("What is the capital of France?",
                Arrays.asList("Berlin", "Madrid", "Paris", "Rome"), 2),
            new QuizItem("Which planet is known as the Red Planet?",
                Arrays.asList("Earth", "Mars", "Jupiter", "Saturn"), 1),
            new QuizItem("What is the largest ocean on Earth?",
                Arrays.asList("Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean"), 3),
            new QuizItem("Who wrote 'To Kill a Mockingbird'?",
                Arrays.asList("Harper Lee", "Mark Twain", "Ernest Hemingway", "F. Scott Fitzgerald"), 0),
            new QuizItem("What is the chemical symbol for gold?",
                Arrays.asList("Au", "Ag", "Pb", "Fe"), 0),
            new QuizItem("What is the smallest prime number?",
                Arrays.asList("0", "1", "2", "3"), 2),
            new QuizItem("In which year did the Titanic sink?",
                Arrays.asList("1912", "1905", "1898", "1923"), 0),
            new QuizItem("What is the hardest natural substance on Earth?",
                Arrays.asList("Gold", "Iron", "Diamond", "Platinum"), 2),
            new QuizItem("Who painted the Mona Lisa?",
                Arrays.asList("Vincent Van Gogh", "Leonardo da Vinci", "Pablo Picasso", "Claude Monet"), 1),
            new QuizItem("Which planet is closest to the Sun?",
                Arrays.asList("Venus", "Earth", "Mercury", "Mars"), 2)
        );

        QuizGame quizGame = new QuizGame(quizItems);
        quizGame.start();
    }
}
