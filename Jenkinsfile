pipeline {
    agent any // Définir l'agent Jenkins qui exécutera le pipeline. "any" signifie qu'il peut être exécuté sur n'importe quel agent disponible.

    tools {
        maven 'Maven 3.9.5' // Spécification de la version du Maven à utiliser pour la construction du projet.

    }

    environment {

        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'  // Nom de l'image Docker à construire
        APP_PORT = '8081'  // Le port où l'application sera exposée

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
           // Étape de vérification de la version de Docker (utile pour déboguer sur Windows).
        stage('Check Docker Version') {
            steps {
                script {
                    echo 'Checking Docker version...'
                    sh 'docker --version'  
                }
            }
        }
        // Étape de construction de l'image Docker pour l'application.
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...'
                    sh '''
                    docker build -t ${DOCKER_IMAGE} .  # Commande Docker pour construire l'image à partir du Dockerfile
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
                    docker stop studentmanagement || true  // Arrêter le conteneur existant s'il est en cours d'exécution
                    docker rm studentmanagement || true  // Supprimer le conteneur existant
                    docker run -d -p ${APP_PORT}:8080   // Démarrer un nouveau conteneur et exposer le port
                        --name studentmanagement   // Nommer le conteneur
                        ${DOCKER_IMAGE}  // Utiliser l'image Docker construite précédemment
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
