package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class Visualization2Test extends FunSuite with Checkers {

  import observatory.Visualization2._

  val colors =
    List((7.0, Color(0, 0, 0)), (4.0, Color(255, 0, 0)),
      (2.0, Color(255, 255, 0)), (0.0, Color(255, 255, 255)),
      (-2.0, Color(0, 255, 255)), (-7.0, Color(0, 0, 255)))

  test("bilinearInterpolation test 1") {
    assert(bilinearInterpolation(0.3, 0.5, 50, 40, 30, 20) == 39.0)
  }

  test("bilinearInterpolation test 2") {
    assert(bilinearInterpolation(0.0, 0.1, -31.69261799687654, 7.751776416394094, 41.94130652965798, 1.0) == -27.748178555549476)
  }

  test("Error línea 57") {
    assert(bilinearInterpolation(0.0, 0.1, -50.0, -50.0, 50.0, -1.0) == -50.0)
  }

  test("Error línea 53") {
    assert(bilinearInterpolation(0.0, 0.1, -29.400384576878114, 47.380195147158645, 0.0, -7.594489720011424) == -21.722326604474436)
  }

  test("Error línea 53.2") {
    assert(bilinearInterpolation(0.0, 0.1, 20.31829387861326, 15.152874098566684, 32.45399641613888, 43.059024771503886) == 19.801751900608604)
  }

}
