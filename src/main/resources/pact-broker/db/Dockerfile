## Base image
FROM postgres
## Of course, every Dockerfile requires a maintainer
MAINTAINER me
## Add a special script to initialize db for pact broker
ADD ./initial.sh /docker-entrypoint-initdb.d/