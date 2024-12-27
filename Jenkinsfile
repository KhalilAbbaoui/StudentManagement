pipeline {
    agent any // Définir l'agent Jenkins qui exécutera le pipeline. "any" signifie qu'il peut être exécuté sur n'importe quel agent disponible.

    tools {
        maven 'Maven 3.9.5' // Spécification de la version du Maven à utiliser pour la construction du projet.
        jdk 'JDK 21' //Spécification du JDK
    }

    environment {

        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'  // Nom de l'image Docker à construire
        APP_PORT = '8081'  // Le port où l'application sera exposée
        SONARQUBE_URL = 'http://127.0.0.1:9999'  // URL de l'instance SonarQube
        SONARQUBE_TOKEN = credentials('squ_c7a5a0a3b0462a523a3c3c20429ffc5512d8a3bd')  // Token SonarQube récupéré à partir des credentials Jenkins
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
                    sh '''
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
                    sh '''
                    mvn sonar:sonar \
                        -Dsonar.projectKey=student-management \
                        -Dsonar.host.url=${SONARQUBE_URL} \
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
                    sh '''
                    mvn test
                    '''
                }
            }
        }

        // Étape de construction de l'image Docker pour l'application.
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...'
                    sh '''
                    docker build -t ${DOCKER_IMAGE} .
                    '''
                }
            }
        }

        // Étape de démarrage du conteneur Docker avec l'application.
        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...'
                    sh '''
                    docker stop studentmanagement || true
                    docker rm studentmanagement || true
                    docker run -d -p ${APP_PORT}:8080
                        --name studentmanagement
                        ${DOCKER_IMAGE}
                    '''
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
