package converter;

import java.security.InvalidParameterException;
import java.util.*;

/**
 * Class to convert Roman Numerals to decimal numbers and vice versa
 */
public class RomanNumeralConverter {

    /**
     * Class to hold numeral data (i.e. the string of the numeral and the value)
     */
    private static class Numeral{

        public Numeral(String num, int val) {
            this.numeral = num;
            this.value = val;
        }

        public String numeral;
        public int value;

        /**
         * Comparator for numerals
         *
         * @param that numeral to compare to
         * @return true or false depending on if this numeral is less than that numeral
         */
        boolean isLessThan(Numeral that) {
            return this.value < that.value;
        }
    }

    private final Numeral M = new Numeral("M", 1000);
    private final Numeral CM = new Numeral("CM", 900);
    private final Numeral D = new Numeral("D", 500);
    private final Numeral CD = new Numeral("CD", 400);
    private final Numeral C = new Numeral("C", 100);
    private final Numeral XC = new Numeral("XC", 90);
    private final Numeral L = new Numeral("L", 50);
    private final Numeral XL = new Numeral("XL", 40);
    private final Numeral X = new Numeral("X", 10);
    private final Numeral IX = new Numeral("IX", 9);
    private final Numeral V = new Numeral("V", 5);
    private final Numeral IV = new Numeral("IV", 4);
    private final Numeral I = new Numeral("I", 1);
    private final int MAX = 3999;

    Map<String, Numeral> numerals = new LinkedHashMap<>() {{
        put("M", M);
        put("CM", CM);
        put("D", D);
        put("CD", CD);
        put("C", C);
        put("XC", XC);
        put("L", L);
        put("XL", XL);
        put("X", X);
        put("IX", IX);
        put("V", V);
        put("IV", IV);
        put("I", I);
    }};

    List<String> subtractivesAsStrings = new ArrayList<>(Arrays.asList(
            "CM", "CD", "XC", "XL", "IX", "IV"));

    List<String> numeralsAsStrings;

    StringBuilder numeralResult;

    int decimalResult;

    /**
     * Constructor to reset all editable values
     */
    public RomanNumeralConverter() {
        numeralResult = new StringBuilder();
        decimalResult = 0;
        numeralsAsStrings = new ArrayList<>(Arrays.asList(
                "M", "M", "M", "CM", "D", "CD", "C", "C", "C", "XC", "L",
                "XL", "X", "X", "X", "IX", "V", "IV", "I", "I", "I"));
    }


    /**
     * Converts numeral to decimal or decimal to numeral
     *
     * @param userInput user's input (numeral or decimal)
     * @return String value of the conversion
     * @throws InvalidParameterException if user's input was invalid
     */
    public String convert(String userInput) throws InvalidParameterException {

        boolean isDecimal = false;

        // Check to see if input is a decimal
        try{
            isDecimal = isValidDecimal(userInput);
        } catch (NumberFormatException | InvalidParameterException ex){

            // If input contains invalid characters, throw exception
            if(ex.getClass() == InvalidParameterException.class){
                throw new InvalidParameterException("Decimal numbers must be between 1-3999");
            }

            // If it isn't a decimal, continue
        }

        // If it is a decimal
        if(isDecimal) {
            // Parse string to int
            int number = Integer.parseInt(userInput);

            // And convert
            return decimalToNumeral(number);
        } else {

            // If not a decimal, convert the numeral to a decimal
            return numeralToDecimal(userInput);
        }
    }


    /**
     * Convert a decimal number to a numeral
     * - it is confirmed to be decimal in previous methods
     *
     * @param number integer to convert to roman numeral
     * @return String value of the roman numeral
     */
    public String decimalToNumeral(int number) {

        // Reset previous numeral result if it existed
        numeralResult = new StringBuilder();

        // For each numeral
        for (Map.Entry<String, Numeral> entry : numerals.entrySet()) {

            // Check if numeral should be added, if so, collect remainder
            int newNum = checkNumeral(entry.getValue(), number);

            // If numeral was added, reset number to pass into checkNumeral method
            if(newNum != -1) {
                number = newNum;
            }
        }

        return numeralResult.toString();
    }

    /**
     * Checks if current numeral goes into number
     *
     * @param numeral current numeral to check
     * @param num current number to check
     * @return remainder, if exists, -1 if numeral doesn't go into number
     */
    public int checkNumeral(Numeral numeral, int num) {

        // If the remainder in 1, 2, or 3 add corresponding roman numerals
        switch (num) {
            case 1 -> {
                numeralResult.append(I.numeral);
                return 0;
            }
            case 2 -> {
                numeralResult.append(I.numeral).append(I.numeral);
                return 0;
            }
            case 3 -> {
                numeralResult.append(I.numeral).append(I.numeral).append(I.numeral);
                return 0;
            }
        }

        // Calculate remainder
        int newNum = num % numeral.value;

        // If the remainder is less than the current number,
        // and the numeral goes into the number exactly once
        if((newNum) < num && newNum == (num - numeral.value)) {
            // Add the current numeral to our result and return the new number
            numeralResult.append(numeral.numeral);
            return newNum;
        }

        // Else if the numeral goes into the number more than once
        else if(newNum < (num - numeral.value)) {

            // Add the numeral to the result the correct number of times
            int timesToAdd = num / numeral.value;
            numeralResult.append(String.valueOf(numeral.numeral).repeat(Math.max(0, timesToAdd)));
            return newNum;
        }

        // If the numeral doesn't go into the number, return -1 as a pseudo-boolean
        return -1;

    }

