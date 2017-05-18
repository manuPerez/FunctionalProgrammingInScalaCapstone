package observatory

import observatory.Visualization.predictTemperature
import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

import scala.collection.mutable

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers {

  import observatory.Manipulation._

  val r = scala.util.Random
  val a = scala.util.Random

  val latitudes = Array[Int](70, 30, -30, -70)
  val longitudes = Array[Int](-160, -118, -76, -34, 34, 76, 118, 160)

  var temperaturess = Seq[scala.collection.mutable.Map[Location, Double]]()

  for(i <- 0 to 3) {
    var grid = scala.collection.mutable.Map[Location, Double]()
    for (lat <- 0 to 3; lon <- 0 to 7) {
      grid += (Location(latitudes(lat), longitudes(lon)) -> (r.nextDouble() + a.nextInt(30)))
    }
    val temps: Seq[mutable.Map[Location, Double]] = Seq(grid)
    val s = temperaturess ++ temps
    temperaturess = s
  }

//  test("makeGrid 1") {
//    assert(makeGrid(temperatures = ))
//  }
//  test("Average 1") {
//    assert(average(temperaturess)(70, -160) == 13.508901166359683)
//  }

  test("Error línea 33") {
//    val allYearTemperatures: Seq[(Int, Iterable[(Location, Double)])] = for{
//      year <- 1975 until 2016
//    } yield (year, Extraction.locationYearlyAverageRecords(
//      Extraction.locateTemperatures(year, "stations.csv", s"$year.csv")))
//
//    val test: Seq[(Int, Double)] = allYearTemperatures.map(t => (t._1, predictTemperature(t._2, Location(90.0, -180.0))))
//
//    test.foreach(v => println(v._1 + " -> " + v._2))

    assert(1 == 1)
  }

//  test("Deviation 1") {
//    assert(deviation(temperatures, average(temperaturess))(33, 36) == 2.411295273173711)
//  }
}