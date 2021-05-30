---
title: Web Security Attacks
categories:
- Web
- Security
tags:
- XSS
- CSRF
date: 2021/5/28 20:00:09
updated: 2021/5/28 12:00:09
---

> *Types of attacks - Web security | MDN*. (2021, May 7). Mozilla.org. https://developer.mozilla.org/en-US/docs/Web/Security/Types_of_attacks#cross-site_scripting_xss

# Security Attacks

## [Cross-site scripting (XSS)](https://developer.mozilla.org/en-US/docs/Web/Security/Types_of_attacks#cross-site_scripting_xss)

Cross-site scripting (XSS) is a security exploit which allows an attacker to inject into a website malicious client-side code. This code is executed by the victims and lets the attackers bypass access controls and impersonate users. According to the Open Web Application Security Project, XSS was the [seventh most common Web app vulnerability](https://owasp.org/www-project-top-ten/2017/Top_10) in 2017.

These attacks succeed if the Web app does not employ enough validation or encoding. The user's browser cannot detect the malicious script is untrustworthy, and so gives it access to any cookies, session tokens, or other sensitive site-specific information, or lets the malicious script rewrite the [HTML](https://developer.mozilla.org/en-US/docs/Glossary/HTML) content.

Cross-site scripting attacks usually occur when 1) data enters a Web app through an untrusted source (most often a Web request) or 2) dynamic content is sent to a Web user without being validated for malicious content.

The malicious content often includes [JavaScript](https://developer.mozilla.org/en-US/docs/Glossary/JavaScript), but sometimes HTML, Flash, or any other code the browser can execute. The variety of attacks based on XSS is almost limitless, but they commonly include transmitting private data like cookies or other session information to the attacker, redirecting the victim to a webpage controlled by the attacker, or performing other malicious operations on the user's machine under the guise of the vulnerable site.

XSS attacks can be put into three categories: stored (also called persistent), reflected (also called non-persistent), or DOM-based.

- **Stored XSS Attacks**

    The injected script is stored permanently on the target servers. The victim then retrieves this malicious script from the server when the browser sends a request for data.

- **Reflected XSS Attacks**

    When a user is tricked into clicking a malicious link, submitting a specially crafted form, or browsing to a malicious site, the injected code travels to the vulnerable website. The Web server reflects the injected script back to the user's browser, such as in an error message, search result, or any other response that includes data sent to the server as part of the request. The browser executes the code because it assumes the response is from a "trusted" server which the user has already interacted with.

- **DOM-based XSS Attacks**

    The payload is executed as a result of modifying the DOM environment (in the victim’s browser) used by the original client-side script. That is, the page itself does not change, but the client side code contained in the page runs in an unexpected manner because of the malicious modifications to the DOM environment.

## [Cross-site request forgery (CSRF)](https://developer.mozilla.org/en-US/docs/Web/Security/Types_of_attacks#cross-site_request_forgery_csrf)

CSRF (sometimes also called XSRF) is a related class of attack. The attacker causes the user's browser to perform a request to the website's backend without the user's consent or knowledge. An attacker can use an XSS payload to launch a CSRF attack.

Wikipedia mentions a good example for CSRF. In this situation, someone includes an image that isn’t really an image (for example in an unfiltered chat or forum), instead it really is a request to your bank’s server to withdraw money:

```html
<img src="https://bank.example.com/withdraw?account=bob&amount=1000000&for=mallory">
```

Now, if you are logged into your bank account and your cookies are still valid (and there is no other validation), you will transfer money as soon as you load the HTML that contains this image. For endpoints that require a POST request, it's possible to programmatically trigger a <form> submit (perhaps in an invisible <iframe>) when the page is loaded:

```html
<form action="https://bank.example.com/withdraw" method="POST">
  <input type="hidden" name="account" value="bob">
  <input type="hidden" name="amount" value="1000000">
  <input type="hidden" name="for" value="mallory">
</form>
<script>window.addEventListener('DOMContentLoaded', (e) => { document.querySelector('form').submit(); }</script>
```

There are a few techniques that should be used to prevent this from happening:

- GET endpoints should be idempotent—actions that enact a change and do not retrieve data should require sending a POST (or other HTTP method) request. POST endpoints should not interchangeably accept GET requests with parameters in the query string.
- A CSRF token should be included in <form> elements via a hidden input field. This token should be unique per user and stored (for example, in a cookie) such that the server can look up the expected value when the request is sent. For all non-GET requests that have the potential to perform an action, this input field should be compared against the expected value. If there is a mismatch, the request should be aborted.
- This method of protection relies on an attacker being unable to predict the user's assigned CSRF token. The token should be regenerated on sign-in.
- Cookies that are used for sensitive actions (such as session cookies) should have a short lifetime with the SameSite attribute set to Strict or Lax. (See SameSite cookies above). In supporting browsers, this will have the effect of ensuring that the session cookie is not sent along with cross-site requests and so the request is effectively unauthenticated to the application server.
- Both CSRF tokens and SameSite cookies should be deployed. This ensures all browsers are protected and provides protection where SameSite cookies cannot help (such as attacks originating from a separate subdomain).

