Range expression support for Java
=================================

```
(2:5) [-1:1.5] 7 -1
```

## Download

[Download JAR library →](out/artifacts/range-expression/range-expression.jar)

## Example

```java
public static void main(String[] args) {
    String input = "(-2.3:-0.5) [ -.7 : +2.5 ] 42.23";
    Range.Array ranges = Range.match(input);
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
