/**
 * @author Kalculos Hub
 * @apiNote using kalculos dash events
 * @since <a href="https://github.com/kalculos/dash/">dash repo</a>
 */
module dash.core.api {
    exports io.ib67.dash.event.handler;
    exports io.ib67.dash.event;
    exports io.ib67.dash.event.bus;

    requires kiwi.core;
    requires lombok;
    requires org.jetbrains.annotations;
    requires com.github.spotbugs.annotations;
}