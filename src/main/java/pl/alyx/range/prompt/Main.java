package pl.alyx.range.prompt;

import pl.alyx.range.Range;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("Example range specifications:");
        System.out.println("    [1:5]      Anything between 1 and 5 including both");
        System.out.println("    (2.3:)     Must be higher than 2.3");
        System.out.println("    [:-5)      Must be lower than -5 excluding it");
        System.out.println();
        try (Scanner scanner = new Scanner(System.in)) {
            String line;
            while (true) {
                System.out.print("Enter range specification > ");
                line = scanner.nextLine();
                if (line.length() == 0) {
                    break;
                }
                if (!Range.isValid(line)) {
                    System.out.println("Invalid range expression");
                }
                Range.Array ranges = Range.match(line);
                if (ranges.isEmpty()) {
                    System.out.println("Range set is empty");
                    continue;
                }

                System.out.println();

                for (Range range : ranges) {
                    String text = range.toString();
                    String extra = "";
                    String normalized = range.normalize().toString();
                    if (text.equals(normalized)) {
                    } else {
                        extra = " normalized to " + normalized;
                    }
                    System.out.printf("%s%s%n", text, extra);
                }

                System.out.println();

                while (true) {
                    System.out.print("Enter value to check > ");
                    line = scanner.nextLine();
                    if (line.length() == 0) {
                        break;
                    }
                    try {
                        double value = Double.parseDouble(line.replace(',', '.'));
                        System.out.println();
                        System.out.printf("Value is %f%n", value);
                        System.out.println();
                        System.out.printf("Check for any is %s%n", ranges.checkAny(value) ? "true" : "false");
                        System.out.printf("Check for all is %s%n", ranges.checkAll(value) ? "true" : "false");
                        System.out.println();
                        for (Range range : ranges) {
                            System.out.printf("Check for %s is %s%n", range, range.check(value) ? "true" : "false");
                        }
                        System.out.println();
                    } catch (NumberFormatException numberFormatException) {
                        System.out.println("Value is not a number");
                        continue;
                    }
                }
            }
        }
    }
}
