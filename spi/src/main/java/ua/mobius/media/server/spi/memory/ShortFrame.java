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
package ua.mobius.media.server.spi.memory;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import ua.mobius.media.server.spi.format.Format;

/**
 *
 * @author yulian oifa
 */
public class ShortFrame {
    private ShortPartition partition;
    private short[] data;

    private volatile int offset;
    private volatile int length;

    private volatile long timestamp;
    private volatile long duration = Long.MAX_VALUE;
    private volatile long sn;

    private volatile boolean eom;
    private volatile Format format;
    private volatile String header;
    
    protected AtomicBoolean inPartition=new AtomicBoolean(false);
    
    protected ShortFrame(ShortPartition partition, short[] data) {
        this.partition = partition;
        this.data = data;
    }

    protected void reset() {
        this.timestamp = 0;
        this.duration = 0;
        this.sn = 0;
        this.eom = false;
    }
    
    public String getHeader() {
        return header;
    }
    
    public void setHeader(String header) {
        this.header = header;
    }
    
    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }
    
    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public short[] getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getSequenceNumber(){
        return sn;
    }

    public void setSequenceNumber(long sn) {
        this.sn = sn;
    }

    public boolean isEOM() {
        return this.eom;
    }

    public void setEOM(boolean value) {
        this.eom = value;
    }

    public Format getFormat() {
        return format;
    }

    public void setFormat(Format format) {
        this.format = format;
    }    

    public void recycle() {
        partition.recycle(this);
    }

    @Override
    public ShortFrame clone() {
        ShortFrame frame = ShortMemory.allocate(data.length);
        System.arraycopy(data, offset, frame.data, offset, length);
        frame.offset = offset;
        frame.length = length;
        frame.duration = duration;
        frame.sn = sn;
        frame.eom = eom;
        frame.format = format;
        frame.timestamp = timestamp;
        frame.header = header;
        return frame;
    }
}
