package ch.makery.wordsearch.model


class GameBoard(selectedDifficulty: String) {
  def getSelectedDifficulty: String = selectedDifficulty
}

object GameBoard {
  private var gameBoard: GameBoard = _

  def setGameBoard(selectedDifficulty: String): Unit = {
    gameBoard = new GameBoard(selectedDifficulty)
  }

  def getGameBoard: GameBoard = gameBoard
}