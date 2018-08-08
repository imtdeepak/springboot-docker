# Using Docker to Push Sample App

This is documentation on how to use docker and dtr.predix.io to create and push a
docker image to DockerHub. The example app simply lists the names of a few basketball players
on a GET request.

## Dockerfile

The Dockerfile that I included in this repo simply gets the necessary dependencies in JDK8, exposes
port 8080 to the outside world, and put the executable JAR in the container, using the ADD command.

__Note on Volumes__

Volumes are a mechanism to persist data generated by the container on the Host OS, and share directories from the Host OS with the container. 

The `VOLUME` instruction creates a mount point on the container with the specified path; in our case `/tmp`.

Spring boot applications create working directories for Tomcat at the `/tmp` folder by default.

## Makefile

This Makefile contains several targets needed to push the image to DTR.

__build__

use the command `make build` to build the Docker image.

__run__

use the command, `make run` to run the image on port 5000 on your local machine (check localhost:5000 to see if it works.)

__tag__

user the command `make tag` to tag the image with the correct DTR repository (explained later)

## DTR

Steps to push an Image to DTR

1) Login to dtr.predix.io
2) Create a repository ex: sso/micro-service-example
3) Yag the local image with the name of the repository. The name of the repository is
`dtr.predix.io/<sso>/<name-of-repo>` - this is where you run the `make run` command.
4) Login to dtr.predix.io on terminal with `docker login dtr.predix.io`
5) Push the image with `docker push dtr.predix.io/<sso>/<name-of-repo>`

