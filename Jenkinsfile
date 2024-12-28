pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'
        APP_PORT = '8080'
        HOST_PORT = '8081'
        DOCKER_CREDENTIALS = credentials('student-management')  // Ensure this ID matches your Docker Hub credentials in Jenkins
        SCANNERHOME = tool 'sonar'  // Ensure this matches your SonarQube scanner installation name
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
                    mvn clean compile
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
                            "${SCANNERHOME}\\sonar-scanner" -Dsonar.projectKey=student-management
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
                    docker.build("${DOCKER_IMAGE}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to the registry...'
                    docker.withRegistry('', 'dockerhub-credentials') {  // Make sure the credentials ID here is correct
                        docker.image("${DOCKER_IMAGE}").push()
                    }
                }
            }
        }

        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...'
                    def app = docker.image("${DOCKER_IMAGE}")
                    app.withRun("-p ${HOST_PORT}:${APP_PORT}") {
                        echo "Application running at port ${HOST_PORT}"
                    }
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
