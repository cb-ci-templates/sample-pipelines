pipeline {
    agent {
        kubernetes {
            yaml '''
                apiVersion: v1
                kind: Pod
                spec:
                  containers:
                  - name: shell
                    image: caternberg/ci-utils:1.0
                    command:
                    - sleep
                    args:
                    - infinity
                '''
            defaultContainer 'shell'
        }
    }

    stages {
        stage('GetChangesByCurls') {
            environment {
                JENKINS_TOKEN = credentials("jenkins-token")
                BUILD_URL_STRIPPED = "${BUILD_URL}.replaceAll('^https://','')"
            }
            steps {
                echo "Hello world"
                sh "curl -L -o changelog.xml https://${JENKINS_TOKEN}@$BUILD_URL_STRIPPED}/api/xml?wrapper=changes&xpath=//changeSet//comment"
                sh "cat changelog.xml"
                sh "env|sort"
            }
        }
        stage('GetChangesByGroovy') {
            steps {
                //see https://devops.stackexchange.com/questions/2310/get-all-change-logs-of-since-last-successful-build-in-jenkins-pipeline
                script {
                    def changeLogSets = currentBuild.changeSets
                    println changeLogSets
                }
            }
        }
    }
}
