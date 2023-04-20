# FlySpring Documentation

FlySpring is a simple framework on top of Spring Boot that aims to make Java development more enjoyable and productive. It provides a supercharged command-line interface (CLI) called `flyfly` that automates common tasks such as code reloading, testing, formatting, and dependency management. FlySpring also leverages HTMX and Arakoo to enable server-side rendering and dynamic HTML updates without writing any JavaScript.

## Installation

To use FlySpring, you need to have Java 11 or higher and Maven installed on your system. You also need to install the `flyfly` CLI by following the instructions on the [GitHub repository](https://github.com/arakoodev/FlySpring).



Alternatively, you can clone one of the [examples](https://github.com/arakoodev/FlySpring/tree/main/Examples) from the GitHub repository and run `mvn install` inside the project directory.

## Development

To run your FlySpring project in development mode, you can use the flyfly run command. This will start a local server on port 8080 and watch for any changes in your source code. Whenever you make a change, flyfly will automatically reload your application and refresh your browser.

To format your code according to the Google Java Style Guide, you can use flyfly format command. This will apply consistent and elegant formatting to your Java code.

To get help on using flyfly, you can use flyfly help command. This will display a list of available commands and options.

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