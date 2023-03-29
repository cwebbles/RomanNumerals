package converter;

import java.security.InvalidParameterException;
import java.util.Objects;
import java.util.Scanner;

public class Converter {

    public static void main(String[] args){

        RomanNumeralConverter converter = new RomanNumeralConverter();

        System.out.println("----Welcome to the Roman Numeral Converter!----");

        Scanner scanner = new Scanner(System.in);
        String result = "";

        boolean continueToPlay = true;
        while(continueToPlay){
            System.out.println("Please enter a decimal number or a roman numeral to convert!");
            System.out.println("- Decimal numbers must be between 1-3999");
            System.out.println("- Roman numerals must be valid");
            System.out.print("Enter here: ");

            boolean validRequest = false;
            while(!validRequest){
                try{
                    String userInput = scanner.next();
                    result = converter.convert(userInput);
                    validRequest = true;
                } catch(InvalidParameterException ex){
                    System.out.println(ex.getMessage());
                    System.out.print("Please enter a valid request: ");
                }
            }

            System.out.printf("Here is the conversion: %s\n", result);
            System.out.println("Would you like to convert another number (y/n)?");
            String playAgain = scanner.next();
            if(Objects.equals(playAgain, "n")){
                continueToPlay = false;
            }
        }

        System.out.println("Thank you!");


    }
}
