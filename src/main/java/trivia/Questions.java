package trivia;

import java.util.LinkedList;

import static java.lang.System.out;

import static trivia.Questions.Category.POP;
import static trivia.Questions.Category.SCIENCE;
import static trivia.Questions.Category.SPORTS;
import static trivia.Questions.Category.ROCK;

public class Questions {
    public static final int QUESTION_COUNT = 50;
    
    LinkedList<String> popQuestions = new LinkedList<>();
    LinkedList<String> scienceQuestions = new LinkedList<>();
    LinkedList<String> sportsQuestions = new LinkedList<>();
    LinkedList<String> rockQuestions = new LinkedList<>();

    public enum Category {
        POP, SCIENCE, SPORTS, ROCK;

        @Override
        public String toString() {
            return name().charAt(0) + name().substring(1).toLowerCase();
        }
    }

    public Questions() {
        populateQuestions();
    }

    void populateQuestions() {
        for (int i = 0; i < QUESTION_COUNT; i++) {
            popQuestions.addLast(createQuestion(i, POP));
            scienceQuestions.addLast(createQuestion(i, SCIENCE));
            sportsQuestions.addLast(createQuestion(i, SPORTS));
            rockQuestions.addLast(createQuestion(i, ROCK));
        }
    }

    public String createQuestion(int index, Category category) {
        return category + " Question " + index;
    }

    void askQuestion(int place) {
        switch (currentCategory(place)) {
            case POP -> out.println(popQuestions.removeFirst());
            case SCIENCE -> out.println(scienceQuestions.removeFirst());
            case SPORTS -> out.println(sportsQuestions.removeFirst());
            case ROCK -> out.println(rockQuestions.removeFirst());
            default -> throw new IllegalStateException("Unexpected value: " + currentCategory(place));
        }
    }

    Category currentCategory(int place) {
        return switch (place % 4) {
            case 0 -> ROCK;
            case 1 -> POP;
            case 2 -> SCIENCE;
            default -> SPORTS;
        };
    }


}