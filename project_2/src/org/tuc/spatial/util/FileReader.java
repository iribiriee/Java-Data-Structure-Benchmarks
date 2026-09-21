package org.tuc.spatial.util;

import org.tuc.spatial.TucPoint;
import org.tuc.spatial.Monster;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileReader {

    private static final int STRING_LENGTH = 50;

    public static List<TucPoint> readCoordinatesFromFile(String inputFile) {
        ArrayList<TucPoint> coordList = new ArrayList<>();

        // Prepend src/ to file path
        inputFile = "src/" + inputFile;

        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile))) {
            while (dis.available() > 0) {
                int x = dis.readInt();
                int y = dis.readInt();
                coordList.add(new TucPoint(x, y));
            }
        } catch (IOException e) {
            System.err.println("Error reading coordinate file: " + inputFile);
            e.printStackTrace();
        }

        return coordList;
    }

    public static List<Monster> readMonsterFromDisk(int N, String type) {
        // Build the path assuming files are in src/
        String inputFile = "src/monsters_" + type + "_" + N + ".bin";
        ArrayList<Monster> monsterList = new ArrayList<>();

        try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile))) {
            int TUPLE_SIZE = 4 + 4 + STRING_LENGTH; // x (4 bytes), y (4 bytes), name (50 bytes)

            while (dis.available() >= TUPLE_SIZE) {
                int x = dis.readInt();
                int y = dis.readInt();

                byte[] stringBytes = new byte[STRING_LENGTH];
                dis.readFully(stringBytes);

                String text = new String(stringBytes, StandardCharsets.UTF_8).trim();
                monsterList.add(new Monster(x, y, text));
            }

        } catch (IOException e) {
            System.err.println("Error reading monster file: " + inputFile);
            e.printStackTrace();
        }

        return monsterList;
    }
}
