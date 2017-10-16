package com.tahsinsayeed.reliableudp;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import static org.junit.Assert.assertFalse;

public class FrameTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();
    private final File dataFile;
    private Frame frame;

    public FrameTest() throws Exception {
        dataFile = getDataFile();
    }

    @Before
    public void setup() throws Exception {
        frame = new Frame(dataFile);
    }


    private File getDataFile() throws IOException {
        folder.create();
        return folder.newFile("dataFile.txt");
    }

    private void writeToFile(String str) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile));
        writer.write(str);
        writer.flush();
    }



    @Test
    public void onEmptyFile_hasNextIsFalseAfterInit() throws Exception {
        assertFalse(frame.hasNext());
    }

   @Test(expected = Frame.NoMoreFrame.class)
   public void whenHasNextFalse_nextThrows() throws Exception {
       frame.next();
   }
   
//   @Test
//   public void () throws Exception {
//
//   }


//    @Test
//    public void onPartialRead_loadsStringRead() throws Exception {
//        String s = "data";
//        writeToFile(s);
//        assertEquals("0" + s, sender.loadNextPacket());
//    }
//
//    @Test
//    public void hasNextIsFalseAfterPartialRead() throws Exception {
//        String s = "data";
//        writeToFile(s);
//        sender.loadNextPacket();
//        sender.loadNextPacket();
//    }
//
//    @Test
//    public void firstCharOfPacketHasHeader() throws Exception {
//        writeToFile("data");
//        String s = sender.loadNextPacket();
//        assertEquals('0', s.charAt(0));
//    }
//
//    @Test
//    public void atTheBeginningFrameLoadsNpackets() throws Exception {
//        sender.loadPacket();
//        assertEquals(8, sender.getFrame().size());
//    }
//






















}
