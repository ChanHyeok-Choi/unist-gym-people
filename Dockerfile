FROM ubuntu:22.04

#Just common practices
ENV DEBIAN_FRONTEND=noninteractive
ENV LD_LIBRARY_PATH=/usr/local/lib
ENV LIBRARY_PATH=/usr/local/lib

#Standard update-upgrade packages
RUN apt-get -y -q update upgrade
RUN apt-get install -y vim curl git openjdk-17-jdk maven

#Install MongoDB
RUN wget -qO - https://www.mongodb.org/static/pgp/server-6.0.asc | apt-key add -
RUN echo "deb [ arch=amd64,arm64 ] https://repo.mongodb.org/apt/ubuntu jammy/mongodb-org/6.0 multiverse" | tee /etc/apt/sources.list.d/mongodb-org-6.0.list
RUN apt-get update
RUN apt-get install -y mongodb-org

#Since systemctl is not present in Docker container, we need these commands to start MongoDB
RUN rm -rf /var/lib/apr/lists/*  \
    && rm -rf /var/lib/mongodb \
    && mv /etc/mongod.conf /etc/mongod.conf.orig
RUN mkdir -p /data/db /data/configdb
RUN chown -R mongodb:mongodb /data/db /data/configdb

WORKDIR /root/project
COPY run.sh /root/project/run.sh
RUN chmod +x /root/project/run.sh
CMD ["/bin/bash"]