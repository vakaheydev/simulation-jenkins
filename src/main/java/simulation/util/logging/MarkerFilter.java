package simulation.util.logging;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

import java.util.List;

import static ch.qos.logback.classic.Level.ERROR;

public class MarkerFilter extends Filter<ILoggingEvent> {
    @Override
    public FilterReply decide(ILoggingEvent event) {
        Marker markerToMatch = MarkerFactory.getMarker("IMPORTANT");
        List<Marker> markerList = event.getMarkerList();
        Level level = event.getLevel();

        if (level.isGreaterOrEqual(ERROR) || markerList.contains(markerToMatch)) {
            return FilterReply.ACCEPT;
        }

        return FilterReply.DENY;
    }
}
