package pl.alyx.range;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Range {

    //region Array

    public static class Array extends ArrayList<Range> {

        private String extractExpression = "" +
                "  (?<minimumInclude>  [\\(\\[]  )\n" +
                "    \\s*\n" +
                "    (?<minimumValue>  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  )?\n" +
                "  \\s* : \\s*\n" +
                "    (?<maximumValue>  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  )?\n" +
                "    \\s*\n" +
                "  (?<maximumInclude>  [\\)\\]]  )\n" +
                "|\n" +
                "  (?<constantValue>\n" +
                "    (?:  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  ) \n" +
                "  )";

        private String validateExpression = "" +
                "^\n" +
                "(?:\n" +
                "\\s*\n" +
                "(?:\n" +
                "  [\\(\\[]\n" +
                "    \\s*\n" +
                "    (?:  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  )?\n" +
                "  \\s* : \\s*\n" +
                "    (?:  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  )?\n" +
                "    \\s*\n" +
                "  [\\)\\]]\n" +
                "|\n" +
                "  (?:\n" +
                "    (?:  [\\-\\+]? (?:[0-9]+(?:\\.[0-9]*)?|[0-9]*\\.[0-9]+)  ) \n" +
                "  )\n" +
                "\n" +
                ")\n" +
                ")+\n" +
                "$\n";

        private Array setExtractExpression(String extractExpression) {
            this.extractExpression = extractExpression;
            return this;
        }

        private Array setValidateExpression(String validateExpression) {
            this.validateExpression = validateExpression;
            return this;
        }

        @Override
        public String toString() {
            List<String> array = new ArrayList<>();
            for (Range range : this) {
                array.add(range.toString());
            }
            return String.join(" ", array);
        }

        /***
         * Check if number matches any range.
         *
         * @param value
         * @return
         */
        public boolean checkAny(double value) {
            for (Range range : this) {
                if (range.check(value)) {
                    return true;
                }
            }
            return false;
        }

        /***
         * Check if value matches all ranges.
         *
         * @param value
         * @return
         */
        public boolean checkAll(double value) {
            for (Range range : this) {
                if (!range.check(value)) {
                    return false;
                }
            }
            return true;
        }

        /**
         * Match text for set of ranges and values.
         *
         * @param input Text containing set of ranges and values like "(2:5) [-1:4.5] 7 -1"
         * @return
         */
        public Array match(String input) {
            if (input == null) {
                return this;
            }
            Pattern pattern = Pattern.compile(this.extractExpression, Pattern.COMMENTS | Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(input);
            while (matcher.find()) {
                String constantValue = matcher.group("constantValue");
                if (constantValue != null && constantValue.length() > 0) {
                    constantValue = constantValue.replace(',', '.');
                    if (constantValue.equals(".")) continue;
                    double value = Double.valueOf(constantValue);
                    Range range = new Range().setConstantValue(value).setConstantSpecified(true);
                    this.add(range);
                } else {
                    String minimumValue = matcher.group("minimumValue");
                    String maximumValue = matcher.group("maximumValue");
                    if (minimumValue == null && maximumValue == null) {
                        continue;
                    }
                    Range range = new Range();
                    if (minimumValue != null && minimumValue.length() > 0) {
                        minimumValue = minimumValue.replace(',', '.');
                        if (!minimumValue.equals(".")) {
                            double value = Double.valueOf(minimumValue);
                            range.setMinimumSpecified(true).setMinimumValue(value);
                        }
                    }
                    String minimumInclude = matcher.group("minimumInclude");
                    if (minimumInclude != null && minimumInclude.equals("[")) {
                        range.setMinimumInclude(true);
                    }
                    if (maximumValue != null && maximumValue.length() > 0) {
                        maximumValue = maximumValue.replace(',', '.');
                        if (!maximumValue.equals(".")) {
                            double value = Double.valueOf(maximumValue);
                            range.setMaximumSpecified(true).setMaximumValue(value);
                        }
                    }
                    String maximumInclude = matcher.group("maximumInclude");
                    if (maximumInclude != null && maximumInclude.equals("]")) {
                        range.setMaximumInclude(true);
                    }
                    if (range.isMinimumSpecified() || range.isMaximumSpecified()) {
                        this.add(range);
                    }
                }
            }
            return this;
        }

        public boolean isValid(String input) {
            if (input == null || input.trim().length() == 0)
                return false;
            Pattern pattern = Pattern.compile(this.validateExpression, Pattern.COMMENTS | Pattern.MULTILINE);
            return pattern.matcher(input).find();
        }
    }

    //endregion

    //region Constructor

    public Range() {
    }

    //endregion

    //region Private

    private double minimumValue;
    private double maximumValue;

    private boolean minimumSpecified;
    private boolean maximumSpecified;

    private boolean minimumInclude;
    private boolean maximumInclude;

    private double constantValue;
    private boolean constantSpecified;

    private int maximumFractionDigits = 16;

    //endregion

    //region Property

    public double getMinimumValue() {
        return minimumValue;
    }

    public Range setMinimumValue(double minimumValue) {
        this.minimumValue = minimumValue;
        return this;
    }

    public double getMaximumValue() {
        return maximumValue;
    }

    public Range setMaximumValue(double maximumValue) {
        this.maximumValue = maximumValue;
        return this;
    }

    public boolean isMinimumSpecified() {
        return minimumSpecified;
    }

    public Range setMinimumSpecified(boolean minimumSpecified) {
        this.minimumSpecified = minimumSpecified;
        return this;
    }

    public boolean isMaximumSpecified() {
        return maximumSpecified;
    }

    public Range setMaximumSpecified(boolean maximumSpecified) {
        this.maximumSpecified = maximumSpecified;
        return this;
    }

    public boolean isMinimumInclude() {
        return minimumInclude;
    }

    public Range setMinimumInclude(boolean minimumInclude) {
        this.minimumInclude = minimumInclude;
        return this;
    }

    public boolean isMaximumInclude() {
        return maximumInclude;
    }

    public Range setMaximumInclude(boolean maximumInclude) {
        this.maximumInclude = maximumInclude;
        return this;
    }

    public double getConstantValue() {
        return constantValue;
    }

    public Range setConstantValue(double constantValue) {
        this.constantValue = constantValue;
        return this;
    }

    public boolean isConstantSpecified() {
        return this.constantSpecified;
    }

    public Range setConstantSpecified(boolean constantSpecified) {
        this.constantSpecified = constantSpecified;
        return this;
    }

    public Range setMaximumFractionDigits(int maximumFractionDigits) {
        this.maximumFractionDigits = maximumFractionDigits;
        return this;
    }

    //endregion

    @Override
    public String toString() {
        if (!this.constantSpecified && !this.minimumSpecified && !this.maximumSpecified) {
            return "";
        }
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ROOT));
        df.setMaximumFractionDigits(this.maximumFractionDigits);
        if (this.constantSpecified) {
            return String.format(df.format(this.constantValue));
        } else {
            StringBuilder s = new StringBuilder();
            s.append(this.minimumInclude ? "[" : "(");
            if (minimumSpecified) {
                s.append(df.format(this.minimumValue));
            }
            s.append(":");
            if (this.maximumSpecified) {
                s.append(df.format(this.maximumValue));
            }
            s.append(this.maximumInclude ? "]" : ")");
            return s.toString();
        }
    }

    /***
     * Check if value is in range between minimum and maximum or constant value.
     *
     * @param value Number to be checked
     * @return Boolean true if value is in range
     */
    public boolean check(double value) {
        if (!this.constantSpecified && !this.minimumSpecified && !this.maximumSpecified) {
            return false;
        }
        if (this.constantSpecified) {
            return value == this.constantValue;
        } else {
            if (this.minimumSpecified) {
                if (this.minimumInclude) {
                    if (value < this.minimumValue) {
                        return false;
                    }
                } else {
                    if (!(value > this.minimumValue)) {
                        return false;
                    }
                }
            }
            if (this.maximumSpecified) {
                if (this.maximumInclude) {
                    if (value > this.maximumValue) {
                        return false;
                    }
                } else {
                    if (!(value < this.maximumValue)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    public Range normalize() {
        if (this.minimumSpecified && this.maximumSpecified) {
            if (this.minimumValue > this.maximumValue) {
                boolean previousInclude = this.minimumInclude;
                this.minimumInclude = this.maximumInclude;
                this.maximumInclude = previousInclude;
                double previousValue = this.minimumValue;
                this.minimumValue = this.maximumValue;
                this.maximumValue = previousValue;
            } else if (this.minimumValue == this.maximumValue) {
                if (!this.minimumInclude || !this.maximumInclude) {
                    this.constantSpecified = false;
                    this.minimumSpecified = false;
                    this.maximumSpecified = false;
                } else {
                    this.constantValue = this.minimumValue;
                    this.constantSpecified = true;
                }
            }
        }
        if (!this.minimumSpecified) {
            if (this.minimumInclude) {
                this.minimumInclude = false;
            }
        }
        if (!this.maximumSpecified) {
            if (this.maximumInclude) {
                this.maximumInclude = false;
            }
        }
        return this;
    }

    /**
     * Match text for set of ranges and values.
     *
     * @param input Text containing set of ranges and values like "(2:5) [-1:4.5] 7 -1"
     * @return New array of range objects
     */
    public static Array match(String input) {
        return new Array().match(input);
    }

    /***
     * Check if input text is valid string containing only one or more range expressions.
     *
     * @param input
     * @return
     */
    public static boolean isValid(String input) {
        return new Array().isValid(input);
    }
}