package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers


@RunWith(classOf[JUnitRunner])
class InteractionTest extends FunSuite with Checkers {

  import observatory.Interaction._

  test("Tile Location 1") {
    val zoom = 3
    val x = 0
    val y = 0

    assert(tileLocation(zoom, x, y) == Location(85.05112877980659, -180))
  }

}
