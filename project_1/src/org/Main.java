package org;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {
    public static void main(String[] args) {
        int[] numbers = {184385, 50581, 7877, 128587, 186971, 151275, 190694, 181953, 14506, 107696}; //for N = 100000
        

        int[] N = new int[]{30, 50, 100, 200, 500, 800, 1000, 5000, 10000, 50000, 100000}; // Times each command (insert, search, delete) will run

        int[] K = new int[0];   // Initializing array K (will contain k random non-unique keys)

        for (int j = 0; j <= N.length - 1; j++) {  // Inserting N elements
            int k = 0;
            if (N[j] < 201) { // Selecting the appropriate K based on N
                k = 10;
            } else if (N[j] < 1001 && N[j] > 200) {
                k = 50;
            } else if (N[j] > 1000) {
                k = 100;
            }

            DList myDList = new DList(); // Creating a new instance of DList (1a)
            DListPool myDListPool = new DListPool();  // Creating a new instance of DListPool (1b)
            AList myAList = new AList(2*N[j] + 100);  // Creating a new instance of AList (2a)
            AListPool myAListPool = new AListPool(2*N[j] + 100);  // Creating a new instance of AListPool (2b)

            //-----------------------------------INSERTING FILE DATA IN LISTS-------------------------------------------//
            String inputFile = "data_" + N[j] + ".bin"; // Construct file name
            try (DataInputStream dis = new DataInputStream(new FileInputStream(inputFile))) {
                Globals.numOfCommands = 0;

                while (dis.available() >= Globals.TUPLE_SIZE) { // Ensure we have a full tuple to read
                    int number = dis.readInt(); // Read the integer (4 bytes),
                    // java default is Big Endian
                    byte[] stringBytes = new byte[Globals.MAX_LENGTH];
                    dis.readFully(stringBytes); // Read the 50-byte string
                    // Convert byte array to string, trimming trailing spaces
                    String text = new String(stringBytes, StandardCharsets.UTF_8).trim();

                    //inserting files
                    myDList.insert(number, text);
                    myDListPool.insert(number, text);
                    myAList.insert(number, text);
                    myAListPool.insert(number, text);

                    // now number contains our key and text our data
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

             
            // ---------------------------------------- SEARCHING K 1a ----------------------------------------
            System.out.println("Start Searching in 1a K = " + k + "(initial list 1a for N = " + N[j] + ", " + N[j] + " elements inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startISearchingK1a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we search the element in 1a
                myDList.search(K[elementKey]);
            }
            long stopSearchingK1a = System.nanoTime();
            long totalTimeSearchingK1a =  stopSearchingK1a - startISearchingK1a; // Total time for searching in 1a
            long avCommandsSearchK1a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsSearchK1a);
            System.out.println("Total time for searching in 1a " + k + " elements: " + totalTimeSearchingK1a);
            double meanTimePerSearchRepetitionK1a = (double) totalTimeSearchingK1a / k; // Average time of each search for k searches in 1a
            System.out.println("Average time for each search in 1a for " + k + " elements: " + meanTimePerSearchRepetitionK1a + "\n");

            // ---------------------------------------- INSERTING K 1a ----------------------------------------
            System.out.println("Start Inserting in 1a K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInsertingK1a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 1a
                String data = generateRandomString(Globals.MAX_LENGTH);
                myDList.insert(K[elementKey], data);
            }
            long stopInsertingK1a = System.nanoTime();
            long totalTimeInsertK1a =  stopInsertingK1a - startInsertingK1a; // Total time for inserting in 1a
            long avCommandsInsertK1a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsertK1a);
            System.out.println("Total time for inserting in 1a " + k + " elements: " + totalTimeInsertK1a);
            double meanTimePerRepetitionK1a = (double) totalTimeInsertK1a / k; // Average time of each insert for k inserts in 1a
            System.out.println("Average time for each insert in 1a for " + k + " elements: " + meanTimePerRepetitionK1a + "\n");

            // ---------------------------------------- DELETING K 1a ----------------------------------------
            System.out.println("Start Deleting in 1a K = " + k + "(" + N[j] + "+" + k + " elements already inserted)");
            Globals.numOfCommands = 0;
            long startDeletingK1a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we delete the element from 1a
                myDList.delete(K[elementKey]);
            }
            long stopDeletingK1a = System.nanoTime();
            long totalTimeDeleteK1a =  stopDeletingK1a - startDeletingK1a; // Total time for deleting in 1a
            long avCommandsDeleteK1a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsDeleteK1a);
            System.out.println("Total time for deleting in 1a " + k + " elements: " + totalTimeDeleteK1a);
            double meanTimePerDeleteRepetitionK1a = (double) totalTimeDeleteK1a / k; // Average time of each delete for k deletes in 1a
            System.out.println("Average time for each delete in 1a for " + k + " elements: " + meanTimePerDeleteRepetitionK1a + "\n");

            // ---------------------------------------- INSERTING K 1a ----------------------------------------
            System.out.println("Start Inserting in 1a K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInserting2K1a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 1a
                String data = generateRandomString(Globals.MAX_LENGTH);
                myDList.insert(K[elementKey], data);
            }
            long stopInserting2K1a = System.nanoTime();
            long totalTimeInsert2K1a =  stopInserting2K1a - startInserting2K1a; // Total time for inserting in 1a
            long avCommandsInsert2K1a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsert2K1a);
            System.out.println("Total time for inserting in 1a " + k + " elements: " + totalTimeInsert2K1a);
            double meanTimePerRepetition2K1a = (double) totalTimeInsert2K1a / k; // Average time of each insert for k inserts in 1a
            System.out.println("Average time for each insert in 1a for " + k + " elements: " + meanTimePerRepetition2K1a + "\n");


            

 
            
            // ---------------------------------------- SEARCHING K 1b ----------------------------------------
            System.out.println("Start Searching in 1b K = " + k + "(initial list 1b for N = " + N[j] + ", " + N[j] + " elements inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startISearchingK1b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we search the element in 1b
                myDListPool.search(K[elementKey]);
            }
            long stopSearchingK1b = System.nanoTime();
            long totalTimeSearchingK1b =  stopSearchingK1b - startISearchingK1b; // Total time for searching in 1b
            long avCommandsSearchK1b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsSearchK1b);
            System.out.println("Total time for searching in 1b " + k + " elements: " + totalTimeSearchingK1b);
            double meanTimePerSearchRepetitionK1b = (double) totalTimeSearchingK1b / k; // Average time of each search for k searches in 1b
            System.out.println("Average time for each search in 1b for " + k + " elements: " + meanTimePerSearchRepetitionK1b + "\n");

            // ---------------------------------------- INSERTING K 1b ----------------------------------------
            System.out.println("Start Inserting in 1b K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInsertingK1b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 1b
                String data = generateRandomString(Globals.MAX_LENGTH);
                myDListPool.insert(K[elementKey], data);
            }
            long stopInsertingK1b = System.nanoTime();
            long totalTimeInsertK1b =  stopInsertingK1b - startInsertingK1b; // Total time for inserting in 1b
            long avCommandsInsertK1b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsertK1b);
            System.out.println("Total time for inserting in 1b " + k + " elements: " + totalTimeInsertK1b);
            double meanTimePerRepetitionK1b = (double) totalTimeInsertK1b / k; // Average time of each insert for k inserts in 1b
            System.out.println("Average time for each insert in 1b for " + k + " elements: " + meanTimePerRepetitionK1b + "\n");

            // ---------------------------------------- DELETING K 1b ----------------------------------------
            System.out.println("Start Deleting in 1b K = " + k + "(" + N[j] + "+" + k + " elements already inserted)");
            Globals.numOfCommands = 0;
            long startDeletingK1b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we delete the element from 1b
                myDListPool.delete(K[elementKey]);
            }
            long stopDeletingK1b = System.nanoTime();
            long totalTimeDeleteK1b =  stopDeletingK1b - startDeletingK1b; // Total time for deleting in 1b
            long avCommandsDeleteK1b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsDeleteK1b);
            System.out.println("Total time for deleting in 1b " + k + " elements: " + totalTimeDeleteK1b);
            double meanTimePerDeleteRepetitionK1b = (double) totalTimeDeleteK1b / k; // Average time of each delete for k deletes in 1b
            System.out.println("Average time for each delete in 1b for " + k + " elements: " + meanTimePerDeleteRepetitionK1b + "\n");

            // ---------------------------------------- INSERTING K 1b ----------------------------------------
            System.out.println("Start Inserting in 1b K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInserting2K1b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 1b
                String data = generateRandomString(Globals.MAX_LENGTH);
                myDListPool.insert(K[elementKey], data);
            }
            long stopInserting2K1b = System.nanoTime();
            long totalTimeInsert2K1b =  stopInserting2K1b - startInserting2K1b; // Total time for inserting in 1b
            long avCommandsInsert2K1b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsert2K1b);
            System.out.println("Total time for inserting in 1b " + k + " elements: " + totalTimeInsert2K1b);
            double meanTimePerRepetition2K1b = (double) totalTimeInsert2K1b / k; // Average time of each insert for k inserts in 1b
            System.out.println("Average time for each insert in 1b for " + k + " elements: " + meanTimePerRepetition2K1b + "\n");




            
            // ---------------------------------------- SEARCHING K 2a ----------------------------------------
            System.out.println("Start Searching in 2a K = " + k + "(initial list 2a for N = " + N[j] + ", " + N[j] + " elements inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startISearchingK2a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we search the element in 2a
                myAList.search(K[elementKey]);
            }
            long stopSearchingK2a = System.nanoTime();
            long totalTimeSearchingK2a =  stopSearchingK2a - startISearchingK2a; // Total time for searching in 2a
            long avCommandsSearchK2a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsSearchK2a);
            System.out.println("Total time for searching in 1a " + k + " elements: " + totalTimeSearchingK2a);
            double meanTimePerSearchRepetitionK2a = (double) totalTimeSearchingK2a / k; // Average time of each search for k searches in 2a
            System.out.println("Average time for each search in 1a for " + k + " elements: " + meanTimePerSearchRepetitionK2a + "\n");

            // ---------------------------------------- INSERTING K 2a ----------------------------------------
            System.out.println("Start Inserting in 2a K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInsertingK2a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 2a
                String data = generateRandomString(Globals.MAX_LENGTH);
                myAList.insert(K[elementKey], data);
            }
            long stopInsertingK2a = System.nanoTime();
            long totalTimeInsertK2a =  stopInsertingK2a - startInsertingK2a; // Total time for inserting in 2a
            long avCommandsInsertK2a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsertK2a);
            System.out.println("Total time for inserting in 2a " + k + " elements: " + totalTimeInsertK2a);
            double meanTimePerRepetitionK2a = (double) totalTimeInsertK2a / k; // Average time of each insert for k inserts in 2a
            System.out.println("Average time for each insert in 2a for " + k + " elements: " + meanTimePerRepetitionK2a + "\n");

            // ---------------------------------------- DELETING K 2a ----------------------------------------
            System.out.println("Start Deleting in 2a K = " + k + "(" + N[j] + "+" + k + " elements already inserted)");
            Globals.numOfCommands = 0;
            long startDeletingK2a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we delete the element from 2a
                myAList.delete(K[elementKey]);
            }
            long stopDeletingK2a = System.nanoTime();
            long totalTimeDeleteK2a =  stopDeletingK2a - startDeletingK2a; // Total time for deleting in 2a
            long avCommandsDeleteK2a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsDeleteK2a);
            System.out.println("Total time for deleting in 2a " + k + " elements: " + totalTimeDeleteK2a);
            double meanTimePerDeleteRepetitionK2a = (double) totalTimeDeleteK2a / k; // Average time of each delete for k deletes in 1a
            System.out.println("Average time for each delete in 2a for " + k + " elements: " + meanTimePerDeleteRepetitionK2a + "\n");

            // ---------------------------------------- INSERTING K 2a ----------------------------------------
            System.out.println("Start Inserting in 2a K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInserting2K2a = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 2a
                String data = generateRandomString(Globals.MAX_LENGTH);
                myAList.insert(K[elementKey], data);
            }
            long stopInserting2K2a = System.nanoTime();
            long totalTimeInsert2K2a =  stopInserting2K2a - startInserting2K2a; // Total time for inserting in 1b
            long avCommandsInsert2K2a = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsert2K2a);
            System.out.println("Total time for inserting in 2a " + k + " elements: " + totalTimeInsert2K2a);
            double meanTimePerRepetition2K2a = (double) totalTimeInsert2K2a / k; // Average time of each insert for k inserts in 2a
            System.out.println("Average time for each insert in 2a for " + k + " elements: " + meanTimePerRepetition2K2a + "\n");

            

            


            // ---------------------------------------- SEARCHING K 2b ----------------------------------------
            System.out.println("Start Searching in 2b K = " + k + "(initial list 2b for N = " + N[j] + ", " + N[j] + " elements inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startISearchingK2b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we search the element in 2b
                myAListPool.search(K[elementKey]);
            }
            long stopSearchingK2b = System.nanoTime();
            long totalTimeSearchingK2b =  stopSearchingK2b - startISearchingK2b; // Total time for searching in 2b
            long avCommandsSearchK2b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsSearchK2b);
            System.out.println("Total time for searching in 2b " + k + " elements: " + totalTimeSearchingK2b);
            double meanTimePerSearchRepetitionK2b = (double) totalTimeSearchingK2b / k; // Average time of each search for k searches in 2b
            System.out.println("Average time for each search in 2b for " + k + " elements: " + meanTimePerSearchRepetitionK2b + "\n");

            // ---------------------------------------- INSERTING K 2b ----------------------------------------
            System.out.println("Start Inserting in 2b K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInsertingK2b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 2b
                String data = generateRandomString(Globals.MAX_LENGTH);
                myAListPool.insert(K[elementKey], data);
            }
            long stopInsertingK2b = System.nanoTime();
            long totalTimeInsertK2b =  stopInsertingK2b - startInsertingK2b; // Total time for inserting in 2b
            long avCommandsInsertK2b = (long) Globals.numOfCommands / k; 
            System.out.println("Average Number of Commands ran: " + avCommandsInsertK2b);
            System.out.println("Total time for inserting in 2a " + k + " elements: " + totalTimeInsertK2b);
            double meanTimePerRepetitionK2b = (double) totalTimeInsertK2b / k; // Average time of each insert for k inserts in 2b
            System.out.println("Average time for each insert in 2b for " + k + " elements: " + meanTimePerRepetitionK2b + "\n");

            // ---------------------------------------- DELETING K 1b ----------------------------------------
            System.out.println("Start Deleting in 2b K = " + k + "(" + N[j] + "+" + k + " elements already inserted)");
            Globals.numOfCommands = 0;
            long startDeletingK2b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, we delete the element from 2b
                myAListPool.delete(K[elementKey]);
            }
            long stopDeletingK2b = System.nanoTime();
            long totalTimeDeleteK2b =  stopDeletingK2b - startDeletingK2b; // Total time for deleting in 2b
            long avCommandsDeleteK2b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsDeleteK2b);
            System.out.println("Total time for deleting in 2a " + k + " elements: " + totalTimeDeleteK2b);
            double meanTimePerDeleteRepetitionK2b = (double) totalTimeDeleteK2b / k; // Average time of each delete for k deletes in 2b
            System.out.println("Average time for each delete in 2b for " + k + " elements: " + meanTimePerDeleteRepetitionK2b + "\n");

            // ---------------------------------------- INSERTING K 1b ----------------------------------------
            System.out.println("Start Inserting in 2b K = " + k + "(" + N[j] + " elements already inserted)");
            K = generateRandomArrayDupl(N[j], k);
            Globals.numOfCommands = 0;
            long startInserting2K2b = System.nanoTime();
            for(int elementKey = 0; elementKey <= k - 1; elementKey++){    // Lopping in the array K k times (it contains k keys). For each key in array K, I create an Element and insert it in 2b
                String data = generateRandomString(Globals.MAX_LENGTH);
                myAListPool.insert(K[elementKey], data);
            }
            long stopInserting2K2b = System.nanoTime();
            long totalTimeInsert2K2b =  stopInserting2K2b - startInserting2K2b; // Total time for inserting in 2b
            long avCommandsInsert2K2b = (long) Globals.numOfCommands / k;
            System.out.println("Average Number of Commands ran: " + avCommandsInsert2K2b);
            System.out.println("Total time for inserting in 2b " + k + " elements: " + totalTimeInsert2K2b);
            double meanTimePerRepetition2K2b = (double) totalTimeInsert2K2b / k; // Average time of each insert for k inserts in 2b
            System.out.println("Average time for each insert in 2b for " + k + " elements: " + meanTimePerRepetition2K2b + "\n");

            //--------------------------------------SEARCHING CERTAIN KEYS -----------------------------------------
            if ( N[j] == 100000 ) {
                List<String> data = new ArrayList<>();
                for (int num : numbers) {
                    MyElement element = (MyElement) myAListPool.search(num);
                    if (element != null) {
                        data.add(element.getData());
                    }  
                }
                String result = String.join(" ", data);
                System.out.println(result);
            }
            

        } 
    }



    public static int[] generateRandomArray(int N) {    // Generate random Unique integers
        int[] randomNumbers = new int[N];

        Random random = new Random();

        Set<Integer> generatedNumbers = new HashSet<>();    // Storing all the numbers that we have already used

        for (int i = 0; i < N; i++) {   // Filling the Array with random unique numbers from 1 to 2*N
            int randomNumber;
            do {
                randomNumber = random.nextInt(2 * N) + 1; // Τυχαίος αριθμός από 1 έως 2*N
            } while (generatedNumbers.contains(randomNumber)); // Checking if we have already used this number
            randomNumbers[i] = randomNumber;
            generatedNumbers.add(randomNumber); // Adding the new number in the Array
        }

        return randomNumbers;   // Returning the Array
    }

    public static int[] generateRandomArrayDupl(int N, int K) { // Generate random Non Unique integers
        int[] randomNumbers = new int[K];

        Random random = new Random();

        // Filling the array with random numbers from 1 to 2*N
        for (int i = 0; i < K; i++) {   // Filling the Array with random non-unique numbers from 1 to 2*N
            randomNumbers[i] = random.nextInt(2 * N) + 1;
        }

        return randomNumbers;   // Returning the Array
    }


    public static String generateRandomString(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }
        return sb.toString();
    }

}
