package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.{LetterHint, GameBoard, GameManager}
import scalafx.Includes._
import scalafx.scene.control.{Button, Label}
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color
import scalafx.scene.shape.Line
import scalafxml.core.macros.sfxml

@sfxml
class GameController(
                      val gameGrid: GridPane,
                      val word1: Label,
                      val word2: Label,
                      val word3: Label,
                      val gameOver: Label,
                      val line1: Line,
                      val line2: Line,
                      val line3: Line,
                      val backToHome: Button,
                      val newGame: Button,
                      val hint: Button
                    ) {

  private val gameBoard: GameBoard = GameBoard.getGameBoard
  private var gameManager: GameManager = _
  private var currentSelection = Seq.empty[(String, Int, Int)]
  var difficulty = gameBoard.getSelectedDifficulty
  private var wordsFound: Int = 0


  initialize()

  def initialize(): Unit = {
    val difficulty = gameBoard.getSelectedDifficulty
    val (rows, columns) = boardSize(difficulty)
    setupGameBoard(rows, columns)

    gameManager = new GameManager(gameGrid, rows, columns, handAlphabetClick, null)

    val showHint = new LetterHint(gameManager)
    gameManager.hintSystem = showHint

    gameManager.alphabetInserts(rows, columns)

    val selectWords = gameManager.selectWords()
    displayWords(selectWords)

    backToHome.setOnAction(_ => handleBackToHome())
    newGame.setOnAction(_ => handleNewGame())
    hint.setOnAction(_ => handleHint())
  }

  private def handAlphabetClick(letter: String, row: Int, col: Int): Unit = {
    val formedWord = currentSelection.map(_._1).mkString + letter
    val startsNewWord = gameManager.getSelectedWords.exists(w => w.startsWith(formedWord) || w.startsWith(letter))

    if (currentSelection.isEmpty || isBeside(currentSelection.last, (letter, row, col)) && startsNewWord) {
      currentSelection :+= (letter, row, col)
    } else {
      resetSelection()
      if (startsNewWord) {
        currentSelection :+= (letter, row, col)
      }
    }
    checkIfWordFormed()
  }

  private def isBeside(lastSelection: (String, Int, Int), newSelection: (String, Int, Int)): Boolean = {
    val (_, lastRow, lastCol) = lastSelection
    val (_, newRow, newCol) = newSelection
    val side = lastRow == newRow && (Math.abs(newCol - lastCol) == 1)

    side
  }

  private def checkIfWordFormed(): Unit = {
    val formedWord = currentSelection.map(_._1).mkString
    if (gameManager.getSelectedWords.contains(formedWord)) {
      currentSelection.foreach { case (_, row, col) =>
        gameManager.wordStyle(row, col, Color.Green)
      }
      gameManager.wordFound(formedWord)
      crossWord(formedWord)
      resetSelection()
    } else if (currentSelection.nonEmpty && !gameManager.getSelectedWords.exists(word => word.startsWith(formedWord))) {
      resetSelection()
    }
  }

  private def resetSelection(): Unit = {
    currentSelection = Seq.empty
  }

  private def displayWords(words: Seq[String]): Unit = {
    if (words.length >= 3) {
      word1.setText(words(0))
      word2.setText(words(1))
      word3.setText(words(2))
    }
  }

  //ChatGPT for crossWord
  private def crossWord(correctWord: String): Unit = {
    val wordLineMap = Map(
      word1.getText -> line1,
      word2.getText -> line2,
      word3.getText -> line3
    )
    wordLineMap.get(correctWord).foreach(_.setVisible(true))
    wordsFound += 1
    checkGameOver()
  }

  private def checkGameOver(): Unit = {
    if (wordsFound == 3) {
      gameOver.setVisible(true)
      List(word1, word2, word3).foreach(_.setVisible(false))
      List(line1, line2, line3).foreach(_.setVisible(false))

      backToHome.setVisible(true)
      newGame.setVisible(true)
    }
  }

  def handleBackToHome(): Unit = {
    MainApp.showHome()
  }

  def handleNewGame(): Unit = {
    val newSelectWords = gameManager.resetGame()
    displayWords(newSelectWords)
    gameOver.setVisible(false)
    List(word1, word2, word3).foreach(_.setVisible(true))
    List(line1, line2, line3).foreach(_.setVisible(false))
    backToHome.setVisible(false)
    newGame.setVisible(false)
    wordsFound = 0
  }

  def handleHint(): Unit = {
    gameManager.hintSystem.showHint()
  }

  def boardSize(difficulty: String): (Int, Int) = {
    difficulty.toLowerCase match {
      case "easy" => (7, 7)
      case "medium" => (9, 9)
      case "hard" => (11, 11)
    }
  }

  def setupGameBoard(rows: Int, columns: Int): Unit = {
    gameGrid.children.clear()

    val rowConstraints = new javafx.scene.layout.RowConstraints
    rowConstraints.setMinHeight(10)
    rowConstraints.setPrefHeight(30)
    rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES) //ChatGPT for setVgrow

    gameGrid.rowConstraints = Seq.fill(rows)(rowConstraints)

    val columnConstraints = new javafx.scene.layout.ColumnConstraints
    columnConstraints.setMinWidth(10)
    columnConstraints.setPrefWidth(100)
    columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES)

    gameGrid.columnConstraints = Seq.fill(columns)(columnConstraints)

    //ChatGPT for alignment
    gameGrid.alignment = javafx.geometry.Pos.CENTER
  }
}