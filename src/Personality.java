/**
 * CS312 Assignment 8.
 * 
 * On my honor, Danica Padlan, this programming assignment is my own work and I have
 * not shared my solution with any other student in the class.
 *
 * A program to read a file with raw data from a Keirsey personality test
 * and print out the results.
 *
 *  email address: danica_padlan@yahoo.com
 *  UTEID: dmp3357
 *  TA name: Nikhil Kumar
 *  Number of slip days used on this assignment: 0
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class Personality {
    //* must add class constants
    // CS312 Student- Add your constants here.

    public static final int setOf10 = 10;
    public static final int quest7 = 7;
    public static final int sensVIntQuest = 2;
    public static final int thinkVPercQuest = 4;

    // The main method to process the data from the personality tests
    public static void main(String[] args) {
        // do not make any other Scanners connected to System.in
        Scanner keyboard = new Scanner(System.in);
        Scanner fileScanner = getFileScanner(keyboard);

        // CS312 students - your code and methods calls go here
        readData(fileScanner);

        fileScanner.close();
        keyboard.close();
    }

    // CS312 Students add you methods here
    //reads through each line of file
    public static void readData(Scanner scan){
        //skips over the first line with number
        scan.nextLine();
        while(scan.hasNext()){
            //[num A, num B] format
            int[] evi = new int[2], svn = new int[2],tvf = new int[2],jvp = new int[2];
            String name = scan.nextLine();
            Scanner line = new Scanner(sepLine(scan.nextLine()));
            //sets up values in arrays
            setCount(line, evi, svn, tvf, jvp);
            int[] scores = {percent(evi),percent(svn),percent(tvf),percent(jvp)};
            printResults(name,scores);
        }
    }

    //separates the answer line with spaces
    public static String sepLine(String line){
        String ans = "";
        //goes through the line and grabs the chars for new string
        for(int x = 0; x < line.length(); x++){
            ans += line.charAt(x) + " ";
        }
        return ans;
    }

    //goes through answers and adds to corresponding question type
    public static void setCount(Scanner line, int[] evi, int[] svn, int[] tvf, int[] jvp){
        while(line.hasNext()){
            //loops in sets of 10 * 7 due to the total questions being 70
            for(int x = 0; x < setOf10; x++){
                //loops through 7 bc of the pattern the questions are set up
                for(int y = 1; y <= quest7; y++){
                    //the first question of the 7 is always EvI questions
                    if(y==1){
                        tallyUp(line.next(),evi);
                    //all other questions are determined by 2(SvN),4(TvF),6(JvP)
                    } else if(y%2 == 0){
                        int[] array = jvp;
                        if(y == thinkVPercQuest){
                            array = tvf;
                        } else if(y == sensVIntQuest){
                            array = svn;
                        }
                        //loops 2 times because there are 2 answers under the same question category
                        for(int z = 0; z < 2; z++){
                            tallyUp(line.next(),array);
                        }
                    }
                }
            }
        }
    }

    //adds score count everytime A or B shows up
    //the array's format is [num of A's, num of B's]
    public static void tallyUp(String ans, int[] scores){
        if(ans.equalsIgnoreCase("A")){
            scores[0]++;
        } else if(ans.equalsIgnoreCase("B")){
            scores[1]++;
        }
    }

    //returns the percent that B appears
    public static int percent(int[] score){
        int total = score[0] + score[1];
        //if A's + B's is not 0
        if(total != 0){
            //calculate the B/A
            return (int) Math.round((((double) score[1] / total) * 100));
        //if A+B = 0, that means all answers were blank
        } else{
            //therefore return -1
            return -1;
        }
    }

    //checks if the answer is less than 0
    public static String checkAns(int score){
        //if score < 0, print "NO ANSWER"
        if(score < 0){
            return "NO ANSWERS";
        //if score > 0, print score
        } else{
            return score + "";
        }
    }

    //gets the logged patients personality type
    public static String getType(int[] score){
        String type = "";
        String estj = "ESTJ";
        String infp = "INFP";
        //goes through score array to find the corresponding letter
        for(int x = 0; x < score.length; x++){
            //if score < 0
            if(score[x] < 0){
                type += "-";
            //if score = 50
            } else if(score[x] == 50){
                type += "X";
            //if score > 50
            } else if(score[x] > 50){
                //grab char from INFP
                type += infp.charAt(x);
            } else{
                //grab char from ESTJ
                type += estj.charAt(x);
            }
        }
        return type;
    }

    //prints out the results in a nice format
    public static void printResults(String name, int[] scores){
        //this is much easier to read and understand vs putting it all on one line
        System.out.printf("%30s:", name);
        System.out.printf("%11s%11s%11s%11s", checkAns(scores[0]), checkAns(scores[1]), checkAns(scores[2]),checkAns(scores[3]));
        System.out.printf(" = %s%n",getType(scores));
    }
    
    // Method to choose a file.
    // Asks user for name of file. 
    // If file not found create a Scanner hooked up to a dummy set of data
    // Example use: 
    public static Scanner getFileScanner(Scanner keyboard){
        Scanner result = null;
        try {
            System.out.print("Enter the name of the file with"
                    + " the personality data: ");
            String fileName = keyboard.nextLine().trim();
            System.out.println();
            result = new Scanner(new File(fileName));
        } catch(FileNotFoundException e) {
            System.out.println("Problem creating Scanner: " + e);
            System.out.println("Creating Scanner hooked up to default data " 
                    + e);
            String defaultData = "1\nDEFAULT DATA\n"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            result = new Scanner(defaultData);
        }
        return result;
    }
}
