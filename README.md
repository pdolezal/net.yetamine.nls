# net.yetamine.nls #

This repository provides a library that offers a more fluent interface for retrieving localized messages than the common `ResourceBundle`. The library does not replace `ResourceBundle` with any own implementation, it rather provides a façade for more alternatives. `ResourceBundle` is just one of possibilities, which is available out of the box.

Notable fact is that the library uses pure Java and just the runtime libraries, no dependency injection, no library nor framework dependency, therefore it might be especially interesting for the code that should be as portable as possible and be adaptable for various frameworks and their localization infrastructure.


## Library design ##

A resource bundle analogue is provided as the `ResourcePackage` interface (or in most cases using its more limited parent, `ResourceProvider`, is sufficient). This interface serves as a factory for various resource types. Resource reference exist as companions of outstanding resource types in order to enable comfortable access to the resources.

The client code uses just a mechanism-agnostic resource references and uses an externally provided resource packages to resolve the resources. Because the resource package is an interface that does not require much, nor specifies any class loader details, it can hide much more easily alternative means like external resources even without using any `ResourceBundle` if not suitable, or even combining more means together.

For better understanding the implications of the design, see examples below.


## Examples ##

The common approach for dealing with resources is to have constants representing resources. Ideally, these constants are *not* just plain names of the resources, but rather proxies or strategies that provide the actual resource on demand. So, it should not be surprising that the constants that we would work with are such smarter objects. However, there are multiple ways of dealing with this starting point – let's see some of them.


### Declaring some resource references ###

One of the classical solution is defining the resource-referring constants as usual constants, often in a dedicated class that provide them for a package or otherwise represent a resource bundle. So, let's do it – the following code snippet shows a declaration of such constants, called in this case *resource references*. For illustrative purposes the comments show the possible content of the particular resource (using the pattern syntax for `MessageFormat` and `ChoiceFormat` from standard Java libraries):  

```{java}
final class Messages {

    // text = Hello {0}, your subscription remains valid for {1} {2}.
    public static final MessageTemplate.Reference TEXT = MessageTemplate.Reference.to("text");
    
    // days = 0#days|1#day|2<days
    public static final IntegerTemplate.Reference DAYS = IntegerTemplate.Reference.to("days");
    
    …
}
```

It is a bit more verbose than a plain string constant with the identifier of a resource, but hopefully not as bad.


### Using the resource references ###

When we have some resource references, we would like to use them. Let's assume that `RESOURCES` contains a reference to a `ResourceProvider` instance (how to get it would be covered later) and `remaining` is an acceptable numeric variable with the number of remaining days. Then following code snippet a message like *Hello Peter, your subscription remains valid for 1 day.* Or perhaps with a different locale: *Halo, Peter, Dein Abonnement läuft in einem Tag ab.*:

```{java}
// Produces properly formatted day amount, e.g., "1 day" or "2 days". 
final String days = DAYS.use(RESOURCES).with(remaining);
// Produces the whole text, with properly formatted days.
final String text = TEXT.use(RESOURCES).with("Peter", days);

System.out.println(text); // Print it. Of course, we could stuff that all in one long statement.
```

Notice the `use`-`with` pattern which allows to fluently specify the source and application of the result. Although the pattern is quite fluent and not so verbose, the code still refers to `RESOURCES` repeatedly, which might become annoying when composing some output from many resources. To mitigate this repetition, it is possible to specify a local implicit resource source. The idea of a local implicit resource source is naturally combined with try-with-resources:

```{java}
try (ResourceContext rc = RESOURCES.context()) {
    System.out.println(TEXT.use().with("Peter", DAYS.use().with(remaining));
}
```

Leaving out the repeated references to `RESOURCES` leads to more compact code. The `use`-`with` pattern is preserved, just `use` now specifies no arguments, relying on the presence of the implicit resource source. Btw. it could be possible to get rid of the `use` invocations completely, but we decided not to do so in order to prevent some confusion and the risk of more likely wrong uses of the construct.


### Declaring resource references revisited ###

Declaring the resource references in the original way works, but still is not very appealing. There are several other ways how to improve it; using an `enum` is quite usual, but maybe not in this way: 

```{java}
enum Title implements ConstantString {
    MR, MS, MRS, MISS;
}
```

That's it all in the source code. When using properties for the resource, the properties could contain following:

```{properties}
MR   = Mr.
MS   = Ms.
MRS  = Mrs.
MISS = Miss
```

And you are done. Using such an `enum` is natural:

```{java}
// Print all the known titles
for (Title title : Title.values()) {
    System.out.println(title.use(RESOURCES));
}
```


### Object resources ###

