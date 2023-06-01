
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Scanner;

public class Main{

    // declaring the method to convert
    // Hexadecimal to Binary
    static String hexToBinary(String hex) {

        // variable to store the converted
        // Binary Sequence
        String binary = "";

        // converting the accepted Hexadecimal
        // string to upper case
        hex = hex.toUpperCase();

        // initializing the HashMap class
        HashMap<Character, String> hashMap = new HashMap<Character, String>();

        // storing the key value pairs
        hashMap.put('0', "0000");
        hashMap.put('1', "0001");
        hashMap.put('2', "0010");
        hashMap.put('3', "0011");
        hashMap.put('4', "0100");
        hashMap.put('5', "0101");
        hashMap.put('6', "0110");
        hashMap.put('7', "0111");
        hashMap.put('8', "1000");
        hashMap.put('9', "1001");
        hashMap.put('A', "1010");
        hashMap.put('B', "1011");
        hashMap.put('C', "1100");
        hashMap.put('D', "1101");
        hashMap.put('E', "1110");
        hashMap.put('F', "1111");

        int i;
        char ch;

        // loop to iterate through the length
        // of the Hexadecimal String
        for (i = 0; i < hex.length(); i++) {
            // extracting each character
            ch = hex.charAt(i);

            // checking if the character is
            // present in the keys
            if (hashMap.containsKey(ch))

                // adding to the Binary Sequence
                // the corresponding value of
                // the key
                binary += hashMap.get(ch);

                // returning Invalid Hexadecimal
                // String if the character is
                // not present in the keys
            else {
                binary = "Invalid Hexadecimal String";
                return binary;
            }
        }

        // returning the converted Binary
        return binary;
    }

    public static String L_B_Endian(String str) {

        String[] newStr = str.split(" ");

        String[] last = new String[newStr.length];

        for (int i = 0; i < newStr.length; i++) {
            last[i] = newStr[newStr.length - i - 1];
        }

        String finalStr = "";

        for (int i = 0; i < newStr.length; i++) {
            finalStr += last[i];
        }

        return finalStr;

    }

    public static long unsigned(String b) {
        int j = 0;
        long num = 0;
        int length = b.length();

        for (int i = length - 1; i >= 0; i--) {
            if (b.charAt(i) == '1') {
                num += Math.pow(2, j);
            }
            j++;
        }
        return num;

    }

    public static long signed(String b) {
        int j = 0;
        long num = 0;
        int length = b.length();

        for (int i = length - 1; i > 0; i--) {
            if (b.charAt(i) == '1') {
                num += Math.pow(2, j);
            }
            j++;
        }
        if (b.charAt(0) == '1') {
            num -= Math.pow(2, length - 1);
        }

        return num;

    }

    public static int signedBit(String b) {
        int sBit;
        if (b.charAt(0) == '1') {
            sBit = -1;
        } else {
            sBit = 1;
        }
        return sBit;
    }

    public static double fraction(String b) {
        int size = b.length();
        double fractionPart = 0;
        double truncationPart = 0;

        switch (size) {
            case 8:
                int j = 1;
                for (int i = 5; i <= 7; i++) {
                    if (b.charAt(i) == '1') {
                        fractionPart +=Math.pow(0.5, j);
                    }
                    j++;
                }
                break;

            case 16:
                int k = 1;
                for (int i = 7; i <= 15; i++) {
                    if (b.charAt(i) == '1') {
                        fractionPart += Math.pow(0.5, k);
                    }
                    k++;
                }
                break;

            case 24:
                int z = 1;
                for (int i = 9; i <= 21; i++) {
                    if (b.charAt(i) == '1') {
                        fractionPart += Math.pow(0.5, z);
                    }
                    z++;
                }

                int y = 1;
                for (int i = 22; i <= 23; i++) {
                    if (b.charAt(i) == '1') {
                        truncationPart += Math.pow(0.5, y);
                    }
                    y++;
                }

                if (truncationPart > 0.5) {
                    fractionPart += Math.pow(0.5, 12);
                } else if ((truncationPart == 0.5) && (b.charAt(21) == '1')) {
                    fractionPart += Math.pow(0.5, 12);
                }

                break;
            case 32:
                int m = 1;
                for (int i = 11; i <= 23; i++) {
                    if (b.charAt(i) == '1') {
                        fractionPart += Math.pow(0.5, m);
                    }
                    m++;
                }
                int n = 1;
                for (int i = 24; i <= 31; i++) {
                    if (b.charAt(i) == '1') {
                        truncationPart += Math.pow(0.5, n);
                    }
                    n++;
                }

                if (truncationPart > 0.5) {
                    fractionPart += Math.pow(0.5, 13);
                } else if ((truncationPart == 0.5) && (b.charAt(23) == '1')) {
                    fractionPart += Math.pow(0.5, 13);
                }

                break;
        }

        return fractionPart;
    }

