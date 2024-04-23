def call(Map params){
    catchError(catchInterruptions: false,message: "Pipeline Stage failed with returnCode ${env.globalReturnCode}", buildResult:'FAILURE', stageResult: 'FAILURE') {
        writeFile encoding: 'utf-8', file: "${WORKSPACE}/sampleFailFast.sh", text: libraryResource ('scripts/sampleFailFast.sh')
        env.globalReturnCode=sh label: 'exitStatus',
                returnStatus: true,
                script: """chmod a+x \${WORKSPACE}/sampleFailFast.sh && \
                            \${WORKSPACE}/sampleFailFast.sh
                         """
        //this.exitOrContinue()
        if ( "${env.globalReturnCode}" != "0" ){
            sh "exit ${env.globalReturnCode}"
        }
        env.MYJSON=sh label: 'stdOut', returnStdout: true, script: "cat \${WORKSPACE}/mytest.json"
    }
    sh "echo ${env.MYJSON}"
    this.exitOrContinue()
}

def exitOrContinue(){
    echo "call exitOrContinue  globalReturnCode:${env.globalReturnCode}"
    if ( "${env.globalReturnCode}" != "0" ){
        sh "exit ${env.globalReturnCode}"
    }
}


