FROM openjdk:17.0.1
ADD /target/deal-0.0.1-SNAPSHOT.jar deal.jar
ENTRYPOINT ["java", "-jar", "deal.jar"]