/*
 * Copyright 2009 TauNova (http://taunova.com). All rights reserved.
 * 
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package net.taunova.importer.pcap.test;

import java.io.DataInputStream;
import java.io.File;
import java.io.IOException;
import net.taunova.importer.pcap.PCapDatalink;
import net.taunova.importer.pcap.PCapEventHandler;
import net.taunova.importer.PCapImportTask;
import net.taunova.importer.pcap.PCapHelper;
import net.taunova.importer.pcap.PCapVersion;

/**
 *
 * @author Renat.Gilmanov
 */
public class ImportTest {

    static class DefaultHandler implements PCapEventHandler {

        @Override
        public void handleInfo(PCapVersion version, PCapDatalink type) {
               System.out.println("Info: " + version + " type: " + type);
        }

        @Override
        public void handleEntity(int saved, int actual, long timestamp, DataInputStream stream) throws IOException {
            System.out.println("  Packet # saved: " + saved + " timestamp: " + timestamp);
            stream.skip(saved);
        }

        @Override
        public void onImportStart() {
            System.out.println("Import start");
        }

        @Override
        public void onImportFinish() {
            System.out.println("Import end");
        }

        @Override
        public void onImportFailed() {
            System.out.println("Import failed");
        }      
    }
    
    /**
     * 
     * @param args 
     */
    public static void main(String[] args) throws Exception {
       System.out.println("Testing PCap import"); 
       DefaultHandler handler = new DefaultHandler();
       PCapImportTask importTask = PCapHelper.createImportTask(new File("data/default_udp.pcap"), handler);
       importTask.init();
       
       while(!importTask.isFinished()) {
           importTask.processNext();
       }
    }
}
