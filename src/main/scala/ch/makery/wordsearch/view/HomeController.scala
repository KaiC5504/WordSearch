package ch.makery.wordsearch.view
import ch.makery.wordsearch.MainApp
import scalafx.Includes._
import scalafxml.core.macros.sfxml

@sfxml
class HomeController {
  def handlePlay():Unit = {
    MainApp.showGameDifficulty()
  }
}