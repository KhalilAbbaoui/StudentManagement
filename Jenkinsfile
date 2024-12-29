pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'
        APP_PORT = '8080'
        HOST_PORT = '8081'
        SCANNERHOME = 'C:\\Program Files\\Jenkins\\.jenkins\\tools\\hudson.plugins.sonar.SonarRunnerInstallation\\sonar\\bin'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                script {
                    echo 'Building the project with Maven...'
                    bat '''
                        mvn clean package
                    '''
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...'
                    withSonarQubeEnv(credentialsId: 'student-sonar', installationName: 'Sonarqube') {
                        bat """
                            "${SCANNERHOME}\\sonar-scanner" -Dsonar.projectKey=student-management -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }

        stage('Test') {
            steps {
                script {
                    echo 'Running tests with Maven...'
                    bat '''
                        mvn test
                    '''
                }
            }
        }

        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...'
                    def customImage = docker.build("${DOCKER_IMAGE}", "--build-arg JAR_FILE=target/StudentManagement-1.0-SNAPSHOT.jar .")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to the registry...'
                    docker.withRegistry('https://index.docker.io/v1/', 'docker-hub-credentials') {
                        def customImage = docker.image("${DOCKER_IMAGE}")
                        customImage.push()
                    }
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...'
                    def container = docker.image("${DOCKER_IMAGE}").run("-p ${HOST_PORT}:${APP_PORT}")
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!'
        }
        failure {
            echo 'An error occurred during the pipeline execution.'
        }
    }
}