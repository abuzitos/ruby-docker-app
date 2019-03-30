pipeline {

    agent {
        docker {
            image 'ruby:2.5'
            args  '-v /tmp:/tmp'
        }
    }

    stages {
      stage('Initialize')
      {
        steps {
          sh("Initialize")
          //sh "docker build ."
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
          //sh "docker run ruby-app"
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
