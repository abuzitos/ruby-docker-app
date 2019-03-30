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
          sh("@@@@@@@@@@")
          sh("Initialize")
          sh("@@@@@@@@@@")
          //sh "docker build ."
        }
      }

      stage('Checkout')
      {
        steps {
          sh("@@@@@@@@@@")
          sh("Checkout")
          sh("@@@@@@@@@@")
        }
      }

      stage('Build')
      {
        steps {
          sh("@@@@@@@@@@")
          sh("Build")
          sh("@@@@@@@@@@")
          //sh "docker run ruby-app"
        }
      }

      stage('Test')
      {
        steps {
          sh("@@@@@@@@@@")
          sh("Test")
          sh("@@@@@@@@@@")
          sh("bundle exec rspec spec")
        }
      }

      stage('Deliver')
      {
        steps {
          sh("@@@@@@@@@@")
          sh("Deliver")
          sh("@@@@@@@@@@")
        }
      }
    }
}
