package ch.makery.wordsearch.model

import scala.util.Random


class LetterHint(gameManager: GameManager) extends HintSystem {

  private var usedHints: Set[String] = Set.empty

  override def showHint(): Unit = {
    val availableWords = gameManager.getSelectedWords.filterNot(word =>
      gameManager.isWordFound(word) || usedHints.contains(word))

    if (availableWords.nonEmpty) {
      val randomWord = availableWords(Random.nextInt(availableWords.length))
      usedHints += randomWord

      gameManager.wordStartPositions.get(randomWord).foreach { case (row, col, _) =>
        gameManager.hintHighlight(row, col)
      }

    } else {
      println("No more hints!")
    }
  }

  def resetHints(): Unit = {
    usedHints = Set.empty
  }

}