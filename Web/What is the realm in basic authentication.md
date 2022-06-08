# [What is the "realm" in basic authentication](https://stackoverflow.com/questions/12701085/what-is-the-realm-in-basic-authentication)

[From RFC 1945 (HTTP/1.0)](https://datatracker.ietf.org/doc/html/rfc1945#section-11) and [RFC 2617 (HTTP Authentication referenced by HTTP/1.1)](https://datatracker.ietf.org/doc/html/rfc2617#page-3)

> The realm attribute (case-insensitive) is required for all authentication schemes which issue a challenge. The realm value (case-sensitive), in combination with the canonical root URL of the server being accessed, defines the protection space. These realms allow the protected resources on a server to be partitioned into a set of protection spaces, each with its own authentication scheme and/or authorization database. The realm value is a string, generally assigned by the origin server, which may have additional semantics specific to the authentication scheme.

In short, pages in the same realm should share credentials. If your credentials work for a page with the realm *"My Realm"*, it should be assumed that the same username and password combination should work for another page with the same realm.