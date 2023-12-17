package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.GameBoard
import scalafx.Includes._
import scalafx.scene.layout.GridPane
import scalafxml.core.macros.sfxml
import javafx.application.Platform
import javafx.scene.control.SplitPane.Divider
import javafx.scene.layout.{ColumnConstraints, RowConstraints}
import scalafx.scene.control.{Label, SplitPane}
import scalafx.scene.control.SplitPane.Divider
import scalafx.scene.paint.Color
import scalafx.scene.text.Font

import scala.util.Random
import scalafx.scene.input.MouseEvent
import scalafx.scene.paint.Color
import scalafx.scene.layout.Background
import scalafx.scene.layout.BackgroundFill
import scalafx.scene.layout.CornerRadii
import scalafx.geometry.Insets
import scalafx.scene.shape.Rectangle

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



  def canPlaceWord(word: String, startRow: Int, startColumn: Int, rows: Int, columns: Int): Boolean = {
    val indices = (0 until word.length).map { k =>
      val targetRow = startRow
      val targetColumn = (startColumn + k) % columns
      (targetRow, targetColumn)
    }

    indices.forall { case (row, column) =>
      gameGrid.getChildren.stream.noneMatch(
        node => GridPane.getColumnIndex(node) == column && GridPane.getRowIndex(node) == row
      )
    }
  }

  def placeWord(word: String, rows: Int, columns: Int, attempts: Int = 10): Unit = {
    if (attempts > 0) {
      val startColumn = Random.nextInt(columns - word.length + 1) // Ensure there is enough space for the word
      val startRow = Random.nextInt(rows)

      if (canPlaceWord(word, startRow, startColumn, rows, columns)) {
        for (k <- 0 until word.length) {
          val letter = word.charAt(k).toString
          val label = new javafx.scene.control.Label(letter)
          label.textFill = Color.Red
          val targetRow = startRow
          val targetColumn = (startColumn + k) % columns // Place each letter in a consecutive column
          gameGrid.add(label, targetColumn, targetRow)
          label.setFont(new Font(30))
          javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)

          val rectangle = new Rectangle {
            width <== gameGrid.width / columns
            height <== gameGrid.height / rows
            fill = Color.Transparent
          }
          rectangle.setOnMousePressed((event: MouseEvent) => {
            rectangle.fill = Color.Blue.opacity(0.1)
          })

          rectangle.setOnMouseDragged((event: MouseEvent) => {
            rectangle.fill = Color.Blue.opacity(0.1)
          })

          rectangle.setOnMouseReleased((event: MouseEvent) => {
            rectangle.fill = Color.Transparent
          })
          gameGrid.add(rectangle, targetColumn, targetRow)

        }
      } else {
        // Try placing the word again
        placeWord(word, rows, columns, attempts - 1)
      }
    }
  }

  def populateGridWithAlphabets(rows: Int, columns: Int): Unit = {
    val words = Seq("HOT", "THIS", "WHY")
    val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    for (word <- words) {
      placeWord(word, rows, columns)
    }

    for (i <- 0 until rows; j <- 0 until columns) {

      if (gameGrid.getChildren.stream.noneMatch(
        node => GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i)
      ) {
        val randomAlphabet = alphabetPool.charAt(Random.nextInt(alphabetPool.length)).toString
        val label = new javafx.scene.control.Label(randomAlphabet)
        label.setFont(new Font(30))
        javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
        gameGrid.add(label, j, i)

        val rectangle = new Rectangle {
          width <== gameGrid.width / columns
          height <== gameGrid.height / rows
          fill = Color.Transparent
        }

        rectangle.setOnMousePressed((event: MouseEvent) => {
          rectangle.fill = Color.Blue.opacity(0.1)
        })

        rectangle.setOnMouseDragged((event: MouseEvent) => {
          rectangle.fill = Color.Blue.opacity(0.1)
        })

        rectangle.setOnMouseReleased((event: MouseEvent) => {
          rectangle.fill = Color.Transparent
        })
        gameGrid.add(rectangle, j, i)

      }
    }
  }




  //  def populateGridWithAlphabets(rows: Int, columns: Int): Unit = {
//    val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
//    val word = "HOT"
//    val wordRowIndex = Random.nextInt(rows)
//
//    var wordPlaced = false
//
//    for (i <- 0 until rows; j <- 0 until columns) {
//      if (!wordPlaced && i == wordRowIndex && j + word.length <= columns && Random.nextBoolean()) {
//        // Check if there is enough space to place the word "HOT"
//        var canPlaceWord = true
//        for (k <- 0 until word.length) {
//          val targetColumn = j + k
//          if (gameGrid.getChildren.stream.anyMatch(
//            node => GridPane.getColumnIndex(node) == targetColumn && GridPane.getRowIndex(node) == i)
//          ) {
//            // There is already something in the target position
//            canPlaceWord = false
//          }
//        }
//
//        if (canPlaceWord) {
//          // Place the word "HOT" if there is enough space and positions are available
//          for (k <- 0 until word.length) {
//            val letter = word.charAt(k).toString
//            val label = new javafx.scene.control.Label(letter)
//            gameGrid.add(label, j + k, i)
//            label.setFont(new Font(30))
//            javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
//          }
//          wordPlaced = true
//        }
//      } else {
//        // Place a random alphabet if the position is not occupied by the word "HOT"
//        if (gameGrid.getChildren.stream.noneMatch(
//          node => GridPane.getColumnIndex(node) == j && GridPane.getRowIndex(node) == i)
//        ) {
//          val randomAlphabet = alphabetPool.charAt(Random.nextInt(alphabetPool.length)).toString
//          val label = new javafx.scene.control.Label(randomAlphabet)
//          gameGrid.add(label, j, i)
//          label.setFont(new Font(30))
//          javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
//        }
//      }
//    }
//
//    for (i <- 0 until rows; j <- 0 until columns) {
//      val randomAlphabet = alphabetPool.charAt(Random.nextInt(alphabetPool.length)).toString
//      val label = new javafx.scene.control.Label(randomAlphabet)
//
//      label.setFont(new Font(30))
//      gameGrid.add(label, j, i)
//      javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
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
// style="-fx-border-color: black; -fx-border-width: 1px; -fx-grid-lines-visible: true;"