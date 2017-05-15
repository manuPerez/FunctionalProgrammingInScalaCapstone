package observatory
import com.sksamuel.scrimage.Image

import scala.collection.immutable

object Main extends App {

//  val colors: Seq[(Double, Color)] =
//    Seq((60.0, Color(255,255,255)),(32.0, Color(255,0,0)),(12.0, Color(255,255,0)),
//      (0.0, Color(0,255,255)),(-15.0, Color(0,0,255)),(-27.0, Color(255,0,255)),
//      (-50.0, Color(33,0,107)),(-60, Color(0,0,0)))
//
//  println(Visualization.interpolateColor(List((0.0,Color(255,0,0)), (2.147483647E9,Color(0,0,255))), 5.3687091175E8))
//
//  Visualization.visualize(temperatures1975, colors)

  val allYearTemperatures: Seq[(Int, Iterable[(Location, Double)])] = for{
    year <- 1978 until 1990
  } yield (year, Extraction.locationYearlyAverageRecords(
    Extraction.locateTemperatures(year, "stations.csv", s"$year.csv")))

  Interaction.generateTiles(allYearTemperatures, Interaction.generateImage)

//  val grid: (Int, Int) => Double = Manipulation.makeGrid(temperatures1975)
//
//  println(grid(0, 180))


  //antes, generar allYearTemperatures para el rango 1975-1989
//  val allTemperatures: Seq[Iterable[(Location, Double)]] = allYearTemperatures.map(v => v._2)

//  val normals: (Int, Int) => Double =
//    Manipulation
//      .average(allTemperatures)
//
//  val allDeviations: Seq[(Int, (Int, Int) => Double)] = for {
//    year <- 1990 until 2016
//    temperatures <- allYearTemperatures.filter(t => t._1 == year).map(t => t._2)
//  } yield (year, Manipulation.deviation(temperatures, normals))

  //antes, cambiar Interaction.generateImage para que genere el directorio /deviations
//  Interaction.generateTiles(allDeviations, Interaction.generateImage)

//  println(deviation1991(90, -180))

//  val colors: Seq[(Double, Color)] =
//      Seq((7.0, Color(0,0,0)),(4.0, Color(255,0,0)),(2.0, Color(255,255,0)),
//        (0.0, Color(255,255,255)),(-2.0, Color(0,255,255)),(-7.0, Color(0,0,255)))
//
//  val img: Image = Visualization2.visualizeGrid(grid, colors, 2, 3, 2)
//  val ouputFile = "target/prueba.png"
//  img.output(ouputFile)

}
