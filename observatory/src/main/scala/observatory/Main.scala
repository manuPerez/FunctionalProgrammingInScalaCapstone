package observatory
import com.sksamuel.scrimage.Image

import scala.collection.immutable

object Main extends App {

  val temperatures: Iterable[(Location, Double)] =
    Extraction.locationYearlyAverageRecords(Extraction.locateTemperatures(1975, "stations.csv", "1975.csv"))

//  println(result.size)
//
//  result.take(10).foreach(t => println(t))
//
//  val colors: Seq[(Double, Color)] =
//    Seq((60.0, Color(255,255,255)),(32.0, Color(255,0,0)),(12.0, Color(255,255,0)),
//      (0.0, Color(0,255,255)),(-15.0, Color(0,0,255)),(-27.0, Color(255,0,255)),
//      (-50.0, Color(33,0,107)),(-60, Color(0,0,0)))
//
//  println(Visualization.interpolateColor(List((0.0,Color(255,0,0)), (2.147483647E9,Color(0,0,255))), 5.3687091175E8))
//
//  Visualization.visualize(temperatures, colors)

  Interaction.generateTiles(Seq((1975, temperatures)), Interaction.generateImage)
}