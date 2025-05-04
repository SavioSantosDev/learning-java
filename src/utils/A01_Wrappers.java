package src.utils;

public class A01_Wrappers {
    public static void main(A02_Strings[] args) {
        // Primitive types:
        byte bytePrimitive = 1;
        short shortPrimitive = 2;
        int intPrimitive = 3;
        long longPrimitive = 4L;
        float floatPrimitive = 5.0f;
        double doublePrimitive = 6.0;
        char charPrimitive = 'a';
        boolean booleanPrimitive = true;
        /*
          The above code demonstrates the use of primitive types in Java. Primitive types
          are the most basic data types in Java and are not objects. They are stored
          directly in memory and have a fixed size.
          The primitive types in Java include:
          - byte: 8-bit signed integer (min -128, max 127)
          - short: 16-bit signed integer (min -32,768, max 32,767)
          - int: 32-bit signed integer (min -2,147,483,648, max 2,147,483,647)
          - long: 64-bit signed integer (min -9,223,372,036,854,775,808, max 9,223,372,036,854,775,807)
          - float: 32-bit floating point number (min 1.4E-45, max 3.4028235E38)
          - double: 64-bit floating point number (min 4.9E-324, max 1.7976931348623157E308)
          - char: 16-bit Unicode character
          - boolean: true or false value
         */

        // Wrapper classes with auto-boxing:
        Byte byteWrapper = 1;
        Short shortWrapper = 2;
        Integer intWrapper = 3;
        Long longWrapper = 4L;
        Float floatWrapper = 5.0f;
        Double doubleWrapper = 6.0;
        Character charWrapper = 'a';
        Boolean booleanWrapper = true;
        /*
          The above code demonstrates the use of wrapper classes in Java. Wrapper classes
          are used to convert primitive types into objects. This is useful when you need
          to store primitive types in data structures that only accept objects, such as
          collections (e.g., List, ArrayList, HashMap).
         */

        // Examples of invalid Wrapper class usage:
        // Byte invalidByteWrapper = 128; // Error: incompatible types: possible lossy conversion from int to byte
        // Integer invalidIntWrapper = 3.14; // Error: incompatible types: possible lossy conversion from double to int
        // Double invalidDoubleWrapper = 3; // Error: incompatible types: possible lossy conversion from int to double
        // Character invalidCharWrapper = 3.14; // Error: incompatible types: possible lossy conversion from double to char
        // Boolean invalidBooleanWrapper = 1; // Error: incompatible types: possible lossy conversion from int to boolean


        // Unboxing:
        byte unboxedByte = byteWrapper; // Auto-unboxing
        short unboxedShort = shortWrapper; // Auto-unboxing
        /*
          The above code demonstrates the use of unboxing in Java. Unboxing is the process
          of converting an object of a wrapper class back into its corresponding primitive type.
          This is done automatically by the Java compiler when you assign a wrapper class
          object to a primitive type variable.
         */

        // Examples of invalid unboxing:
        // byte invalidUnboxedByte = byteWrapper + 1; // Error: incompatible types: possible lossy conversion from int to byte
        // short invalidUnboxedShort = shortWrapper + 1; // Error: incompatible types: possible lossy conversion from int to short
        /*
          The above code demonstrates the use of invalid unboxing in Java. Unboxing can
          fail if the value being unboxed is outside the range of the primitive type.
          For example, if you try to unbox a Byte object with a value greater than 127
          into a byte primitive, it will result in a compilation error.
         */

        // Parsing strings to primitive types:
        Byte parsedByte = Byte.parseByte("1");
        Short parsedShort = Short.parseShort("2");
        Integer parsedInt = Integer.parseInt("3");
        Long parsedLong = Long.parseLong("4");
        Float parsedFloat = Float.parseFloat("5.0");
        Double parsedDouble = Double.parseDouble("6.0");
        Character parsedChar = 'a'; // No parsing needed, just assigning a char
        Boolean parsedBoolean = Boolean.parseBoolean("true");

        // Examples of invalid parsing:
        // Byte invalidParsedByte = Byte.parseByte("128"); // Error: NumberFormatException
        // Short invalidParsedShort = Short.parseShort("32768"); // Error: NumberFormatException
        // Integer invalidParsedInt = Integer.parseInt("2147483648"); // Error: NumberFormatException
        // Long invalidParsedLong = Long.parseLong("9223372036854775808"); // Error: NumberFormatException
        /*
          The above code demonstrates the use of parsing in Java. Parsing is the process
          of converting a string representation of a number into its corresponding primitive type.
          This is done using the parse methods provided by the wrapper classes.
          If the string cannot be parsed into the desired type, a NumberFormatException
          will be thrown.
         */

        // Character methods:
        System.out.println("Is 'a' a letter? " + Character.isLetter('a')); // true
        System.out.println("Is '1' a digit? " + Character.isDigit('1')); // true
        System.out.println("Is ' ' a whitespace? " + Character.isWhitespace(' ')); // true
        System.out.println("Is 'a' uppercase? " + Character.isUpperCase('a')); // false
        System.out.println("Is 'A' uppercase? " + Character.isUpperCase('A')); // true
        System.out.println("Is 'a' lowercase? " + Character.isLowerCase('a')); // true
        System.out.println("Is 'A' lowercase? " + Character.isLowerCase('A')); // false
        /*
        List of all Character methods:
          - isLetter(char ch)
          - isDigit(char ch)
          - isWhitespace(char ch)
          - isUpperCase(char ch)
          - isLowerCase(char ch)
          - toUpperCase(char ch)
          - toLowerCase(char ch)
          - toString(char ch)
          - getNumericValue(char ch)
          - charCount(int codePoint)
          - digit(int codePoint, int radix)
         */
    }
}
