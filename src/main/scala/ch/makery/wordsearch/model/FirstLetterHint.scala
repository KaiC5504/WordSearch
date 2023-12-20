package ch.makery.wordsearch.model

import scalafx.scene.paint.Color
import scalafx.Includes._

import scala.collection.convert.ImplicitConversions.`collection asJava`
import scala.util.Random


class FirstLetterHint(gameManager: GameManager) extends HintSystem {

  private var usedHints: Set[String] = Set.empty

  override def showHint(): Unit = {
    val availableWords = gameManager.getSelectedWords.filterNot(word =>
      gameManager.isWordFound(word) || usedHints.contains(word))

    println(s"Available words for hints after filtering: $availableWords")
    println(s"Words already found: ${gameManager.getSelectedWords.filter(gameManager.isWordFound)}")
    println(s"Used hints so far: $usedHints")

    if (availableWords.nonEmpty) {
      val randomWord = availableWords(Random.nextInt(availableWords.length))
      usedHints += randomWord

      gameManager.wordStartPositions.get(randomWord).foreach { case (row, col, _) =>
        val firstLetterHint = randomWord.head.toString
        println(s"Hint: $firstLetterHint, at row: $row, col: $col")
        gameManager.highlightFirstLetter(row, col)
      }

    } else {
      println("No more hints available!")
    }
  }

  def resetHints(): Unit = {
    usedHints = Set.empty
    println("Used hints have been reset.")
  }

}