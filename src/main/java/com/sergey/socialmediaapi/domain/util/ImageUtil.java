package com.sergey.socialmediaapi.domain.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class ImageUtil {
    public static byte[] compressImage(byte[] data) {
        var compressor = new Deflater();
        compressor.setLevel(Deflater.BEST_COMPRESSION);
        compressor.setInput(data);
        compressor.finish();
        var outputStream = new ByteArrayOutputStream(data.length);
        byte[] temp = new byte[4 * 1024];
        try {
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (!compressor.finished()) {
            int size = compressor.deflate(temp);
            outputStream.write(temp, 0, size);
        }
        return outputStream.toByteArray();
    }

    public static byte[] decompressImage(byte[] data) {
        var decompressor = new Inflater();
        decompressor.setInput(data);
        byte[] temp = new byte[4 * 1024];
        var outputStream = new ByteArrayOutputStream();
        try {
            while (!decompressor.finished()) {
                int count = decompressor.inflate(temp);
                outputStream.write(temp, 0, count);
            }
            outputStream.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return outputStream.toByteArray();

    }
}
