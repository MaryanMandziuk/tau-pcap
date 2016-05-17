/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.taunova.importer.pcap.test;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import net.taunova.importer.PCapImportTask;
import net.taunova.importer.pcap.PCapHelper;
import net.taunova.importer.pcap.exception.PCapInvalidFormat;
import net.taunova.importer.pcap.exception.PCapSourceNotFound;
import org.hamcrest.CoreMatchers;

import org.junit.Assert;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author maryan
 */
public class PCapTest {

    static final String PCAP_EX = ".pcap";

    public PCapTest() {
    }

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    
    /**
     * Create temporary pcap file with data
     * @param name
     * @return
     * @throws IOException
     */
    public File createTempPCap(String name) throws IOException {
        final File testPCap = tempFolder.newFile(name + PCAP_EX);
        DataOutputStream os = new DataOutputStream(new FileOutputStream(testPCap));
        try {
            long[] bin = {0xd4c3b2a102000400L, 0x0000000000000000L,
                0xffff000001000000L, 0x7934c242cd710800L,
                0x4b0000004b000000L,};
            for (int i = 0; i < bin.length; i++) {
                os.writeLong(bin[i]);
            }

        } finally {
            os.close();
        }
        return testPCap;
    }

    
    /**
     * Create temporary pcap file with invalid data
     * @param name
     * @return
     * @throws IOException 
     */
    public File createTempPCapInvalidData(String name) throws IOException {
        final File testPCap = tempFolder.newFile(name + PCAP_EX);
        DataOutputStream os = new DataOutputStream(new FileOutputStream(testPCap));
        try {
            long[] bin = {0xa443b2a102000400L, 0x000000000000000L,
                0xffff000001000000L, 0x7934c242cd710800L,
                0x4b0000004b000000L,};
            for (int i = 0; i < bin.length; i++) {
                os.writeLong(bin[i]);
            }

        } finally {
            os.close();
        }
        return testPCap;
    }
    

    /**
     * Testing PCap.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testPCap() throws Exception {
        System.out.println("Testing PCap");
        
        PrintStream original = System.out;
        final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        
        File pcap = createTempPCap("test");
        ImportTest.DefaultHandler handler = new ImportTest.DefaultHandler();
        PCapImportTask importTask = PCapHelper.createImportTask(pcap, handler);
        importTask.init();
        while(!importTask.isFinished()) {
           importTask.processNext();
        }
        
        
        String expectedOutput = "Info: 2.4 type: 1\n"
                + "Import start\n"
                + "  Packet # saved: 75 timestamp: 1120023673553421\n"
                + "Import end\n";

        assertEquals(expectedOutput, outContent.toString());
        
   
        String expectedToStringOutput = "magic=d4c3b2a1  versionMajor=2"
                + "  versionMinor=4  zoneCorrectio=0  timestampAccuracy=0"
                + "  snapshotLength=ffff  datalinkType=1";

        Assert.assertThat(importTask.toString(),
                CoreMatchers.containsString(expectedToStringOutput));
        
        
        System.setOut(original);
    }
    
    
    /**
     * Testing pcap without data
     * @throws Exception 
     */
    @Test(expected = PCapInvalidFormat.class)
    public void testPCapInvalidFormat() throws Exception {
        System.out.println("Testing pcap without data");
        
        final File testPCap = tempFolder.newFile("test" + PCAP_EX);
        ImportTest.DefaultHandler handler = new ImportTest.DefaultHandler();
        PCapImportTask importTask = PCapHelper.createImportTask(testPCap, handler);
        importTask.init();
    }
    
    
    /**
     * Testing pcap with invalid data
     * @throws Exception 
     */
    @Test(expected = PCapInvalidFormat.class)
    public void testInvalidMagicNumber() throws Exception {
        System.out.println("Testing pcap with invalid data");
        
        final File testPCap = createTempPCapInvalidData("test");
        ImportTest.DefaultHandler handler = new ImportTest.DefaultHandler();
        PCapImportTask importTask = PCapHelper.createImportTask(testPCap, handler);
        importTask.init();
    }
    
    
    /**
     * Testing not existing pcap file
     * @throws Exception 
     */
    @Test(expected = PCapSourceNotFound.class)
    public void testSourceNotFound() throws Exception {
        System.out.println("Testing not existing pcap file");
        
        ImportTest.DefaultHandler handler = new ImportTest.DefaultHandler();
        PCapImportTask importTask = PCapHelper.createImportTask(new File("tcp.pcap"), handler);
        importTask.init();
    }

}
