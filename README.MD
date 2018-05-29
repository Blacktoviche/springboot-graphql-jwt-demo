#  springboot-graphql-jwt-demo

GraphQL java backend representing an ugly way to authenticate/authorize using Spring boot, GraphQL & jsonwebtoken
It's the only way I found to apply a security in your graphql-java
I have tried to integrate it with spring security using 'hasRole("ROLE")' but it didn't work
I think because Mutation & Query are being instantiated by GraphQL not by Spring boot
So I found this way is the only one to implement security into graphql-java backend
It's an ugly way you have to check user in every mutation and query and that's not good

The next repository will be about doing the same thing but the right way using [graphql-spqr](https://github.com/leangen/graphql-spqr)
Which has a better implementation of GraphQL in java, It's so easy to use as it implemented as RestController
Also you can use standard Spring boot security without any problem


## Installation

```bash
# Clone this repository
git clone https://github.com/blacktoviche/springboot-graphql-jwt-demo
# Go into the repository
cd springboot-graphql-jwt-demo
# Install dependencies
mvn install
# Compile the app
mvn compile
# Package the app
mvn package
# Run the app
mvn spring-boot:run
```








## License
- [MIT](LICENSE)

Twitter [@SyrianDeveloper](https://www.twitter.com/SyrianDeveloper)