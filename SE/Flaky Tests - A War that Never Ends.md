---
title: Flaky Tests - A War that Never Ends
categories:
- SE
tags:
- Test
date: 2021/6/30
---



# Flaky Tests - A War that Never Ends

### What is a flaky test?

Don’t you hate when things are not deterministic? A test should constantly pass or fail if no code changes are applied. We should run our tests against a controlled environment and make assertions against an expected output. We may use a test fixture as a baseline for running tests. A test fixture is a fixed state so the results should be repeatable. A flaky test is a test which could fail or pass for the same configuration. Such behavior could be harmful to developers because test failures do not always indicate bugs in the code. Our test suite should act like a bug detector. Non-determinism can plague any kind of test, but it’s particularly prone to affect tests with a broad scope, such as acceptance, functional/UI tests.

A good suite of tests should let you decide whether the code is ready to be released. When I have a test suite that I can trust, a successful test run gives me the green light to proceed with a release. It gives me confidence that I can refactor the code safely. In TDD, we should run all our tests after every code change. Sometimes this is not always possible, but at least every now and then we have to run the whole suite of tests. But at least, we have to ensure that all our tests run successfully after committing our changes. If a test constantly fails, this is not a flaky test and must not be confused.

### Common flaky tests

But how you could introduce a flaky test? Let’s see some common reasons a test could be flaky:

