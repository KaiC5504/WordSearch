package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.{GameBoard, WordGenerator}
import scalafx.Includes._
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import scalafx.scene.paint.Color
import scalafx.scene.text.Font
import scala.util.Random
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Rectangle

@sfxml
class GameController(
                      val gameGrid: GridPane
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

    val wordGenerator = new WordGenerator(gameGrid, rows, columns)
    wordGenerator.alphabetInserts(rows, columns)
  }


  def boardSize(difficulty: String): (Int, Int) = {
    difficulty.toLowerCase match {
      case "easy" => (7, 7)
      case "medium" => (9, 9)
      case "hard" => (11, 11)
      case _ => throw new IllegalArgumentException("Invalid difficulty")
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