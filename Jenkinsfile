pipeline {
    agent any
    tools {
        maven 'Maven3.9.9'
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('Docker') // Docker credentials added to Jenkins
    }
    stages {
        stage('Initialize') {
            steps {
                script {
                    // Define a build timestamp variable
                    env.BUILD_TIMESTAMP = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone('UTC'))
                    echo "Build timestamp: ${env.BUILD_TIMESTAMP}"
                }
            }
        }

        stage('Build and Package') {
            steps {
                dir('student_survey_backend') {  // Navigate to the correct directory
                    sh './mvnw clean package'
                }
            }
        }

        stage('Building API Image') {
            steps {
                script {
                    // Securely handle Docker login
                    withCredentials([usernamePassword(credentialsId: 'Docker',
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                        """
                    }

                    // Build Docker image using the BUILD_TIMESTAMP
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
                    // Push the Docker image to DockerHub
                    sh "docker push ${env.IMAGE_NAME}"
                }
            }
        }

        stage('Deploying to Rancher') {
            steps {
                script {
                    // Deploy the new image to Rancher using kubectl
                    sh "kubectl set image deployment/deployment container-0=${env.IMAGE_NAME}"
                }
            }
        }
    }
}
