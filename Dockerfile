FROM adoptopenjdk/openjdk11:latest AS build
RUN apt-get update
RUN apt-get install wget
RUN wget http://www-us.apache.org/dist/maven/maven-3/3.5.4/binaries/apache-maven-3.5.4-bin.tar.gz
RUN tar -xf apache-maven-3.5.4-bin.tar.gz
RUN mv apache-maven-3.5.4/ apache-maven/
ENV M2_HOME=/apache-maven
ENV MAVEN_HOME=/apache-maven
ENV PATH=${M2_HOME}/bin:${PATH}
RUN apt-get --assume-yes install git
RUN git clone https://github.com/javaguruen/hvemkommer2.git
WORKDIR /hvemkommer2
RUN mvn clean install