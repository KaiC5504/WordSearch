package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.GameBoard
import scalafx.Includes._
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import javafx.application.Platform
import javafx.scene.layout.{ColumnConstraints, RowConstraints}
import scalafx.scene.text.Font

import scala.util.Random


@sfxml
class GameController(
                      val gameGrid: GridPane
                    ) {

  //restartButton.visible = false

  private val gameBoard: GameBoard = GameBoard.getGameBoard


  var isGameOver = false
  var isUserMove = true

  initialize()

  def initialize(): Unit = {
    val difficulty = gameBoard.getSelectedDifficulty
    val (rows, columns) = boardSize(difficulty)
    setupGameBoard(rows, columns)
    populateGridWithAlphabets(rows, columns)
  }

  var difficulty = gameBoard.getSelectedDifficulty

  def populateGridWithAlphabets(rows: Int, columns: Int): Unit = {
    val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    for (i <- 0 until rows; j <- 0 until columns) {
      val randomAlphabet = alphabetPool.charAt(Random.nextInt(alphabetPool.length)).toString
      val label = new javafx.scene.control.Label(randomAlphabet)

      label.setFont(new Font(30))
//      label.alignment = javafx.geometry.Pos.CENTER
//      label.setAlignment(javafx.geometry.Pos.CENTER)
//      label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER)
      gameGrid.add(label, j, i)
      javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
    }
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
// style="-fx-border-color: black; -fx-border-width: 1px; -fx-grid-lines-visible: true;"