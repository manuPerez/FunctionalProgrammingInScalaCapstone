package observatory


import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class VisualizationTest extends FunSuite with Checkers {

  import observatory.Visualization._

  val scale = List((0.0, Color(0, 0, 255)))

  val colors =
    List((60.0, Color(255, 255, 255)), (32.0, Color(255, 0, 0)),
      (12.0, Color(255, 255, 0)), (0.0, Color(0, 255, 255)),
      (-15.0, Color(0, 0, 255)), (-27.0, Color(255, 0, 255)),
      (-50.0, Color(33, 0, 107)), (-60.0, Color(0, 0, 0)))

  test("Color interpolation 1") {
    assert(interpolateColor(scale, -0.5) == Color(0, 0, 255))
  }

  test("Color interpolation 2") {
    assert(interpolateColor(scale, 0.5) == Color(0, 0, 255))
  }

  test("Color interpolation 3") {
    assert(interpolateColor(scale, 0.0) == Color(0, 0, 255))
  }

  test("Color interpolation 4 (with unsorted scale of colors)") {
    assert(interpolateColor(colors, 12.0) == Color(255, 255, 0))
  }

  test("Color interpolation 5 (with unsorted scale of colors)") {
    assert(interpolateColor(colors, 62) == Color(255, 255, 255))
  }

  test("Color interpolation 6 (with unsorted scale of colors)") {
    assert(interpolateColor(colors, 6) == Color(128, 255, 128))
  }

  test("getDistance test (extreme case 1)") {
    assert(greatCircleDistance(Location(-12.0, 85.0), Location(12.0, -95.0)) == math.Pi)
  }

  test("Get Distance New York - Paris") {
    val newYork = Location(40.70597954587119, -73.9780035)
    val paris = Location(48.858877666233724, 2.3475568999999723)
    assert(greatCircleDistance(newYork, paris) == 0.9159281814139144)
  }

  test("Get Distance London - Paris") {
    val london = Location(51.528868434293244, -0.10161819999996169)
    val paris = Location(48.858877666233724, 2.3475568999999723)
    assert(greatCircleDistance(london, paris) == 0.05403395241540504)
  }

  test("Predict Temperature 1") {
    val temperatures: Seq[(Location, Double)] = Seq(
      /*roma*/(Location(41.91022566604198, 12.535997900000098), 23.0),
      /*madrid*/(Location(40.43807216375375, -3.6795366500000455), 18.0),
      /*new york*/(Location(40.70597954587119, -73.9780035), -12.0),
      /*buenos aires*/(Location(-34.61578211494823, -58.43332029999999), -5.0),
      /*sidney*/(Location(-33.848716732561876, 150.93196309999996), -6.0),
      /*berlín*/(Location(52.50786264022465, 13.426145399999996), 14),
      /*londres*/(Location(51.528868434293244, -0.10161819999996169), 9),
      /*bruselas*/(Location(50.85501203497024, 4.375389949999999), 0.0))

    assert(predictTemperature(temperatures,
      Location(48.858877666233724, 2.3475568999999723)) == 5.009121275404119)
  }

  test("Predict Temperature 2") {
    val temperatures: Seq[(Location, Double)] = Seq(
      /*roma*/(Location(41.91022566604198, 12.535997900000098), 23.0),
      /*madrid*/(Location(40.43807216375375, -3.6795366500000455), 18.0),
      /*new york*/(Location(40.70597954587119, -73.9780035), -12.0),
      /*buenos aires*/(Location(-34.61578211494823, -58.43332029999999), -5.0),
      /*sidney*/(Location(-33.848716732561876, 150.93196309999996), -6.0),
      /*berlín*/(Location(52.50786264022465, 13.426145399999996), 14),
      /*londres*/(Location(51.528868434293244, -0.10161819999996169), 9),
      /*bruselas*/(Location(50.85501203497024, 4.375389949999999), 0.0))

    assert(predictTemperature(temperatures,
      Location(37.75771992816863, -122.43760000000003)) == -0.48818243654428684)
  }

  test("Predict Temperature 3") {
    val temperatures: Seq[(Location, Double)] = Seq(
      /*roma*/(Location(41.91022566604198, 12.535997900000098), 23.0),
      /*madrid*/(Location(40.43807216375375, -3.6795366500000455), 18.0),
      /*new york*/(Location(40.70597954587119, -73.9780035), -12.0),
      /*buenos aires*/(Location(-34.61578211494823, -58.43332029999999), -5.0),
      /*sidney*/(Location(-33.848716732561876, 150.93196309999996), -6.0),
      /*berlín*/(Location(52.50786264022465, 13.426145399999996), 14),
      /*londres*/(Location(51.528868434293244, -0.10161819999996169), 9),
      /*bruselas*/(Location(50.85501203497024, 4.375389949999999), 0.0))

    assert(predictTemperature(temperatures,
      Location(0.0, 0.0)) == 10.307879596080703)
  }

  test("getDistance test (extreme case 2)") {
    assert(greatCircleDistance(Location(0.0, 0.0), Location(0.0, 0.0)) == 0.0)
  }

  test("Error línea 93") {
    val temperatures: Seq[(Location, Double)] = Seq(
            /*roma*/(Location(41.91022566604198, 12.535997900000098), 23.0),
            /*madrid*/(Location(40.43807216375375, -3.6795366500000455), 18.0),
            /*new york*/(Location(40.70597954587119, -73.9780035), -12.0),
            /*buenos aires*/(Location(-34.61578211494823, -58.43332029999999), -5.0),
            /*sidney*/(Location(-33.848716732561876, 150.93196309999996), -6.0),
            /*berlín*/(Location(52.50786264022465, 13.426145399999996), 14),
            /*londres*/(Location(51.528868434293244, -0.10161819999996169), 9),
            /*bruselas*/(Location(50.85501203497024, 4.375389949999999), 0.0),
            (Location(0.0, 0.0), -3.0))

    println(predictTemperature(temperatures, Location(0.0, 0.0)))

    assert(predictTemperature(temperatures,
      Location(0.0, 0.0)) == -3.0)
  }

  test("Error línea 70 de errores4") {
    val temperatures = Seq((Location(-7.0, -1.0), 11.0), (Location(77.0, -160.0), 18.0))

    assert(predictTemperature(temperatures,
      Location(90.0, -180.0)) == 17.87648778450616)
  }
}
