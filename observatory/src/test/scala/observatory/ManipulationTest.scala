package observatory

import org.junit.runner.RunWith
import org.scalatest.FunSuite
import org.scalatest.junit.JUnitRunner
import org.scalatest.prop.Checkers

@RunWith(classOf[JUnitRunner])
class ManipulationTest extends FunSuite with Checkers {

  import observatory.Manipulation._
  val temperaturess = Seq(
    Seq((Location(65,27),2.2084481175390263),
      (Location(17,121),26.838888888888896),
      (Location(31,-91),20.520833333333336),
      (Location(51,1),11.393260473588345),
      (Location(69,18),3.1562308164518122),
      (Location(50,-104),2.806318681318681),
      (Location(33,36),16.27501534683855),
      (Location(52,24),6.011574074074074),
      (Location(50,-128),4.825800376647835),
      (Location(67,-170),-6.040903540903541)),
    Seq((Location(65,27),1.084481175390263),
      (Location(17,121),16.38888888888896),
      (Location(31,-91),18.20833333333336),
      (Location(51,1),10.93260473588345),
      (Location(69,18),0.562308164518122),
      (Location(50,-104),1.06318681318681),
      (Location(33,36),13.7501534683855),
      (Location(52,24),4.574074074074),
      (Location(50,-128),1.25800376647835),
      (Location(67,-170),-7.40903540903541)),
    Seq((Location(65,27),5.084481175390263),
      (Location(17,121),20.38888888888896),
      (Location(31,-91),13.20833333333336),
      (Location(51,1),9.3260473588345),
      (Location(69,18),6.62308164518122),
      (Location(50,-104),4.6318681318681),
      (Location(33,36),10.501534683855),
      (Location(52,24),9.74074074074),
      (Location(50,-128),1.5800376647835),
      (Location(67,-170),-2.0903540903541))
  )

  val temperatures = Seq((Location(65,27),0.2084481175390263),
      (Location(17,121),16.838888888888896),
      (Location(31,-91),10.520833333333336),
      (Location(51,1),5.393260473588345),
      (Location(69,18),1.1562308164518122),
      (Location(50,-104),0.806318681318681),
      (Location(33,36),6.27501534683855),
      (Location(52,24),16.011574074074074),
      (Location(50,-128),14.825800376647835),
      (Location(67,-170),-16.040903540903541))

  test("Average 1") {
    assert(average(temperaturess)(33, 36) == 13.508901166359683)
  }

  test("Average 2") {
    assert(average(temperaturess)(52,24) == 6.775462962962692)
  }

  test("Deviation 1") {
    assert(deviation(temperatures, average(temperaturess))(33, 36) == 2.411295273173711)
  }
}