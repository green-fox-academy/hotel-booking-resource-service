package com.mawsitsit.Service;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.LoggingEvent;
import ch.qos.logback.core.filter.AbstractMatcherFilter;
import ch.qos.logback.core.spi.FilterReply;
import java.util.Arrays;
import java.util.List;

public class StdOutFilter extends AbstractMatcherFilter {

  @Override
  public FilterReply decide(Object event) {
    if (!isStarted())
    {
      return FilterReply.NEUTRAL;
    }

    LoggingEvent loggingEvent = (LoggingEvent) event;

    List<Level> eventsToKeep = Arrays.asList(Level.TRACE, Level.DEBUG, Level.INFO);
    return eventsToKeep.contains(loggingEvent.getLevel()) ? FilterReply.NEUTRAL : FilterReply.DENY;
  }
}