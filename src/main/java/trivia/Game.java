package trivia;

import java.util.ArrayList;
import java.util.LinkedList;

import static java.lang.System.out;

// REFACTOR ME
public class Game implements IGame {
   public static final int QUESTION_COUNT = 50;
   public static final int BOARD_SIZE = 12;
   public static final int MAX_PLAYERS = 6;
   public static final int WINNING_COINS = 6;

   public enum Category {
      POP, SCIENCE, SPORTS, ROCK;

      @Override
        public String toString() {
             return name().charAt(0) + name().substring(1).toLowerCase();
        }
   }

   ArrayList<String> players = new ArrayList<>();
   int[] places = new int[MAX_PLAYERS];
   int[] coins = new int[MAX_PLAYERS];
   boolean[] inPenaltyBox = new boolean[MAX_PLAYERS];

   LinkedList<String> popQuestions = new LinkedList<>();
   LinkedList<String> scienceQuestions = new LinkedList<>();
   LinkedList<String> sportsQuestions = new LinkedList<>();
   LinkedList<String> rockQuestions = new LinkedList<>();

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public Game() {
      populateQuestions();
   }

   private void populateQuestions() {
      for (int i = 0; i < QUESTION_COUNT; i++) {
         popQuestions.addLast(createQuestion(i, Category.POP));
         scienceQuestions.addLast(createQuestion(i, Category.SCIENCE));
         sportsQuestions.addLast(createQuestion(i, Category.SPORTS));
         rockQuestions.addLast(createQuestion(i, Category.ROCK));
      }
   }

   public String createQuestion(int index, Category category) {
      return category + " Question " + index;
   }

   public boolean add(String playerName) {
      players.add(playerName);
      places[howManyPlayers()] = 0;
      coins[howManyPlayers()] = 0;
      inPenaltyBox[howManyPlayers()] = false;

      out.println(playerName + " was added");
      out.println("They are player number " + players.size());
      return true;
   }

   public int howManyPlayers() {
      return players.size();
   }

   public void roll(int roll) {
      out.println(players.get(currentPlayer) + " is the current player");
      out.println("They have rolled a " + roll);

      if (inPenaltyBox[currentPlayer]) {
         if (roll % 2 != 0) {
            isGettingOutOfPenaltyBox = true;

            out.println(players.get(currentPlayer) + " is getting out of the penalty box");
            places[currentPlayer] = places[currentPlayer] + roll;
            if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - BOARD_SIZE;

            out.println(players.get(currentPlayer)
                               + "'s new location is "
                               + places[currentPlayer]);
            out.println("The category is " + currentCategory(places[currentPlayer]));
            askQuestion(places[currentPlayer]);
         } else {
            out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
            isGettingOutOfPenaltyBox = false;
         }

      } else {

         places[currentPlayer] = places[currentPlayer] + roll;
         if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - BOARD_SIZE;

         out.println(players.get(currentPlayer)
                            + "'s new location is "
                            + places[currentPlayer]);
         out.println("The category is " + currentCategory(places[currentPlayer]));
         askQuestion(places[currentPlayer]);
      }

   }

   private void askQuestion(int place) {
      switch (currentCategory(place)) {
         case POP -> out.println(popQuestions.removeFirst());
         case SCIENCE -> out.println(scienceQuestions.removeFirst());
         case SPORTS -> out.println(sportsQuestions.removeFirst());
         case ROCK -> out.println(rockQuestions.removeFirst());
          default -> throw new IllegalStateException("Unexpected value: " + currentCategory(place));
      }
   }


   private Category currentCategory(int place) {
      return switch (place % 4) {
         case 0 -> Category.POP;
         case 1 -> Category.SCIENCE;
         case 2 -> Category.SPORTS;
         default -> Category.ROCK;
      };
   }

   public boolean wasCorrectlyAnswered() {
      if (inPenaltyBox[currentPlayer]) {
         if (isGettingOutOfPenaltyBox) {
            out.println("Answer was correct!!!!");
            coins[currentPlayer]++;
            out.println(players.get(currentPlayer)
                               + " now has "
                               + coins[currentPlayer]
                               + " Gold Coins.");

            boolean winner = didPlayerWin();
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;

            return winner;
         } else {
            currentPlayer++;
            if (currentPlayer == players.size()) currentPlayer = 0;
            return true;
         }


      } else {

         out.println("Answer was corrent!!!!");
         coins[currentPlayer]++;
         out.println(players.get(currentPlayer)
                            + " now has "
                            + coins[currentPlayer]
                            + " Gold Coins.");

         boolean winner = didPlayerWin();
         currentPlayer++;
         if (currentPlayer == players.size()) currentPlayer = 0;

         return winner;
      }
   }

   public boolean wrongAnswer() {
      out.println("Question was incorrectly answered");
      out.println(players.get(currentPlayer) + " was sent to the penalty box");
      inPenaltyBox[currentPlayer] = true;

      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
      return true;
   }


   private boolean didPlayerWin() {
      return coins[currentPlayer] != WINNING_COINS;
   }
}
