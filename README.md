## What this is

This is a support package for [grappa](https://github.com/parboiled1/grappa).

Its goal is to include debugging tools:

* source code generation from a parser's bytecode using
  [procyon](https://bitbucket.org/mstrobel/procyon/);
* performance testing using [caliper](https://code.google.com/p/caliper/).

**Requires a JDK 1.7**: procyon doesn't run on Java 6.

## Note about the usage of caliper

Unfortunately, for now, you have to do as follows:

* clone caliper, install it into your local Maven repository;
* use [the webapp](https://microbenchmarks.appspot.com) to publish results.

To clone and install caliper, you need the JDK (of course) and a maven
installation (unfortunately). Then (assuming a Unix system):

```
git clone https://code.google.com/p/caliper/
cd caliper/caliper
mvn install
```

