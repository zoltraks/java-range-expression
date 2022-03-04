Range expression support for Java
=================================

This is Java library project which contains classes for matching numeric values in ranges specified by range expression text.

Range expression is simply the list of numbers or ranges specified as pairs of numbers separated by colon between square and round brackets.

```
1 2 3 4 5 6
```

Above is just a list of possible numbers.

```
(2:5) [-1:1.5] 7 -3
```

That one should be read as:

 - "higher than 2" and "less than 5", or...
 - "higher or equal -1" and "less or equal 1.5", or...
 - "equals 7", or...
 - "equals -3".

This library provides checking functionality if number is covered by any range specified this way.

Some ranges might need normalization and this is also supported.

For example ``(4:1)`` would mean "higher than 4" and "less than 1" which is impossible.
After normalization it would be changed to ``(1:4)``.

More complex example of range expression is below.

```
( -2.7182818:+3.) [+.7182818:-3.1415927]
[:.5] [ : .4] [-1:-3] (+4:+9)
[0:0]  [ 1 : 1 ]  (+0:-0)  [0:0.] (+.0:-0.) ( +.0 : -0. )
[ : ] (:) (  :)
[1.:2.] (.7:.3) [-1.:-2.] (-.7:-.3)
[ +0. : -.5 ]
12 +3.1415927 -2.7182818
```

## Download

[Download JAR library →](out/artifacts/range-expression/range-expression.jar)

## Example

```java
public static void main(String[] args) {
    String input = "(-2.3:-0.5) [ -.7 : +2.5 ] 42.23";
    Range.Array ranges = Range.create(input);
    for (Range range : ranges) {
        System.out.println(range.toString());
    }
}
```

## Regexp

Here are expressions used to extract one or more range specifications from text input.

```regexp
(?<minimumInclude>  [\(\[]  )
\s*
  (?<minimumValue>
    [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)
  )?
\s* : \s*
  (?<maximumValue>
    [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)
  )?
\s*
(?<maximumInclude>  [\)\]]  )
|
  (?<constantValue>
    [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)
  ) 
)
```

Next one is used to check validity of input line.

```regexp
^
(?:
\s*
(?:

  [\(\[]
    \s*
    (?:  [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)  )?
  \s* : \s*
    (?:  [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)  )?
    \s*
  [\)\]]
  |
  (?:
    (?:  [\-\+]? (?:[0-9]+(?:\.[0-9]*)?|[0-9]*\.[0-9]+)  ) 
  )

)
)+
$
```
