pipeline {
    agent any // Définir l'agent Jenkins qui exécutera le pipeline. "any" signifie qu'il peut être exécuté sur n'importe quel agent disponible.


    environment {
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'  // Nom de l'image Docker à construire
        APP_PORT = '8080'  // Le port où l'application sera exposée à l'intérieur du conteneur
        HOST_PORT = '8081'  // Le port de la machine hôte pour accéder à l'application (à modifier si 8081 est aussi occupé)
        SONARQUBE_URL = 'http://127.0.0.1:9999'  // URL de l'instance SonarQube
        SONARQUBE_TOKEN = credentials('squ_74dac99f6078206e7af50274574e4465806da753')  // Token SonarQube récupéré à partir des credentials Jenkins
    }

    stages {
        // Étape de récupération du code depuis le repository source.
        stage('Checkout') {
            steps {
                checkout scm // Cette étape récupère le code source du projet.
            }
        }

        // Étape de construction du projet avec Maven.
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

        // Étape d'analyse du code avec SonarQube.
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...'
                    bat '''
                    mvn sonar:sonar ^
                        -Dsonar.projectKey=student-management ^
                        -Dsonar.host.url=${SONARQUBE_URL} ^
                        -Dsonar.login=${SONARQUBE_TOKEN}
                    '''
                }
            }
        }

        // Étape pour exécuter les tests unitaires avec Maven.
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

        // Étape de construction de l'image Docker pour l'application avec le plugin Docker Jenkins.
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...'
                    docker.build("${DOCKER_IMAGE}")
                }
            }
        }

        // Étape de démarrage du conteneur Docker avec l'application en utilisant le plugin Docker Jenkins.
        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...'
                    def app = docker.image("${DOCKER_IMAGE}")
                    app.withRun("-p ${HOST_PORT}:${APP_PORT}") { // Mapper le port 8081 de la machine hôte vers le port 8080 du conteneur
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
