pipeline {
    agent any 
    tools {
        maven 'Maven 3.8.1'
        jdk 'jdk8'
    }
    stages {
        withCredentials([sshUserPrivateKey(credentialsId: 'sms', keyFileVariable: 'identity', passphraseVariable: '', usernameVariable: 'centos')]) {				 
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
                    sh 'docker build . -t vonex/api_integra-api:${BUILD_NUMBER}'
                }
            }
            stage ('Run docker') {
                steps {
                    sh ' docker stop integra-api' 
                    sh ' docker rm integra-api'
                    sh ' docker container run --network intranet -h integra-api -d --name integra-api -p 8086:8086 vonex/api_integra-api:${BUILD_NUMBER}'
                }
            } 
        }
    }
}


