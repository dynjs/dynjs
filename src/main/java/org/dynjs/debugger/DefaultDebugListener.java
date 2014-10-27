package org.dynjs.debugger;

import org.dynjs.debugger.events.Event;

/**
 * @author Bob McWhirter
 */
public class DefaultDebugListener implements DebugListener {

    @Override
    public void on(Event event) {
        if ( event.getEvent().equals( "break" ) ) {
            System.err.println( "auto-continue" );
            event.getDebugger().CONTINUE( Debugger.StepAction.NEXT );
        }
    }
}