String-based resources are the most common case of resources that therefore they have special support for easier use. Besides that, they are special because they are often stored in text files like Java properties or XML documents and they can be often dealt with by non-developers as well. Object resources differ: they can't be stored in such a natural way, they need more programmatic support (and therefore developers to mantain them) etc. Fortunately, they are so rare.

However, even for rare cases it is quite good to have at least some support for them. Actually, all string-based resources described above have a relationship to object resources, employing the `ResourceObject` interface. For other resource than these standard string-based resources, arbitrary instances of the `ResourceObject` instances can be created. Object resources have one outstanding specific: their definitions may employ a fallback that is hardwired in the code, therefore the code can be runnable even when no resources are available. This may be often desired with the respect to the nature of object resources.

One outstanding case of real use of an object resource in JDK is related to `java.awt.ComponentOrientation`. Here is an example of dealing with such a resource using our object resources:

```{java}
// Resource constant definition using a fallback value when no resource provider provides the resource
static final ResourceObject<ComponentOrientation> ORIENTATION 
= ResourceObject.constant("orientation", ComponentOrientation.LEFT_TO_RIGHT);

// Using it then is as common as for other resources
if (ORIENTATION.use(RESOURCES).isLeftToRight()) {
    // Draw from left to right
} else {
    // Flip text direction
}
```


### Working with a `ResourcePackage` ###

A `ResourcePackage` instance may be constructed to use a `Supplier` to decide the locale automatically, hence `Locale::getDefault` is the natural choice which works well. But it is possible always to ask for a specific `Locale` instead. Putting the pieces together, a language selection menu could be rendered with similar code:

```{java}
supportedLocales().forEach(locale -> {
    displayLocaleOption(locale, MESSAGE.use(RESOURCES.locale(locale)))
});
```


### Getting a `ResourcePackage` instance ###

Last, but not least: how to get some resource package to work with? Well, this depends on your own decision and on what you want to use as the underlying source of the actual resources. Perhaps you can let your favourite framework inject some into your classes. Or if you are satisfied with usual properties resources, you can use our `ResourceBundleProvider` based on the standard `ResourceBundle` facility. Or you can implement your own solution that fits your needs better. Here we mention briefly our `ResourceBundleProvider`.

As the simplest case, let's use the `Title` type listed above. If we take it as the name of the resource package and let the resources for the package load by the class's `ClassLoader`, then we simply add resources `Title.properties` and possibly localized variants like `Title_de.properties` and load the resource package:

```{java}
// In some class, perhaps in Title itself:
static final ResourcePackage RESOURCES = ResourceBundleProvider.bundle(Title.class);
```

That's it. Otherwise `ResourceBundleProvider` offers several other methods to deal with more complex cases, including possible customization of the loading process, so that it should be possible to hook it into any framework or environment where `ResourceBundle` has at least remote chance to work.


### Cream on the top: resource discovery ###

Resource references have one drawback: they just name the resources that they refer to, while their usage relies on the correctness of the actual resource (e.g., that it does not provide wrong placeholders). Because even the default resources are decoupled from the source, it is quite easy to make a mistake, or perhaps a resource might not be updated according to a change in the source code. The decoupling of the default resources increases the probability that they would be missing as well.

To mitigate these dangers, we provide resource discovery. The idea is that the source code annotates the resource references appopriately with the default values, so that the developer can maintain both the code and the values, and lets a resource package loader construct the default resource package from the annotations:

```{java}
@ResourceStockpile
enum Titles implements ConstantString {
    @ResourceString("Mr.") MR,
    @ResourceString("Ms.") MS,
    @ResourceString("Mrs.") MRS,
    @ResourceString("Miss") MISS;    
}

// The loading the resource package
static final ResourcePackage RESOURCES = ResourceBundleProvider.discover(MethodHandles.lookup(), Title.class);
``` 

The discovery can even aggregate resources from multiple classes into a single resource package, so that it is possible to have multiple `enum`s for different purposes and "implementing" different resource types, while the clients use a single common resource package without being aware of such split. The approach works for usual constant as well (i.e., there is absolutely no requirement to use `enum`s only). 

The discovery enables yet another interesting possibility: the template for translating the resources to other languages can be generated from the sources (well, the tool might be some next project), which we consider to be a better solution than occasional reverse approach when the source code with the resource references is generated from a resource.


### How much does it cost? ###

Since any façade is another layer of indirection, it introduces some (mild) overhead. Direct getting a `ResourceBundle` instance and using it might be a bit faster, but at the cost of less readable code. Another consideration is laziness: this library is as lazy as possible by default and delays both loading a resource and producing any result already on the façade level until the resolution is inevitable. This is especially useful for making various error and logging messages that are a subject of a condition. For the cases when a resource bundle is repeated multiple times in a sequence, it is always possible to request a fully resolved instance; the resource context technique with try-with-resources actually uses the full resolution automatically, therefore comfortable use goes alongside with better performance.


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
