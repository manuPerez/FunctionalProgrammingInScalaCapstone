package observatory

import java.time.LocalDate
import org.apache.spark.rdd.RDD
import scala.io.Source._

/**
  * 1st milestone: data extraction
  */
object Extraction {

  import org.apache.spark.sql.SparkSession

  val T_EMPTY: Double = fahrenheitToCelsius(9999.9)
  val numPartitions = 8

  val spark =
    SparkSession
      .builder()
      .appName("Extraction Tempereatures")
      .config("spark.master", "local")
      .getOrCreate()

  def fahrenheitToCelsius(num: Double): Double = ((num - 32)*5)/9
  /**
    * @param year             Year number
    * @param stationsFile     Path of the stations resource file to use (e.g. "/stations.csv")
    * @param temperaturesFile Path of the temperatures resource file to use (e.g. "/1975.csv")
    * @return A sequence containing triplets (date, location, temperature)
    */
  def locateTemperatures(year: Int, stationsFile: String, temperaturesFile: String): Iterable[(LocalDate, Location, Double)] = {

    val inputStations = stationsFile.head match {
      case '/' => getClass.getResourceAsStream(stationsFile)
      case _ => getClass.getClassLoader.getResourceAsStream(stationsFile)
    }

    val inputTemperatures = temperaturesFile.head match {
      case '/' => getClass.getResourceAsStream(temperaturesFile)
      case _ => getClass.getClassLoader.getResourceAsStream(temperaturesFile)
    }

    val allLinesStations: RDD[String] =
      spark.sparkContext
        .makeRDD(fromInputStream(inputStations).getLines().toList)
        .repartition(numPartitions)
        .coalesce(numPartitions)

    val allLinesTemperatures: RDD[String] =
      spark.sparkContext
        .makeRDD(fromInputStream(inputTemperatures).getLines().toList)
        .repartition(numPartitions)
        .coalesce(numPartitions)

    val rddLinesStations: RDD[((String, String), Location)] = allLinesStations
      .map(line => {
        val li = line.split(",")
        if(li.length == 4 && !li(2).isEmpty && !li(3).isEmpty)
          ((li(0), li(1)),Location(li(2).toDouble, li(3).toDouble))
        else
          (("*", "*"), Location(0.0,0.0))
      })
      .filter(t => !t._1._1.equals("*"))

    rddLinesStations.cache()

    val rddLinesTemperatures: RDD[((String, String), (LocalDate, Double))] = allLinesTemperatures
      .map(line => {
        val li: Array[String] = line.split(",")
        if (li.length == 4)
          ((li(0), li(1)), (LocalDate.of(year, li(2).toInt, li(3).toInt), T_EMPTY))
        else
          ((li(0), li(1)), (LocalDate.of(year, li(2).toInt, li(3).toInt), fahrenheitToCelsius(li(4).toDouble)))
      })

    val result: Seq[(LocalDate, Location, Double)] =
      rddLinesTemperatures
        .join(rddLinesStations)
        .mapValues(v => (v._1._1, v._2, v._1._2))
        .groupByKey()
        .mapValues((v => v.toSeq))
        .reduceByKey((v1,v2) => (v1 ++ v2))
        .map(v => v._2)
        .collect()
        .foldLeft(Seq[(LocalDate, Location, Double)]())((a, b) => a ++ b)

    result
  }

  /**
    * @param records A sequence containing triplets (date, location, temperature)
    * @return A sequence containing, for each location, the average temperature over the year.
    */
  def locationYearlyAverageRecords(records: Iterable[(LocalDate, Location, Double)]): Iterable[(Location, Double)] = {

    val recordsRDD: RDD[(LocalDate, Location, Double)] =
      spark.sparkContext
        .parallelize(records.toList)
        .repartition(numPartitions)
        .coalesce(numPartitions)

    val preGrouped: RDD[(String, (Location, Double))] = recordsRDD
      .map(
        x => (x._1.getYear+":"+x._2,(x._2, x._3, 1))
      )
      .reduceByKey(
        (v1,v2) => (v1._1, v1._2 + v2._2, v1._3 + v2._3)
      )
      .map(
        v => (v._1, (v._2._1, v._2._2/v._2._3))
      )

    val result: Iterable[(Location, Double)] =
      preGrouped
        .groupByKey()
        .map(v => v._2)
//        .collect()
//        .foldLeft(Seq[(Location, Double)]())((v1, v2) => v1 ++ v2)
        .reduce((v1, v2) => v1 ++ v2)

    result
  }

}