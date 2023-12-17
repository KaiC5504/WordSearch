package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import ch.makery.wordsearch.model.GameBoard
import scalafx.Includes._
import javafx.collections.{FXCollections, ObservableList}
import scalafxml.core.macros.sfxml
import scalafx.scene.control.ChoiceBox

@sfxml
class GameDifficultyController(
                              private val gameDifficultyChoiceBox: ChoiceBox[String]
                              ) {
  // Initialize game difficulty choice box
  var difficulty: ObservableList[String] = FXCollections.observableArrayList("Easy", "Medium", "Hard")
  gameDifficultyChoiceBox.setItems(difficulty)

  //default difficulty set to easy
  gameDifficultyChoiceBox.value = "Easy"

  // Handle start button
  def handleStart(): Unit = {

    // Get selected difficulty
    val selectedDifficulty = gameDifficultyChoiceBox.getValue

    GameBoard.setGameBoard(selectedDifficulty)

    MainApp.showGame()
  }

  def handleBack(): Unit = {
    MainApp.showHome()
  }
}
