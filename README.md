# Morn

Morn will be a library for logical programming in Java.

## Installation

To build and install morn run `mvn install`.


## Examples

A working example looks like this (compare [The Art of Prolog](https://mitpress.mit.edu/books/art-prolog)):

```Java
final Functor father = new Functor("father");
final Functor mother = new Functor("mother");
final Functor male = new Functor("male");
final Functor female = new Functor("female");
final Functor son = new Functor("son");
final Functor daughter = new Functor("daughter");
final Functor grandfather = new Functor("grandfather");

final Constant terach = new Constant("terach");
final Constant abraham = new Constant("abraham");
final Constant nachor = new Constant("nachor");
final Constant haran = new Constant("haran");
final Constant isaac = new Constant("isaac");
final Constant lot = new Constant("lot");
final Constant milcah = new Constant("milcah");
final Constant yiscah = new Constant("yiscah");
final Constant sarah = new Constant("sarah");

final Variable x = new Variable("X");
final Variable y = new Variable("y");
final Variable z = new Variable("z");

final KnowledgeBase kb = Morn.buildKB()
    .addFact(father.apply(terach, abraham))
    .addFact(father.apply(terach, nachor))
    .addFact(father.apply(terach, haran))
    .addFact(father.apply(abraham, isaac))
    .addFact(father.apply(haran, lot))
    .addFact(father.apply(haran, milcah))
    .addFact(father.apply(haran, yiscah))
    .addFact(mother.apply(sarah, isaac))
    .addFact(male.apply(terach))
    .addFact(male.apply(abraham))
    .addFact(male.apply(nachor))
    .addFact(male.apply(haran))
    .addFact(male.apply(isaac))
    .addFact(male.apply(lot))
    .addFact(female.apply(sarah))
    .addFact(female.apply(milcah))
    .addFact(female.apply(yiscah))
    .addRule(son.apply(x, y), father.apply(y, x), male.app(x))
    .addRule(daughter.apply(x, y), father.apply(y, x)female.apply(x))
    .addRule(grandfather.apply(x, y), father.apply(x, z)father.apply(z, y));

kb.query(son.apply(isaac, abraham));
kb.query(daughter.apply(isaac, abraham));
kb.query(daughter.apply(milcah, haran));
kb.query(grandfather.apply(terach, isaac));
```

Further examples might be found in [MornTest.java](core/src/test/java/de/henm/morn/core/MornTest.java).