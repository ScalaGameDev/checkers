package checkers.userinterface.chrome

import checkers.consts._
import checkers.core.{ApplicationCallbacks, GameModelReader, PlayerDescription}
import checkers.util.Formatting
import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.prefix_<^._

object TopChrome {

  case class Props(gameModel: GameModelReader,
                   widthPixels: Int,
                   heightPixels: Int,
                   applicationCallbacks: ApplicationCallbacks)

}

class TopChrome(playerPanel: PlayerPanel) {
  import TopChrome._

  class TopChromeBackend($: BackendScope[Props, Unit]) {

    def render(props: Props): ReactElement = {
      val panelWidth = props.widthPixels / 2
      val panelHeight = props.heightPixels

      def createPanelProps(side: Side, player: PlayerDescription) = {
        val model = props.gameModel
        val isPlayerTurn = model.displayTurnToMove == side
        val waitingForMove = model.inputPhase.waitingForMove
        val endingTurn = model.inputPhase.endingTurn
        val jumpIndicator = isPlayerTurn && (!endingTurn) && model.playerMustJump
        val scoreDisplay = if(model.scoreDisplayEnabled) {
          Some(model.getScore(side).toString)
        } else None

        val computerThinking = waitingForMove && player.isComputer && isPlayerTurn
        val clock = model.playerClock(side)
        val clockDisplay = Formatting.clockDisplay(clock)

        PlayerPanel.Props(
          widthPixels = panelWidth,
          heightPixels = panelHeight,
          side = side,
          playerName = player.displayName,
          isComputerPlayer = player.isComputer,
          clockDisplay = clockDisplay,
          scoreDisplay = scoreDisplay,
          isPlayerTurn = isPlayerTurn,
          waitingForMove = waitingForMove,
          endingTurn = endingTurn,
          jumpIndicator = jumpIndicator,
          thinkingIndicator = computerThinking,
          rushButtonEnabled = computerThinking,
          applicationCallbacks = props.applicationCallbacks
        )
      }

      def makePanel(panelProps: PlayerPanel.Props, translateX: Int, translateY: Int) = {
        <.svg.g(
          ^.svg.transform := s"translate($translateX,$translateY)",
          playerPanel.component(panelProps)
        )
      }

      val darkProps = createPanelProps(DARK, props.gameModel.darkPlayer)
      val darkPanel = makePanel(darkProps, 0, 0)

      val lightProps = createPanelProps(LIGHT, props.gameModel.lightPlayer)
      val lightPanel = makePanel(lightProps, panelWidth, 0)

      <.svg.svg(
        ^.id := "top-chrome",
        ^.svg.width := s"${props.widthPixels}px",
        ^.svg.height := s"${props.heightPixels}px",
        //^.onMouseMove ==> handleMouseMove,
        //SceneFrame((props._1, props._2, state))
        darkPanel,
        lightPanel
      )
    }
  }

  val component = ReactComponentB[Props]("TopChrome")
    .renderBackend[TopChromeBackend]
    //    .shouldComponentUpdateCB { case ShouldComponentUpdate(scope, nextProps, _) =>
    //      val result = scope.props != nextProps
    //      CallbackTo.pure(result)
    //    }
    .build

}