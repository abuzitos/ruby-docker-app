node('jenkins-slave') {
    checkout scm
    stage('Build') {
        docker.image('ruby').inside {
            sh 'ruby --version'
        }
    }
}

/*
pipeline {

    agent {
      docker { image 'ruby:2.5' }
     }

    stages {
      stage('Initialize')
      {
        steps {
          sh "echo 'Initialize'"
          sh "docker build ."
        }
      }

      stage('Checkout')
      {
        steps {
          sh "echo 'Checkout'"
        }
      }

      stage('Build')
      {
        steps {
          sh "echo 'Build'"
          sh "docker run ruby-app"
        }
      }

      stage('Test')
      {
        steps {
          sh "echo 'Test'"
          sh "bundle exec rspec spec"
        }
      }

      stage('Deliver')
      {
        steps {
          sh "echo 'Deliver'"
        }
      }
    }
}
*/
