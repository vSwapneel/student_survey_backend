pipeline {
//     agent any
    agent { label 'kubernetes-agent' }
    tools {
        maven 'Maven3.9.9'
    }
    environment {
        DOCKERHUB_CREDENTIALS = credentials('Docker')
    }
    stages {
        stage('Initialize') {
            steps {
                script {
                    env.BUILD_TIMESTAMP = new Date().format("yyyyMMddHHmmss", TimeZone.getTimeZone('UTC'))
                    echo "Build timestamp: ${env.BUILD_TIMESTAMP}"
                }
            }
        }
//         stage('Build and Package') {
//             steps {
//                 sh 'chmod +x ./mvnw'  // Add this line to ensure `mvnw` is executable
//                 sh './mvnw clean package'
//             }
//         }

        stage('Clone Repository') {
            steps {
                checkout scm
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install'
            }
        }
        stage('Building API Image') {
            steps {
                script {
                    withCredentials([usernamePassword(credentialsId: 'Docker',
                                                      usernameVariable: 'DOCKER_USER',
                                                      passwordVariable: 'DOCKER_PASS')]) {
                        sh """
                            echo "\$DOCKER_PASS" | docker login -u "\$DOCKER_USER" --password-stdin
                        """
                    }
                    def imageName = "vswapneel/assignment3:${env.BUILD_TIMESTAMP}"
                    sh "docker build -t ${imageName} ."
                    env.IMAGE_NAME = imageName
                }
            }
        }
        stage('Pushing Image to DockerHub') {
            steps {
                script {
                    sh "docker push ${env.IMAGE_NAME}"
                }
            }
        }
        stage('Deploying to Rancher') {
            steps {
                script {
                    sh "kubectl set image deployment/deployment container-0=${env.IMAGE_NAME}"
                }
            }
        }
    }
}
