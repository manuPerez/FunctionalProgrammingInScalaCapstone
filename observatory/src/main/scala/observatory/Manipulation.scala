package observatory

/**
  * 4th milestone: value-added information
  */
object Manipulation {

  /**
    * @param temperatures Known temperatures
    * @return A function that, given a latitude in [-89, 90] and a longitude in [-180, 179],
    *         returns the predicted temperature at this location
    */
  def makeGrid(temperatures: Iterable[(Location, Double)]): (Int, Int) => Double = {
    import observatory.Visualization._

    (lat: Int, lon: Int) => predictTemperature(temperatures, Location(lat, lon))
  }

  /**
    * @param temperaturess Sequence of known temperatures over the years (each element of the collection
    *                      is a collection of pairs of location and temperature)
    * @return A function that, given a latitude and a longitude, returns the average temperature at this location
    */
  def average(temperaturess: Iterable[Iterable[(Location, Double)]]): (Int, Int) => Double = {

    def avg(ts: Iterable[Double]) = ts.sum.toString.toDouble / ts.size

    (lat: Int, lon: Int) => avg(temperaturess.map(t => t.toMap.find(_._1 == Location(lat, lon)).get._2))

  }

  /**
    * @param temperatures Known temperatures
    * @param normals A grid containing the “normal” temperatures
    * @return A sequence of grids containing the deviations compared to the normal temperatures
    */
  def deviation(temperatures: Iterable[(Location, Double)], normals: (Int, Int) => Double): (Int, Int) => Double = {
    val dev =
      (lat: Int, lon: Int) => {
        val temperature: Double = temperatures.toMap.find(_._1 == Location(lat, lon)).get._2
        val mean: Double = normals(lat, lon)
        val c: Double = Math.pow(temperature - mean, 2)
        val d: Double = 1.00 / (temperatures.size - 1)

        val e = Math.sqrt(d * c)
        e
      }
    dev
  }


}

