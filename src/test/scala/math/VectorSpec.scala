package math

import cats.syntax.semigroup._
import org.scalatest.{FunSuite, Matchers}

class VectorSpec extends FunSuite with Matchers {

  test("should calculate magnitude of vectors") {
    Vector2(0.3, 0.4).magnitude shouldBe 0.5
    Vector2(10, 20).magnitude shouldBe 22.360679774997898
    Vector2(1, 41.3).magnitude shouldBe 41.31210476361619
    Vector2(2, 2).magnitude shouldBe 2.8284271247461903
    Vector2(2, -2).magnitude shouldBe 2.8284271247461903
  }

  test("should add vectors") {
    Vector2(3, 4) |+| Vector2(10, 3) shouldBe Vector2(13, 7)
    Vector2(-3, 4) |+| Vector2(3, 3) shouldBe Vector2(0, 7)
    Vector2(-3, 4) |+| Vector2(-3, 3) shouldBe Vector2(-6, 7)
    Vector2(1, 1) |+| Vector2(2, 2) shouldBe Vector2(3, 3)
  }

  test("should normalise vectors") {
    val v1 = Vector2(5, 10)
    v1.normalized shouldBe Vector2(5 / v1.magnitude, 10 / v1.magnitude)

    val v2 = Vector2(31, -4)
    v2.normalized shouldBe Vector2(31 / v2.magnitude, -4 / v2.magnitude)

    val v3 = Vector2(-2, -3)
    v3.normalized shouldBe Vector2(-2 / v3.magnitude, -3 / v3.magnitude)

    val v4 = Vector2(0, 0)
    v4.normalized shouldBe Vector2.ZeroVector

  }

}
