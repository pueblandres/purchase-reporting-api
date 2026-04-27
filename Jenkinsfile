pipeline {
    agent any

    stages {
        stage('Build, Test and Coverage') {
            steps {
                bat 'mvn clean verify jacoco:report'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
            }
        }
    }

    post {
        success {
            echo 'Pipeline finalizado correctamente.'
        }
        failure {
            echo 'Pipeline falló. Revisar logs.'
        }
    }
}