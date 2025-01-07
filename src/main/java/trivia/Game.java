package trivia;

import java.util.ArrayList;

import static java.lang.System.out;

// REFACTOR ME
public class Game implements IGame {

   public static final int BOARD_SIZE = 12;
   public static final int MAX_PLAYERS = 6;
   public static final int WINNING_COINS = 6;
   final Questions questions = new Questions();

   ArrayList<String> players = new ArrayList<>();
   int[] locations = new int[MAX_PLAYERS];
   int[] coins = new int[MAX_PLAYERS];
   boolean[] inPenaltyBox = new boolean[MAX_PLAYERS];

   int currentPlayer = 0;
   boolean isGettingOutOfPenaltyBox;

   public boolean add(String playerName) {
      players.add(playerName);
      locations[howManyPlayers()] = 0;
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

       if (!inPenaltyBox[currentPlayer]) {
          movePlayerToNewLocation(roll);
          askQuestion();
          return;
       }

       if (roll % 2 != 0) {
          movePlayerOutOfPenaltyBox(roll);
       } else {
          keepPlayerInPenaltyBox();
       }
   }

   private void keepPlayerInPenaltyBox() {
      out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
      isGettingOutOfPenaltyBox = false;
   }

   private void movePlayerOutOfPenaltyBox(int roll) {
      isGettingOutOfPenaltyBox = true;
      out.println(players.get(currentPlayer) + " is getting out of the penalty box");
      movePlayerToNewLocation(roll);
      askQuestion();
   }

   private void askQuestion() {
      out.println("The category is " + questions.currentCategory(locations[currentPlayer]));
      questions.askQuestion(locations[currentPlayer]);
   }

   private void movePlayerToNewLocation(int roll) {
      locations[currentPlayer] = locations[currentPlayer] + roll;
      if (locations[currentPlayer] >= BOARD_SIZE) locations[currentPlayer] = locations[currentPlayer] - BOARD_SIZE;

      out.println(players.get(currentPlayer)
                         + "'s new location is "
                         + locations[currentPlayer]);
   }

   public boolean wasCorrectlyAnswered() {
       if (!inPenaltyBox[currentPlayer]) {
          return handleCorrectAnswer();
       }

       if (isGettingOutOfPenaltyBox) {
          return handleCorrectAnswer();
       }

       moveToNextPlayer();
       return true;
   }

   private boolean handleCorrectAnswer() {
      out.println("Answer was correct!!!!");
      coins[currentPlayer]++;
      out.println(players.get(currentPlayer)
                         + " now has "
                         + coins[currentPlayer]
                         + " Gold Coins.");

      boolean winner = didPlayerWin();
      moveToNextPlayer();

      return winner;
   }

   private void moveToNextPlayer() {
      currentPlayer++;
      if (currentPlayer == players.size()) currentPlayer = 0;
   }

   public boolean wrongAnswer() {
      out.println("Question was incorrectly answered");
      out.println(players.get(currentPlayer) + " was sent to the penalty box");
      inPenaltyBox[currentPlayer] = true;

      moveToNextPlayer();
      return true;
   }

   private boolean didPlayerWin() {
      return coins[currentPlayer] != WINNING_COINS;
   }
}
