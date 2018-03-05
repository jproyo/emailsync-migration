FROM bigtruedata/sbt:0.13.15-2.12.2
MAINTAINER Juan Pablo Royo <juanpablo.royo@gmail.com>


WORKDIR /opt/loan-dojo

ADD . /opt/loan-dojo/
RUN sbt pack

CMD ["/opt/loan-dojo/target/pack/bin/loan-dojo"]
