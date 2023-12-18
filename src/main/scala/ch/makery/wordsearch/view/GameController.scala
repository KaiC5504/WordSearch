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

  initialize()

  def initialize(): Unit = {
    val difficulty = gameBoard.getSelectedDifficulty
    val (rows, columns) = boardSize(difficulty)
    setupGameBoard(rows, columns)

    val wordGenerator = new WordGenerator(gameGrid, rows, columns)
    wordGenerator.alphabetInserts(rows, columns)
  }

//  def initialize(): Unit = {
//    val difficulty = gameBoard.getSelectedDifficulty
//    val (rows, columns) = boardSize(difficulty)
//    setupGameBoard(rows, columns)
//    alphabetInserts(rows, columns)
//  }


  var difficulty = gameBoard.getSelectedDifficulty



//  def canPlaceWord(word: String, startRow: Int, startColumn: Int, rows: Int, columns: Int): Boolean = {
//    val indices = (0 until word.length).map { k =>
//      val targetRow = startRow
//      val targetColumn = (startColumn + k) % columns
//      (targetRow, targetColumn)
//    }
//
//    indices.forall { case (row, column) =>
//      gameGrid.getChildren.stream.noneMatch(
//        node => GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row
//      )
//    }
//  }
//
//  def placeWord(word: String, rows: Int, columns: Int, attempts: Int = 10): Unit = {
//    if (attempts > 0) {
//      val startColumn = Random.nextInt(columns - word.length + 1) // Ensure there is enough space for the word
//      val startRow = Random.nextInt(rows)
//
//      if (canPlaceWord(word, startRow, startColumn, rows, columns)) {
//        for (k <- 0 until word.length) {
//          val letter = word.charAt(k).toString
//          val label = new javafx.scene.control.Label(letter)
//          label.textFill = Color.Red
//          val targetRow = startRow
//          val targetColumn = (startColumn + k) % columns // Place each letter in a consecutive column
//          gameGrid.add(label, targetColumn, targetRow)
//          label.setFont(new Font(30))
//          javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
//
//          val rectangle = new Rectangle {
//            width <== gameGrid.width / columns
//            height <== gameGrid.height / rows
//            fill = Color.Transparent
//          }
//          rectangle.setOnMousePressed((event: MouseEvent) => {
//            rectangle.fill = Color.Blue.opacity(0.1)
//          })
//
//          rectangle.setOnMouseDragged((event: MouseEvent) => {
//            rectangle.fill = Color.Blue.opacity(0.1)
//          })
//
//          rectangle.setOnMouseReleased((event: MouseEvent) => {
//            rectangle.fill = Color.Transparent
//          })
//          gameGrid.add(rectangle, targetColumn, targetRow)
//
//        }
//      } else {
//        // Try placing the word again
//        placeWord(word, rows, columns, attempts - 1)
//      }
//    }
//  }
//
//  def alphabetInserts(rows: Int, columns: Int): Unit = {
//    val words = Seq("HOT", "THIS", "WHY")
//    val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//
//    for (word <- words) {
//      placeWord(word, rows, columns)
//    }
//
//    for (i <- 0 until rows; j <- 0 until columns) {
//
//      if (gameGrid.getChildren.stream.noneMatch(
//        node => GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i)
//      ) {
//        val randomAlphabet = alphabetPool.charAt(Random.nextInt(alphabetPool.length)).toString
//        val label = new javafx.scene.control.Label(randomAlphabet)
//        label.setFont(new Font(30))
//        javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
//        gameGrid.add(label, j, i)
//
//        val rectangle = new Rectangle {
//          width <== gameGrid.width / columns
//          height <== gameGrid.height / rows
//          fill = Color.Transparent
//        }
//
//        rectangle.setOnMousePressed((event: MouseEvent) => {
//          rectangle.fill = Color.Blue.opacity(0.1)
//        })
//
//        rectangle.setOnMouseDragged((event: MouseEvent) => {
//          rectangle.fill = Color.Blue.opacity(0.1)
//        })
//
//        rectangle.setOnMouseReleased((event: MouseEvent) => {
//          rectangle.fill = Color.Transparent
//        })
//        gameGrid.add(rectangle, j, i)
//
//      }
//    }
//  }

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