package flocking

import math.Vector2
import org.scalajs.dom
import org.scalajs.dom.html.Canvas

import scala.util.Random
import cats.syntax.semigroup._

case class Entity(id: Int, position: Vector2, velocity: Vector2)

object Flocking {

  val seperationWeight = 1.3
  val cohesionWeight   = 1.0
  val alignmentWeight  = 1.0

  val canvas = {
    val c = dom.document.createElement("canvas").asInstanceOf[Canvas]
    c.width = (0.95 * dom.window.innerWidth).toInt
    c.height = (0.95 * dom.window.innerHeight).toInt
    c
  }

  val ctx = canvas.getContext("2d")

  private var state: Seq[Entity] = createEntities(n = 25)

  def main(args: Array[String]): Unit = {
    dom.document.body.appendChild(canvas)
    dom.window.setInterval(() => renderPositions, 200)
  }

  def renderPositions = {

    state = run(state)

    ctx.clearRect(0, 0, canvas.width, canvas.height)

    state.foreach { v =>
      ctx.beginPath()
      ctx.arc(v.position.x, v.position.y, 2, 0, 2 * Math.PI)
      ctx.stroke()
    }

  }

  import FlockingRules._

  def run(currentState: Seq[Entity]): Seq[Entity] = {

    val updatedEntities = currentState.map { e =>
      val neighbours = findNeighbours(currentState, e)
      val alignment  = calculateAlignment(e, neighbours) * alignmentWeight
      val cohesion   = calculateCohesion(e, neighbours) * cohesionWeight
      val seperation = calculateSeparation(e, neighbours) * seperationWeight

      val speed = 1.0
      val updatedVelocity = (alignment |+| cohesion |+| seperation).normalized * speed
      val updatedPosition = e.position |+| updatedVelocity

      e.copy(velocity = updatedVelocity, position = updatedPosition)
    }

    updatedEntities
  }

  private def createEntities(n: Int): Seq[Entity] = {
    val multiplier = 10
    (1 to n).map { i =>
      Entity(
        id = i,
        position = Vector2(x = Random.nextDouble() * multiplier, y = Random.nextDouble() * multiplier),
        velocity = Vector2(x = Random.nextDouble() * multiplier, y = Random.nextDouble() * multiplier).normalized
      )
    }
  }

}

object FlockingRules {

  def withinDistance(a: Entity, b: Entity, distance: Double) = {
    val directionVector = Vector2(x = a.position.x - b.position.x, y = a.position.y - b.position.y)
    directionVector.magnitude >= distance
  }

  def findNeighbours(currentState: Seq[Entity], target: Entity): Seq[Entity] = {
    val neighbours = currentState.foldLeft(Seq.empty[Entity]) { (acc, entity) =>
      if (entity.id != target.id && withinDistance(target, entity, 50))
        acc.:+(entity)
      else
        acc
    }
    neighbours
  }

  def calculateSeparation(entity: Entity, nearby: Seq[Entity]): Vector2 = {

    //overall direction of nearby
    val (xPosition, yPosition) = nearby.foldLeft((0.0, 0.0)) { (acc, value) =>
      (acc._1 + (value.position.x - entity.position.x), acc._2 + (value.position.y - entity.position.y))
    }

    //negate direction
    Vector2(x = xPosition * -1.0, y = yPosition * -1.0).normalized
  }

  def calculateAlignment(entity: Entity, nearby: Seq[Entity]): Vector2 = {
    nearby.size match {
      case 0 => entity.velocity
      case _ =>
        val (xAlign, yAlign) = nearby.foldLeft((0.0, 0.0)) { (acc, value) =>
          (acc._1 + value.velocity.x, acc._2 + value.velocity.y)
        } //todo combine

        val (xAlignAvg, yAlignAvg) = (xAlign / nearby.size, yAlign / nearby.size)

        Vector2(xAlignAvg, yAlignAvg).normalized
    }

  }

  def calculateCohesion(entity: Entity, nearby: Seq[Entity]): Vector2 = {
    val (xPosition, yPosition) = nearby.foldLeft((0.0, 0.0)) { (acc, entity) =>
      (acc._1 + entity.position.x, acc._2 + entity.position.y)
    }

    val averagePosition = Vector2(xPosition / nearby.size, yPosition / nearby.size)

    //Generate vector that represents direction of Entity -> averagePosition
    val targetDirection = Vector2(x = averagePosition.x - entity.position.x, y = averagePosition.y - entity.position.y)

    targetDirection.normalized
  }
}
