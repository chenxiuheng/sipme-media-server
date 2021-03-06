/*
 * 15/07/13 - Change notice:
 * This file has been modified by Mobius Software Ltd.
 * For more information please visit http://www.mobius.ua
 */
package ua.mobius.javax.media.mscontrol.networkconnection;

import javax.media.mscontrol.EventType;
import javax.media.mscontrol.MediaErr;
import javax.media.mscontrol.Qualifier;
import javax.media.mscontrol.networkconnection.SdpPortManager;
import javax.media.mscontrol.networkconnection.SdpPortManagerEvent;
import javax.media.mscontrol.resource.Trigger;
import ua.mobius.fsm.State;
import ua.mobius.fsm.StateEventHandler;
import ua.mobius.fsm.TransitionHandler;

/**
 * 
 * @author amit.bhayani
 * @author kulikov
 */
public class SdpPortManagerEventImpl implements SdpPortManagerEvent, TransitionHandler, StateEventHandler {

    private SdpPortManagerImpl source = null;
    private EventType eventType = null;
    private boolean isSuccessful = false;

    public SdpPortManagerEventImpl(SdpPortManagerImpl source, EventType eventType) {
        this.source = source;
        this.eventType = eventType;
        this.isSuccessful = true;
    }

    public byte[] getMediaServerSdp() {
        return source.getLocalDescriptor().getBytes();
    }

    public Qualifier getQualifier() {
        // TODO Auto-generated method stub
        return null;
    }

    public Trigger getRTCTrigger() {
        // TODO Auto-generated method stub
        return null;
    }

    public MediaErr getError() {
        return source.connection.error;
    }

    public String getErrorText() {
        return source.connection.errorMsg;
    }

    public EventType getEventType() {
        return this.eventType;
    }

    public SdpPortManager getSource() {
        return this.source;
    }

    public boolean isSuccessful() {
        return this.isSuccessful;
    }
    
    @Override
    public String toString() {
        return eventType.toString();
    }

    public void process(State state) {
        source.fireEvent(this);
    }

    public void onEvent(State state) {
        source.fireEvent(this);
    }
}