    /**
     * Convert a roman numeral to a decimal
     *
     * @param numeral numeral to convert to a decimal
     * @return String value of the decimal
     */
    public String numeralToDecimal(String numeral) {

        decimalResult = 0;

        // Make sure the numeral is valid
        List<Numeral> checkedNumerals = areValidNumerals(numeral);

        // If the numeral is invalid throw exception
        if(Objects.isNull(checkedNumerals)){
            throw new InvalidParameterException("This is an invalid Roman numeral.");
        } else {
            // Else, add all the numerals together and return the value as a String
            for(Numeral checkedNumeral : checkedNumerals){
                decimalResult += checkedNumeral.value;
            }

            return String.valueOf(decimalResult);
        }

    }

    /**
     * Check if a certain numeral is a subtractive
     *
     * @param check numeral to check
     * @return true or false depending on if the numeral is a subtractive
     */
    public boolean isSubtractive(String check){
        return subtractivesAsStrings.contains(check);
    }

    /**
     * Checks to see if input is a valid decimal number
     *
     * @param input user input
     * @return true or false if it is a valid decimal number or not
     * @throws InvalidParameterException if the user's input isn't within the specified range
     * @throws NumberFormatException if the user's input isn't a decimal
     */
    public boolean isValidDecimal(String input) throws InvalidParameterException, NumberFormatException {

        // Attempt to parse the input to a decimal
        int number = Integer.parseInt(input);

        // If it parsed successfully, check to see if it in the range
        if(number > MAX || number < 1) {
            throw new InvalidParameterException();
        }

        return true;
    }

    /**
     * Checks if the numeral is valid
     *
     * @param input current numeral to check
     * @return true or false if it exists in map
     */
    private boolean isValidNumeral(String input) {
        return numerals.containsKey(input);
    }

    /**
     * Parses numerals into a list,
     * checks to see if numerals are in the correct order
     *
     * @param input user's numeral to convert
     * @return List of numerals contained in user's numeral
     */
    private List<Numeral> areValidNumerals(String input){
        // Resets numeralsAsStrings to contain all valid numerals and valid frequencies
        numeralsAsStrings = new ArrayList<>(Arrays.asList(
                "M", "M", "M", "CM", "D", "CD", "C", "C", "C", "XC", "L",
                "XL", "X", "X", "X", "IX", "V", "IV", "I", "I", "I"));

        char[] numeralArray = input.toCharArray();

        List<Numeral> checkNumerals = new ArrayList<>();

        // For each numeral in the array
        for(int i = 0; i < numeralArray.length; i++){

            // Check to see if, when paired up with next numeral, creates a subtractive
            String subtractiveCheck = "";
            try{
                subtractiveCheck = String.valueOf(numeralArray[i]) + numeralArray[i+1];

                // If the index goes out of bounds, ignore, we know it is the last one
            } catch (ArrayIndexOutOfBoundsException ignored){}

            // Get current numeral as string
            String numeralAsString = Character.toString(numeralArray[i]);

            // If the numeral and the next numeral are a subtractive
            if(isSubtractive(subtractiveCheck)){
                // And we haven't added it before
                if(numeralsAsStrings.contains(subtractiveCheck)){
                    // Add current numeral and next to the List
                    checkNumerals.add(numerals.get(subtractiveCheck));

                    // Remove subtractive from List, it cannot occur again
                    numeralsAsStrings.remove(subtractiveCheck);

                    // Step past used numerals
                    i++;
                } else {
                    // If user used a subtractive more than once, throw exception
                    throw new InvalidParameterException("This is an invalid Roman numeral.");
                }

            }

            // If it isn't a subtractive, check to see if it is a valid numeral
            else if(isValidNumeral(numeralAsString)) {
                // If it is a valid single numeral
                if(numeralsAsStrings.contains(numeralAsString)){
                    // Add it to list and remove from possible numerals
                    checkNumerals.add(numerals.get(numeralAsString));
                    numeralsAsStrings.remove(numeralAsString);
                } else {
                    // If numeral used too many times, throw exception
                    throw new InvalidParameterException("This is an invalid Roman numeral.");
                }
            } else {
                // If isn't a valid numeral, throw exception
                throw new InvalidParameterException("This doesn't contain only Roman numerals.");
            }
        }

        // Assert that each numeral is greater than the one before
        for(int i = 0; i < checkNumerals.size() - 1; i++){
            if(checkNumerals.get(i).isLessThan(checkNumerals.get(i+1))){
                // If not, return a null List
                return null;
            }
        }

        return checkNumerals;
    }



}
