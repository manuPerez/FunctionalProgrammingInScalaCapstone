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
    assert(bilinearInterpolation(0.3, 0.5, 50, 40, 30, 20) == 37.0)
  }

  test("bilinearInterpolation test 2") {
    assert(bilinearInterpolation(0.0, 0.1, -31.69261799687654, 7.751776416394094, 41.94130652965798, 1.0) == -24.329225544223085)
  }

}
