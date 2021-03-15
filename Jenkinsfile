pipeline {
    agent {
        docker {
        image 'workout-diary-backend-integration-tests'
        args '-u 0:0 --network host'
        alwaysPull true
        registryUrl 'https://docker.io/komlevvladimir/'
        registryCredentialsId '384551f5-8107-49c1-a749-827bfe18f7cc'
        }
    }
    environment {
    HOME="/tests"
    }
    stages {
        stage('Test') {
            steps {
                sh "./gradlew clean test -i --no-daemon"
            }
        }
    }
}
