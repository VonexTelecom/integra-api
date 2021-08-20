pipeline {
    agent any 
    tools {
        maven 'Maven 3.8.1'
        jdk 'jdk8'
    }
    stages {
        stage ('Build') {
            steps {    
                sh ' mvn clean install -DskipTests'
            }
        }
        /*stage ('Test') {
            steps {    
                sh ' mvn test'
            }
        }*/       
        stage ('Imagem docker') {
            steps {
                sh 'docker build . -t vonex/api_usuario:${BUILD_NUMBER}'
            }
        }
        stage ('Run docker') {
            steps {
                sh ' docker stop integra-usuario' 
                sh ' docker rm integra-usuario'
                sh ' docker container run --network intranet -h integra-usuario -d --name integra-usuario -p 8080:8080 vonex/api_usuario:${BUILD_NUMBER}'
            }
        }        
    }
}


