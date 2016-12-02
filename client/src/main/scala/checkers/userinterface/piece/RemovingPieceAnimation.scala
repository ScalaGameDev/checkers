package checkers.userinterface.piece

import checkers.consts._
import checkers.core.Board
import checkers.util.{Easing, Point}
import japgolly.scalajs.react._

object RemovingPieceAnimation {

  case class Props(piece: Occupant,
                   fromSquare: Int,
                   progress: Double,
                   rotationDegrees: Double)

}

class RemovingPieceAnimation(physicalPiece: PhysicalPiece) {

  import RemovingPieceAnimation._

  class Backend($: BackendScope[Props, Unit]) {
    def render(props: Props): ReactElement = {
      val t = Easing.easeInQuad(props.progress)
      val ptA = startingPoint(props.piece, props.fromSquare)
      val ptB = AnimationHelpers.exitPoint(props.piece, props.fromSquare)

      val x0 = ptA.x
      val x = x0 + t * (ptB.x - x0)

      val y0 = ptA.y
      val y = y0 + t * (ptB.y - y0)

      val physicalPieceProps = PhysicalPieceProps.default.copy(piece = props.piece,
        x = x,
        y = y,
        rotationDegrees = props.rotationDegrees)

      val pieceElement = physicalPiece.create(physicalPieceProps)

      pieceElement
    }
  }

  val create = ReactComponentB[Props]("RemovingPieceAnimation")
    .renderBackend[Backend]
    .shouldComponentUpdateCB { case ShouldComponentUpdate(scope, nextProps, _) =>
      val result = scope.props != nextProps
      CallbackTo.pure(result)
    }
    .build

  private def startingPoint(piece: Occupant, fromSquare: Int): Point = {
    Board.squareCenter(fromSquare)
  }


}