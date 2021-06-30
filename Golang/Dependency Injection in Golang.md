---
title: "Dependency Injection" in Golang
categories:
- Golang
tags:
- golang
- DI
date: 2021/6/30
---



# "Dependency Injection" in Golang

> [www.openmymind.net](https://www.openmymind.net/Dependency-Injection-In-Go/)

The worst kept secret about unit testing in general, and TDD specifically, is that it's primarily a design activity with future correctness as a side effect. Unit tests don't uncover all design problems, but they are effective with respect to cohesion and coupling issues and the rather significant principals that relate to these.

One thing I've noticed from programmers who've spent most of their lives with dynamic languages is that, even after years, they've rarely learned the basic design lessons unit testing teaches. Why? Because the very nature of dynamic languages mean that decoupling is a language facility as opposed to a design concern. Why go through the hassle of using various inversion of control approaches and frameworks when you can rewrite the behavior of any thing, at any time.

I'm no fan of the ceremony surrounding Inversion of Control and the impact some of the most popular types, like Dependency Injection, has on production code. But, having never learned the cost of low cohesion and tight coupling from unit testing, dynamic programmers often violate sound design.

Since I've already learned the tough love lessons that C# and Java have to teach, I've always been weary of going back to a static language. Just because I think it's important to learn and feel, doesn't mean I ever want to go back to that hell. Despite this weariness, most of my time is now spent in Go; which begs the question, how does Go approach the problem?

First, I'm not sure if this is considered idiomatic Go, but it's definitely what feels the most natural to me. It all comes down to two important facts. First, functions are first class citizens. Second, tests have access to the internal package members. Combined, we can keep our production code relatively lightweight, while letting our tests focus on the unit.

Consider the following banality:

```go
func AverageVolume(symbol string, day time.Time) float64 {
  volumes, err := getVolumes(stock, day)
  if err != nil { return 0 } 

  total := float64(0)
  for _, volume := range volumes { total += volume }
  return total / float64(len(volumes))
}

func getVolumes(symbol string, day time.Time) ([]float64, error) {
  resp, err := http.Get("http://blah.com/volume/" + symbol + ".json?day=" + day.Format("2006-01-02"))
  if err != nil { return nil, err }
  defer resp.Body.Close()
  body, _ := ioutil.ReadAll(resp.Body)
  ....
}
```

Whatever language you are using, `AverageVolume` is tightly coupled to a web service. In a dynamic language, you could mock the call to `getVolume`, or `http.Get` (which doesn't make it any less tightly coupled, it just makes the coupling less of a painful reality). In a static language, you'd find a way (e.g. DI) to make `AverageVolume` work against an interface, which could be swapped out when testing.

In Go, you can change how `getVolumes` is defined:

```go
var getVolumes = func(symbol string, day time.Time) ([]float64, error) {
  resp, err := http.Get("http://blah.com/volume/" + symbol + ".json?day=" + day.Format("2006-01-02"))
  ....
}
```

In production, this code just works. For tests, you can now change the private `getVolumes`:

```go
// Note, this is more verbose than it needs to be

// More of a mock, where we want to specifically test the interaction between
// AverageVolume and its dependency (getVolumes). A lot of people prefer
// to leave this for integration tests
func TestProperlyGetsAllTheVolumes(t *testing.T) {
  var called bool
  now := time.Now()
  getVolumes = func(symbol string, day time.Time) ([]float64, error) {
    if symbol != "spice" { t.Errorf("Expected %v to equal spice", symbol)}
    if day != now { t.Errorf("Expected %v to equal spice", symbol)}
    called = true
    return nil, nil
  }
  AverageVolume("spice", now)
  if called == false { t.Error("GetVolumes should have been called") }
}

// More of a stub, where we test a behavior of AverageVolume
func TestReturnsZeroOnError(t *testing.T) {
  getVolumes = func(symbol string, day time.Time) ([]float64, error) { return nil, errors.Error("some error") }
  actual := AverageVolume("spice", time.Now())
  if actual != 0 { t.Errorf("Expected %f to equal 0", actual)}
}

// More of a stub, where we test a behavior of AverageVolume
func TestReturnsTheAverage(t *testing.T) {
  getVolumes = func(symbol string, day time.Time) ([]float64, error) { return []float64{3,4,6,7,9}, nil }
  actual := AverageVolume("spice", time.Now())
  if actual != 5.8 { t.Errorf("Expected %f to equal 5.8", actual)}
}
```

The above is possible in any language where functions are first class citizens (of note, lambdas make it cleaner). However, by giving test packages access to private members, Go makes it a little cleaner and less intrusive.

I find this approach strikes a balance between the typical extremes of the dynamic and static worlds. It's much less involved than what you typically see in Java/C#, but it isn't free either. There's enough pain to make bad design smell. It isn't a perfect balance, but for now, I'm anxious to see how far we can take it.

