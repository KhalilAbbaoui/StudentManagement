pipeline {
    agent any // This specifies that the pipeline can run on any available agent.

    environment {
        // Defining environment variables to be used in later stages.
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest' // Docker image name with tag
        APP_PORT = '8080' // Port inside the container
        HOST_PORT = '8081' // Port on the host machine to map to the container
        SCANNERHOME = 'C:\\Program Files\\Jenkins\\.jenkins\\tools\\hudson.plugins.sonar.SonarRunnerInstallation\\sonar\\bin' // Path to the SonarQube scanner
    }

    stages {
        // Stage for checking out the source code from the SCM (e.g., Git).
        stage('Checkout') {
            steps {
                checkout scm // Check out the code from the repository configured in Jenkins
            }
        }

        // Stage for building the project with Maven.
        stage('Build') {
            steps {
                script {
                    echo 'Building the project with Maven...'
                    // Windows batch command to build the project using Maven.
                    bat '''
                        mvn clean package
                    '''
                }
            }
        }

        // Stage for running the SonarQube analysis on the code.
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...'
                    // Executes SonarQube analysis with the environment credentials and scanner path.
                    withSonarQubeEnv(credentialsId: 'student-sonar', installationName: 'Sonarqube') {
                        bat """
                            "${SCANNERHOME}\\sonar-scanner" -Dsonar.projectKey=student-management -Dsonar.java.binaries=target/classes
                        """
                    }
                }
            }
        }

        // Stage for running the tests with Maven.
        stage('Test') {
            steps {
                script {
                    echo 'Running tests with Maven...'
                    // Run tests using Maven
                    bat '''
                        mvn test
                    '''
                }
            }
        }

        // Stage for building the Docker image.
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...'
                    // Build the Docker image using the Dockerfile and pass the JAR file as a build argument.
                    def customImage = docker.build("${DOCKER_IMAGE}", "--build-arg JAR_FILE=target/StudentManagement-1.0-SNAPSHOT.jar .")
                }
            }
        }

        // Stage for pushing the Docker image to a Docker registry (Docker Hub in this case).
        stage('Push Docker Image') {
            steps {
                script {
                    echo 'Pushing Docker image to the registry...'
                    // Push the image to the Docker registry using the credentials configured in Jenkins.
                    docker.withRegistry("", "docker-hub-credentials") {
                        def customImage = docker.image("${DOCKER_IMAGE}")
                        customImage.push() // Push the built image to the registry
                    }
                }
            }
        }

        // Stage for running the application as a Docker container.
        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...'
                    // Run the Docker container, mapping the ports to the host and container.
                    def container = docker.image("${DOCKER_IMAGE}").run("-p ${HOST_PORT}:${APP_PORT}")
                }
            }
        }
    }

    post {
        success {
            echo 'Pipeline executed successfully!' // Message when the pipeline completes successfully
        }
        failure {
            echo 'An error occurred during the pipeline execution.' // Message if the pipeline fails
        }
    }
}