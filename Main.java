package bullscows;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        BullCowGameProcess gameProcess = new BullCowGameProcess();
        BullCowErrors bullCowErrors = new BullCowErrors();

        Scanner scanner = new Scanner(System.in);

        final int inputLength, symbolsLength;


        System.out.println("Input the length of the secret code:");
        System.out.print("> ");

        if (scanner.hasNextBigInteger()) {
            inputLength = scanner.nextInt();
        } else {
            inputLength = 0;
            bullCowErrors.invalidNum(scanner.next());
        }

        System.out.println("Input the number of possible symbols in the code:");
        System.out.print("> ");

        if (scanner.hasNextBigInteger()) {
            symbolsLength = scanner.nextInt();
        } else {
            symbolsLength = 0;
            bullCowErrors.invalidNum(scanner.next());
        }

        if (inputLength == 0 || symbolsLength == 0)
            bullCowErrors.custom("Error","You cannot play with 0");

        if (symbolsLength < inputLength)
            bullCowErrors.notPossibleNum(inputLength,symbolsLength);

        if (symbolsLength > 36)
            bullCowErrors.maximumNum();



        String randomText = gameProcess.generateRandomNumber(inputLength,symbolsLength);

        String symbolType = gameProcess.getSymbolsTypes(symbolsLength);


        System.out.println("The secret is prepared: " + "*".repeat(inputLength) + " " + symbolType +  ".");
        System.out.println("Okay, let's start a game!");

        int turn = 1;
        while (true) {

            System.out.println("Turn " + turn + ":");
            turn++;

            System.out.print("> ");
            String guessedText = scanner.next();

            if (guessedText.length() != randomText.length()) {
                bullCowErrors.custom("Not equal!",
                        "You should enter a " + randomText.length() + " length number");
            }

            String grade = gameProcess.checkBullsCows(randomText,guessedText);

            System.out.println(grade);

            if (randomText.equals(guessedText)) {
                System.out.println("Congratulations! You guessed the secret code.");
                break;
            }

        }

    }
}



class BullCowGameProcess {

    String generateRandomNumber(int inputLength, int symbolsLength) {
        StringBuilder value = new StringBuilder();

        ArrayList<String> symbols = new ArrayList<>();
        for (int i = 0; i <= 9; i++) {
            symbols.add(Integer.toString(i));
        }


        for (char i = 97; i <= 122; i++) {
            symbols.add(Character.toString(i));
        }

        for (int i = 0; i < inputLength; i++) {
            int randomNumber = (int) Math.floor(Math.random() * (symbolsLength));
            String randomChar = symbols.get(randomNumber);

            if (!value.toString().contains(randomChar)) value.append(randomChar);
            else i--;

        }

        return value.toString();

    }


    String getSymbolsTypes(int symbolsLength) {
        String symbolsType = "(0-aabb)";


        if (symbolsLength <= 10){
            symbolsType = symbolsType.replace("aa", Integer.toString(symbolsLength - 1));
            symbolsType = symbolsType.replace("bb","");
        } else {
            symbolsType = symbolsType.replace("aa","9");

            char lastAlphabet = (char) (97 + symbolsLength - 10 - 1);

            symbolsType = symbolsType.replace("bb",", a-" + lastAlphabet);
        }


        return symbolsType;
    }


    String checkBullsCows(String randomText,String guessedText) {

        String text = "Grade: AABB";

        if (guessedText.equals(randomText)) {
            text = text.replace("AABB",randomText.length() + " bulls");
        }
        else {

            ArrayList<Integer> bullsCowsList = getBullsCows(randomText, guessedText);

            int bullsCount = bullsCowsList.get(0);
            int cowsCount = bullsCowsList.get(1);

            if (bullsCount > 0) {
                text = text.replace("AA", bullsCount + " bull" );
                if (bullsCount > 1) {text = text.replace("bull", "bulls");}
            } else text = text.replace("AA","");


            if (cowsCount > 0) {

                if (bullsCount > 0) text = text.replace("BB"," and BB");

                text = text.replace("BB", cowsCount + " cow" );
                if (cowsCount > 1) {text = text.replace("cow", "cows");}

            } else text = text.replace("BB","");


            if (bullsCount == 0 && cowsCount == 0) {
                text = text + "None.";
            }

        }


        return text;
    }


    ArrayList<Integer> getBullsCows(String rText,String gText) {
        String randomText = rText;
        String guessedText = gText;
        int bullsCount = 0, cowsCount = 0;

        for (int i = 0; i < randomText.length(); i++) {
            String letter = guessedText.substring(i, i + 1);
            if (
                    randomText.substring(i, i + 1).equals(letter)
            ) {
                bullsCount++;

                randomText = new StringBuilder(randomText).deleteCharAt(i).toString();
                guessedText = new StringBuilder(guessedText).deleteCharAt(i).toString();
                i--;
            }
        }

        for (String a : guessedText.split("")) {

            for (String b : randomText.split("")) {

                if (a.equals(b)) cowsCount++;

            }
            randomText = randomText.replace(a,"");


        }



        ArrayList<Integer> bullsCowsList = new ArrayList<>();
        bullsCowsList.add(bullsCount);
        bullsCowsList.add(cowsCount);

        return bullsCowsList;

    }

}


class BullCowErrors {

    public void custom(String title, String message) {
        System.out.println(title + ": " + message);
        System.exit(0);
    }


    public void invalidNum(String text) {
        System.out.println("Error: " + text + " isn't a valid number.");
        System.exit(0);
    }

    public void notPossibleNum(int input, int symbols) {
        System.out.println("Error: it's not possible to generate a code " +
                "with a length of " + input + " with " + symbols +" unique symbols.");
        System.exit(0);
    }

    public void maximumNum() {
        System.out.println("Error: maximum number of possible " +
                "symbols in the code is 36 (0-9, a-z).");
        System.exit(0);
    }

}