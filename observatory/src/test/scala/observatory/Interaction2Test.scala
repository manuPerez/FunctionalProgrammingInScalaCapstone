package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Interaction2Test extends FunSuite with Checkers {

  val scaleDeviations: Seq[(Double, Color)] =
    Seq((7.0, Color(0,0,0)),(4.0, Color(255,0,0)),(2.0, Color(255,255,0)),
      (0.0, Color(255,255,255)),(-2.0, Color(0,255,255)),(-7.0, Color(0,0,255)))

  test("layerUrlPattern Test") {
    val selectedLayer = Signal(Layer(LayerName.Temperatures, scaleDeviations, Range(1975,19912)))
    val selectedYear = Signal(1987)

    assert(Interaction2.layerUrlPattern(selectedLayer, selectedYear)() == "target/temperatures/1987/")
  }

  test("yearSelection Test: Error línea 33 errores6") {
    val selectedLayer = Signal(Layer(LayerName.Temperatures, scaleDeviations, Range(1975,1992)))
    val selectedYearOk = Signal(1987)
    val selectedYearMinus = Signal(1974)
    val selectedYearMaximus = Signal(1992)
    assert(Interaction2.yearSelection(selectedLayer, selectedYearOk)() == 1987)
    assert(Interaction2.yearSelection(selectedLayer, selectedYearMinus)() == 1975)
    assert(Interaction2.yearSelection(selectedLayer, selectedYearMaximus)() == 1991)
  }

  test("caption Test: Error línea 37 errores6") {
    val selectedLayer = Signal(Layer(LayerName.Temperatures, scaleDeviations, Range(1975,19912)))
    val selectedYear = Signal(1987)

    assert(Interaction2.caption(selectedLayer, selectedYear)() == "Temperatures(1987)")
  }
}
