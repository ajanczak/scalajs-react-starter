import japgolly.scalajs.react.{BackendScope, _}
import japgolly.scalajs.react.vdom.prefix_<^._

import scala.scalajs.js.JSApp

object Main extends JSApp {
  def main(): Unit = {

    val TodoList = ReactComponentB[List[String]]("TodoList")
      .render_P { props =>
        def createItem(itemText: String) = <.li(itemText)
        <.ul(props map createItem)
      }
      .build

    case class State(items: List[String], text: String)

    class Backend($: BackendScope[Unit, State]) {
      def onChange(e: ReactEventI) =
        $.modState(_.copy(text = e.target.value))

      def handleSubmit(e: ReactEventI) =
        e.preventDefaultCB >>
          $.modState(s => State(s.items :+ s.text, ""))

      def render(state: State) =
        <.div(
          <.h3("TODO"),
          TodoList(state.items),
          <.form(^.onSubmit ==> handleSubmit,
            <.input(^.onChange ==> onChange, ^.value := state.text),
            <.button("Add #", state.items.length + 1)
          )
        )
    }

    val TodoApp = ReactComponentB[Unit]("TodoApp")
      .initialState(State(Nil, ""))
      .renderBackend[Backend]
      .buildU

//    val mountNode = "body"
//
//    ReactDOM.render(TodoApp(), mountNode)

  }
}