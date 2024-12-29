pipeline {
    agent any

    environment {
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'
        APP_PORT = '8080'
        HOST_PORT = '8081'
        DOCKER_CREDENTIALS = credentials('student-management')  // Ensure this ID matches your Docker Hub credentials in Jenkins
        SCANNERHOME = 'C:\\Program Files\\Jenkins\\.jenkins\\tools\\hudson.plugins.sonar.SonarRunnerInstallation\\sonar\\bin'  // Define the full path here'  // Ensure this matches your SonarQube scanner installation name
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
                    docker.build("${DOCKER_IMAGE}","--build-arg JAR_FILE=target/StudentManagement-1.0-SNAPSHOT.jar .")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to the registry...'
                    docker.withRegistry('', '8fa4c95a-f141-47b0-aa20-f517ccb22168') {  // Make sure the credentials ID here is correct
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
