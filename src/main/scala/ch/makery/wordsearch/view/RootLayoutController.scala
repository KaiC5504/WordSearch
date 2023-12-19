package ch.makery.wordsearch.view

import ch.makery.wordsearch.MainApp
import scalafxml.core.macros.sfxml

@sfxml
class RootLayoutController() {

  def handleHome(): Unit = {
    MainApp.showHome()
  }

  def handleClose(): Unit = {
    System.exit(0)
  }

  def handleTutorial(): Unit = {
    MainApp.showTutorial()
  }
}