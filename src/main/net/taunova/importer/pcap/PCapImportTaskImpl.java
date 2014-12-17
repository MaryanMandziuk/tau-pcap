/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 *
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import net.taunova.importer.PCapImportTask;
import net.taunova.importer.pcap.exception.PCapSourceNotFound;
import net.taunova.importer.pcap.exception.PCapInvalidFormat;

/**
 *
 * 
 * 
 * @author Renat Gilmanov
 *
 * http://wiki.wireshark.org/Development/LibpcapFileFormat
 */
class PCapImportTaskImpl implements PCapImportTask {

    protected final int BE_MAGIC = 0xa1b2c3d4;
    protected final int LE_MAGIC = 0xd4c3b2a1;

    /*
     * magic_number: used to detect the file format itself and the byte ordering.
     *               The writing application writes 0xa1b2c3d4 with it's native
     *               byte ordering format into this field. The reading application
     *               will read either 0xa1b2c3d4 (identical) or 0xd4c3b2a1 (swapped).
     * 
     * version_major, version_minor: the version number of this file format
     *               (current version is 2.4)
     * 
     * thiszone: the correction time in seconds between GMT (UTC) and the local
     *               timezone of the following packet header timestamps.
     * 
     * sigfigs: in theory, the accuracy of time stamps in the capture;
     *               in practice, all tools set it to 0
     * 
     * snaplen: the "snapshot length" for the capture (typically 65535 or even more,
     *                but might be limited by the user), see: incl_len vs. orig_len below
     * 
     * network: data link layer type (e.g. 1 for Ethernet, see wiretap/libpcap.c or
     *               libpcap's pcap-bpf.h for details), this can be various types
     *               like Token Ring, FDDI, etc.
     */
    protected short versionMajor    = 0;
    protected short versionMinor    = 0;

    protected int magicNumber       = 0;
    protected int zoneCorrection    = 0;
    protected int accuracy          = 0;
    protected int snapshotLength    = 0;
    protected int datalinkType      = 0;

    protected boolean convert = false;

    protected File file = null;
    protected PCapEventHandler handler = null;

    private DataInputStream inputStream;
    private boolean importFinished = false;

    /**
     * 
     * @param file
     * @param handler 
     */
    public PCapImportTaskImpl(File file, PCapEventHandler handler) {
        this.file = file;
        this.handler = handler;       
    }

    /**
     * This method also tries to parse a PCap header in order to make sure 
     * proper has been supplied.
     * 
     * @throws net.taunova.importer.pcap.exception.PCapSourceNotFound
     * @throws Exception
     */
    @Override
    public void init() throws PCapSourceNotFound, PCapInvalidFormat {
        try{
            inputStream = new DataInputStream(new FileInputStream(file));
        }catch(FileNotFoundException e) {
            throw new PCapSourceNotFound(e);
        }
        decodeHeader(inputStream);
        handler.onImportStart();

        try {
            processNext();
        } catch (Exception ex) {
            throw new PCapInvalidFormat(ex);
        }
    }

    /**
     *
     * @throws IOException
     */
    @Override
    public void processNext() throws PCapInvalidFormat {
        try {
            decodeEntry(inputStream);
        } catch (EOFException e) {
            importFinished = true;
            handler.onImportFinish();
        } catch (PCapInvalidFormat ex) {
            handler.onImportFailed();
            throw ex;
        }
    }

    /**
     * 
     * @return
     */
    @Override
    public boolean isFinished() {
        return importFinished;
    }

    /**
     *  guint32 magic_number;    magic number
     *  guint16 version_major;   major version number
     *  guint16 version_minor;   minor version number
     *  gint32  thiszone;        GMT to local correction
     *  guint32 sigfigs;         accuracy of timestamps
     *  guint32 snaplen;         max length of captured packets, in octets
     *  guint32 network;         data link type
     */
    protected void decodeHeader(DataInputStream dataInput) throws PCapInvalidFormat {
        try {
            magicNumber = dataInput.readInt();

            if (magicNumber != BE_MAGIC && magicNumber != LE_MAGIC) {
                throw new PCapInvalidFormat("Wrong pcap magic: " + Integer.toHexString(magicNumber));
            } else if (magicNumber == LE_MAGIC) {
                convert = true;
            }

            versionMajor   = (convert) ? PCapHelper.convert(dataInput.readShort()) : dataInput.readShort();
            versionMinor   = (convert) ? PCapHelper.convert(dataInput.readShort()) : dataInput.readShort();
            zoneCorrection = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            accuracy       = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            snapshotLength = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            datalinkType   = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();

            handler.handleInfo(new PCapVersion(versionMajor, versionMinor), 
                    new PCapDatalink(datalinkType));
        } catch (IOException ex) {
            throw new PCapInvalidFormat(ex);
        }
    }

    /**
     * guint32 ts_sec;     timestamp seconds
     * guint32 ts_usec;    timestamp microseconds
     * guint32 incl_len;   number of octets of packet saved in file
     * guint32 orig_len;   actual length of packet
     */
    protected void decodeEntry(DataInputStream dataInput) throws PCapInvalidFormat, EOFException {
        try {
            int seconds      = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            int microseconds = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            int savedLength  = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();
            int actualLength = (convert) ? PCapHelper.convert(dataInput.readInt()) : dataInput.readInt();

            long timestamp   = ((long) seconds) * 1000000 + microseconds;
            handler.handleEntity(savedLength, actualLength, timestamp, dataInput);
        } catch (EOFException e) {
            throw e;
        } catch (IOException ex) {
            throw new PCapInvalidFormat(ex);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        
        if (null != file) {
            builder.append("pcap-importer: '");
            builder.append(file.getAbsolutePath());
            builder.append("' ");
        }

        builder.append("magic=").append(Integer.toHexString(magicNumber));
        builder.append("  versionMajor=").append(Integer.toHexString(versionMajor));
        builder.append("  versionMinor=").append(Integer.toHexString(versionMinor));
        builder.append("  zoneCorrectio=").append(Integer.toHexString(zoneCorrection));
        builder.append("  timestampAccuracy=").append(Integer.toHexString(accuracy));
        builder.append("  snapshotLength=").append(Integer.toHexString(snapshotLength));
        builder.append("  datalinkType=").append(Integer.toHexString(datalinkType));
        return builder.toString();
    }
}
