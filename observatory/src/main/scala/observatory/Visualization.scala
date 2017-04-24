package observatory

import com.sksamuel.scrimage.{Image, Pixel}

/**
  * 2nd milestone: basic visualization
  */
object Visualization {

  val POTENCY = 2
  val LAT = 10000
  val LON = 40000
  val GLAT = 90
  val GLON = 360
  val V_IMG = 65536
  val RGB = 256
  val RADIOUS = 6371 // radious of The Earth
  val SIZE_IMAGE = 360 * 180
  val ALPHA = 127

  def toRad(value: Double): Double = value * Math.PI / 180

  /**
    * Haversine Distance
    * @param loc1 Initial Location
    * @param loc2 Final Location
    * @return distance in kms between loc1 and loc2
    */
  def getDistance(loc1: Location, loc2: Location): Double = {
    val latDistance: Double = toRad(loc2.lat - loc1.lat)
    val lonDistance: Double = toRad(loc2.lon - loc1.lon)

    val a: Double = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) +
            Math.cos(toRad(loc1.lat)) * Math.cos(toRad(loc2.lat)) *
            Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2)

    val c: Double =
      2 * Math.atan2(
        Math.sqrt(a),
        Math.sqrt(1-a).isNaN match {
          case true => 0
          case false => Math.sqrt(1-a)
        })

    val distance: Double = RADIOUS * c

//    if(distance.isNaN)
//      println("stop")

    distance
  }

  /**
    * @param temperatures Known temperatures: pairs containing a location and the temperature at this location
    * @param location Location where to predict the temperature
    * @return The predicted temperature at `location`
    */
  def predictTemperature(temperatures: Iterable[(Location, Double)], location: Location): Double = {

    val min_points = 8

    val points: Iterable[(Double, Double)] =
      temperatures
        .map(
          t => (getDistance(t._1, location), t._2)
        )
        .toList
        .sortBy(a => a._1)
        .take(min_points)

    val results: (Double, Double) = points
      .map(
        v => {
          val inv_dist = 1 / Math.pow(v._1, POTENCY) match {case 0 => 1 case _ => Math.pow(v._1, POTENCY)}
          val inv_dist_z = v._2 * inv_dist
          (inv_dist_z, inv_dist)
        }
      )
      .reduce(
        (v1, v2) => {
          val sum_inv_dist_z = v1._1 + v2._1
          val sum_inv_dist = v1._2 + v2._2

          (sum_inv_dist_z, sum_inv_dist)
        })

    val result = results._1 / (results._2.isNaN match {case true => 1 case false => results._2})

//    if(result.isNaN)
//      println("stop")

    result
  }

  def getAnt(list: List[(Double, Color)], value: Double): (Double, Color) =
    list match {
      case x :: List() if (x._1 > value) => (value, x._2)
      case x :: List() if (x._1 < value) => x
      case x :: xs if (x._1 > value) => (value, x._2)
      case x :: xs if (xs.head._1 > value) => x
      case x :: xs if (xs.head._1 < value) => getAnt(xs, value)
    }

  def getPos(list: List[(Double, Color)], value: Double): (Double, Color) =
    list match {
      case x :: List() if (x._1 < value) => (value, x._2)
      case x :: List() if (x._1 > value) => x
      case x :: xs if (x._1 < value) => getPos(xs, value)
      case x :: xs if (xs.head._1 < value) => getPos(xs, value)
      case x :: xs if (xs.head._1 > value) => x
    }

  /**
    * @param points Pairs containing a value and its associated color
    * @param value The value to interpolate
    * @return The color that corresponds to `value`, according to the color scale defined by `points`
    */
  def interpolateColor(points: Iterable[(Double, Color)], value: Double): Color = {

    val exist: Option[(Double, Color)] = points.find(v => v._1 == value)

    if(!exist.isEmpty)
      exist.get._2
    else {
      val pointsSorted: List[(Double, Color)] = points.toList.sortBy(v => v._1)

      val valueAnt: (Double, Color) = getAnt(pointsSorted, value)
      val valuePos: (Double, Color) = getPos(pointsSorted, value)

      /**
        * Interpolación lineal
        *
        * y = ((x - x1) / (x2 - x1)) * (y2 - y1) + y1
        */
      val red: Int =
      (
        Math.round(
          ((value - valueAnt._1) / (valuePos._1 - valueAnt._1))
            * (valuePos._2.red - valueAnt._2.red)
            + valueAnt._2.red
        )
      ).toInt
      val green: Int =
      (
        Math.round(
          ((value - valueAnt._1) / (valuePos._1 - valueAnt._1))
            * (valuePos._2.green - valueAnt._2.green)
            + valueAnt._2.green
        )
      ).toInt
      val blue: Int =
      (
        Math.round(
          ((value - valueAnt._1) / (valuePos._1 - valueAnt._1))
            * (valuePos._2.blue - valueAnt._2.blue)
            + valueAnt._2.blue
        )
      ).toInt

      Color(red, green, blue)
    }
  }

  /**
    * @param temperatures Known temperatures
    * @param colors Color scale
    * @return A 360×180 image where each pixel shows the predicted temperature at its location
    */
  def visualize(temperatures: Iterable[(Location, Double)], colors: Iterable[(Double, Color)]): Image = {

    val pixels = new Array[Pixel](SIZE_IMAGE)
    var i = 0
//    for(y <- -90 to 89; x <- -180 to 179) {
    for(y <- 90 to -89 by -1; x <- -180 to 179) {
      val color: Color = interpolateColor(colors, predictTemperature(temperatures, Location(y, x)))
      pixels(i) = Pixel(color.red, color.green, color.blue, ALPHA)
      i = i + 1
    }

    val ouputFile = "target/some-image.png"
    val img: Image = Image(360, 180, pixels)
    img.output(ouputFile)
    img
  }

}

