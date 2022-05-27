package com.timevary.radar.tcp.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author ：ywb
 * @date ：Created in 2022/1/12 13:48
 * @modified By：
 */
@Component
@Slf4j
public class ReadFirmware {

    private byte[] firmwareFile;

    public byte[] readFile(File file) {
        if (this.firmwareFile != null) {
            return this.firmwareFile;
        }
        synchronized (this) {
            if (this.firmwareFile != null) {
                return this.firmwareFile;
            }
            ByteBuffer fileBuf = null;
            try (FileInputStream fileInputStream = new FileInputStream(file);
                 FileChannel channel = fileInputStream.getChannel()) {
                fileBuf = ByteBuffer.allocate((int) file.length());
                channel.read(fileBuf);
                byte[] array = fileBuf.array();
                fileBuf.clear();
                this.firmwareFile = array;
            } catch (Exception e) {
                log.error("read updateFile error : {}", e.getMessage());
            } finally {
                if (fileBuf != null) {
                    fileBuf.clear();
                }
            }
        }
        return this.firmwareFile;
    }

}
