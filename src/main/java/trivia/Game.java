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

   public static final String POP = "Pop";
   public static final String SCIENCE = "Science";
   public static final String SPORTS = "Sports";
   public static final String ROCK = "Rock";

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
      for (int i = 0; i < QUESTION_COUNT; i++) {
         popQuestions.addLast("Pop Question " + i);
         scienceQuestions.addLast(("Science Question " + i));
         sportsQuestions.addLast(("Sports Question " + i));
         rockQuestions.addLast(createRockQuestion(i));
      }
   }

   public String createRockQuestion(int index) {
      return "Rock Question " + index;
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
            out.println("The category is " + currentCategory());
            askQuestion();
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
         out.println("The category is " + currentCategory());
         askQuestion();
      }

   }

   private void askQuestion() {
      if (currentCategory().equals(POP))
         out.println(popQuestions.removeFirst());
      if (currentCategory().equals(SCIENCE))
         out.println(scienceQuestions.removeFirst());
      if (currentCategory().equals(SPORTS))
         out.println(sportsQuestions.removeFirst());
      if (currentCategory().equals(ROCK))
         out.println(rockQuestions.removeFirst());
   }


   private String currentCategory() {
      if (places[currentPlayer] == 0) return POP;
      if (places[currentPlayer] == 4) return POP;
      if (places[currentPlayer] == 8) return POP;
      if (places[currentPlayer] == 1) return SCIENCE;
      if (places[currentPlayer] == 5) return SCIENCE;
      if (places[currentPlayer] == 9) return SCIENCE;
      if (places[currentPlayer] == 2) return SPORTS;
      if (places[currentPlayer] == 6) return SPORTS;
      if (places[currentPlayer] == 10) return SPORTS;
      return ROCK;
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