- **Concurrency**: Does your test run standalone? Are there any other threads that could affect the flow? In integration tests, maybe some batch jobs running in parallel or a background thread could also disrupt your test under execution. If you run your test against a live system, are there any other external requests that could affect you? Other tests running concurrently? Also, in an asynchronous application, sometimes the order of execution should not be taken for granted.
- **Caching**: Do you cache data? We should consider caches during test development. Sometimes due to time manipulation (time travel), cache evictions or stale data, the outcome of the test may become unpredictable.
- **Setup — Cleanup state**: Are you tidying things up? A good test should always setup its expected environment and always cleaning up any custom behavior to a vanilla state. This is one of most difficult flaky test to identify since it is not the one that fails, but consecutive tests could get affected.
- **Dynamic content**: Usually, when you test UI, you want your tests to run fast. But sometimes, a test might need to wait for dynamic content to load first. Some asynchronous calls to load data can impose a delay. You should have in mind that tests would run much faster rather in case of human interaction.
- **Time bombs**: Does your test requests for the current time? In which timezone? You should not make assumptions that your test will always run in the same timezone as developed. Do you measure time intervals accurately? Let’s say, your test gathers the events of the day. The number of the events will change often. You have to follow the same logic as your class under test. What if this test runs near midnight? If your test is time bounded with the current time, you have to keep in mind for all special occasions (e.g. you might not be able to run your integration tests during overnight system’s maintenance periods). Always bear in mind that a test will live in your test suite for many years after and will run many many times, in all possible moments.
- **Infrastructure issues**: Sometimes, it is not your test that is flaky. Your test might fail for external reasons. A bug in the testing framework, selenium driver or a problem with that browser version could waste you a lot of time while trying to figure out what is wrong with your test. Other random incidents, like Continuous Integration (CI) node failures, network issues, database outage etc. are usually more easy to spot.
- **3rd party systems**: Is everything under your control? Integration tests that do not run against a stubbed external environment, inevitably depend on 3rd party systems. You are also verifying external systems’ correctness. And I mean every component your systems interacts with. There should be tests that validate the integration with external systems, but those should be few. You should try to stub all external systems when you check the integrity of your system. Those tests are called [integration contract tests](https://martinfowler.com/bliki/IntegrationContractTest.html).

### Understanding the flakiness

[*Continuous Integration*](https://en.wikipedia.org/wiki/Continuous_integration) *is the practice of merging all developer working copies to a shared pipeline several times a day*. A flaky test could block/delay development until spotted and resolved. The problem is that you do not know if you caused the test failure or if it is flaky. There is no easy way to deal with flaky tests. But there are some practices that could help you spot them and deal with them.

### How to spot flaky tests

As a very first step, re-run all failed tests with clean system state. This is an easy way to identify if the failed tests are constantly failing or they are flaky. But a successful re-run does not mean that you can ignore the flaky test. It is an easy way to identify that test is flaky indeed and you have to deal with it. There are tools that support automatic re-running failed tests in development or CI environment that could help you get through.

Place any spotted flaky test in a quarantined area. Teams should follow a strict process when spotting a flaky test. After you record this down, you could also place this test in the quarantined area. This will let others know that this test is possibly flaky and will be investigated. But the main reason is that all other healthy tests will remain in trust. This does not mean that you can postpone the investigation. Shortly someone has to pick this up. You can enforce this by setting either a number limit of quarantined items or a time limit in the quarantine area.

Running tests frequently in scheduled builds at different times of day could reveal flaky tests. It is better to spot a flaky test early rather emerging during a release.

### How to deal with flaky tests

In order to deal with them, you should somehow record all the tests that are flaky. Upon a failure, you have to gather all related data. Logs, memory dumps, system current state or even screenshots in UI tests, that can help you investigate later what went wrong. A ticketing system works fine for storing all that data. This will let you know how many flaky tests are they. You can create a new ticket for that flaky test so someone will pick this up.

When you have identified that a test is flaky, if this test lives long in your codebase, you should try to figure out when it was introduced. As for example, if this test has failed in your CI pipeline again, you can try to find out what code changes could have affected its behavior.

Tests that make assertions on dynamic content have to wait for content to load. Putting a test to sleep for some time is not a good practice. UI tests are slow enough and you don’t want to make them even slower. You could use callbacks if those are provided by the dynamic content provider. If there are no callbacks, you can use polling in small wait intervals. The wait interval is the minimum time that you have to wait when content is not available, thus it should be short. But also, it should be easily configurable. Test run environment could change, so the wait interval will need tweaking over time.

Tests that usually pass but rarely fail, are hard to reproduce. This is where the data that we mentioned earlier that should be gathered can help. Once we spot them, we have what is needed to reproduce the faulty scenario. Another way to investigate those is running the test multiple times till you end up with a failure. Then we should do some post-mortem analysis to identify the root cause. Unfortunately, this is not an always win procedure, but it is free of cost while you are investigating possible reasons.

The best way to deal with time bombs is wrapping the system clock with routines that can be replaced with a seeded value for testing. You can use this [clock stub](https://martinfowler.com/articles/nonDeterminism.html#Time) to time travel to a particular time and frozen at that time, allowing your tests to have complete control over its movements. That way you can synchronize your test data to the values in the seeded clock.

As said, a carelessly written test that does not clear its state after execution could waste you a lot of time, trying to figure out why other tests are failing. Those tests might assume that system is in a vanilla state which also wrong. A way to deal this kind of flakiness is to rerun all your tests in the same order when it failed. A test might pass when running separately and fail under specific execution order. In general, you should configure your tests to run randomly to identify tests that could get affected by other bad written tests. Most testing libraries provide a way to execute tests in random order. Use this option, as it will force you to write more resilient and stable tests.

### A war you can’t win

When having a big suite of tests, it is hard to avoid having flaky tests, especially on UI/integration tests. Usually, the insertion rate is the same as the dealing rate. There should be a level of awareness in the teams about flaky tests and should be part of the [team culture](https://hackernoon.com/the-importance-of-team-culture-af6fffead7b5) to guard the tests. After all, its team’s productivity that gets affected. When you get used to seeing your pipeline red, you inevitably pay less attention to other problems as well. One recurring problematic test becomes unreliable, so unreliable that you ignore whether it passes or fails. To make things worse, others will also look at the red pipeline and notice that the failures are in non-deterministic tests, but soon they’ll lose the discipline to take any actions. Once that discipline is lost, then a failure in the healthy deterministic tests will get ignored too. A red pipeline should be like an alert. It is like the traffic lights. Red means we should not continue the development!

### A test which fails is not always flaky!

As a rule of thumb, if you face a flaky test, do not assume that this is a test problem. You should suspect production code first and then the test. Sometimes a flaky test can be flawless and has just revealed a bug in your code. Just remember, a bug’s best place to hide is a flaky test that developers would assume that something is wrong with the test and not the code.

![img](https://hackernoon.com/hn-images/1*Ksz4cD1ZEvV75j7FlOYKzw.jpeg)

### Further Reading

[Eradicating Non-Determinism in Tests](https://martinfowler.com/articles/nonDeterminism.html)

[No more flaky tests on the Go team](https://www.thoughtworks.com/insights/blog/no-more-flaky-tests-go-team)

[Flaky Tests at Google and How We Mitigate Them](https://testing.googleblog.com/2016/05/flaky-tests-at-google-and-how-we.html)

[How to Deal With and Eliminate Flaky Tests](https://semaphoreci.com/community/tutorials/how-to-deal-with-and-eliminate-flaky-tests)