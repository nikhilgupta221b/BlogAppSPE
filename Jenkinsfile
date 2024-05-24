pipeline {
    agent any

    environment {
        FRONTEND_IMAGE = 'nikhilguptaiiitb/frontend-app'
        BACKEND_IMAGE = 'nikhilguptaiiitb/backend-app'
    }

    stages {
        stage('Clone Repository') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [],
                    submoduleCfg: [],
                    userRemoteConfigs: [[url: 'https://github.com/nikhilgupta221b/BlogAppSPE.git']]
                ])
            }
        }

        stage('Build Frontend Docker Image') {
            steps {
                script {
                    docker.build("$FRONTEND_IMAGE", "./front")
                }
            }
        }

        stage('Build Backend Docker Image') {
            steps {
                script {
                    docker.build("$BACKEND_IMAGE", "./back")
                }
            }
        }

        stage('Push Docker Images to DockerHub') {
            steps {
                script {
                    docker.withRegistry('https://registry.hub.docker.com', 'DockerhubCred') {
                        docker.image("$FRONTEND_IMAGE").push('latest')
                        docker.image("$BACKEND_IMAGE").push('latest')
                    }
                }
            }
        }

        stage('Deploy with Ansible') {
            steps {
                ansiblePlaybook(
                    playbook: 'deploy.yml',
                    inventory: 'hosts.ini'
                )
            }
        }
    }

    post {
        always {
            cleanWs()
        }
    }
}
