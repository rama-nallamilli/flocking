package flocking

import math.Vector2
import org.scalatest.{FunSuite, Matchers}

class FlockingSpec extends FunSuite with Matchers {

  test("withinDistance") {
    val a = Entity(id = 1, position = Vector2(1, 1), velocity = Vector2.ZeroVector)
    val b = Entity(id = 2, position = Vector2(1, 3), velocity = Vector2.ZeroVector)
    FlockingRules.withinDistance(a, b, 1) shouldBe true
    FlockingRules.withinDistance(a, b, 1.99) shouldBe true
    FlockingRules.withinDistance(a, b, 2.1) shouldBe false
  }

}
