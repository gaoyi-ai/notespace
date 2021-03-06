---
title: Understanding Cross-Site Request Forgery (CSRF) and its Mitigations.
categories:
- Web
- Security
tags:
- CSRF
date: 2021/6/25
---



> [goteleport.com](https://goteleport.com/blog/csrf-attacks/)

> Understanding Cross-Site Request Forgery (CSRF) and its Mitigations.

![](https://goteleport.com/blog/images/2021/xss-attacks/csrf.png)

What is a CSRF Attack?
----------------------

Cross-Site Request Forgery (CSRF) attacks allow an attacker to forge and submit requests as a logged-in user to a web application. CSRF exploits the fact that HTML elements send ambient credentials (like cookies) with requests, even cross-origin.

Like XSS, to launch a CSRF attack the attacker has to convince the victim to either click on or navigate to a link. Unlike XSS, CSRF only allows an attacker to make requests to the victim’s origin and does not give the attacker code execution within that origin. This does not mean CSRF attacks are any less important to defend against. As we’ll see in the examples, CSRF can be as dangerous as XSS.

Background of CSRF
------------------

The web originated as a platform to view static documents. Interactivity was added fairly early with the addition of the `POST` verb to HTTP and `<form>` elements to HTML. Support for storing state was added in the form of cookies.

CSRF attacks exploit the following properties of the web: cookies are used to store credentials, HTML elements (unlike JavaScript) are allowed to make cross-origin requests, HTML elements send all cookies (and thus credentials) along all requests.

CSRF puts this all together. The attacker creates a malicious website that contains HTML elements which submit a request to the victim’s origin. When the victim navigates to the attacker’s site, the browser attaches all cookies for the victim’s origin to the request. This makes the request, generated by the attacker, appear to be submitted by the victim.

If you didn’t fully grasp everything above, don’t worry, it’ll make more sense as we cover some examples below.

Exploitable Examples of CSRF Attacks
------------------------------------

### GET Requests

The classic CSRF exploit is usually illustrated with an `HTTP GET` request that changes the state of a web application. `GET` requests that change state are a great candidate for CSRF for two reasons. First, simply loading the site in a browser triggers them. Second, browsers send all cookies, including session cookies, along with the request.

To illustrate this attack, launch the web application in Figure (1). It’s a simple web application that allows changing a user’s email address through a `HTTP GET` request. Because it has no CSRF protection, an attacker can trigger an account takeover (a fun activity enjoyed throughout the world) by simply having the user navigate to a link.

To run this application, save the code from Figure (1) in a file called `csrf1.go` and then type `go run csrf1.go` to run the application. First, navigate to the application at [http://localhost:8080](http://localhost:8080/) to view your logged-in account. Note how the email address associated with your account is initially `bar@example.com`.

To trigger the CSRF vulnerability, imagine an attacker tricks you into clicking on the following link: [http://localhost:8080/update?email=baz@example.com](http://localhost:8080/update?email=baz@example.com) or tricks you into visiting a web page that loads that address in an HTML element (like an `<img>` element).

```
package main

import (
	"fmt"
	"html/template"
	"log"
	"net/http"
	"time"
)

const (
	viewTemplate = `
{{define "T"}}
<p>Email address: {{.}}</p>
<form action="/update" method="get">
  <input  autofocus>
  <input type="submit" value="Update">
</form>
{{end}}`
)

type account struct {
	Name  string
	Email string
}

var sessions map[string]*account

func viewHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html; charset=utf-8")

	// Extract the session cookie.
	cookie, err := getSessionCookie(r)
	if err != nil {
		// This is simply a hack to initialize a session. For a real session, the
		// session cookie would be set after successful login.
		setSessionCookie(w, r)

		// Redirect back to root.
		http.Redirect(w, r, "/", 302)
		return
	}

	// Find account for this logged in session.
	account, ok := sessions[cookie]
	if !ok {
		http.Error(w, "invalid session", 500)
		return
	}

	// Write out template.
	tpl := template.Must(template.New("view").Parse(viewTemplate))
	err = tpl.ExecuteTemplate(w, "T", account.Email)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
}

func updateHandler(w http.ResponseWriter, r *http.Request) {
	// Extract the session cookie and find session for this account.
	cookie, err := getSessionCookie(r)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	account, ok := sessions[cookie]
	if !ok {
		http.Error(w, "account not found", 500)
		return
	}

	// Extract new email address from query parameters.
	emails, ok := r.URL.Query()["email"]
	if !ok {
		http.Error(w, "email missing", 500)
		return
	}
	if len(emails) != 1 {
		http.Error(w, "email missing", 500)
		return
	}

	// Update account.
	account.Email = emails[0]
	sessions[cookie] = account

	// Redirect back to root.
	http.Redirect(w, r, "/", 302)
}

func setSessionCookie(w http.ResponseWriter, r *http.Request) {
	// Set a fake session cookie for this example.
	cookie := http.Cookie{
		Name:    "session",
		Value:   "1dd6fd9ceab04196a5b776d605078877",
		Expires: time.Now().Add(1 * time.Hour),
	}
	http.SetCookie(w, &cookie)
}

func getSessionCookie(r *http.Request) (string, error) {
	for _, cookie := range r.Cookies() {
		if cookie.Name == "session" {
			return cookie.Value, nil
		}
	}
	return "", fmt.Errorf("no session cookie found")
}

func main() {
	sessions = make(map[string]*account)

	sessions["1dd6fd9ceab04196a5b776d605078877"] = &account{
		Name:  "foo",
		Email: "bar@example.com",
	}

	http.HandleFunc("/", viewHandler)
	http.HandleFunc("/update", updateHandler)
	log.Fatal(http.ListenAndServe(":8080", nil))
}


```

_Figure 1: CSRF with a GET request_

Let’s dig into this application further to understand how CSRF works. Once the user visits the site, a session cookie `session=1dd6fd9ceab04196a5b776d605078877` is set in the browser. This may appear contrived but is used to simplify the example — the actual session cookie would be set after the user has successfully authenticated to the web application. The point to note is that all subsequent requests to the web application will now be issued with the cookie `session=1dd6fd9ceab04196a5b776d605078877`.

CSRF happens in the `updateHandler` function. The `updateHandler` appears to be safe. Before executing any application logic, it checks if the session cookie exists. If a cookie exists and matches the cookie stored in the backend, it appears like no vulnerability exists.

However, recall that the browser sends all cookies along with the request. This means the attacker has no need to know the session cookie. By convincing the victim to navigate to the link, the browser will attach the session cookie to the request and issue the authenticated request on behalf of the user triggering the CSRF attack.

### POST Requests

`HTTP GET` requests are not the only requests that can be victim to CSRF. `HTTP POST` requests are just as susceptible even though the attack vector is somewhat different.

Because browsers don’t issue `HTTP POST` requests from addresses entered in the URL bar, the attacker has to convince the victim to navigate to a malicious site that can generate and submit a request for the user.

What is in the request the attacker generates? Recall that HTML elements can make cross-origin requests, which means if the attacker can generate and submit a `<form>` the browser will attach all cookies and submit an authenticated request for the attacker.

To see this in action, the application in Figure (1) has been updated to change state over a `HTTP POST` request. To run this application, save the code from Figure (2) in a file called `csrf2.go` and then type `go run csrf2.go` to run the application. First navigate to the application at [http://localhost:8080](http://localhost:8080/) to view your logged-in account. Note how the email address associated with your account is initially `bar@example.com`.

Change the email address associated with your account and take a look at the request that is sent in debug console of your browser. Just make sure to set it back to bar@example.com once you’re done.

```
package main

import (
	"fmt"
	"html/template"
	"log"
	"net/http"
	"time"
)

const (
	viewTemplate = `
{{define "T"}}
<p>Email address: {{.}}</p>
<form action="/update" method="post">
  <input  autofocus>
  <input type="submit" value="Update">
</form>
{{end}}`
)

type account struct {
	Name  string
	Email string
}

var sessions map[string]*account

func viewHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html; charset=utf-8")

	// Extract the session cookie.
	cookie, err := getSessionCookie(r)
	if err != nil {
		// This is simply a hack to initialize a session. For a real session, the
		// session cookie would be set after successful login.
		setSessionCookie(w, r)

		// Redirect back to root.
		http.Redirect(w, r, "/", 302)
		return
	}

	// Find account for this logged in session.
	account, ok := sessions[cookie]
	if !ok {
		http.Error(w, "invalid session", 500)
		return
	}

	// Write out template.
	tpl := template.Must(template.New("view").Parse(viewTemplate))
	err = tpl.ExecuteTemplate(w, "T", account.Email)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
}

func updateHandler(w http.ResponseWriter, r *http.Request) {
	// Only accept POST requests.
	if r.Method != "POST" {
		http.Error(w, "unsupported verb", 500)
		return
	}

	// Extract the session cookie and find session for this account.
	cookie, err := getSessionCookie(r)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	account, ok := sessions[cookie]
	if !ok {
		http.Error(w, "account not found", 500)
		return
	}

	// Extract new email address from form parameters.
	err = r.ParseForm()
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
	emails, ok := r.Form["email"]
	if !ok {
		http.Error(w, "email missing", 500)
		return
	}
	if len(emails) != 1 {
		http.Error(w, "email missing", 500)
		return
	}

	// Update account.
	account.Email = emails[0]
	sessions[cookie] = account

	// Redirect back to root.
	http.Redirect(w, r, "/", 302)
}

func setSessionCookie(w http.ResponseWriter, r *http.Request) {
	// Set a fake session cookie for this example.
	cookie := http.Cookie{
		Name:    "session",
		Value:   "1dd6fd9ceab04196a5b776d605078877",
		Expires: time.Now().Add(1 * time.Hour),
	}
	http.SetCookie(w, &cookie)
}

func getSessionCookie(r *http.Request) (string, error) {
	for _, cookie := range r.Cookies() {
		if cookie.Name == "session" {
			return cookie.Value, nil
		}
	}
	return "", fmt.Errorf("no session cookie found")
}

func main() {
	sessions = make(map[string]*account)

	sessions["1dd6fd9ceab04196a5b776d605078877"] = &account{
		Name:  "foo",
		Email: "bar@example.com",
	}

	http.HandleFunc("/", viewHandler)
	http.HandleFunc("/update", updateHandler)
	log.Fatal(http.ListenAndServe(":8080", nil))
}


```

_Figure 2: CSRF with a POST request._

To exploit this application, open a text editor and save the contents of Figure(3) to a file called `csrf2.html` and open it in a web browser. Notice how the email address of the account was updated to `baz@example.com` by simply navigating to a malicious site?

```
<form action="http://example.com:8080/update" method="post">
  <input baz@example.com" autofocus>
  <input type="submit" value="Update">
</form>
<script>
  document.forms[0].submit();
</script>

```

_Figure 3: Exploiting a POST CSRF request._

This is a clear illustration of how `<form>` elements are not bound by the same origin policy. The browser will happily submit the form to a different origin and furthermore attach all of a site’s cookies to the request allowing an attacker to gain control of the victim’s account.

Mitigation Techniques
---------------------

### Token-Based Mitigations

Recall the properties that allow CSRF to occur:

1.  HTML elements are allowed to make cross-origin requests.
2.  HTML elements send all cookies with requests.
3.  Cookies are used to store credentials.

Token-based mitigations use an approach that addresses all three issues. Instead of just relying on a single cookie to store credentials, credentials are split across a session cookie and a CSRF cookie. The CSRF cookie used to validate the user is the one submitting the request and not an attacker. Why doesn’t the CSRF cookie fall victim to the same issues as the session cookie? Because with token-based mitigations, the application is updated to add the CSRF token to the request header (which can’t be done by an HTML form element in a technique called double submit).

To update the application in Figure (2), when a session is created for the user, the server will store a CSRF cookie along with the session in the backend. The cookie will also be sent back to the browser in the form of a cookie. The web application will need to be updated to submit all future requests in JavaScript to allow the client application to read in the cookie and attach it as a header. When the web application receives a request, it can compare the cookie in the header with the cookie in the backend. If they match, the server can be sure the request is legitimate.

To see this mitigation in action, save the code from Figure (4) in a file called `csrf3.go` and then type `go run csrf3.go` to run the application.

```
package main

import (
	"context"
	"crypto/rand"
	"crypto/subtle"
	"encoding/base64"
	"encoding/json"
	"fmt"
	"html/template"
	"log"
	"net/http"
	"time"
)

const (
	contextSession = "contextSession"

	scriptContent = `
function getCookie(name) {
   var value = "; " + document.cookie;
   var parts = value.split("; " + name + "=");
   if (parts.length == 2) return parts.pop().split(";").shift();
}

function loginSubmit(){
   fetch("/update", {
      method: "POST",
      credentials: "same-origin",
      headers: {
         "Content-Type": "application/json",
         "X-CSRF-Token": getCookie("csrf")
      },
      body: JSON.stringify({
	     "email": document.getElementById("email").value
      })
   }).then(response => {
      if (response.ok) {
         window.location.replace("/");
         return;
      } else {
         console.log("Request failed: "+response.status)
      }
   }).catch(error => {
      console.log("Request failed: "+error)
   });
}

window.onload = function () {
   document.getElementById("submit").onclick = loginSubmit;
};`

	viewTemplate = `
{{define "T"}}
<script src="script.js"></script>
<p>Email address: {{.}}</p>
<input  autofocus>
<input type="submit" value="Update">
{{end}}`
)

type loginRequest struct {
	Email string `json:"email"`
}

var sessions map[string]*session

type session struct {
	Name  string
	Email string
	Token string
	CSRF  string
}

func viewHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "text/html; charset=utf-8")

	// Find the session, if a session does not exist, just create one. This is
	// a simple hack to initialize a session. For a real session, the session
	// cookie would be set after successful login.
	session, err := getSession(r)
	if err != nil {
		err := createSession(w)
		if err != nil {
			http.Error(w, err.Error(), 500)
			return
		}

		// Redirect back to root.
		http.Redirect(w, r, "/", 302)
		return
	}

	// Write out template.
	tpl := template.Must(template.New("view").Parse(viewTemplate))
	err = tpl.ExecuteTemplate(w, "T", session.Email)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
}

func updateHandler(w http.ResponseWriter, r *http.Request) {
	// Extract session out of context.
	session, ok := r.Context().Value(contextSession).(*session)
	if !ok {
		http.Error(w, "invalid session", 500)
		return
	}

	// Extract new email address from request.
	var lr loginRequest
	err := json.NewDecoder(r.Body).Decode(&lr)
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}

	// Update account.
	session.Email = lr.Email
	sessions[session.Token] = session

	// Redirect back to root.
	http.Redirect(w, r, "/", 302)
}

func scriptHandler(w http.ResponseWriter, r *http.Request) {
	w.Header().Set("Content-Type", "application/javascript; charset=utf-8")

	_, err := w.Write([]byte(scriptContent))
	if err != nil {
		http.Error(w, err.Error(), 500)
		return
	}
}

func authHandler(method string, contentType string, next http.HandlerFunc) http.HandlerFunc {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		// Only accept certain request methods.
		if r.Method != method {
			http.Error(w, "unsupported verb", 500)
			return
		}

		// Extract the session from the request. If the session is valid, attach
		// the session ID to the request context.
		session, err := getSession(r)
		if err != nil {
			http.Error(w, err.Error(), 500)
			return
		}
		ctx := context.WithValue(r.Context(), contextSession, session)

		// Check the content type and only accept JSON requests. This is a security
		// measure to prevent text/plain requests sent from a form that look like
		// JSON to be accepted.
		contentType := r.Header.Get("Content-Type")
		if contentType != contentType {
			http.Error(w, "invalid content type", 500)
			return
		}

		// Check the request was sent with a valid CSRF token.
		csrfToken := r.Header.Get("X-CSRF-Token")
		if subtle.ConstantTimeCompare([]byte(csrfToken), []byte(session.CSRF)) != 1 {
			http.Error(w, "invalid csrf token", 500)
			return
		}

		// Everything checks out, call the actual handler.
		next.ServeHTTP(w, r.WithContext(ctx))
	})
}

func createSession(w http.ResponseWriter) error {
	// Generate random session and CSRF tokens.
	sessionCookie, err := randomNumber()
	if err != nil {
		return err
	}
	csrfCookie, err := randomNumber()
	if err != nil {
		return err
	}

	// Create the session in the data store.
	sessions[sessionCookie] = &session{
		Name:  "foo",
		Email: "bar@example.com",
		Token: sessionCookie,
		CSRF:  csrfCookie,
	}

	// Set the cookies in the response.
	http.SetCookie(w, &http.Cookie{
		Name:     "session",
		Value:    sessionCookie,
		SameSite: http.SameSiteStrictMode,
		Expires:  time.Now().Add(1 * time.Hour),
	})
	http.SetCookie(w, &http.Cookie{
		Name:     "csrf",
		SameSite: http.SameSiteStrictMode,
		Value:    csrfCookie,
		Expires:  time.Now().Add(1 * time.Hour),
	})

	return nil
}

func getSession(r *http.Request) (*session, error) {
	var sessionCookie string

	for _, cookie := range r.Cookies() {
		if cookie.Name == "session" {
			sessionCookie = cookie.Value
		}
	}
	if sessionCookie == "" {
		return nil, fmt.Errorf("no session cookie found")
	}

	// Find account for this logged in session.
	session, ok := sessions[sessionCookie]
	if !ok {
		return nil, fmt.Errorf("no session found")
	}

	return session, nil
}

func randomNumber() (string, error) {
	// Generate a large secure random number.
	buf := make([]byte, 16)
	_, err := rand.Read(buf)
	if err != nil {
		return "", err
	}

	return base64.StdEncoding.EncodeToString(buf), nil
}

func main() {
	sessions = make(map[string]*session)

	http.HandleFunc("/", viewHandler)
	http.HandleFunc("/update", authHandler(http.MethodPost, "application/json", updateHandler))
	http.HandleFunc("/script.js", scriptHandler)

	log.Fatal(http.ListenAndServe(":8080", nil))
}


```

_Figure 4: An application using token-based CSRF mitigation._

Admittedly, this application is much larger than the previous one, but it’s also much safer.

The first change is that the authentication code, for sessions and CSRF tokens, has all been moved to its own wrapper. This has several benefits. It provides a clean split between authentication logic and application login. It allows developers to easily add authenticated handlers. It also makes code auditing and making changes easier by focusing all security critical code in one location.

Another change is how the request is submitted. Instead of using an HTML `<form>` element to submit the request, the client uses the Fetch API to submit JSON requests. This allows the client to add headers to the request, which is how the CSRF token is now added.

To prove to ourselves this application is as safe as we claim, let’s try and write an exploit. The first thing we need to do is somehow use an HTML element to submit a JSON formatted request. We can use [JSON parameter padding](http://blog.opensecurityresearch.com/2012/02/json-csrf-with-parameter-padding.html) to craft a JSON request in a `<form>` element. Save the content of Figure (5) in a file called `csrf3.html` and open it in a web browser.

```
<form action="http://example.com:8080/update" method=post enctype="text/plain" >
<input name='{"email":"baz@example.com"}, "ignore_me":"' value='test"}'type='hidden'  type='hidden'>
<input type=submit>
</form>
<script>
  document.forms[0].submit();
</script>

```

_Figure 5: Attempting to exploit a request with CSRF mitigation._

Notice that while we’re able to submit a JSON-formatted request, the request fails. It fails for two reasons. The first is that that the `Content-Type` is wrong. The second is that the CSRF token no longer validates. Try commenting out those checks if you want to restore CSRF to the application.

### Same-Site Cookies

Same-Site cookies are another defense mechanism against CSRF. Recall the properties that allow CSRF to occur:

1.  HTML elements are allowed to make cross-origin request.
2.  HTML elements send all cookies with request.
3.  Cookies are used to store credentials.

Same-Site cookies tackle issue #2. Same-Site cookies allow developers to restrict which cookies are sent along with a request.

Before getting into the details of Same-Site cookies, let’s cover a little terminology. When a web application sets a cookie on a website, the browser stores a few things. We’ve talked about cookies in terms of key/value data, but cookies also have a domain field. The domain field is used to differentiate first-party cookies and third-party cookies.

First-party cookies are cookies where the domain field on the cookie matches the URL in the address bar. First-party cookies are typically used by the web application itself to store data about the session.

Third-party cookies are cookies where the domain field on the cookie does not match the URL in the address bar. Third-party cookies are typically used by analytics software.

Same-Site cookies have another field that controls if the browser will send first party cookies with a request from an HTML element at a different URL. Let’s examine this with a concrete example.

Suppose you have a website at `example.com` that sets a session cookie with the domain `example.com` and Same-Site value of `lax`. Let’s suppose an attacker at `evil.com` tries to issue a request with an HTML element. Because the domain in the URL does not match the domain in the cookie and Same-Site policy is being enforced, the cookie will not be sent along with the request.

One thing to note, in `lax` mode, cookies are sent along with safe HTTP requests, like `HTTP GET`, but are not sent for potentially unsafe requests like `HTTP POST`. This means if your application has state changes occur over a `HTTP GET` request, Same-Site cookies will not help you.

Same-Site cookies have two additional modes: `strict` and `none`. If `none` is used, nothing changes from today, all cookies will be sent with a request even from a different site. The `strict` mode has some interesting uses that we may cover in a later article.

See the bolded lines in Figure (6) for details on how to set Same-Site cookies in a Go application.

```
// Set the cookies in the response.
http.SetCookie(w, &http.Cookie{
    Name:     "session",
    Value:    sessionCookie,
    SameSite: http.SameSiteLaxMode,
    Domain:   "example.com",
    Expires:  time.Now().Add(1 * time.Hour),
})
http.SetCookie(w, &http.Cookie{
    Name:     "csrf",
    Value:    csrfCookie,
    SameSite: http.SameSiteLaxMode,
    Domain:   "example.com",
    Expires:  time.Now().Add(1 * time.Hour),
})


```

_Figure 6: An example of Same-Site=lax cookies._

Future Work
-----------

The biggest change is that starting with Chrome 80, the Same-Site policy will be `lax` instead of `none`. This is a positive step in the direction of web security. However, since the change only applies to Chrome, that doesn’t mean token-based mitigations can be dropped. Other browsers like Firefox and Safari have not changed their defaults.

Conclusion
----------

We’ve seen how HTML elements can be abused to enable CSRF attacks. However with some changes to your application, you can effectively mitigate CSRF.

If you enjoyed this article, you may want to learn more about another common type of web application vulnerability: [XSS Attacks](https://goteleport.com/blog/xss-attacks/).

**Related Posts**

*   [Preventing SSRF Attacks](https://goteleport.com/blog/ssrf-attacks/)
*   [In Search for a Perfect Access Control System](https://goteleport.com/blog/access-controls/)
*   [Preventing XSS Attacks](https://goteleport.com/blog/xss-attacks/)

