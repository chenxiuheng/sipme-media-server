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
package ua.mobius.media.server.impl.resource.mediaplayer.audio.gsm;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.sound.sampled.AudioFormat.Encoding;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import ua.mobius.media.server.impl.resource.mediaplayer.Track;
import ua.mobius.media.server.spi.format.AudioFormat;
import ua.mobius.media.server.spi.format.Format;
import ua.mobius.media.server.spi.format.FormatFactory;
import ua.mobius.media.server.spi.memory.ByteFrame;
import ua.mobius.media.server.spi.memory.ByteMemory;

import org.apache.log4j.Logger;
/**
 *
 * @author Oifa Yulian
 */
public class GsmTrackImpl implements Track {

    /** GSM Encoding constant used by Java Sound API */
    private final static Encoding GSM_ENCODING = new Encoding("GSM0610");
    
    /** audio stream */
    private transient AudioInputStream stream = null;
    private InputStream inStream;
    private BufferedInputStream bufferedInputStream;
    
    private AudioFormat format;
    private int period = 20;
    private int frameSize;
    private boolean eom;
    private long duration;
    private long timestamp;
    
    private static final Logger logger = Logger.getLogger(GsmTrackImpl.class);
    
    public GsmTrackImpl(URL url) throws UnsupportedAudioFileException, IOException {
    	inStream=url.openStream();
        stream = AudioSystem.getAudioInputStream(inStream);
        bufferedInputStream = new BufferedInputStream(stream);

        format = getFormat(stream);
        if (format == null) {
        	bufferedInputStream.close();
        	stream.close();
        	inStream.close();
            throw new UnsupportedAudioFileException();
        }

        duration = (long)(stream.getFrameLength()/stream.getFormat().getFrameRate() * 1000);
        frameSize = 33;
    }

    public void setPeriod(int period) {
        throw new IllegalArgumentException("Period can not be changed");
    }

    public int getPeriod() {
        return period;
    }

    private AudioFormat getFormat(AudioInputStream stream) {
        Encoding encoding = stream.getFormat().getEncoding();
        if (encoding.equals(GSM_ENCODING)) {
            return FormatFactory.createAudioFormat("gsm", 8000);
        } 
        return null;
    }

    public long getMediaTime() {
        return timestamp;
    }
    
    public void setMediaTime(long timestamp) {
        this.timestamp = timestamp;
        try {
        	bufferedInputStream.reset();
            long offset = frameSize * (timestamp / period);
            bufferedInputStream.skip(offset);
        } catch (IOException e) {
        	logger.error(e);
        }
    }
    
    public long getDuration() {
        return duration;
    }
    
    /**
     * Reads packet from currently opened stream.
     * 
     * @param packet
     *            the packet to read
     * @param offset
     *            the offset from which new data will be inserted
     * @return the number of actualy read bytes.
     * @throws java.io.IOException
     */
    private int readPacket(byte[] packet, int offset, int psize) throws IOException {
        int length = 0;
        try {
            while (length < psize) {
                int len = bufferedInputStream.read(packet, offset + length, psize - length);
                if (len == -1) {
                    return length;
                }
                length += len;
            }
            return length;
        } catch (Exception e) {
        	logger.error(e);
        }
        return length;
    }

    private void padding(byte[] data, int count) {
        int offset = data.length - count;
        for (int i = 0; i < count; i++) {
            data[i + offset] = 0;
        }
    }
    
    public ByteFrame process(long timestamp) throws IOException {
        ByteFrame frame = ByteMemory.allocate(frameSize);
        byte[] data = frame.getData();
        
        int len = readPacket(data, 0, frameSize);
        if (len == 0) {
            eom = true;
        }

        if (len < frameSize) {
            padding(data, frameSize - len);
            eom = true;
        }

        frame.setOffset(0);
        frame.setLength(frameSize);
        frame.setEOM(eom);
        frame.setDuration(period);
        timestamp += period;
        return frame;
    }

    public void close() {
        try {
        	bufferedInputStream.close();
            stream.close();
            inStream.close();
        } catch (Exception e) {
        	logger.error(e);
        }
    }

    public AudioFormat getFormat() {
        return format;
    }
}
