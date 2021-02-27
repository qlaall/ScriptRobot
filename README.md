# ScriptRobot

ScriptRobot is your new project powered by [Ktor](http://ktor.io) framework.

<img src="https://repository-images.githubusercontent.com/40136600/f3f5fd00-c59e-11e9-8284-cb297d193133" alt="Ktor" width="100" style="max-width:20%;">

Company website: qlaall Ktor Version: 1.5.2 Kotlin Version: 1.4.10
BuildSystem: [Gradle with Kotlin DSL](https://docs.gradle.org/current/userguide/kotlin_dsl.html)

# Ktor Documentation

Ktor is a framework for quickly creating web applications in Kotlin with minimal effort.

* Ktor project's [Github](https://github.com/ktorio/ktor/blob/master/README.md)
* Getting started with [Gradle](http://ktor.io/quickstart/gradle.html)
* Getting started with [Maven](http://ktor.io/quickstart/maven.html)
* Getting started with [IDEA](http://ktor.io/quickstart/intellij-idea.html)

Selected Features:

* [CORS](#cors-documentation-jetbrainshttpswwwjetbrainscom)
* [DefaultHeaders](#defaultheaders-documentation-jetbrainshttpswwwjetbrainscom)
* [Routing](#routing-documentation-jetbrainshttpswwwjetbrainscom)
* [ContentNegotiation](#contentnegotiation-documentation-jetbrainshttpswwwjetbrainscom)
* [kotlinx.serialization](#kotlinx.serialization-documentation-jetbrainshttpswwwjetbrainscom)
* [HTML DSL](#html-dsl-documentation-jetbrainshttpswwwjetbrainscom)
* [CSS DSL](#css-dsl-documentation-jetbrainshttpswwwjetbrainscom)

## CORS Documentation ([JetBrains](https://www.jetbrains.com))

Enable Cross-Origin Resource Sharing (CORS)

### Description

Ktor by default provides an interceptor for implementing proper support for Cross-Origin Resource Sharing (`CORS`).

### Usage

## Basic usage:

First of all, install the `CORS` feature into your application.

```kotlin
fun Application.main() {
  ...
  install(CORS)
  ...
}
```

The default configuration to the CORS feature handles only `GET`, `POST` and `HEAD` HTTP methods and the following
headers:

```
HttpHeaders.Accept
HttpHeaders.AcceptLanguages
HttpHeaders.ContentLanguage
HttpHeaders.ContentType
```

## Advanced usage:

Here is an advanced example that demonstrates most of CORS-related API functions

```kotlin
fun Application.main() {
  ...
  install(CORS)
  {
    method(HttpMethod.Options)
    header(HttpHeaders.XForwardedProto)
    anyHost()
    host("my-host")
    // host("my-host:80")
    // host("my-host", subDomains = listOf("www"))
    // host("my-host", schemes = listOf("http", "https"))
    allowCredentials = true
    allowNonSimpleContentTypes = true
    maxAge = Duration.ofDays(1)
  }
  ...
}
```

### Options

* `method("HTTP_METHOD")` : Includes this method to the white list of Http methods to use CORS.
* `header("header-name")` : Includes this header to the white list of headers to use CORS.
* `exposeHeader("header-name")` : Exposes this header in the response.
* `exposeXHttpMethodOverride()` : Exposes `X-Http-Method-Override` header in the response
* `anyHost()` : Allows any host to access the resources
* `host("hostname")` : Allows only the specified host to use `CORS`, it can have the port number, a list of subDomains
  or the supported schemes.
* `allowCredentials` : Includes `Access-Control-Allow-Credentials` header in the response
* `allowNonSimpleContentTypes`: Inclues `Content-Type` request header to the white list for values other than simple
  content types.
* `maxAge`: Includes `Access-Control-Max-Age` header in the response with the given max age()

## DefaultHeaders Documentation ([JetBrains](https://www.jetbrains.com))

This feature adds a default set of headers to HTTP responses

### Description

The `DefaultHeaders` feature adds the standard `Server` and `Date` headers into each response. Moreover, you can provide
additional default headers and override the `Server` header.

### Usage

## Installation

To install the `DefaultHeaders` feature, pass it to the `install` function in the application initialization code. This
can be the `main` function ...

```kotlin
import io.ktor.features.*
// ...
fun Application.main() {
  install(DefaultHeaders)
  // ...
}
```

... or a specified `module`:

```kotlin
import io.ktor.features.*
// ...
fun Application.module() {
    install(DefaultHeaders)
    // ...
}
```

The `DefaultHeaders` feature adds the `Server` and `Date` headers into each response. If necessary, you can override
the `Server`, as described in `Override Headers` section.

## Add Additional Headers

To customize a list of default headers, pass a desired header to `install` by using the `header(name, value)` function.
The name parameter accepts an `HttpHeaders` value, for example:

```kotlin
install(DefaultHeaders) {
    header(HttpHeaders.ETag, "7c876b7e")
}
```

To add a custom header, pass its name as a string value:

```kotlin
install(DefaultHeaders) {
    header("Custom-Header", "Some value")
}
```

## Override Headers

To override the `Server` header, use a corresponding `HttpHeaders` value:

```kotlin
install(DefaultHeaders) {
    header(HttpHeaders.Server, "Custom")
}
```

Note that the `Date` header is cached due to performance reasons and cannot be overridden by using `DefaultHeaders`. If
you need to override it, do not install the `DefaultHeaders` feature and use route interception instead.

## Customize Headers for Specific Routes

If you need to add headers for a specific route only, you can append desired headers into a response. The code snippet
below shows how to do this for the `/order` request:

```kotlin
get("/order") {
    call.response.headers.append(HttpHeaders.ETag, "7c876b7e")
}
```

You can learn more about routing in Ktor from [Routing in Ktor](https://ktor.io/docs/routing-in-ktor.html).

### Options

* `header` -- specify default value for the given header()

## Routing Documentation ([JetBrains](https://www.jetbrains.com))

Allows to define structured routes and associated handlers.

### Description

Routing is a feature that is installed into an Application to simplify and structure page request handling. This page
explains the routing feature. Extracting information about a request, and generating valid responses inside a route, is
described on the requests and responses pages.

```application.install(Routing) {
    get("/") {
        call.respondText("Hello, World!")
    }
    get("/bye") {
        call.respondText("Good bye, World!")
    }

```

`get`, `post`, `put`, `delete`, `head` and `options` functions are convenience shortcuts to a flexible and powerful
routing system. In particular, get is an alias to `route(HttpMethod.Get, path) { handle(body) }`, where body is a lambda
passed to the get function.

### Usage

## Routing Tree

Routing is organized in a tree with a recursive matching system that is capable of handling quite complex rules for
request processing. The Tree is built with nodes and selectors. The Node contains handlers and interceptors, and the
selector is attached to an arc which connects another node. If selector matches current routing evaluation context, the
algorithm goes down to the node associated with that selector.

Routing is built using a DSL in a nested manner:

```kotlin
route("a") { // matches first segment with the value "a"
  route("b") { // matches second segment with the value "b"
     get {…} // matches GET verb, and installs a handler
     post {…} // matches POST verb, and installs a handler
  }
}
```

```kotlin
method(HttpMethod.Get) { // matches GET verb
   route("a") { // matches first segment with the value "a"
      route("b") { // matches second segment with the value "b"
         handle { … } // installs handler
      }
   }
}
```kotlin
route resolution algorithms go through nodes recursively discarding subtrees where selector didn't match.

Builder functions:
* `route(path)` – adds path segments matcher(s), see below about paths
* `method(verb)` – adds HTTP method matcher.
* `param(name, value)` – adds matcher for a specific value of the query parameter
* `param(name)` – adds matcher that checks for the existence of a query parameter and captures its value
* `optionalParam(name)` – adds matcher that captures the value of a query parameter if it exists
* `header(name, value)` – adds matcher that for a specific value of HTTP header, see below about quality

## Path
Building routing tree by hand would be very inconvenient. Thus there is `route` function that covers most of the use cases in a simple way, using path.

`route` function (and respective HTTP verb aliases) receives a `path` as a parameter which is processed to build routing tree. First, it is split into path segments by the `/` delimiter. Each segment generates a nested routing node.

These two variants are equivalent:

```kotlin
route("/foo/bar") { … } // (1)

route("/foo") {
   route("bar") { … } // (2)
}
```

### Parameters

Path can also contain parameters that match specific path segment and capture its value into `parameters` properties of
an application call:

```kotlin
get("/user/{login}") {
   val login = call.parameters["login"]
}
```

When user agent requests `/user/john` using `GET` method, this route is matched and `parameters` property will
have `"login"` key with value `"john"`.

### Optional, Wildcard, Tailcard

Parameters and path segments can be optional or capture entire remainder of URI.

* `{param?}` –- optional path segment, if it exists it's captured in the parameter
* `*` –- wildcard, any segment will match, but shouldn't be missing
* `{...}` –- tailcard, matches all the rest of the URI, should be last. Can be empty.
* `{param...}` –- captured tailcard, matches all the rest of the URI and puts multiple values for each path segment
  into `parameters` using `param` as key. Use `call.parameters.getAll("param")` to get all values.

Examples:

```kotlin
get("/user/{login}/{fullname?}") { … }
get("/resources/{path...}") { … }
```

## Quality

It is not unlikely that several routes can match to the same HTTP request.

One example is matching on the `Accept` HTTP header which can have multiple values with specified priority (quality).

```kotlin
accept(ContentType.Text.Plain) { … }
accept(ContentType.Text.Html) { … }
```

The routing matching algorithm not only checks if a particular HTTP request matches a specific path in a routing tree,
but it also calculates the quality of the match and selects the routing node with the best quality. Given the routes
above, which match on the Accept header, and given the request header `Accept: text/plain; q=0.5, text/html` will
match `text/html` because the quality factor in the HTTP header indicates a lower quality fortext/plain (default is 1.0)
.

The Header `Accept: text/plain, text/*` will match `text/plain`. Wildcard matches are considered less specific than
direct matches. Therefore the routing matching algorithm will consider them to have a lower quality.

Another example is making short URLs to named entities, e.g. users, and still being able to prefer specific pages
like `"settings"`. An example would be

* `https://twitter.com/kotlin` -– displays user `"kotlin"`
* `https://twitter.com/settings` -- displays settings page

This can be implemented like this:

```kotlin
get("/{user}") { … }
get("/settings") { … }
```

The parameter is considered to have a lower quality than a constant string, so that even if `/settings` matches both,
the second route will be selected.

### Options

No options()

## ContentNegotiation Documentation ([JetBrains](https://www.jetbrains.com))

Provides automatic content conversion according to Content-Type and Accept headers.

### Description

The `ContentNegotiation` feature serves two primary purposes:

* Negotiating media types between the client and server. For this, it uses the `Accept` and `Content-Type` headers.
* Serializing/deserializing the content in the specific format, which is provided by either the
  built-in `kotlinx.serialization` library or external ones, such as `Gson` and `Jackson`, amongst others.

### Usage

## Installation

To install the `ContentNegotiation` feature, pass it to the `install` function in the application initialization code.
This can be the `main` function ...

```kotlin
import io.ktor.features.*
// ...
fun Application.main() {
  install(ContentNegotiation)
  // ...
}
```

... or a specified `module`:

```kotlin
import io.ktor.features.*
// ...
fun Application.module() {
    install(ContentNegotiation)
    // ...
}
```

## Register a Converter

To register a converter for a specified `Content-Type`, you need to call the register method. In the example below, two
custom converters are registered to deserialize `application/json` and `application/xml` data:

```kotlin
install(ContentNegotiation) {
    register(ContentType.Application.Json, CustomJsonConverter())
    register(ContentType.Application.Xml, CustomXmlConverter())
}
```

### Built-in Converters

Ktor provides the set of built-in converters for handing various content types without writing your own logic:

* `Gson` for JSON

* `Jackson` for JSON

* `kotlinx.serialization` for JSON, Protobuf, CBOR, and so on

See a corresponding topic to learn how to install the required dependencies, register, and configure a converter.

## Receive and Send Data

### Create a Data Class

To deserialize received data into an object, you need to create a data class, for example:

```kotlin
data class Customer(val id: Int, val firstName: String, val lastName: String)
```

If you use `kotlinx.serialization`, make sure that this class has the `@Serializable` annotation:

```kotlin
import kotlinx.serialization.Serializable

@Serializable
data class Customer(val id: Int, val firstName: String, val lastName: String)
```

### Receive Data

To receive and convert a content for a request, call the `receive` method that accepts a data class as a parameter:

```kotlin
post("/customer") {
    val customer = call.receive<Customer>()
}
```

The `Content-Type` of the request will be used to choose a converter for processing the request. The example below shows
a sample HTTP client request containing JSON data that will be converted to a `Customer` object on the server side:

```kotlin
post http://0.0.0.0:8080/customer
Content-Type: application/json

{
  "id": 1,
  "firstName" : "Jet",
  "lastName": "Brains"
}
```

### Send Data

To pass a data object in a response, you can use the `respond` method:

```kotlin
post("/customer") {
    call.respond(Customer(1, "Jet", "Brains"))
}
```

In this case, Ktor uses the `Accept` header to choose the required converter.

## Implement a Custom Converter

In Ktor, you can write your own converter for serializing/deserializing data. To do this, you need to implement
the `ContentConverter` interface:

```kotlin
interface ContentConverter {
    suspend fun convertForSend(context: PipelineContext<Any, ApplicationCall>, contentType: ContentType, value: Any): Any?
    suspend fun convertForReceive(context: PipelineContext<ApplicationReceiveRequest, ApplicationCall>): Any?
}
```

Take a look at
the [GsonConverter](https://github.com/ktorio/ktor/blob/master/ktor-features/ktor-gson/jvm/src/io/ktor/gson/GsonSupport.kt)
class as an implementation example.

### Options

No options()

## kotlinx.serialization Documentation ([JetBrains](https://www.jetbrains.com))

Handles JSON serialization using kotlinx.serialization library

### Description

ContentNegotiation allows you to use content converters provided by the `kotlinx.serialization` library. This library
supports `JSON`, `CBOR`, `ProtoBuf`, and other formats.

### Usage

## Register the JSON Converter

To register the JSON converter in your application, call the `json` method:

```kotlin
import io.ktor.serialization.*

install(ContentNegotiation) {
    json()
}
```

Inside the `json` method, you can access
the [JsonBuilder](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-json/kotlinx-serialization-json/kotlinx.serialization.json/-json-builder/index.html)
API, for example:

```kotlin
install(ContentNegotiation) {
    json(Json {
        prettyPrint = true
        isLenient = true
        // ...
    })
}
```

## Register an Arbitrary Converter

To register an arbitrary converter from the kotlinx.serialization library (such as Protobuf or CBOR), call
the `serialization` method and pass two parameters:

* The required `ContentType` value.
* An object of the class implementing the required encoder/decoder.

For example, you can register
the [Cbor](https://kotlin.github.io/kotlinx.serialization/kotlinx-serialization-cbor/kotlinx-serialization-cbor/kotlinx.serialization.cbor/-cbor/index.html)
converter in the following way:

```kotlin
install(ContentNegotiation) {
    serialization(ContentType.Application.Cbor, Cbor.Default)
}
```

### Options

No options()

## HTML DSL Documentation ([JetBrains](https://www.jetbrains.com))

Generate HTML using Kotlin code like a pure-core template engine

### Description

HTML DSL integrates the `kotlinx.html` library into Ktor and allows you to respond to a client with HTML blocks. With
HTML DSL, you can write pure HTML in Kotlin, interpolate variables into views, and even build complex HTML layouts using
templates.

### Usage

## Send HTML in Response

To send an HTML response, call the `ApplicationCall.respondHtml` method inside the required route:

```kotlin
get("/") {
    val name = "Ktor"
    call.respondHtml {
        head {
            title {
                +name
            }
        }
        body {
            h1 {
                +"Hello from $name!"
            }
        }
    }
}
```

In this case, the following HTML will be sent to the client:

```html
<head>
    <title>Ktor</title>
</head>
<body>
    <h1>Hello from Ktor!</h1>
</body>
```

To learn more about generating HTML using kotlinx.html, see
the [kotlinx.html wiki](https://github.com/Kotlin/kotlinx.html/wiki).

## Templates

In addition to generating plain HTML, Ktor provides a template engine that can be used to build complex layouts. You can
create a hierarchy of templates for different parts of an HTML page, for example, a root template for the entire page,
child templates for a page header and footer, and so on. Ktor exposes the following API for working with templates:

1. To respond with an HTML built based on a specified template, call the `ApplicationCall.respondHtmlTemplate` method.
2. To create a template, you need to implement the `Template` interface and override the `Template.apply` method
   providing HTML.
3. Inside a created template class, you can define placeholders for different content types:

* `Placeholder` is used to insert the content. `PlaceholderList` can be used to insert the content that appears multiple
  times (for example, list items).
* `TemplatePlaceholder` can be used to insert child templates and create nested layouts.

### Example

Let's see the example of how to create a hierarchical layout using templates. Imagine we have the following HTML:

```html
<body>
<h1>Ktor</h1>
<article>
    <h2>Hello from Ktor!</h2>
    <p>Kotlin Framework for creating connected systems.</p>
</article>
</body>
```

We can split the layout of this page into two parts:

* A root layout template for a page header and a child template for an article.
* A child template for the article content.

Let's implement these layouts step-by-step:

1. Call the `respondHtmlTemplate` method and pass a template class as a parameter. In our case, this is
   the `LayoutTemplate` class that should implement the `Template` interface:

```kotlin
get("/") {
    call.respondHtmlTemplate(LayoutTemplate()) {
        // ...
    }
}
```

Inside the block, we will be able to access a template and specify its property values. These values will substitute
placeholders specified in a template class. We'll create LayoutTemplate and define its properties in the next step.

2. A root layout template will look in the following way:

```kotlin
class LayoutTemplate: Template<HTML> {
    val header = Placeholder<FlowContent>()
    val content = TemplatePlaceholder<ContentTemplate>()
    override fun HTML.apply() {
        body {
            h1 {
                insert(header)
            }
            insert(ContentTemplate(), content)
        }
    }
}
```

The class exposes two properties:

* The `header` property specifies a content inserted within the h1 tag.
* The `content` property specifies a child template for article content.

3. A child template will look as follows:

```kotlin
class ContentTemplate: Template<FlowContent> {
    val articleTitle = Placeholder<FlowContent>()
    val articleText = Placeholder<FlowContent>()
    override fun FlowContent.apply() {
        article {
            h2 {
                insert(articleTitle)
            }
            p {
                insert(articleText)
            }
        }
    }
}
```

This template exposes the `articleTitle` and `articleText` properties, whose values will be inserted inside
the `article`.

4. Now we are ready to send HTML built using the specified property values:

```kotlin
get("/") {
    call.respondHtmlTemplate(LayoutTemplate()) {
        header {
            +"Ktor"
        }
        content {
            articleTitle {
                +"Hello from Ktor!"
            }
            articleText {
                +"Kotlin Framework for creating connected systems."
            }
        }
    }
}
```

### Options

No options()

## CSS DSL Documentation ([JetBrains](https://www.jetbrains.com))

Generate CSS using Kotlin code

### Description

`CSS DSL` extends `HTML DSL` and allows you to author stylesheets in Kotlin by using the `kotlin-css` wrapper.

### Usage

To send a CSS response, you need to extend `ApplicationCall` by adding the `respondCss` method to serialize a stylesheet
into a string and send it to the client with the `CSS` content type:

```kotlin
suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
   this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
```

Then, you can provide CSS inside the required [route](Routing_in_Ktor.md):

```kotlin
get("/styles.css") {
    call.respondCss {
        body {
            backgroundColor = Color.darkBlue
            margin(0.px)
        }
        rule("h1.page-title") {
            color = Color.white
        }
    }
}
```

Finally, you can use the specified CSS for an HTML document created with [HTML DSL](html_dsl.md):

```kotlin
get("/html-dsl") {
    call.respondHtml {
        head {
            link(rel = "stylesheet", href = "/styles.css", type = "text/css")
        }
        body {
            h1(classes = "page-title") {
                +"Hello from Ktor!"
            }
        }
    }
}
```

### Options

No options()

# Reporting Issues / Support

Please use [our issue tracker](https://youtrack.jetbrains.com/issues/KTOR) for filing feature requests and bugs. If
you'd like to ask a question, we recommmend [StackOverflow](https://stackoverflow.com/questions/tagged/ktor) where
members of the team monitor frequently.

There is also community support on the [Kotlin Slack Ktor channel](https://app.slack.com/client/T09229ZC6/C0A974TJ9)

# Reporting Security Vulnerabilities

If you find a security vulnerability in Ktor, we kindly request that you reach out to the JetBrains security team via
our [responsible disclosure process](https://www.jetbrains.com/legal/terms/responsible-disclosure.html).

# Contributing

Please see [the contribution guide](CONTRIBUTING.md) and the [Code of conduct](CODE_OF_CONDUCT.md) before contributing.

TODO: contribution of features guide (link)