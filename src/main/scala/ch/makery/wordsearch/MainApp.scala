package ch.makery.wordsearch


import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.scene.image.Image

object MainApp extends JFXApp{

  val rootResource = getClass.getResource("view/RootLayout.fxml")
  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
  loader.load();
  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Word Search"
    icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
    scene = new Scene() {
      root = roots
    }
    width = 900
    height = 680
  }

  def showHome(): Unit = {
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showGameDifficulty(): Unit = {
    val resource = getClass.getResource("view/GameDifficulty.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def showGame(): Unit = {
    val resource = getClass.getResource("view/Game.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }
  showHome()
}