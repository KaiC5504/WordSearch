package ch.makery.wordsearch.model


import ch.makery.wordsearch.MainApp
import scalafx.Includes._
import scalafx.scene.layout.GridPane
import scalafx.scene.paint.Color
import scalafx.scene.text.{Font, FontWeight}

import scala.util.Random
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Rectangle

class GameManager(val gameGrid: GridPane, val rows: Int, val columns: Int, clickHandler: (String, Int, Int) => Unit, var hintSystem: HintSystem) {

  private var _selectedWords: Seq[String] = Seq.empty
  private var foundWords: Set[String] = Set.empty
  val wordsPool = Seq("HOT", "THIS", "WHY", "BIRD", "SAND", "LOVE", "MAY")
  val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
  val maxLengthOfWords: Int = wordsPool.map(_.length).max
  private val rectangleMap: scala.collection.mutable.Map[(Int, Int), Rectangle] = scala.collection.mutable.Map.empty
  private val labelMap: scala.collection.mutable.Map[(Int, Int), javafx.scene.control.Label] = scala.collection.mutable.Map.empty
  var wordStartPositions: Map[String, (Int, Int, Boolean)] = Map()

  def selectWords(): Seq[String] = {
    if (_selectedWords.isEmpty) {
      _selectedWords = Random.shuffle(wordsPool).take(3)
      println(s"Selected words for the game: ${_selectedWords.mkString(", ")}")
    }
    _selectedWords
  }
  def getSelectedWords: Seq[String] = _selectedWords

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
//          label.textFill = Color.Red
          val targetRow = startRow
          val targetColumn = (startColumn + k) % columns // Place each letter in a consecutive column
          gameGrid.add(label, targetColumn, targetRow)
          labelMap((targetRow, targetColumn)) = label
          label.setFont(new Font(30))
          javafx.scene.layout.GridPane.setHalignment(label, javafx.geometry.HPos.CENTER)
          wordStartPositions += (word -> (startRow, startColumn, true))

          val rectangle = new Rectangle {
            width <== gameGrid.width / columns
            height <== gameGrid.height / rows
            fill = Color.Transparent
            userData = (targetRow, targetColumn, letter)
          }

          rectangle.setOnMousePressed((event: MouseEvent) => {
            rectangle.fill = Color.Blue.opacity(0.1)
            val (row, col, clickedLetter) = rectangle.userData.asInstanceOf[(Int, Int, String)]
            clickHandler(clickedLetter, row, col)
          })

          rectangle.setOnMouseDragged((event: MouseEvent) => {
            rectangle.fill = Color.Blue.opacity(0.1)
          })

          rectangle.setOnMouseReleased((event: MouseEvent) => {
            rectangle.fill = Color.Transparent
          })
          gameGrid.add(rectangle, targetColumn, targetRow)

          rectangleMap((targetRow, targetColumn)) = rectangle
        }
      } else {
        // Try placing the word again
        placeWord(word, rows, columns, attempts - 1)
      }
    }
  }

  def alphabetInserts(rows: Int, columns: Int): Unit = {
    val randomSelectedWords = selectWords()
    val alphabetPool = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"

    for (word <- randomSelectedWords) {
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
          userData = (i, j, randomAlphabet)
        }

        rectangle.setOnMousePressed((event: MouseEvent) => {
          rectangle.fill = Color.Blue.opacity(0.1)
          val (row, col, clickedLetter) = rectangle.userData.asInstanceOf[(Int, Int, String)]
          clickHandler(clickedLetter, row, col)
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

  def changeLabelStyle(row: Int, col: Int, color: Color): Unit = {
    labelMap.get((row, col)).foreach { label =>
      label.setTextFill(color)
      label.setFont(Font.font("Arial", FontWeight.Bold, 30))
    }
  }

  def wordFound(word: String): Unit = {
    foundWords += word
  }

  def isWordFound(word: String): Boolean = {
    foundWords.contains(word)
  }


  def hintHighlight(row: Int, col: Int): Unit = {
    labelMap.get((row, col)).foreach { label =>
      label.setTextFill(Color.Orange)
    }
  }

  def showHint(): Unit = {
    hintSystem.showHint()
  }

  def resetGame(): Seq[String] = {
    _selectedWords = Seq.empty

    gameGrid.children.clear()

    rectangleMap.clear()
    labelMap.clear()

    foundWords = Set.empty

    alphabetInserts(rows, columns)
    val newWords = selectWords()

    hintSystem match {
      case hint: LetterHint =>
        hint.resetHints()
      case _ =>
    }

    newWords
  }



}