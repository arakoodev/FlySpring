# FlySpring Documentation

FlySpring is a simple framework on top of Spring Boot that aims to make Java development more enjoyable and productive. It provides a supercharged command-line interface (CLI) called `flyfly` that automates common tasks such as code reloading, testing, formatting, and dependency management. FlySpring also leverages HTMX and Arakoo to enable server-side rendering and dynamic HTML updates without writing any JavaScript.

## Installation

To use FlySpring, you need to have Java 11 or higher and Maven installed on your system. You also need to install the `flyfly` CLI by following the instructions on the [GitHub repository](https://github.com/arakoodev/FlySpring).

To start a new FlySpring project, you can use the `flyfly init` command and choose a template from the available options. This will create a Maven project with the necessary dependencies and configuration files.

Alternatively, you can clone one of the [examples](https://github.com/arakoodev/FlySpring/tree/main/Examples) from the GitHub repository and run `mvn install` inside the project directory.

## Development

To run your FlySpring project in development mode, you can use the `flyfly dev` command. This will start a local server on port 8080 and watch for any changes in your source code. Whenever you make a change, `flyfly` will automatically reload your application and refresh your browser.

You can also use `flyfly test` to run your tests using Testcontainers, which provide isolated and reproducible environments for testing. `flyfly` will automatically set up and tear down the containers for you.

To format your code according to the Google Java Style Guide, you can use `flyfly format`. This will use Google Java Format to apply consistent and readable formatting to your code.

## Deployment

To deploy your FlySpring project, you can use the `flyfly build` command. This will create a JAR file that contains your application and all its dependencies. You can then run the JAR file using `java -jar target/flyspring.jar`.

You can also use `flyfly deploy` to deploy your application to a cloud platform such as Heroku or AWS. You will need to provide some configuration options such as the platform name, the app name, and the credentials. `flyfly` will then use the appropriate tools to build and deploy your application.

## Features

FlySpring offers several features that make Java development easier and faster. Some of these features are:

- Autoroute: A plugin that automatically maps your controller methods to HTTP endpoints based on their names and annotations. You don't need to write any explicit routing code or configuration files.
- HTMX: A library that allows you to update parts of your web page dynamically using HTML attributes and AJAX requests. You don't need to write any JavaScript code or deal with complex frameworks.
- Arakoo: A library that provides a simple and elegant way to write HTML templates using Java code. You don't need to learn any new syntax or use any external files.
- Testcontainers: A library that allows you to run your tests against real databases, message brokers, web servers, and other services using Docker containers. You don't need to install or configure any external dependencies or mock any components.

## Contributing

FlySpring is an open-source project that welcomes contributions from anyone who is interested in improving it. To contribute to FlySpring, you need to follow these steps:

- Fork the [GitHub repository](https://github.com/arakoodev/FlySpring) and clone it locally.
- Create a new branch for your feature or bug fix.
- Make your changes and commit them with clear and descriptive messages.
- Push your branch to your forked repository and create a pull request against the main branch of the original repository.
- Follow the [contributing guidelines](https://github.com/arakoodev/FlySpring/blob/main/cla.md) and sign the Contributor License Agreement (CLA).
- Wait for feedback from the maintainers and address any comments or requests.

## Acknowledgements

FlySpring is inspired by the spirit of Next.js and other modern web frameworks. It is built on top of Spring Boot, which is a powerful and popular framework for Java development. It also uses several other libraries and tools that make FlySpring possible, such as HTMX, Arakoo, Testcontainers, Google Java Format, Heroku CLI, AWS CLI, and more.