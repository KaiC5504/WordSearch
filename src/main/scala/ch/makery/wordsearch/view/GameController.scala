package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.{GameBoard, GameManager}
import javafx.fxml.FXML
import scalafx.Includes._
import scalafx.scene.control.Label
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml

@sfxml
class GameController(
                      val gameGrid: GridPane,
                      val word1: Label,
                      val word2: Label,
                      val word3: Label
                    ) {



  private val gameBoard: GameBoard = GameBoard.getGameBoard


  var isGameOver = false
  var isUserMove = true
  var difficulty = gameBoard.getSelectedDifficulty

  initialize()

  def initialize(): Unit = {
    val difficulty = gameBoard.getSelectedDifficulty
    val (rows, columns) = boardSize(difficulty)
    setupGameBoard(rows, columns)

    val wordGenerator = new GameManager(gameGrid, rows, columns, handleLetterClick)
    wordGenerator.alphabetInserts(rows, columns)

    val selectWords = wordGenerator.selectWords()
    updateLabels(selectWords)
  }

  private def handleLetterClick(letter: String, row: Int, col: Int): Unit = {
    // Logic for what happens when a letter is clicked
    println(s"Clicked letter: $letter at position ($row, $col)")
  }

  private def updateLabels(words: Seq[String]): Unit = {
    if (words.length >= 3) {
      word1.setText(words(0))
      word2.setText(words(1))
      word3.setText(words(2))
    }
  }


  def boardSize(difficulty: String): (Int, Int) = {
    difficulty.toLowerCase match {
      case "easy" => (7, 7)
      case "medium" => (9, 9)
      case "hard" => (11, 11)
      case _ => throw new IllegalArgumentException("Invalid")
    }
  }

  def setupGameBoard(rows: Int, columns: Int): Unit = {
    gameGrid.children.clear()

    val rowConstraints = new javafx.scene.layout.RowConstraints
    rowConstraints.setMinHeight(10)
    rowConstraints.setPrefHeight(30)
    rowConstraints.setVgrow(javafx.scene.layout.Priority.SOMETIMES)

    gameGrid.rowConstraints = Seq.fill(rows)(rowConstraints)

    val columnConstraints = new javafx.scene.layout.ColumnConstraints
    columnConstraints.setMinWidth(10)
    columnConstraints.setPrefWidth(100)
    columnConstraints.setHgrow(javafx.scene.layout.Priority.SOMETIMES)

    gameGrid.columnConstraints = Seq.fill(columns)(columnConstraints)

    gameGrid.alignment = javafx.geometry.Pos.CENTER
  }
}