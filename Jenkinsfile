pipeline {
    agent any // Définir l'agent Jenkins qui exécutera le pipeline. "any" signifie qu'il peut être exécuté sur n'importe quel agent disponible.

    tools {
        maven 'Maven 3.9.5' // Spécifie la version de Maven à utiliser pour la construction du projet.
    }

    environment {
        // Définition des variables d'environnement utilisées dans le pipeline.
        DOCKER_IMAGE = 'khalilabbaoui/studentmanagement:latest'  // Nom de l'image Docker à construire
        APP_PORT = '8081'  // Le port où l'application sera exposée
        SONARQUBE_URL = 'http://localhost:9000'  // URL de l'instance SonarQube
        SONARQUBE_TOKEN = credentials('sonarqube-token')  // Token SonarQube récupéré à partir des credentials Jenkins
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
                    echo 'Building the project with Maven...' // Message d'information dans les logs
                    sh '''
                    mvn clean compile  // Commande Maven pour nettoyer et compiler le projet
                    '''
                }
            }
        }

        // Étape d'analyse du code avec SonarQube.
        stage('SonarQube Analysis') {
            steps {
                script {
                    echo 'Running SonarQube analysis...' // Message d'information dans les logs
                    sh '''
                    mvn sonar:sonar  // Exécution de l'analyse SonarQube sur le projet
                        -Dsonar.projectKey=student-management   // Clé de projet SonarQube
                        -Dsonar.host.url=${SONARQUBE_URL}   // URL de l'instance SonarQube
                        -Dsonar.login=${SONARQUBE_TOKEN}  // Token d'authentification pour accéder à SonarQube
                    '''
                }
            }
        }

        // Étape pour exécuter les tests unitaires avec Maven.
        stage('Test') {
            steps {
                script {
                    echo 'Running tests with Maven...' // Message d'information dans les logs
                    sh '''
                    mvn test  // Commande Maven pour exécuter les tests unitaires du projet
                    '''
                }
            }
        }

        // Étape de construction de l'image Docker pour l'application.
        stage('Docker Build') {
            steps {
                script {
                    echo 'Building the Docker image for StudentManagement...' // Message d'information dans les logs
                    sh '''
                    docker build -t ${DOCKER_IMAGE} .  // Commande Docker pour construire l'image à partir du Dockerfile
                    '''
                }
            }
        }

        // Étape de démarrage du conteneur Docker avec l'application.
        stage('Run Application') {
            steps {
                script {
                    echo 'Starting the StudentManagement application container...' // Message d'information dans les logs
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
            echo 'Pipeline executed successfully!'  // Message de réussite lorsque le pipeline se termine avec succès
        }
        failure {
            echo 'An error occurred during the pipeline execution.'  // Message d'erreur lorsque le pipeline échoue
        }
    }
}
