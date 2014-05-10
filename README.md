## What this is

This is a support package for [grappa](https://github.com/parboiled1/grappa).

Its goal is to include debugging tools:

* source code generation from a parser's bytecode using
  [procyon](https://bitbucket.org/mstrobel/procyon/);
* performance testing using [caliper](https://code.google.com/p/caliper/).

**Requires a JDK 1.7**: procyon doesn't run on Java 6.

## Note about the usage of caliper

Unfortunately, for now, I have not found a way to do anything other than:

* clone, compile and provide it in this package;
* use [the webapp](https://microbenchmarks.appspot.com) to publish results.

The Caliper run API is unfortunately very poorly documented.

