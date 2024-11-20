pipeline {
    agent any
    tools {
        maven 'Maven3.9.9'
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('Docker') // Docker credentials added to Jenkins and names the set as docker-pass
    }
    stages {
        stage('Initialize') {
            steps {
                script {
                    // Defining a build timestamp variable
                    env.BUILD_TIMESTAMP = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone('UTC'))
                    echo "Build timestamp: ${env.BUILD_TIMESTAMP}"
                }
            }
        }


        stage('Building API Image') {
            steps {
                script {
                    // Checkout SCM
                    checkout scm

                    // Change directory to the project root
                    dir('assignment3-backend') {
                        // Set permissions on the mvnw script
                        sh 'chmod +x ./mvnw'

                        // Build the project using the Maven wrapper
                        sh './mvnw clean package'
                    }

                    // Securely handle Docker login
                    withCredentials([usernamePassword(credentialsId: 'Docker',
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                        """
                    }

                    // Build Docker image
                    def imageName = "vswapneel/assignment3:${env.BUILD_TIMESTAMP}"
                    sh "docker build -t ${imageName} ."

                    // Save image name for later stages
                    env.IMAGE_NAME = imageName
                }
            }
        }




        stage('Pushing Image to DockerHub') {
            steps {
                script {
                    // Pushing the Docker image to DockerHub
                    sh "docker push ${env.IMAGE_NAME}"
                }
            }
        }

        stage('Deploying to Rancher') {
            steps {
                script {
                    // Deploying the new image to Rancher
                    sh "kubectl set image deployment/deployment container-0=${env.IMAGE_NAME}"
                }
            }
        }
    }
}
