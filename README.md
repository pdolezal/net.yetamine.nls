# net.yetamine.nls #

This repository provides a library that offers a more fluent interface for retrieving localized messages than the common `ResourceBundle`. The library replaces `ResourceBundle` with no own implementation, rather provides a façade for more alternatives. `ResourceBundle` is just one of possibilities, which is available out of the box.

Notable fact is that the library uses pure Java and just the runtime libraries, no deep magic, no dependency injection, no library nor framework dependency, therefore it might be especially interesting for the code that should be as portable as possible and be adaptable for various frameworks and their localization infrastructure.


## Library design ##

A resource bundle analogue is provided as the `ResourcePackage` interface. The interface serves as a factory for the most common resource types: a constant string, a message template and a numeric template. The role of a rich resource identifier in the source have their "reference" counter-parts.

Combined together, the client code uses just a mechanism-agnostic resource references and uses an externally supplied resource packages to resolve the resources. Because the resource package is an interface that does not require much, nor specifies any class loader details, it can hide much more easily alternative means like external resources even without using `ResourceBundle`s if not suitable, or even combining more means together.

For better understanding the implications of the design, see examples below.


## Examples ##

Declaration of resource references (i.e., resource identifiers) in the classical way, the comments shows the content of a classical property file for a `ResourceBundle`:

```{java}
final class Messages {

    // text = Hello {0}, your subscription remains valid for {1} {2}.
    public static final MessageTemplate.Reference TEXT = MessageTemplate.Reference.to("text");
    
    // days = 0#days|1#day|2<days
    public static final IntegerTemplate.Reference DAYS = IntegerTemplate.Reference.to("days");
    
    …
}
```

Assuming that `RESOURCES` contains a reference to `ResourcePackage` and `remaining` the number of remaining days, following code prints a message like *Hello Peter, your subscription remains valid for 1 day.* or perhaps with a different locale *Halo, Peter, Dein Abonnement läuft in einem Tag ab.*:

```{java}
// Produces "1 day" or "2 days" etc.
final String days = DAYS.from(RESOURCES).with(remaining);
// Produces the whole text, with properly formatted days.
final String text = TEXT.from(RESOURCES).with("Peter", remaining, days);

System.out.println(text); // Print it. Of course, we could stuff that all in one statement.
```

Notice the `from`-`with` pattern which allows to fluently specify the source and application of the result.

Well, there is much cooler use, thanks to employing `interface` instead of carving all in `class` types. What about turning an `enum` in a resource stockpile as it is quite usual? Maybe not as usual as this small magic trick:

```{java}
enum Title implements ConstantString {
    MR, MS, MRS, MISS;
}
```

That's it. When using common `ResourceBundle` implementation, define your resources like:

```{properties}
MR   = Mr.
MS   = Ms.
MRS  = Mrs.
MISS = Miss
```

And you are done. Using the `enum` is natural:

```{java}
// Print all the known titles
for (Title title : Title.values()) {
    System.out.println(title.from(RESOURCES));
}
```


## Prerequisites ##

For building this project is needed:

* JDK 8 or newer.
* Maven 3.3 or newer.

For using the built library is needed:

* JRE 8 or newer.


## Licensing ##

The whole content of this repository is licensed under the [CC BY-SA 4.0][CC-BY-SA] license. Contributions are accepted only under the same licensing terms, under the terms of [CC BY 4.0][CC-BY], or under a public domain license (like [CC0][CC0]), so that the work based on the contributions might be published under the CC BY-SA license terms.

[CC-BY-SA]:  http://creativecommons.org/licenses/by-sa/4.0/
[CC-BY]:     http://creativecommons.org/licenses/by/4.0/
[CC0]:       http://creativecommons.org/choose/zero/

[![Yetamine logo](http://petr.dolezal.matfyz.cz/files/Yetamine_small.svg "Our logo")](http://petr.dolezal.matfyz.cz/files/Yetamine_large.svg)
