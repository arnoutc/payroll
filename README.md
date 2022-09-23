[Concourse CI Java Web Service application pipeline demo...](https://github.com/arnoutc/payroll)

# Concourse-CI Java Spring Boot Web Services pipeline demo

A Basic Concourse CI pipeline of a Java application  (build, test, deploy to Kubernetes)

Note: :octocat: stands for the GitHub page.

#### _Contributions welcome. Anything missing? Send in a pull request. Thanks._

## Table of Contents

- [Bringing up the Concourse-CI user interface](#Bringing)
  - [Prepare a docker-compose.yml](#Prepare a docker-compose.yml)
  - [Kick off Docker](#Kick off Docker)
-[Deploying pipeline code](#Deploying pipeline code)
    - [Install Fly](#Install Fly)
    - [Login into Concourse-CI ](#Login into Concourse-CI)
    - [Add JIB to Maven pom.xml](#Add JIB to Maven pom.xml)
    - [Compile the code and push](#Compile the code and push)
- [Create the pipeline](#Create the pipeline)
  - [Create the test task](#Create the test task)
  - [Create the docker build and package task](#Create the docker build and package task)
  - [Create the pipeline and use tasks ](#Create the pipeline)
- [Fire up the pipeline](#Fire up the pipeline)
- [Install the pipeline](#Install the pipeline)
- [Destroy the pipeline](#Destroy the pipeline)
- [Articles used](#Articles used)
- [Meta](#Meta)

# Bringing up the Concourse-CI user interface

## Prepare a docker-compose.yml 

Do the following:

```curl -O https://concourse-ci.org/docker-compose.ymlconcourse-ci```

## Kick off Docker

   ```docker-compose up -d```

The UI is then available at http://localhost:8080

# Deploying pipeline code

## Install Fly 

MacOSx:

<code>curl 'http://localhost:8080/api/v1/cli?arch=amd64&platform=darwin' -o fly 
&& chmod +x ./fly && mv ./fly /usr/local/bin/</code>

For other operating systems see [Concourse-CI ](http://concourse-ci.org)

## Login into Concourse-CI 

<code>fly -t tutorial login -c http://localhost:8080 -u test -p test</code>

You should see output in terminal:
<code><br/>logging in to team 'main' <br/>
target saved</code>

## Add JIB to Maven pom.xml

NOTE: having <format>OCI</format> failed as a JSON file is expected in that image, referred to <format>Docker</format>

## Compile the code and push

```mvn compile jib:build -Djib.to.image=quay.io/youruser/yourimage:latest -Djib.to.auth.username=$REGISTRY_USERNAME -Djib.to.auth.password=$REGISTRY_PASSWORD```

where jib.to.image is your repo URL (e.g. quay.io/ECR), $REGISTRY_USERNAME = AWS and $REGISTRY_PASSWORD=$(ews ecr-get-login-password)

Result: the image is build and pushed to AWS Elastic Container Registry (ECR)

# Create the pipeline

## Create the test task

ci/concourse/tasks/unit-test.yml

## Create the docker build and package task

ci/concourse/tasks/maven-package.yml

## Create the pipeline 

ci/concourse/pipeline.yml

# Fire up the pipeline

<code>fly -t my-server login -k -c ${CONCOURSE_URL} -u ${CONCOURSE_USER} -p ${CONCOURSE_PASSWORD}</code>

# Install the pipeline

<code>fly -t tutorial set-pipeline -c ci/concourse/pipeline.yml -p payroll \<br />
-v registry-username=AWS \<br/>
-v registry-password=$(aws ecr get-login-password) \ <br/>
-v image-name=220779065597.dkr.ecr.eu-west-2.amazonaws.com/payroll \<br/>
-v k8s_cluster_url=https://127.0.0.1:50513 \<br/>
-v k8s_namespace=payroll \ <br/>
-v k8s_admin_user=kind-cluster \ <br/>
-v k8s_admin_token= ???? \ <br/>
-v k8s_ca_base64= ???? <br/>
</code>

Above fly command assumes you are using:

1. AWS ECR repository I set up under 220779065597.dkr.ecr.eu-west-2.amazonaws.com/payroll)
2. A local Kind Kubernetes cluster. I have added a Kind configuration file and nginx controller in ci/kubernetes

Manually rigger the pipeline (unpause)

<code>fly -t tutorial unpause-pipeline -p payroll</code>

# Destroy the pipeline

<code>fly -t tutorial destroy-pipeline -p payroll</code>

# Articles used

- [Concourse-CI ](http://concourse-ci.org)
- [Building a continuous deployment pipeline with Kubernetes and Concours-CI](https://blog.alterway.fr/en/building-a-continious-deployment-pipeline-with-kubernetes-and-concourse-ci.html) By Romain Guichard (Merci beaucoup Romain!)
- [Building a Docker Image with Maven and JIB](https://www.tutorialworks.com/concourse-java-pipeline/#building-a-docker-image-with-maven-and-jib) by Tom Donohue (Thanks Tom Donohue!)
- [Dockerizing Java Apps using Jib](https://www.baeldung.com/jib-dockerizing) Thanks Baeldung!
- [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
- [Setting up a Kind cluster](https://kind.sigs.k8s.io/docs/user/quick-start/)

## Meta

**License**

This basic concourse setup is dedicated to PA, partners and customers. Use it as you please with no restrictions whatsoever.

**Questions? Comments?**

Send them along to arnout.cator@paconsulting.com or ping me in Slack. Thanks!
