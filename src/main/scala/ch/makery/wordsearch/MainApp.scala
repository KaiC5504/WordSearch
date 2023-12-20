package ch.makery.wordsearch


import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.Includes._
import scalafxml.core.{FXMLLoader, NoDependencyResolver}
import javafx.{scene => jfxs}
import scalafx.scene.image.Image
import scalafx.stage.{Modality, Stage}

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
    width = 1000
    height = 780
  }

  def showHome(): Unit = {
    val resource = getClass.getResource("view/Home.fxml")
    val loader = new FXMLLoader(resource, NoDependencyResolver)
    loader.load();
    val roots = loader.getRoot[jfxs.layout.AnchorPane]
    this.roots.setCenter(roots)
  }

  def closeApp(): Unit = {
    stage.close()
  }

  def showTutorial(): Unit = {
    val resource = getClass.getResourceAsStream("view/Tutorial.fxml")
    val loader = new FXMLLoader(null, NoDependencyResolver)
    loader.load(resource);
    val roots2 = loader.getRoot[jfxs.Parent]

    val tutorial = new Stage() {
      initModality(Modality.ApplicationModal)
      initOwner(stage)
      title = "Tutorial"
      icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
      scene = new Scene() {
        root = roots2
      }
      width = 900
      height = 680
    }

    tutorial.showAndWait()
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