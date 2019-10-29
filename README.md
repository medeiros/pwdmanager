# Pwdmanager

[![TravisCI](https://travis-ci.org/medeiros/pwdmanager.svg?branch=master)](https://travis-ci.org/medeiros/pwdmanager)
[![Code Size](https://img.shields.io/github/languages/code-size/medeiros/pwdmanager)](https://img.shields.io/github/languages/code-size/medeiros/pwdmanager)
[![License](https://img.shields.io/github/license/medeiros/pwdmanager)](https://img.shields.io/github/license/medeiros/pwdmanager)

Password management tool, using Java Web, Spring, Redis, Vue.js, DDD and
TDD technologies.

## Installation

Redis must be installed. If not yet, it can be done in Arch Linux with:

    $ sudo pacman -S redis

This is a Maven application, that can be installed with:

    mvn clean package

## Usage

This is a simple web application. It's main endpoint can be just
called in a Web browser, as explained in the 'Run' section, below.

#### Run

First of all, Redis server must be up and running. This can be done
in Arch Linux by running the following:

    $ redis-server

After that, the application must be started with a single jar call:

    $ java -jar <jarfile>

This will execute embedded Tomcat, that will create a web app in port
8080. So, the application can be called by any Web browser in the
following URL:

    http://localhost:8080/

##### Run tests

You can type:

    $ mvn clean test

To run unit tests; and

    $ mvn clean integration-test

To run both unit and integration tests.

## Design choices

Some important design choices are defined in the table below:

|Category|Topic|Description|References
|---|---|---|---|
|Architecture & Design|DDD layered architecture|Adopted as a way decouple the domain problem (transactions, accounts and its business rules) from different levels of technical elements (json parsing, input redirection), so that the domain can evolve regardless of technical layers and those can be changed later without any impact to the domain. According to DDD layer theory, there are four layers (presentation, application, domain and infrastructure) and, as dependency rule, each layer cannot access the above layer. |[archfirst]|
|Architecture & Design|Web application|A web application that decouples front-and and back-end. Using Vue.js in the front and Spring (Boot+MVC) in the backend.|[spring-boot] [spring-mvc] [vue.js]|
|Architecture & Design|Key-Value NoSQL database|Using Redis as storage technology (and Jedis as Java client). The key-value model seems a good fit for password management data.|[redis] [jedis] [spring-data-redis] [baeldung-spring-redis]|
|Code best practices|Reducing boilerplate code|Using *lombok* library as a way to remove boilerplate code in Java. Lombok can generate getters, setters, builders and others, during compile-time, based on annotations.|[lombok]|
|Code best practices|TDD|A way to design and build classes that do just the necessary and nothing more.|[tdd]|
|Code best practices|Integration tests|It's a good practice to test code altogether with Redis in an embedded environment designed just for tests. This way, the code is constructed and validated before put into production.|[emb-redis-server]|
|VCS|Git Branching Model|A branch model is an organized way to structure a git project for further collaboration.|[git-branching]|
|VCS|Git Commit|Adopting best practices in Git commit messages.|[chris-beams]|

[redis]: https://redis.io/
[jedis]: https://github.com/xetorthio/jedis
[lombok]: https://projectlombok.org
[tdd]: https://www.amazon.com/Test-Driven-Development-Kent-Beck/dp/0321146530
[git-branching]: https://nvie.com/posts/a-successful-git-branching-model/
[chris-beams]: https://chris.beams.io/posts/git-commit/
[archfirst]: https://archfirst.org/domain-driven-design-6-layered-architecture/
[spring-boot]: https://spring.io/projects/spring-boot
[spring-data-redis]: https://docs.spring.io/spring-data/data-redis/docs/current/reference/html/#reference
[baeldung-spring-redis]: https://www.baeldung.com/spring-data-redis-tutorial
[spring-mvc]: https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html
[vue.js]: https://vuejs.org/
[emb-redis-server]: https://github.com/kstyrc/embedded-redis


#### Logical structure: layers

The packages' structure (in `src/main/java` dir) was designed as DDD layering (above described),
and it is as following:

|Package/Layer|Function|Allowed to access|Currently accessing
|---|---|---|---|
`br.com.arneam.pwdmanager.presentation` | Responsible to interact with the external world (users). This is where web pages are located. | itself, `application`, `domain` and `infrastructure` layers|itself and `application` layer
`br.com.arneam.pwdmanager.application` | Responsible to serialize and deserialize data, expose REST services (as entry-points to the presentation layer), and orchestrate calls to domain's aggregated roots. This layer is stateless.|itself, `domain` and `infrastructure`|itself, `domain` and `infrastructure` layers
`br.com.arneam.pwdmanager.domain` |  Responsible to implement business rules. | itself and `infrastructure` layer | itself and `infrastructure` layer
`br.com.arneam.pwdmanager.infrastructure` | Responsible to interact with external world (data, integration with third party tools, etc) and to keep useful objects for the domain that are not part of the domain layer. | itself | itself

## License

Copyright © 2019 medeiros

Distributed under the MIT License.
