ooo-util
========

A place for OOO utilities.

This is a public library, so we don't want hopelessly threerings-specific
things in there (put those in the newly slimmed down threerings library). What
we can do is rely on Guava, which is what keeps useful things out of samskivert
and often pushes them into weird places like Narya.

What we can also do is use ooo-util to export the latest version of samskivert
and Guava to all of our other projects. A project that relies on the latest
snapshot of ooo-util can expect to have the latest snapshot of samskivert and
Guava, and when either samskivert or Guava ships a versioned release, we'll
ship a versioned release of ooo-util so that projects can bump to that release
to obtain the latest transitive depends.

We should strongly resist the addition of additional depends to this library,
unless they are indisputably useful to every single library and project in the
whole wide world. Guava made that cut a long while back, and samskivert lingers
with us like an embarrassing uncle, but I don't imagine another universally
useful library is likely to come along in this decade. But if it does, we've
got a place to put it.
