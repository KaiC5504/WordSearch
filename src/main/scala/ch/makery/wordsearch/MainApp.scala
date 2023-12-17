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

//  val rootResource = getClass.getResource("view/RootLayout.fxml")
//  val loader = new FXMLLoader(rootResource, NoDependencyResolver)
//  loader.load();
//  val roots = loader.getRoot[jfxs.layout.BorderPane]

  stage = new PrimaryStage {
    title = "Word Search"
    icons += new Image(getClass.getResourceAsStream("/images/icon.png"))
  }

}