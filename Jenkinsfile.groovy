pipeline {
  agent { }
  stages {
    stage('requirements') {
      steps {
        sh 'echo \'requirements\''
      }
    }
    stage('build') {
      steps {
        sh 'echo \'build\''
      }
    }
    stage('test') {
      steps {
        sh 'echo \'test\''
      }
    }
  }
}
