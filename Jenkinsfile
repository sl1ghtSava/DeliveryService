pipeline {
    agent any

    environment {
        DELIVERY_SERVICE = 'FastDelivery'
        APP_VERSION = '1.0.0'
    }

    stages {
        stage('Build') {
            steps {
                bat 'mvn compile'
            }
        }

        stage('Test') {
            steps {
                bat 'mvn test'
            }
        }

        stage('Package') {
            steps {
                bat 'mvn package -DskipTests'
                echo "Переменная окружения DELIVERY_SERVICE = ${DELIVERY_SERVICE}"
                echo "Версия приложения APP_VERSION = ${APP_VERSION}"
            }
        }
    }
}