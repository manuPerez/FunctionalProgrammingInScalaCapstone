package observatory

import com.sksamuel.scrimage.{Image, Pixel}

import observatory.Visualization._
import Math._

/**
  * 5th milestone: value-added information visualization
  */
object Visualization2 {

  /**
    * @param x X coordinate between 0 and 1
    * @param y Y coordinate between 0 and 1
    * @param d00 Top-left value
    * @param d01 Bottom-left value
    * @param d10 Top-right value
    * @param d11 Bottom-right value
    * @return A guess of the value at (x, y) based on the four known values, using bilinear interpolation
    *         See https://en.wikipedia.org/wiki/Bilinear_interpolation#Unit_Square
    */
  def   bilinearInterpolation(
    x: Double,
    y: Double,
    d00: Double,
    d01: Double,
    d10: Double,
    d11: Double
  ): Double = {
    val f1 = ((1 - x) * d00) + (x * d01)
    val f2 = ((1 - x) * d10) + (x * d11)

    val fFinal = ((1 - y) * f1) + (y * f2)
    fFinal
  }

  /**
    * @param grid Grid to visualize
    * @param colors Color scale to use
    * @param zoom Zoom level of the tile to visualize
    * @param x X value of the tile to visualize
    * @param y Y value of the tile to visualize
    * @return The image of the tile at (x, y, zoom) showing the grid using the given color scale
    */
  def visualizeGrid(
    grid: (Int, Int) => Double,
    colors: Iterable[(Double, Color)],
    zoom: Int,
    x: Int,
    y: Int
  ): Image = {

    val imageWidth = 256
    val imageHeight = 256

    val pixels: Array[Pixel] =
        (0 until imageWidth * imageHeight).par.map {
          position =>
            val xPos: Double = (position % imageWidth).toDouble / imageWidth + x
            val yPos: Double = (position / imageHeight).toDouble / imageHeight + y
            val location: Location = tile(zoom, xPos, yPos)

            val (maxLatitude, minLatitude) = (ceil(location.lat), floor(location.lat))
            val (maxLongitude, minLongitude) = (ceil(location.lon), floor(location.lon))

            val d00 = grid(maxLatitude.toInt, minLongitude.toInt)
            val d01 = grid(minLatitude.toInt, minLongitude.toInt)
            val d10 = grid(maxLatitude.toInt, maxLongitude.toInt)
            val d11 = grid(minLatitude.toInt, maxLongitude.toInt)

            val xFraction = location.lon - minLongitude
            val yFraction = maxLatitude - location.lat

            val color = interpolateColor(colors, bilinearInterpolation(xFraction, yFraction, d00, d01, d10, d11))
            Pixel(color.red, color.green, color.blue, alpha = 127)
      }.toArray

      Image(imageHeight, imageWidth, pixels)

  }

  def tile(zoom: Int, x: Double, y: Double): Location = {
    val lat = Math.toDegrees(Math.atan(Math.sinh(Math.PI * (1.0 - 2.0 * y.toDouble / (1<<zoom)))))
    val lon = x.toDouble / (1<<zoom) * 360.0 - 180.0

    Location(lat, lon)
  }

}
