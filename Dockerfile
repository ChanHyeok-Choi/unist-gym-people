FROM ubuntu:22.04
RUN apt-get update && \
    apt-get install -y vim curl git openjdk-17-jdk maven
WORKDIR /root/project
COPY run.sh /root/project/run.sh
RUN chmod +x /root/project/run.sh
CMD ["/bin/bash"]