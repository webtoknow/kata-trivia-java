package trivia;

import java.util.ArrayList;

import static java.lang.System.out;

// REFACTOR ME
public class Game implements IGame {

   public static final int BOARD_SIZE = 12;
   public static final int WINNING_COINS = 6;
   final Questions questions = new Questions();

   ArrayList<Player> players = new ArrayList<>();
   int currentPlayerIndex = 0;

   boolean isGettingOutOfPenaltyBox;

   public boolean add(String playerName) {
      players.add(new Player(playerName));

      out.println(playerName + " was added");
      out.println("They are player number " + players.size());
      return true;
   }

   public void roll(int roll) {
      Player currentPlayer = players.get(currentPlayerIndex);

      out.println(currentPlayer.getName() + " is the current player");
      out.println("They have rolled a " + roll);

       if (currentPlayer.isNotInPenaltyBox()) {
          currentPlayer.movePlayerToNewLocation(roll);
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
      out.println(players.get(currentPlayerIndex).getName() + " is not getting out of the penalty box");
      isGettingOutOfPenaltyBox = false;
   }

   private void movePlayerOutOfPenaltyBox(int roll) {
      Player currentPlayer = players.get(currentPlayerIndex);
      isGettingOutOfPenaltyBox = true;
      out.println(currentPlayer.getName() + " is getting out of the penalty box");
      currentPlayer.movePlayerToNewLocation(roll);
      askQuestion();
   }

   private void askQuestion() {
      int currentPlayerLocation = players.get(currentPlayerIndex).getLocation();
      out.println("The category is " + questions.currentCategory(currentPlayerLocation));
      questions.askQuestion(currentPlayerLocation);
   }



   public boolean wasCorrectlyAnswered() {
       if (players.get(currentPlayerIndex).isNotInPenaltyBox()) {
          return handleCorrectAnswer();
       }

       if (isGettingOutOfPenaltyBox) {
          return handleCorrectAnswer();
       }

       moveToNextPlayer();
       return true;
   }

   private boolean handleCorrectAnswer() {
      Player currentPlayer = players.get(currentPlayerIndex);

      out.println("Answer was correct!!!!");
      currentPlayer.setCoins(currentPlayer.getCoins() + 1);
      out.println(players.get(currentPlayerIndex).getName()
                         + " now has "
                         + currentPlayer.getCoins()
                         + " Gold Coins.");

      boolean winner = currentPlayer.didPlayerWin();
      moveToNextPlayer();

      return winner;
   }

   private void moveToNextPlayer() {
      currentPlayerIndex++;
      if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
   }

   public boolean wrongAnswer() {
      Player currentPlayer = players.get(currentPlayerIndex);

      out.println("Question was incorrectly answered");
      out.println(currentPlayer.getName() + " was sent to the penalty box");

      currentPlayer.setInPenaltyBox(true);

      moveToNextPlayer();
      return true;
   }

}
