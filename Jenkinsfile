pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'
        APP_PORT = '8080'
        HOST_PORT = '8081'
        SONARQUBE_URL = 'http://127.0.0.1:9999'
        SONARQUBE_TOKEN = credentials('be476250-e643-4a50-83ed-ab9fe6296087')
        //ID for this token be476250-e643-4a50-83ed-ab9fe6296087
        // token squ_74dac99f6078206e7af50274574e4465806da753
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
                    echo "SonarQube URL: ${SONARQUBE_URL}"
                    bat """
                    mvn sonar:sonar ^
                        -Dsonar.projectKey=student-management ^
                        -Dsonar.host.url=%SONARQUBE_URL% ^
                        -Dsonar.login=%SONARQUBE_TOKEN%
                    """
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
