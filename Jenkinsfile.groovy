#!/usr/bin/env groovy
import hudson.model.*

node('master') {
    sh("@@@@@ your shell script @@@@@")
    agent {
      docker { image 'ruby' }
     }

     stages {
       stage('Initialize')
       {
         steps {
           sh("Initialize")
           sh("docker build .")
         }
       }
    }
}

pipeline {



    agent none

    stages {
      stage('Initialize')
      {
        steps {
          sh("echo 'Initialize'")
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
