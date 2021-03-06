/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc. and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

/*
 * 15/07/13 - Change notice:
 * This file has been modified by Mobius Software Ltd.
 * For more information please visit http://www.mobius.ua
 */
package ua.mobius.media.server.spi;

import java.io.IOException;
import ua.mobius.media.server.utils.Text;



/**
 *
 * @author Yulian Oifa
 * @author amit bhayani
 */
public interface Connection {
    /**
     * Gets the identifier of this connection.
     *
     * @return integer.
     */
    public int getId();
    
    /**
     * Gets the identifier of this connection.
     *
     * @return hex view of the integer.
     */
    public String getTextualId();
    
    /**
     * Gets whether connection should be bound to local or remote interface , supported only for rtp connections.
     *
     * @return boolean value
     */
    public boolean getIsLocal();
    
    /**
     * Gets whether connection should be bound to local or remote interface , supported only for rtp connections.
     *
     * @return boolean value
     */
    public void setIsLocal(boolean isLocal);
    
    /**
     * Returns state of this connection
     * @return
     */
    public ConnectionState getState();

    /**
     * Gets the current mode of this connection.
     *
     * @return integer constant indicating mode.
     */
    public ConnectionMode getMode();

    /**
     * Modify mode of this connection for all known media types.
     * 
     * @param mode the new mode of the connection.
     */
    public void setMode(ConnectionMode mode) throws ModeNotSupportedException;
    
    /**
     * Sets the endpoint which executes this connection.
     *
     * @param the endpoint object.
     */
    public void setEndpoint(Endpoint endpoint);
    
    /**
     * Gets the endpoint which executes this connection.
     *
     * @return the endpoint object.
     */
    public Endpoint getEndpoint();

    /**
     * Gets the descriptor of this connection in SDP format.
     * 
     * @return SDP descriptor as text string.
     */
    public String getDescriptor();    
    
    /**
     * Joins endpoint wich executes this connection with other party.
     *
     * @param other the connection executed by other party endpoint.
     * @throws IOException
     */
    public void setOtherParty(Connection other) throws IOException;

    /**
     * Joins endpoint which executes this connection with other party.
     *
     * @param descriptor the SDP descriptor of the other party.
     * @throws IOException
     */
    public void setOtherParty(byte[] descriptor) throws IOException;

    /**
     * Joins endpoint which executes this connection with other party.
     *
     * @param descriptor the SDP descriptor of the other party.
     * @throws IOException
     */
    public void setOtherParty(Text descriptor) throws IOException;
    
    /**
     * Adds connection state listener.
     * 
     * @param listener to be registered
     */
    public void addListener(ConnectionListener listener);

    /**
     * Sets connection failure listener.
     * 
     * @param listener to be registered
     */
    public void setConnectionFailureListener(ConnectionFailureListener connectionFailureListener);
    
    /**
     * Removes connection state listener.
     * 
     * @param listener to be unregistered
     */
    public void removeListener(ConnectionListener listener);

    /**
     * The number of packets of the specified media type received .
     * 
     * @param media the media type.
     * @return the number of packets.
     */
    public long getPacketsReceived();
    
    /**
     * The total number of bytes  received .
     * 
     * @return the number of bytes.
     */
    public long getBytesReceived();
    
    /**
     * The number of packets of the specified media type transmitted.
     * 
     * @param media the media type 
     * @return the number of packets.
     */
    public long getPacketsTransmitted();

    /**
     * The total number of bytes transmitted.
     * 
     * @return the number of bytes.
     */
    public long getBytesTransmitted();
    
    /**
     * The average jitter value accross all media types.
     * 
     * @return average jitter value. 
     */
    public double getJitter();
    
}