    public static double floatNumber(String b) {
        int size = b.length();
        int l = 0;

        if (size == 8) {
            l = 4;
        } else if (size == 16) {
            l = 6;
        } else if (size == 24) {
            l = 8;
        } else if (size == 32) {
            l = 10;
        }

        char[] e = new char[l];

        for (int i = 1; i <= l; i++) {
            e[i - 1] = b.charAt(i);
        }

        String ex = new String(e);

        double Bias = Math.pow(2, l - 1) - 1;
        double E = 0;
        double mantissa = 0;

        if (unsigned(ex) == 0) {
            E = 1 - Bias;
            mantissa = fraction(b);
        } else if ((unsigned(ex) != 0) && (unsigned(ex) != (2 ^ l - 1))) {
            E = unsigned(ex) - Bias;
            mantissa = 1 + fraction(b);
        }

        double decimalValue = (signedBit(b) * mantissa * Math.pow(2, E));

        if ((unsigned(ex) == (Math.pow(2, l) - 1)) && fraction(b) == 0.0 && signedBit(b) == 1) {
            decimalValue = Double.POSITIVE_INFINITY;
        } else if ((unsigned(ex) == (Math.pow(2, l) - 1)) && fraction(b) == 0.0 && signedBit(b) == -1) {
            decimalValue = Double.NEGATIVE_INFINITY;
        } else if ((unsigned(ex) == (Math.pow(2, l) - 1)) && fraction(b) != 0.0) {
            decimalValue = Double.NaN;
        }

        return decimalValue;
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        System.out.print("Byte ordering: ");
        String b_type = input.next();
        String bType= b_type.toLowerCase();
        input.next();

        System.out.print("Data type: ");
        String d_type = input.next();
        String dType = d_type.toLowerCase();

        System.out.print("Data type size: ");
        int size = input.nextInt();

        try (Scanner scanner = new Scanner(new BufferedReader(new FileReader("input.txt")))) {
            PrintStream writeToTxt = new PrintStream(new FileOutputStream("output.txt", false));
            String h_number = null;
            while (scanner.hasNextLine()) {

                String numInput = scanner.nextLine();

                for (int i = 0; i < numInput.length(); i += size * 3) {
                    int endIndex = Math.min(i + size * 3, numInput.length());
                    String num = numInput.substring(i, endIndex);

                    if (bType.contains("little")) {
                        h_number = L_B_Endian(num);
                    } else {
                        String[] array = num.split(" ");
                        h_number = "";

                        for (int j = 0; j < size; j++) {
                            h_number += array[j];
                        }
                    }

                    String b_number = hexToBinary(h_number);

                    if (dType.contains("unsigned")) {
                        long d_number = unsigned(b_number);
                        writeToTxt.print(d_number + " ");
                    } else if (dType.contains("int")||dType.contains("signed")) {
                        long d_number = signed(b_number);
                        writeToTxt.print(d_number + " ");
                    } else if (dType.contains("float")) {
                        double d_number = floatNumber(b_number);
                        String dN = Double.toString(d_number);


                        if(dN.contains("e")||dN.contains("E")){
                            int e= dN.indexOf('E');
                            char[] fr= new char[e+1];
                            char[] st= new char[dN.length()-e];
                            for(int k=0;k<e;k++){
                                fr[k]=dN.charAt(k);
                            }
                            for(int k=e;k<dN.length();k++){
                                st[k-e]=dN.charAt(k);
                            }
                            String frr = new String(fr);
                            String stt = new String(st);
                            double n= Double.parseDouble(frr);
                            writeToTxt.printf("%.5f",n);
                            writeToTxt.print(stt+" ");

                        }else if(d_number==0){
                            writeToTxt.print(new DecimalFormat("##.#####").format(d_number)+" ");

                        }else{
                            writeToTxt.printf("%.5f ",d_number);

                        }
                    }

                }
                writeToTxt.println("");

            }
            writeToTxt.close();
            input.close();
        }

        catch (FileNotFoundException e) {
            input.close();
            throw new RuntimeException(e);

        }

    }

}